package co.uk.kenkwok.tulipmania.ui.main

import co.uk.kenkwok.tulipmania.models.ApiCredentials
import co.uk.kenkwok.tulipmania.models.CurrencyPair
import co.uk.kenkwok.tulipmania.models.ExchangeName
import co.uk.kenkwok.tulipmania.models.PriceItem
import co.uk.kenkwok.tulipmania.network.NetworkService
import co.uk.kenkwok.tulipmania.ui.base.BaseViewModel
import co.uk.kenkwok.tulipmania.utils.CurrencyUtils
import co.uk.kenkwok.tulipmania.utils.NetworkUtils
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by kekwok on 18/09/2017.
 */

class MainViewModel(private val networkService: NetworkService, private val apiCredentials: ApiCredentials) : BaseViewModel() {

    private val anxPriceItemSubject = PublishSubject.create<PriceItem>()
    private val bitstampPriceItemSubject = PublishSubject.create<PriceItem>()
    private val bitfinexPriceItemSubject = PublishSubject.create<PriceItem>()
    private var displayHkd = false

    val anxTickerObservable: Observable<PriceItem>
        get() = anxPriceItemSubject.hide()

    val bitstampTickerObservable: Observable<PriceItem>
        get() = bitstampPriceItemSubject.hide()

    val bitfinexTickerObservable: Observable<PriceItem>
        get() = bitfinexPriceItemSubject.hide()

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

    private fun getBitfinexTicker() {
        val currencyPair = "btcusd"
        compositeDisposable?.add(
                networkService
                        .getBitfinexTickerData(currencyPair)
                        .subscribe (
                                { ticker ->
                                    val priceItem = PriceItem(
                                            exchangeName = ExchangeName.BITFINEX,
                                            exchangePrice = "$".plus(CurrencyUtils.convertDisplayCurrency(ticker.bid)),
                                            twentyFourHourHigh = "$".plus(CurrencyUtils.convertDisplayCurrency(ticker.high)),
                                            twentyFourHourLow = "$".plus(CurrencyUtils.convertDisplayCurrency(ticker.low))
                                    )
                                    bitfinexPriceItemSubject.onNext(priceItem) },
                                { throwable ->
                                    bitfinexPriceItemSubject.onError(throwable)
                                })
        )
    }

    private fun getBitstampTicker() {
        val currencyPair = "btcusd"
        compositeDisposable?.add(
                networkService
                        .getBitstampTickerData(currencyPair)
                        .subscribe (
                                { ticker ->
                                    val priceItem = PriceItem(
                                            exchangeName = ExchangeName.BITSTAMP,
                                            exchangePrice = "$".plus(CurrencyUtils.convertDisplayCurrency(ticker.bid)),
                                            twentyFourHourHigh = "$".plus(CurrencyUtils.convertDisplayCurrency(ticker.high)),
                                            twentyFourHourLow = "$".plus(CurrencyUtils.convertDisplayCurrency(ticker.low))
                                    )
                                    bitstampPriceItemSubject.onNext(priceItem) },
                                { throwable ->
                                    bitstampPriceItemSubject.onError(throwable)
                                })
        )
    }

    private fun getAnxTicker() {
        val currencyPair = "BTCUSD"
        val extraCcyPairs = "BTCHKD"
        val data = "/$currencyPair/money/ticker"
        val restSign = NetworkUtils.generateRestSign(apiCredentials.apiSecret, data.toByteArray())

        compositeDisposable?.add(
                networkService
                        .getAnxTickerData(apiCredentials.apiKey, restSign, currencyPair, extraCcyPairs)
                        .subscribe({ ticker ->

                            anxPriceItemSubject.onNext(
                                    if (displayHkd) {
                                        convertCurrencyPairToPriceItem(ticker.data.btchkd)
                                    } else {
                                        convertCurrencyPairToPriceItem(ticker.data.btcusd)
                                    })
                        }) { throwable -> anxPriceItemSubject.onError(throwable) }
        )
    }

    private fun convertCurrencyPairToPriceItem(pair: CurrencyPair): PriceItem {
        return PriceItem(
                exchangeName = ExchangeName.ANXPRO,
                exchangePrice = CurrencyUtils.convertDisplayCurrency(pair.sell.displayShort),
                twentyFourHourHigh = CurrencyUtils.convertDisplayCurrency(pair.high.displayShort),
                twentyFourHourLow = CurrencyUtils.convertDisplayCurrency(pair.low.displayShort)
        )
    }
}
