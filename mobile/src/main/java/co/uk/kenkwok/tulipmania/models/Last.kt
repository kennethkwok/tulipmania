package co.uk.kenkwok.tulipmania.models

import com.google.gson.annotations.SerializedName

/**
 * Created by kwokk on 17/04/2017.
 */

data class Last(
        val currency: String,
        val display: String,
        @SerializedName("display_short")
        val displayShort: String,
        val value: String,
        @SerializedName("value_int")
        val valueInt: String
)

