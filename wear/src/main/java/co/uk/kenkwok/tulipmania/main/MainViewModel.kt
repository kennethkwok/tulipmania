package co.uk.kenkwok.tulipmania.main

import co.uk.kenkwok.tulipmania.models.ApiCredentials
import co.uk.kenkwok.tulipmania.models.Ticker
import co.uk.kenkwok.tulipmania.network.NetworkService
import co.uk.kenkwok.tulipmania.utils.NetworkUtils
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

/**
 * Created by kwokk on 25/09/2017.
 */

class MainViewModel(private val networkService: NetworkService, private val apiCredentials: ApiCredentials) {

    private var compositeDisposable: CompositeDisposable? = null
    private val tickerSubject = PublishSubject.create<Ticker>()

    val tickerObservable: Observable<Ticker>
        get() = tickerSubject.hide()

    fun onResume() {}

    fun onCreate() {
        compositeDisposable = CompositeDisposable()

        getTicker()
    }

    fun onPause() {}

    fun onStop() {}

    fun onDestroy() {
        compositeDisposable?.dispose()
    }

    private fun getTicker() {
        val currencyPair = "BTCUSD"
        val extraCcyPairs = "BTCHKD"
        val data = "/$currencyPair/money/ticker"
        val restSign = NetworkUtils.generateRestSign(apiCredentials.apiSecret, data.toByteArray())

        compositeDisposable?.add(
                networkService
                        .getTickerData(apiCredentials.apiKey, restSign, currencyPair, extraCcyPairs)
                        .subscribe({ ticker -> tickerSubject.onNext(ticker) }) { throwable -> tickerSubject.onError(throwable) }
        )
    }
}
