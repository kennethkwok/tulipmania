package co.uk.kenkwok.tulipmania.models

/**
 * Created by kwokk on 13/11/2017.
 */
data class PriceItem(
        val exchangePrice: String,
        val twentyFourHourHigh: String,
        val twentyFourHourLow: String,
        val exchangeName: ExchangeName
)