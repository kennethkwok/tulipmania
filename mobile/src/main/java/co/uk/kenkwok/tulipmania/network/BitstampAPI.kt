package co.uk.kenkwok.tulipmania.network

import co.uk.kenkwok.tulipmania.models.BitstampTicker
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by kwokk on 13/11/2017.
 */
interface BitstampAPI {
    /**
     * Gets the most recent price ticker from Bitstamp
     * @param currencyPair btcusd, , btceur, eurusd, xrpusd, xrpeur, xrpbtc, ltcusd, ltceur, ltcbtc, ethusd, etheur, ethbtc
     */
    @GET("/api/v2/ticker/{currencyPair}")
    fun getMarketTickerObservable(@Path("currencyPair") currencyPair: String): Observable<BitstampTicker>
}