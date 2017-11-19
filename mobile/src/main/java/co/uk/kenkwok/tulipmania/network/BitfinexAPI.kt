package co.uk.kenkwok.tulipmania.network

import co.uk.kenkwok.tulipmania.models.BitfinexTicker
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by kwokk on 19/11/2017.
 */
interface BitfinexAPI {
    /**
     * Gets the most recent price ticker from Bitfinex
     * @param currencyPair btcusd
     */
    @GET("/v1/pubticker/{currencyPair}")
    fun getMarketTickerObservable(@Path("currencyPair") currencyPair: String): Observable<BitfinexTicker>
}