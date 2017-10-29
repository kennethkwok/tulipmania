package co.uk.kenkwok.tulipmania.models

/**
 * Ｔhe most recent ticker for a currency pair
 *
 * Created by kwokk on 17/04/2017.
 */
data class Ticker(
    val result: String,
    val data: Data
)