package co.uk.kenkwok.tulipmania.network

import co.uk.kenkwok.tulipmania.models.*
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.TimeUnit


/**
 * Created by kwokk on 24/11/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class NetworkServiceImplTest {

    @Mock lateinit var anxApi: ANXApi
    @Mock lateinit var bitstampApi: BitstampAPI
    @Mock lateinit var bitfinexApi: BitfinexAPI

    private lateinit var networkService: NetworkService

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler {
            _ -> Schedulers.trampoline()
        }

        MockitoAnnotations.initMocks(this)
        networkService = NetworkServiceImpl(anxApi, bitstampApi, bitfinexApi)
    }

    @Test
    fun testGetAnxData() {
        Mockito.`when`(anxApi.getMarketTickerObservable(apiKey = anyString(),
                restSign = anyString(),
                currencyPair = anyString(),
                extraCurrencyPair = anyString())).thenReturn(Observable.just(generateAnxTicker()))

        val testObserver = networkService.getAnxTickerData("", "", "btcusd", "btchkd").test()

        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)

        testObserver
                .assertNoErrors()
                .assertValue(generateAnxTicker())
    }

    @Test
    fun testGetBitstampData() {
        Mockito.`when`(bitstampApi.getMarketTickerObservable(
                currencyPair = anyString())).thenReturn(Observable.just(generateBitstampTicker()))

        val testObserver = networkService.getBitstampTickerData("btcusd").test()

        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)

        testObserver
                .assertNoErrors()
                .assertValue(generateBitstampTicker())
    }

    @Test
    fun testGetBitfinexData() {
        Mockito.`when`(bitfinexApi.getMarketTickerObservable(
                currencyPair = anyString())).thenReturn(Observable.just(generateBitfinexTicker()))

        val testObserver = networkService.getBitfinexTickerData("btcusd").test()

        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)

        testObserver
                .assertNoErrors()
                .assertValue(generateBitfinexTicker())
    }

    private fun generateAnxTicker(): AnxTicker {
        val displayShort = "12,345.67 USD"
        val valueInt = "1"
        val currency = "USD"
        val display = "12,345.6789 USD"
        val value = "12345.6789"

        val currencyPair = CurrencyPair(
                high = High(currency, display, displayShort, value, valueInt),
                low = Low(currency, display, displayShort, value, valueInt),
                avg = Avg(currency, display, displayShort, value, valueInt),
                vwap = Vwap(currency, display, displayShort, value, valueInt),
                vol = Vol(currency, display, displayShort, value, valueInt),
                last = Last(currency, display, displayShort, value, valueInt),
                buy = Buy(currency, display, displayShort, value, valueInt),
                sell = Sell(currency, display, displayShort, value, valueInt),
                now = Long.MAX_VALUE,
                dataUpdateTime = Long.MAX_VALUE
        )

        return AnxTicker(result = "",
                data = Data(currencyPair, currencyPair))
    }

    private fun generateBitstampTicker(): BitstampTicker {
        return BitstampTicker(
                high = "8340.00",
                last = "8201.01",
                timestamp = "1511531683",
                bid = "8201.01",
                vwap = "8126.37",
                volume = "10070.82185018",
                low = "7876.00",
                ask = "8201.06",
                open = "7988.96"
        )
    }

    private fun generateBitfinexTicker(): BitfinexTicker {
        return BitfinexTicker(
                mid = "123.00",
                bid = "456.00",
                ask = "1,234.00",
                lastPrice = "999.00",
                low = "10.00",
                high = "1,567.12",
                volume = "9,233,123.43",
                timestamp = "1"
        )
    }
}