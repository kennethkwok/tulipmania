package co.uk.kenkwok.tulipmania.ui.main

import android.content.Context
import android.util.Log
import co.uk.kenkwok.tulipmania.R
import co.uk.kenkwok.tulipmania.models.*
import co.uk.kenkwok.tulipmania.network.NetworkService
import co.uk.kenkwok.tulipmania.service.BitfinexService
import co.uk.kenkwok.tulipmania.ui.base.BaseViewModel
import co.uk.kenkwok.tulipmania.utils.ANXCredentialUtils
import co.uk.kenkwok.tulipmania.utils.CurrencyUtils
import co.uk.kenkwok.tulipmania.utils.NetworkUtils
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by kekwok on 18/09/2017.
 */

class MainViewModel(private val networkService: NetworkService,
                    private val context: Context) : BaseViewModel() {
    override val TAG = "MainViewModel"
    private val tickerItemSubject = PublishSubject.create<RecyclerViewTickerItem>()

    val tickerObservable: Observable<RecyclerViewTickerItem>
        get() = tickerItemSubject.hide()

    override fun onStart() {
        super.onStart()
        getAnxTicker()
        getBitstampTicker()
    }

    fun subscribeWebSocketUpdates(service: BitfinexService) {
        compositeDisposable.addAll(
                service.getBitfinexBTCTickerObservable().subscribe({ ticker ->
                    val tickerItem = RecyclerViewTickerItem(tickerItem = PriceItem(
                            cryptoType = CryptoType.BTC,
                            exchangeName = ExchangeName.BITFINEX,
                            exchangePrice = CurrencyUtils.convertDisplayCurrency(ticker.bid.toString()),
                            twentyFourHourHigh = CurrencyUtils.convertDisplayCurrency(ticker.high.toString()),
                            twentyFourHourLow = CurrencyUtils.convertDisplayCurrency(ticker.low.toString()))
                    )
                    tickerItemSubject.onNext(tickerItem)
                }),
                service.getBitfinexETHTickerObservable().subscribe({ ticker ->
                    val tickerItem = RecyclerViewTickerItem(tickerItem = PriceItem(
                            cryptoType = CryptoType.ETH,
                            exchangeName = ExchangeName.BITFINEX,
                            exchangePrice = CurrencyUtils.convertDisplayCurrency(ticker.bid.toString()),
                            twentyFourHourHigh = CurrencyUtils.convertDisplayCurrency(ticker.high.toString()),
                            twentyFourHourLow = CurrencyUtils.convertDisplayCurrency(ticker.low.toString()))
                    )
                    tickerItemSubject.onNext(tickerItem)
                }),
                service.getBitfinexXRPTickerObservable().subscribe({ ticker ->
                    val tickerItem = RecyclerViewTickerItem(tickerItem = PriceItem(
                            cryptoType = CryptoType.XRP,
                            exchangeName = ExchangeName.BITFINEX,
                            exchangePrice = CurrencyUtils.convertDisplayCurrency(ticker.bid.toString()),
                            twentyFourHourHigh = CurrencyUtils.convertDisplayCurrency(ticker.high.toString()),
                            twentyFourHourLow = CurrencyUtils.convertDisplayCurrency(ticker.low.toString()))
                    )
                    tickerItemSubject.onNext(tickerItem)
                })
        )
    }

    fun initTickerList(): Observable<ArrayList<RecyclerViewTickerItem>> {
        val tickerList = ArrayList<RecyclerViewTickerItem>()
        tickerList.add(RecyclerViewTickerItem(context.getString(R.string.btc_heading)))
        tickerList.add(RecyclerViewTickerItem(tickerItem = PriceItem(cryptoType = CryptoType.BTC, exchangeName = ExchangeName.ANXPRO)))
        tickerList.add(RecyclerViewTickerItem(tickerItem = PriceItem(cryptoType = CryptoType.BTC, exchangeName = ExchangeName.BITFINEX)))
        tickerList.add(RecyclerViewTickerItem(tickerItem = PriceItem(cryptoType = CryptoType.BTC, exchangeName = ExchangeName.BITSTAMP)))

        tickerList.add(RecyclerViewTickerItem(context.getString(R.string.eth_heading)))
        tickerList.add(RecyclerViewTickerItem(tickerItem = PriceItem(cryptoType = CryptoType.ETH, exchangeName = ExchangeName.ANXPRO)))
        tickerList.add(RecyclerViewTickerItem(tickerItem = PriceItem(cryptoType = CryptoType.ETH, exchangeName = ExchangeName.BITFINEX)))
        tickerList.add(RecyclerViewTickerItem(tickerItem = PriceItem(cryptoType = CryptoType.ETH, exchangeName = ExchangeName.BITSTAMP)))

        tickerList.add(RecyclerViewTickerItem(context.getString(R.string.xrp_heading)))
        tickerList.add(RecyclerViewTickerItem(tickerItem = PriceItem(cryptoType = CryptoType.XRP, exchangeName = ExchangeName.BITFINEX)))
        tickerList.add(RecyclerViewTickerItem(tickerItem = PriceItem(cryptoType = CryptoType.XRP, exchangeName = ExchangeName.BITSTAMP)))

        return Observable.just(tickerList)
    }

    private fun getBitfinexTicker() {
        val currencyPair = "btcusd"
        compositeDisposable.add(
                networkService
                        .getBitfinexTickerData(currencyPair)
                        .onErrorResumeNext { throwable: Throwable ->
                            Log.e(TAG, throwable.message)
                            tickerItemSubject.onNext(RecyclerViewTickerItem(error = Throwable(ExchangeName.BITFINEX.exchange)))
                            Observable.empty()
                        }
                        .subscribe { ticker ->
                            val tickerItem = RecyclerViewTickerItem(tickerItem = PriceItem(
                                    cryptoType = CryptoType.BTC,
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
        val bitcoinPair = "btcusd"
        val ethereumPair = "ethusd"
        val ripplePair = "xrpusd"
        compositeDisposable.addAll(
                networkService
                        .getBitstampTickerData(bitcoinPair)
                        .onErrorResumeNext { throwable: Throwable ->
                            Log.e(TAG, throwable.message)
                            tickerItemSubject.onNext(RecyclerViewTickerItem(tickerItem = PriceItem(cryptoType = CryptoType.BTC, exchangeName = ExchangeName.BITSTAMP),
                                    error = Throwable(ExchangeName.BITSTAMP.exchange)))
                            Observable.empty()
                        }
                        .subscribe { ticker ->
                            val tickerItem = RecyclerViewTickerItem(tickerItem = PriceItem(
                                    cryptoType = CryptoType.BTC,
                                    exchangeName = ExchangeName.BITSTAMP,
                                    exchangePrice = CurrencyUtils.convertDisplayCurrency(ticker.bid),
                                    twentyFourHourHigh = CurrencyUtils.convertDisplayCurrency(ticker.high),
                                    twentyFourHourLow = CurrencyUtils.convertDisplayCurrency(ticker.low))
                            )
                            tickerItemSubject.onNext(tickerItem)
                        },
                networkService.getBitstampTickerData(ethereumPair)
                        .onErrorResumeNext { throwable: Throwable ->
                            Log.e(TAG, throwable.message)
                            tickerItemSubject.onNext(RecyclerViewTickerItem(tickerItem = PriceItem(cryptoType = CryptoType.ETH, exchangeName = ExchangeName.BITSTAMP),
                                    error = Throwable(ExchangeName.BITSTAMP.exchange)))
                            Observable.empty()
                        }
                        .subscribe { ticker ->
                            val tickerItem = RecyclerViewTickerItem(tickerItem = PriceItem(
                                    cryptoType = CryptoType.ETH,
                                    exchangeName = ExchangeName.BITSTAMP,
                                    exchangePrice = CurrencyUtils.convertDisplayCurrency(ticker.bid),
                                    twentyFourHourHigh = CurrencyUtils.convertDisplayCurrency(ticker.high),
                                    twentyFourHourLow = CurrencyUtils.convertDisplayCurrency(ticker.low))
                            )
                            tickerItemSubject.onNext(tickerItem)
                        },
                networkService.getBitstampTickerData(ripplePair)
                        .onErrorResumeNext { throwable: Throwable ->
                            Log.e(TAG, throwable.message)
                            tickerItemSubject.onNext(RecyclerViewTickerItem(tickerItem = PriceItem(cryptoType = CryptoType.XRP, exchangeName = ExchangeName.BITSTAMP),
                                    error = Throwable(ExchangeName.BITSTAMP.exchange)))
                            Observable.empty()
                        }
                        .subscribe { ticker ->
                            val tickerItem = RecyclerViewTickerItem(tickerItem = PriceItem(
                                    cryptoType = CryptoType.XRP,
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
        val extraCcyPairs = "BTCHKD,ETHUSD"
        val data = "/$currencyPair/money/ticker"
        val apiCredentials = ANXCredentialUtils.getApiCredentials(context)
        val restSign = NetworkUtils.generateRestSign(apiCredentials.apiSecret, data.toByteArray())

        compositeDisposable.add(
                networkService
                        .getAnxTickerData(apiCredentials.apiKey, restSign, currencyPair, extraCcyPairs)
                        .onErrorResumeNext { throwable: Throwable ->
                            Log.e(TAG, throwable.message)
                            tickerItemSubject.onNext(RecyclerViewTickerItem(tickerItem = PriceItem(cryptoType = CryptoType.BTC, exchangeName = ExchangeName.ANXPRO),
                                    error = Throwable(ExchangeName.ANXPRO.exchange)))
                            tickerItemSubject.onNext(RecyclerViewTickerItem(tickerItem = PriceItem(cryptoType = CryptoType.ETH, exchangeName = ExchangeName.ANXPRO),
                                    error = Throwable(ExchangeName.ANXPRO.exchange)))
                            Observable.empty()
                        }
                        .subscribe { ticker ->
                            tickerItemSubject.onNext(convertCurrencyPairToTickerItem(ticker.data.ethusd, CryptoType.ETH))
                            tickerItemSubject.onNext(convertCurrencyPairToTickerItem(ticker.data.btcusd, CryptoType.BTC))
                        }

        )
    }

    private fun convertCurrencyPairToTickerItem(pair: CurrencyPair, type: CryptoType): RecyclerViewTickerItem {
        return RecyclerViewTickerItem(tickerItem = PriceItem(
                cryptoType = type,
                exchangeName = ExchangeName.ANXPRO,
                exchangePrice = CurrencyUtils.convertDisplayCurrency(pair.sell.displayShort),
                twentyFourHourHigh = CurrencyUtils.convertDisplayCurrency(pair.high.displayShort),
                twentyFourHourLow = CurrencyUtils.convertDisplayCurrency(pair.low.displayShort))
        )
    }
}
