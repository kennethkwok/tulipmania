package co.uk.kenkwok.tulipmania.utils

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by kwokk on 24/11/2017.
 */
@RunWith(AndroidJUnit4::class)
class NetworkUtilsTest {
    private val context: Context = InstrumentationRegistry.getTargetContext()

    @Test
    fun testGenerateANXRestSign() {
        val credentials = ANXCredentialUtils.getApiCredentials(context)
        val data = "/btcusd/money/ticker"
        val restSign = NetworkUtils.generateRestSign(credentials.apiSecret, data.toByteArray())

        Assert.assertNotNull(restSign)
        Assert.assertTrue(restSign.isNotEmpty())
    }
}