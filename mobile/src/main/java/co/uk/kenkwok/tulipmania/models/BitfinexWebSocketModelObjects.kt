package co.uk.kenkwok.tulipmania.models

/**
 * Created by kwokk on 20/12/2017.
 */
data class BitfinexWebSocketTicker(
        val channelId: Int,
        val bid: Float,
        val bidSize: Float,
        val ask: Float,
        val askSize: Float,
        val dailyChange: Float,
        val dailyChangePercent: Float,
        val lastPrice: Float,
        val volume: Float,
        val high: Float,
        val low: Float
)

data class SubscribeMessage(
        val event: String,
        val channel: String,
        val pair: String
)

data class UnsubscribeMessage(
        val event: String,
        val chanId: String
)

data class SubscribeResponse(
        val event: String,
        val status: String? = null,
        val channel: String? = null,
        val chanId: String? = null,
        val msg: String? = null,
        val code: String? = null,
        val pair: String? = null
)