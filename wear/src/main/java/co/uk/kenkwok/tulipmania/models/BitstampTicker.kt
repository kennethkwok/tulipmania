package co.uk.kenkwok.tulipmania.models

/**
 * Created by kwokk on 13/11/2017.
 */
data class BitstampTicker(
        val high: String,
        val last: String,
        val timestamp: String,
        val bid: String,
        val vwap: String,
        val volume: String,
        val low: String,
        val ask: String,
        val open: String
)