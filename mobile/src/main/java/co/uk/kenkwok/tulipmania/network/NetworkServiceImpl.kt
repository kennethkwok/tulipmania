package co.uk.kenkwok.tulipmania.network

import android.support.annotation.Nullable
import co.uk.kenkwok.tulipmania.models.Ticker
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Created by kwokk on 17/04/2017.
 */

class NetworkServiceImpl(retrofit: Retrofit) : NetworkService {

    private val service: ANXApi

    init {
        service = retrofit.create(ANXApi::class.java)
    }

    /**
     * Gets the most recent ticker data every 15 seconds
     * @param currencyPair BTCUSD,BTCHKD,BTCEUR,BTCCAD,BTCAUD,BTCSGD,BTCJPY,BTCGBP,BTCNZD
     * @param apiKey API Key
     * @param restSign Rest sign generated by NetworkUtils class
     */
    override fun getTickerData(apiKey: String, restSign: String, currencyPair: String, @Nullable extraCcyPairs: String): Observable<Ticker> {
        return Observable.interval(0, TICKER_INTERVAL, TimeUnit.SECONDS, Schedulers.io())
                .flatMap {
                    service
                            .getMarketTickerObservable(currencyPair, apiKey, restSign, extraCcyPairs)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }

    companion object {
        private val TICKER_INTERVAL: Long = 15
    }
}
