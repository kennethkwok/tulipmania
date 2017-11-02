package co.uk.kenkwok.tulipmania.models

import com.google.gson.annotations.SerializedName

/**
 * Created by kekwok on 18/09/2017.
 */

data class ApiCredentials(
    @SerializedName("api_key")
    val apiKey: String,
    @SerializedName("api_secret")
    val apiSecret: String
)
