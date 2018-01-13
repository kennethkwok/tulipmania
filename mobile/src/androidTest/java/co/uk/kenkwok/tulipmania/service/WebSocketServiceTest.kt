package co.uk.kenkwok.tulipmania.service

import android.content.Intent
import android.os.IBinder
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ServiceTestRule
import android.support.test.runner.AndroidJUnit4
import co.uk.kenkwok.tulipmania.models.BitfinexWebSocketTicker
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

/**
 * Created by kwokk on 29/12/2017.
 */
@RunWith(AndroidJUnit4::class)
class WebSocketServiceTest {

    @Rule @JvmField val serviceRule = ServiceTestRule()

    private lateinit var serviceIntent: Intent
    private lateinit var binder: IBinder
    private lateinit var service: BitfinexService

    @Before
    fun setup() {
        serviceIntent = Intent(InstrumentationRegistry.getTargetContext(), BitfinexService::class.java)
        binder = serviceRule.bindService(serviceIntent)

        service = (binder as BitfinexService.WebSocketBinder).getService()
    }

    @Test
    fun testServiceSubscribed() {
        val testObserver = service.getBitfinexETHTickerObservable().test()

        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)

        testObserver
                .assertNoErrors()
                .assertValue(BitfinexWebSocketTicker(
                        2,
                        236.62.toFloat(),
                        9.0029.toFloat(),
                        236.88.toFloat(),
                        7.1138.toFloat(),
                        (-1.02).toFloat(),
                        0.toFloat(),
                        236.52.toFloat(),
                        5191.36754297.toFloat(),
                        250.01.toFloat(),
                        220.05.toFloat())
                )
    }

}