package co.uk.kenkwok.tulipmania.mock

import okhttp3.*
import java.io.ByteArrayOutputStream

/**
 * Created by kwokk on 29/11/2017.
 */
class MockHttpInterceptor: Interceptor {
    val MEDIA_JSON = MediaType.parse("application/json")

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val path = request.url().encodedPath()
        var json: String

        if (path.contains("/v1/pubticker/btcusd")) {
            json = getFileAsString("bitfinex_api_sample.json")
            return Response.Builder()
                    .body(ResponseBody.create(MEDIA_JSON, json))
                    .request(chain.request())
                    .protocol(Protocol.HTTP_2)
                    .message("")
                    .code(500)
                    .build()

        } else if (path.contains("/api/v2/ticker/btcusd")) {
            json = getFileAsString("bitstamp_api_sample.json")
            return Response.Builder()
                    .body(ResponseBody.create(MEDIA_JSON, json))
                    .request(chain.request())
                    .protocol(Protocol.HTTP_2)
                    .message("")
                    .code(500)
                    .build()

        } else if (path.contains("ws")) {
            // bitfinex websocket - sends "test" as the default message
            val response = Response.Builder()
                    .body(ResponseBody.create(MediaType.parse("text/plain"), ""))
                    .message("text")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_2)
                    .code(200)
                    .build()
            println(response.message())
            return response

        } else {
            json = getFileAsString("anx_api_sample.json")
        }
        return Response.Builder()
                .body(ResponseBody.create(MEDIA_JSON, json))
                .request(chain.request())
                .protocol(Protocol.HTTP_2)
                .message("")
                .code(200)
                .build()
    }

    private fun getFileAsString(filename: String): String {
        this::class.java.classLoader.getResourceAsStream(filename).use({ inputStream ->
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