package co.uk.kenkwok.tulipmania.service

import android.util.Log
import co.uk.kenkwok.tulipmania.library.websocket.RxWebSocket
import co.uk.kenkwok.tulipmania.library.websocket.entities.SocketOpenEvent
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

    override fun connectWebSocket(): Completable {
        if (!rxWebSocket.isConnected()) {
            rxWebSocket.connect()
            Log.d(TAG, "connectWebSocket() called")
        }

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

    override fun subscribeToTicker(): Flowable<String> {
        rxWebSocket.sendMessage(SubscribeMessage("subscribe", "ticker", "BTCUSD"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ success ->
                    Log.d(TAG, "WebSocket subscribe: $success")
                }, { t ->
                    Log.d(TAG, "sendMessage failure: ${t.message}")
                })

        return Flowable.create({ flowable ->
                rxWebSocket.onTextMessage()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { event ->
                            event.text?.let { text ->
                                flowable.onNext(text)
                            }
                        }
        }, BackpressureStrategy.LATEST)
    }

    override fun unsubscribeFromTicker(channelId: String) {
        rxWebSocket.sendMessage(UnsubscribeMessage("unsubscribe", channelId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ success ->
                    Log.d(TAG, "unsubscribe sendMessage success! $success")
                })
    }

    private fun getChannelId(text: String): String {
        val csv = text.replace(regex = Regex("\\[|\\]"), replacement = "").replace(" ", "")
        val value = csv.split(",")
        return value[0]
    }
}