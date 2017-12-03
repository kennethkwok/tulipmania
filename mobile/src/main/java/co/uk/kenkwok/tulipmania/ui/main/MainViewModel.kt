package co.uk.kenkwok.tulipmania.ui.main

import android.content.Context
import android.util.Log
import co.uk.kenkwok.tulipmania.R
import co.uk.kenkwok.tulipmania.models.CurrencyPair
import co.uk.kenkwok.tulipmania.models.ExchangeName
import co.uk.kenkwok.tulipmania.models.PriceItem
import co.uk.kenkwok.tulipmania.models.RecyclerViewTickerItem
import co.uk.kenkwok.tulipmania.network.NetworkService
import co.uk.kenkwok.tulipmania.ui.base.BaseViewModel
import co.uk.kenkwok.tulipmania.utils.ANXCredentialUtils
import co.uk.kenkwok.tulipmania.utils.CurrencyUtils
import co.uk.kenkwok.tulipmania.utils.NetworkUtils
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by kekwok on 18/09/2017.
 */

class MainViewModel(private val networkService: NetworkService, private val context: Context) : BaseViewModel() {
    private val TAG = "MainViewModel"
    private val tickerItemSubject = PublishSubject.create<RecyclerViewTickerItem>()

    val tickerObservable: Observable<RecyclerViewTickerItem>
        get() = tickerItemSubject.hide()

    override fun onCreate() {
        super.onCreate()
        getAnxTicker()
        getBitstampTicker()
        getBitfinexTicker()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    fun initTickerList(): Observable<ArrayList<RecyclerViewTickerItem>> {
        val tickerList = ArrayList<RecyclerViewTickerItem>()
        tickerList.add(RecyclerViewTickerItem(context.getString(R.string.btc_heading)))
        tickerList.add(RecyclerViewTickerItem(tickerItem = PriceItem(exchangeName = ExchangeName.ANXPRO)))
        tickerList.add(RecyclerViewTickerItem(tickerItem = PriceItem(exchangeName = ExchangeName.BITFINEX)))
        tickerList.add(RecyclerViewTickerItem(tickerItem = PriceItem(exchangeName = ExchangeName.BITSTAMP)))

        return Observable.just(tickerList)
    }

    private fun getBitfinexTicker() {
        val currencyPair = "btcusd"
        compositeDisposable?.add(
                networkService
                        .getBitfinexTickerData(currencyPair)
                        .onErrorResumeNext { throwable: Throwable ->
                            Log.e(TAG, throwable.message)
                            tickerItemSubject.onNext(RecyclerViewTickerItem(error = Throwable(ExchangeName.BITFINEX.exchange)))
                            Observable.empty()
                        }
                        .subscribe { ticker ->
                            val tickerItem = RecyclerViewTickerItem(tickerItem = PriceItem(
                                    exchangeName = ExchangeName.BITFINEX,
                                    exchangePrice = CurrencyUtils.convertDisplayCurrency(ticker.bid),
                                    twentyFourHourHigh = CurrencyUtils.convertDisplayCurrency(ticker.high),
                                    twentyFourHourLow = CurrencyUtils.convertDisplayCurrency(ticker.low))
                            )
                            tickerItemSubject.onNext(tickerItem)
                        }
        )
    }

    private fun getBitstampTicker() {
        val currencyPair = "btcusd"
        compositeDisposable?.add(
                networkService
                        .getBitstampTickerData(currencyPair)
                        .onErrorResumeNext { throwable: Throwable ->
                            Log.e(TAG, throwable.message)
                            tickerItemSubject.onNext(RecyclerViewTickerItem(error = Throwable(ExchangeName.BITSTAMP.exchange)))
                            Observable.empty()
                        }
                        .subscribe { ticker ->
                            val tickerItem = RecyclerViewTickerItem(tickerItem = PriceItem(
                                    exchangeName = ExchangeName.BITSTAMP,
                                    exchangePrice = CurrencyUtils.convertDisplayCurrency(ticker.bid),
                                    twentyFourHourHigh = CurrencyUtils.convertDisplayCurrency(ticker.high),
                                    twentyFourHourLow = CurrencyUtils.convertDisplayCurrency(ticker.low))
                            )
                            tickerItemSubject.onNext(tickerItem)
                        }
        )
    }

    private fun getAnxTicker() {
        val currencyPair = "BTCUSD"
        val extraCcyPairs = "BTCHKD"
        val data = "/$currencyPair/money/ticker"
        val apiCredentials = ANXCredentialUtils.getApiCredentials(context)
        val restSign = NetworkUtils.generateRestSign(apiCredentials.apiSecret, data.toByteArray())

        compositeDisposable?.add(
                networkService
                        .getAnxTickerData(apiCredentials.apiKey, restSign, currencyPair, extraCcyPairs)
                        .onErrorResumeNext { throwable: Throwable ->
                            Log.e(TAG, throwable.message)
                            tickerItemSubject.onNext(RecyclerViewTickerItem(error = Throwable(ExchangeName.ANXPRO.exchange)))
                            Observable.empty()
                        }
                        .subscribe { ticker ->
                            tickerItemSubject.onNext(convertCurrencyPairToTickerItem(ticker.data.btcusd))
                        }

        )
    }

    private fun convertCurrencyPairToTickerItem(pair: CurrencyPair): RecyclerViewTickerItem {
        return RecyclerViewTickerItem(tickerItem = PriceItem(
                exchangeName = ExchangeName.ANXPRO,
                exchangePrice = CurrencyUtils.convertDisplayCurrency(pair.sell.displayShort),
                twentyFourHourHigh = CurrencyUtils.convertDisplayCurrency(pair.high.displayShort),
                twentyFourHourLow = CurrencyUtils.convertDisplayCurrency(pair.low.displayShort))
        )
    }
}
