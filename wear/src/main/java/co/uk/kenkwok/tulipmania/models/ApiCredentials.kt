package co.uk.kenkwok.tulipmania.models

import com.google.gson.annotations.SerializedName

/**
 * Created by kekwok on 18/09/2017.
 */

class ApiCredentials {
    @SerializedName("api_key")
    lateinit var apiKey: String

    @SerializedName("api_secret")
    lateinit var apiSecret: String
}
