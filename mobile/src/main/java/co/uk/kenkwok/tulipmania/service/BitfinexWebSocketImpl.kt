package co.uk.kenkwok.tulipmania.service

import android.util.Log
import co.uk.kenkwok.tulipmania.library.websocket.RxWebSocket
import co.uk.kenkwok.tulipmania.library.websocket.entities.SocketOpenEvent
import co.uk.kenkwok.tulipmania.models.CryptoType
import co.uk.kenkwok.tulipmania.models.SubscribeMessage
import co.uk.kenkwok.tulipmania.models.UnsubscribeMessage
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by kwokk on 03/01/2018.
 */
class BitfinexWebSocketImpl(private val rxWebSocket: RxWebSocket): BitfinexWebSocket {
    private val TAG = BitfinexWebSocketImpl::class.java.simpleName

    private val BITCOIN = "BTCUSD"
    private val ETHEREUM = "ETHUSD"
    private val RIPPLE = "XRPUSD"

    private lateinit var tickerFlowable: Flowable<String>

    override fun getTickerFlowable(): Flowable<String> {
        return tickerFlowable
    }

    override fun connectWebSocket(): Completable {
        if (!rxWebSocket.isConnected()) {
            rxWebSocket.connect()
            Log.d(TAG, "connectWebSocket() called")
        }

        tickerFlowable = Flowable.create({ flowable ->
            rxWebSocket.onTextMessage()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { event ->
                        event.text?.let { text ->
                            flowable.onNext(text)
                        }
                    }
        }, BackpressureStrategy.LATEST)

        return Completable.create { c ->
            rxWebSocket.onOpen()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ socketOpenEvent: SocketOpenEvent ->
                        Log.d(TAG, "WebSocket opened: ${socketOpenEvent.response.toString()}")
                        c.onComplete()
                    }, { t ->
                        Log.e(TAG, "WebSocket onOpen failed: ${t.message}")
                        c.onError(t)
                    })
        }
    }

    override fun closeWebSocket(): Completable {
        return Completable.create { c ->
            rxWebSocket.close()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                success -> Log.d(TAG, "WebSocket closed! $success")
                                c.onComplete()
                            },
                            { t ->
                                Log.e(TAG, "close WebSocket error: ${t.message}")
                                c.onError(t)
                            })
        }
    }

    override fun subscribeToTicker(type: CryptoType) {
        when (type) {
            CryptoType.BTC -> subscribeToTicker(BITCOIN)
            CryptoType.ETH -> subscribeToTicker(ETHEREUM)
            CryptoType.XRP -> subscribeToTicker(RIPPLE)
            else -> {
                Log.e(TAG, "$type not supported currently!")
            }
        }
    }

    override fun unsubscribeFromTicker(channelId: String) {
        rxWebSocket.sendMessage(UnsubscribeMessage("unsubscribe", channelId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ success ->
                    Log.d(TAG, "unsubscribe sendMessage success! $success")
                })
    }

    private fun subscribeToTicker(tag: String) {
        rxWebSocket.sendMessage(SubscribeMessage("subscribe", "ticker", tag))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ success ->
                    Log.d(TAG, "$tag subscribe: $success")
                }, { t ->
                    Log.d(TAG, "sendMessage failure: ${t.message}")
                })
    }
}