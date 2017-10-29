package co.uk.kenkwok.tulipmania.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by kwokk on 17/04/2017.
 */

class Data : Serializable {

    @SerializedName("high")
    @Expose
    lateinit var high: High
    @SerializedName("low")
    @Expose
    lateinit var low: Low
    @SerializedName("avg")
    @Expose
    lateinit var avg: Avg
    @SerializedName("vwap")
    @Expose
    lateinit var vwap: Vwap
    @SerializedName("vol")
    @Expose
    lateinit var vol: Vol
    @SerializedName("last")
    @Expose
    lateinit var last: Last
    @SerializedName("buy")
    @Expose
    lateinit var buy: Buy
    @SerializedName("sell")
    @Expose
    lateinit var sell: Sell
    @SerializedName("now")
    @Expose
    var now: Long = 0
    @SerializedName("dataUpdateTime")
    @Expose
    var dataUpdateTime: Long = 0

    companion object {
        private const val serialVersionUID = 7933887634979230289L
    }

}

