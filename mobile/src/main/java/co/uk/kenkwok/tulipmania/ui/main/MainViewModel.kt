package co.uk.kenkwok.tulipmania.ui.main

import co.uk.kenkwok.tulipmania.models.ApiCredentials
import co.uk.kenkwok.tulipmania.models.CurrencyPair
import co.uk.kenkwok.tulipmania.models.Ticker
import co.uk.kenkwok.tulipmania.network.NetworkService
import co.uk.kenkwok.tulipmania.ui.base.BaseViewModel
import co.uk.kenkwok.tulipmania.utils.NetworkUtils
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by kekwok on 18/09/2017.
 */

class MainViewModel(private val networkService: NetworkService, private val apiCredentials: ApiCredentials) : BaseViewModel() {

    private val currencyPairSubject = PublishSubject.create<CurrencyPair>()
    private var cachedTicker: Ticker? = null
    private var displayHkd = false

    val btcTickerObservable: Observable<CurrencyPair>
        get() = currencyPairSubject.hide()

    override fun onCreate() {
        super.onCreate()
        getTicker()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun getTicker() {
        val currencyPair = "BTCUSD"
        val extraCcyPairs = "BTCHKD"
        val data = "/$currencyPair/money/ticker"
        val restSign = NetworkUtils.generateRestSign(apiCredentials.apiSecret, data.toByteArray())

        compositeDisposable?.add(
                networkService
                        .getTickerData(apiCredentials.apiKey, restSign, currencyPair, extraCcyPairs)
                        .subscribe({ ticker ->

                            currencyPairSubject.onNext(
                                    if (displayHkd) {
                                        ticker.data.btchkd
                                    } else {
                                        ticker.data.btcusd
                                    })

                            cachedTicker = ticker
                        }) { throwable -> currencyPairSubject.onError(throwable) }
        )
    }

    fun displayHkdToggled(isChecked: Boolean) {
        displayHkd = isChecked

        if (cachedTicker != null) {
            currencyPairSubject.onNext(
                    if (displayHkd) {
                        cachedTicker!!.data.btchkd
                    } else {
                        cachedTicker!!.data.btcusd
                    })
        }
    }
}
