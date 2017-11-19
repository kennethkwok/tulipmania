package co.uk.kenkwok.tulipmania.models

import com.google.gson.annotations.SerializedName

/**
 * Created by kwokk on 19/11/2017.
 */
data class BitfinexTicker(
        val mid: String,
        val bid: String,
        val ask: String,
        @SerializedName("last_price")
        val lastPrice: String,
        val low: String,
        val high: String,
        val volume: String,
        val timestamp: String
)