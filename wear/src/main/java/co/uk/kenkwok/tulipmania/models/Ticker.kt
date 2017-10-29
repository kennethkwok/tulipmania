package co.uk.kenkwok.tulipmania.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Ｔhe most recent ticker for a currency pair
 *
 * Created by kwokk on 17/04/2017.
 */
class Ticker {
    @SerializedName("result")
    @Expose
    lateinit var result: String
    @SerializedName("data")
    @Expose
    lateinit var data: Data

    companion object {
        private val serialVersionUID = 3189288793141422572L
    }
}
