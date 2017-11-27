package co.uk.kenkwok.tulipmania.network

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertEquals
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

/**
 * Created by kekwok on 27/11/2017.
 */
class ApiEndpointTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var networkService: NetworkService

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler {
            _ -> Schedulers.trampoline()
        }

        mockWebServer = MockWebServer()
        mockWebServer.start()

        networkService = NetworkServiceImpl(buildAnxApi(), buildBitstampApi(), buildBitfinexApi())
    }

    @Test
    fun getAnxTickerEndpoint() {
        mockWebServer.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(getFileAsString("anx_api_sample.json")))

        val testObserver = networkService.getAnxTickerData("", "", "btcusd", "btchkd").test()

        // test request
        val request = mockWebServer.takeRequest()
        assertEquals("/api/2/btcusd/money/ticker?extraCcyPairs=btchkd", request.path)
        assertEquals("GET", request.method)

        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)

        // test response
        val list = testObserver.assertNoErrors().values()
        assertEquals(1, list.size)

        val ticker = list.get(0)
        assertEquals("69,487.24 HKD", ticker.data.btchkd.high.displayShort)
        assertEquals("6948724341", ticker.data.btchkd.high.valueInt)
        assertEquals("HKD", ticker.data.btchkd.high.currency)
        assertEquals("69,487.24341 HKD", ticker.data.btchkd.high.display)
        assertEquals("69487.24341", ticker.data.btchkd.high.value)

        assertEquals("66,100.00 HKD", ticker.data.btchkd.buy.displayShort)
        assertEquals("6610000000", ticker.data.btchkd.buy.valueInt)
        assertEquals("HKD", ticker.data.btchkd.buy.currency)
        assertEquals("66,100.00000 HKD", ticker.data.btchkd.buy.display)
        assertEquals("66100.00000", ticker.data.btchkd.buy.value)
    }

    @Test
    fun getBitstampTickerEndpoint() {
        mockWebServer.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(getFileAsString("bitstamp_api_sample.json")))

        val testObserver = networkService.getBitstampTickerData("btcusd").test()

        // test request
        val request = mockWebServer.takeRequest()
        assertEquals("/api/v2/ticker/btcusd", request.path)
        assertEquals("GET", request.method)

        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)

        // test response
        val list = testObserver.assertNoErrors().values()
        assertEquals(1, list.size)

        val ticker = list.get(0)
        assertEquals("8340.00", ticker.high)
        assertEquals("8201.01", ticker.last)
        assertEquals("1511531683", ticker.timestamp)
        assertEquals("8201.01", ticker.bid)
        assertEquals("8126.37", ticker.vwap)
        assertEquals("10070.82185018", ticker.volume)
        assertEquals("7876.00", ticker.low)
        assertEquals("8201.06", ticker.ask)
        assertEquals("7988.96", ticker.open)
    }

    @Test
    fun getBitfinexTickerEndpoint() {
        mockWebServer.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(getFileAsString("bitfinex_api_sample.json")))

        val testObserver = networkService.getBitfinexTickerData("btcusd").test()

        // test request
        val request = mockWebServer.takeRequest()
        assertEquals("/v1/pubticker/btcusd", request.path)
        assertEquals("GET", request.method)

        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)

        // test response
        val list = testObserver.assertNoErrors().values()
        assertEquals(1, list.size)

        val ticker = list.get(0)
        assertEquals("8193.55", ticker.mid)
        assertEquals("8193.5", ticker.bid)
        assertEquals("8193.6", ticker.ask)
        assertEquals("8193.5", ticker.lastPrice)
        assertEquals("7871.0", ticker.low)
        assertEquals("8352.1", ticker.high)
        assertEquals("44595.14831384", ticker.volume)
        assertEquals("1511531646.3300447", ticker.timestamp)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private fun buildAnxApi(): ANXApi {
        return buildRetrofit().create(ANXApi::class.java)
    }

    private fun buildBitstampApi(): BitstampAPI {
        return buildRetrofit().create(BitstampAPI::class.java)
    }

    private fun buildBitfinexApi(): BitfinexAPI {
        return buildRetrofit().create(BitfinexAPI::class.java)
    }

    private fun buildRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    companion object {
        fun getFileAsString(filename: String): String {
            ApiEndpointTest::class.java.classLoader.getResourceAsStream(filename).use({ inputStream ->
                val result = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var length: Int = inputStream.read(buffer)
                while (length != -1) {
                    result.write(buffer, 0, length)
                    length = inputStream.read(buffer)
                }

                return result.toString("UTF-8")
            })
        }
    }
}