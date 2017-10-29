package co.uk.kenkwok.tulipmania.models

/**
 * Created by kwokk on 17/04/2017.
 */

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
