package co.uk.kenkwok.tulipmania.models

import com.google.gson.annotations.SerializedName

/**
 * Created by kekwok on 27/09/2017.
 */
data class Data(
    @SerializedName("BTCHKD")
    val btchkd : CurrencyPair,
    @SerializedName("BTCUSD")
    val btcusd : CurrencyPair
)