package co.uk.kenkwok.tulipmania.models

/**
 * Created by kwokk on 13/11/2017.
 */
data class RecyclerViewTickerItem @JvmOverloads constructor(
        val sectionHeading: String = "",
        val tickerItem: PriceItem? = null,
        val error: Throwable? = null
)

data class PriceItem @JvmOverloads constructor(
        val cryptoType: CryptoType,
        val exchangePrice: String? = "-",
        val twentyFourHourHigh: String? = "-",
        val twentyFourHourLow: String? = "-",
        val exchangeName: ExchangeName
)