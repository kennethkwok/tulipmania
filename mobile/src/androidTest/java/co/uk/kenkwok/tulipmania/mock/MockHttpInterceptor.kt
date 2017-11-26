package co.uk.kenkwok.tulipmania.mock

import android.content.Context
import co.uk.kenkwok.tulipmania.R
import okhttp3.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


/**
 * Created by kwokk on 24/11/2017.
 */
class MockHttpInterceptor(private val context: Context) : Interceptor {
    private val MEDIA_JSON = MediaType.parse("application/json")

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        var filename: String
        if (request.url().toString().contains(context.getString(R.string.anxpro_base_url))) {
            filename = "anx_api_sample.json"
        } else if (request.url().toString().contains(context.getString(R.string.bitfinex_base_url))) {
            filename = "bitfinex_api_sample.json"
        } else {
            filename = "bitstamp_api_sample.json"
        }

        val json = parseStream(context.assets.open(filename))

        return Response.Builder()
                .body(ResponseBody.create(MEDIA_JSON, json))
                .request(chain.request())
                .protocol(Protocol.HTTP_2)
                .code(200)
                .build()
    }

    @Throws(IOException::class)
    private fun parseStream(stream: InputStream): String {
        val builder = StringBuilder()
        val br = BufferedReader(InputStreamReader(stream, "UTF-8"))
        var line = br.readLine()
        while (line != null) {
            builder.append(line)
            line = br.readLine()
        }
        br.close()
        return builder.toString()
    }
}