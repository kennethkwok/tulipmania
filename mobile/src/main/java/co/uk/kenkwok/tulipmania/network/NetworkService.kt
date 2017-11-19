package co.uk.kenkwok.tulipmania.network

import android.support.annotation.Nullable
import co.uk.kenkwok.tulipmania.models.BitfinexTicker
import co.uk.kenkwok.tulipmania.models.BitstampTicker
import co.uk.kenkwok.tulipmania.models.Ticker
import io.reactivex.Observable

/**
 * Created by kekwok on 19/09/2017.
 */

interface NetworkService {
    fun getAnxTickerData(apiKey: String, restSign: String, currencyPair: String, @Nullable extraCcyPairs: String): Observable<Ticker>
    fun getBitstampTickerData(currencyPair: String): Observable<BitstampTicker>
    fun getBitfinexTickerData(currencyPair: String): Observable<BitfinexTicker>
}
