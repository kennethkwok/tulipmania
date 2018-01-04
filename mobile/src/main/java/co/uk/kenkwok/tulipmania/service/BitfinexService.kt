package co.uk.kenkwok.tulipmania.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import co.uk.kenkwok.tulipmania.models.BitfinexWebSocketTicker
import co.uk.kenkwok.tulipmania.models.SubscribeResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Background service which maintains the web socket connection
 */
class BitfinexService : Service() {
    @Inject lateinit var bitfinexWebSocket: BitfinexWebSocket
    private val TAG = BitfinexService::class.java.simpleName
    private val binder = WebSocketBinder()
    private var isSubscribedToTicker = false
    private var channelId = ""
    private var webSocketConnectedSubject = BehaviorSubject.create<Boolean>()   // emits most recent item + all subsequent items

    private var webSocketTickerSubject = PublishSubject.create<BitfinexWebSocketTicker>()

    fun getWebSocketTickerObservable(): Observable<BitfinexWebSocketTicker> {
        return webSocketTickerSubject.hide()
    }

    /**
     * subscribes to the bitfinex ticker updates when the service is bound to an activity
     */
    override fun onBind(p0: Intent): IBinder {
        webSocketConnectedSubject
                .subscribeOn(Schedulers.io())
                .subscribe { isConnected ->
                    if (isConnected) {
                        subscribeToTickerUpdates()
                        webSocketConnectedSubject.unsubscribeOn(Schedulers.io())
                    }
                }

        return binder
    }

    /**
     * subscribe to ticker updates when service is already created
     */
    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        webSocketConnectedSubject
                .subscribeOn(Schedulers.io())
                .subscribe { isConnected ->
                    if (isConnected) {
                        subscribeToTickerUpdates()
                        webSocketConnectedSubject.unsubscribeOn(Schedulers.io())
                    }
                }
    }

    /**
     * Unsubscribes from ticker updates when activity unbinds from service during onStop()
     */
    override fun onUnbind(intent: Intent): Boolean {
        if (isSubscribedToTicker) {
            bitfinexWebSocket.unsubscribeFromTicker(channelId)
        }

        return true
    }

    /**
     * Creates a web socket connection. This is invoked when Application.onCreate is called.
     */
    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
        bitfinexWebSocket.connectWebSocket().subscribe { webSocketConnectedSubject.onNext(true) }
    }

    override fun onDestroy() {
        bitfinexWebSocket.closeWebSocket().subscribe {
            webSocketConnectedSubject.onNext(false)
            Log.d(TAG, "Service destroyed")
            super.onDestroy()
        }
    }

    inner class WebSocketBinder: Binder() {
        fun getService(): BitfinexService {
            return this@BitfinexService
        }
    }

    internal fun reset() {
        isSubscribedToTicker = false
        channelId = ""
    }

    internal fun subscribeToTickerUpdates() {
        if (!isSubscribedToTicker) {
            bitfinexWebSocket.subscribeToTicker().subscribe { text ->
                if (text.contains("{")) {
                    try {
                        val response = Gson().fromJson<SubscribeResponse>(text, SubscribeResponse::class.java)

                        if (response.event == "subscribed") {
                            Log.d(TAG, "subscribed")
                            isSubscribedToTicker = true
                        } else if (response.event == "unsubscribed") {
                            Log.d(TAG, "unsubscribed!")
                            reset()
                        } else {
                            Log.e(TAG, "error occurred: ${response.msg}, code: ${response.code}")
                        }
                    } catch (e: JsonSyntaxException) {
                        // message from stream is not a SubscribeResponse
                    }
                } else if (text.contains("hb")) {
                    // heartbeat to indicate websocket connected
                } else {
                    // an actual socket message
                    val ticker = convertStreamToTicker(text)
                    channelId = ticker.channelId.toString()
                    webSocketTickerSubject.onNext(ticker)
                }
            }
        } else {
            Log.d(TAG, "Already subscribed to ticker")
        }
    }

    internal fun convertStreamToTicker(string: String): BitfinexWebSocketTicker {
        val csv = string.replace(regex = Regex("\\[|\\]"), replacement = "").replace(" ", "")
        val value = csv.split(",")
        return BitfinexWebSocketTicker(
                channelId = value[0].toInt(),
                bid = value[1].toFloat(),
                bidSize = value[2].toFloat(),
                ask = value[3].toFloat(),
                askSize = value[4].toFloat(),
                dailyChange = value[5].toFloat(),
                dailyChangePercent = value[6].toFloat(),
                lastPrice = value[7].toFloat(),
                volume = value[8].toFloat(),
                high = value[9].toFloat(),
                low = value[10].toFloat())
    }
}