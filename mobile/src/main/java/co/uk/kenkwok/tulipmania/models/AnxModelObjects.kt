package co.uk.kenkwok.tulipmania.models

import com.google.gson.annotations.SerializedName

/**
 * All model objects for the ANX API contained in this file
 */

data class AnxTicker(
        val result: String,
        val data: Data
)

data class Data(
        @SerializedName("BTCHKD")
        val btchkd : CurrencyPair,
        @SerializedName("ETHUSD")
        val ethusd : CurrencyPair,
        @SerializedName("BTCUSD")
        val btcusd : CurrencyPair
)

data class CurrencyPair (
        val high: High,
        val low: Low,
        val avg: Avg,
        val vwap: Vwap,
        val vol: Vol,
        val last: Last,
        val buy: Buy,
        val sell: Sell,
        val now: Long,
        val dataUpdateTime: Long
)

data class Sell(
        val currency: String,
        val display: String,
        @SerializedName("display_short")
        val displayShort: String,
        val value: String,
        @SerializedName("value_int")
        val valueInt: String
)

data class Buy (
        val currency: String,
        val display: String,
        @SerializedName("display_short")
        val displayShort: String,
        val value: String,
        @SerializedName("value_int")
        val valueInt: String
)

data class Last(
        val currency: String,
        val display: String,
        @SerializedName("display_short")
        val displayShort: String,
        val value: String,
        @SerializedName("value_int")
        val valueInt: String
)


data class Vol(
        val currency: String,
        val display: String,
        @SerializedName("display_short")
        val displayShort: String,
        val value: String,
        @SerializedName("value_int")
        val valueInt: String
)

data class High (
        val currency: String,
        val display: String,
        @SerializedName("display_short")
        val displayShort: String,
        val value: String,
        @SerializedName("value_int")
        val valueInt: String
)

data class Low(
        val currency: String,
        val display: String,
        @SerializedName("display_short")
        val displayShort: String,
        val value: String,
        @SerializedName("value_int")
        val valueInt: String
)

data class Avg(
        val currency: String,
        val display: String,
        @SerializedName("display_short")
        val displayShort: String,
        val value: String,
        @SerializedName("value_int")
        val valueInt: String
)

data class Vwap(
        val currency: String,
        val display: String,
        @SerializedName("display_short")
        val displayShort: String,
        val value: String,
        @SerializedName("value_int")
        val valueInt: String
)
