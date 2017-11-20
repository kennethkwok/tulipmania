package co.uk.kenkwok.tulipmania.utils

import android.content.Context
import android.util.Log
import co.uk.kenkwok.tulipmania.models.ApiCredentials
import com.google.gson.Gson
import java.io.IOException
import java.nio.charset.Charset

/**
 * Created by kekwok on 18/09/2017.
 */

class ANXCredentialUtils {

    companion object {
        private val tag = ANXCredentialUtils::class.java.simpleName

        fun getApiCredentials(context: Context): ApiCredentials {
            val json = getJSONFromAssets(context, "anx.json")
            return Gson().fromJson(json, ApiCredentials::class.java)
        }

        private fun getJSONFromAssets(context: Context, filename: String): String {
            try {
                val inputStream = context.assets.open(filename)
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                return String(buffer, Charset.forName("UTF-8"))
            } catch (e: IOException) {
                Log.e(tag, e.message)
                return ""
            }
        }
    }
}