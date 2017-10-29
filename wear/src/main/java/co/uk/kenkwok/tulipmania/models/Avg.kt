package co.uk.kenkwok.tulipmania.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

/**
 * Created by kwokk on 17/04/2017.
 */

class Avg : Serializable {

    @SerializedName("currency")
    @Expose
    lateinit var currency: String
    @SerializedName("display")
    @Expose
    lateinit var display: String
    @SerializedName("display_short")
    @Expose
    lateinit var displayShort: String
    @SerializedName("value")
    @Expose
    lateinit var value: String
    @SerializedName("value_int")
    @Expose
    lateinit var valueInt: String

    companion object {
        private const val serialVersionUID = 3615071442170656506L
    }

}
