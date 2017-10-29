package co.uk.kenkwok.tulipmania.utils

import android.util.Base64
import android.util.Log
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Created by kwokk on 16/04/2017.
 */

object NetworkUtils {
    /**
     * Your API key and secret will be used to authenticate with ANX servers. This is achieved via the use of two HTTP headers: Rest-Key and Rest-Sign
     * Rest-Key is your API key, Rest-Sign is an HMAC hash constructed from your API secret, your API method path, your post data, and uses the SHA-512 algorithm.
     * @param base64Key
     * @param bytes
     * @return
     */
    fun generateRestSign(base64Key: String, bytes: ByteArray): String {
        try {
            val mac = Mac.getInstance("HmacSHA512")
            val secretKey = SecretKeySpec(Base64.decode(base64Key.toByteArray(), Base64.NO_WRAP), "HmacSHA512")
            mac.init(secretKey)
            mac.update(bytes)
            return Base64.encodeToString(mac.doFinal(), Base64.NO_WRAP).trim { it <= ' ' }
        } catch (e: NoSuchAlgorithmException) {
            Log.getStackTraceString(e)
        } catch (e: InvalidKeyException) {
            Log.getStackTraceString(e)
        }

        return ""
    }
}
