package co.uk.kenkwok.tulipmania.utils

import android.content.Context
import co.uk.kenkwok.tulipmania.models.ApiCredentials
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import java.io.IOException
import java.nio.charset.Charset

/**
 * Created by kekwok on 18/09/2017.
 */

@Module
class JsonUtils {

    @Provides
    fun getApiCredentials(context: Context): ApiCredentials {
        val json = getJSONFromAssets(context, "anx.json")
        val gson = Gson()
        return gson.fromJson(json, ApiCredentials::class.java)
    }

    private fun getJSONFromAssets(context: Context, filename: String): String {
        try {
            val `is` = context.assets.open(filename)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            return String(buffer, Charset.forName("UTF-8"))
        } catch (e: IOException) {
            e.printStackTrace()
            return ""
        }

    }
}
