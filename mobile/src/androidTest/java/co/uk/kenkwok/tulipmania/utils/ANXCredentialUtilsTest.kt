package co.uk.kenkwok.tulipmania.utils

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by kwokk on 23/11/2017.
 */
@RunWith(AndroidJUnit4::class)
class ANXCredentialUtilsTest {
    private val context: Context = InstrumentationRegistry.getTargetContext()

    @Test
    fun testGetANXCredentials() {
        val credentials = ANXCredentialUtils.getApiCredentials(context)
        assertNotNull(credentials.apiKey)
        assertNotNull(credentials.apiSecret)

        assertTrue(credentials.apiKey, credentials.apiKey.startsWith("edbda4a7"))
        assertTrue(credentials.apiSecret, credentials.apiSecret.startsWith("ZKIe6CT"))
    }
}