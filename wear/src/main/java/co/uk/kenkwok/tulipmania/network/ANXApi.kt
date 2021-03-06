package co.uk.kenkwok.tulipmania.network

import android.support.annotation.Nullable
import co.uk.kenkwok.tulipmania.models.Ticker
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by kwokk on 16/04/2017.
 */

interface ANXApi {
    /**
     * Get the most recent ticker for a currency pair
     * @param currencyPair BTCUSD,BTCHKD,BTCEUR,BTCCAD,BTCAUD,BTCSGD,BTCJPY,BTCGBP,BTCNZD
     * @param apiKey API Key
     * @param restSign Rest sign generated by NetworkUtils class
     * @param extraCurrencyPair optional list of extra currency pairs, separated by commas no space
     */
    @GET("/api/2/{currencyPair}/money/ticker")
    fun getMarketTickerObservable(@Path("currencyPair") currencyPair: String, @Header("Rest-Key") apiKey: String, @Header("Rest-Sign") restSign: String, @Nullable @Query("extraCcyPairs") extraCurrencyPair: String): Observable<Ticker>
}
