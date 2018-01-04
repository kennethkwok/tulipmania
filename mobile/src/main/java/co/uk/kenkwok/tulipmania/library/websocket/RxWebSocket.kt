package co.uk.kenkwok.tulipmania.library.websocket

import android.util.Log
import co.uk.kenkwok.tulipmania.library.websocket.entities.*
import com.google.gson.Gson
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.annotations.NonNull
import io.reactivex.annotations.Nullable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Predicate
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.WebSocket

/**
 * RxWebSocket library - simple Rx wrapper for OkHttp WebSockets
 * Source: https://github.com/TeamWanari/websocket-android
 */
class RxWebSocket {
    private val webSocketOnSubscribe: WebSocketOnSubscribe
    private var socketEventProcessor = PublishProcessor.create<SocketEvent>()
    private val disposables = CompositeDisposable()
    private var connectionDisposables: CompositeDisposable? = null
    private var webSocket: WebSocket? = null

    private val eventSource: Flowable<SocketEvent>
        get() = socketEventProcessor.onErrorResumeNext { throwable: Throwable ->
            Log.e(TAG, "RxWebSocket EventSubject internal error occured.")
            Log.e(TAG, throwable.message)
            throwable.printStackTrace()
            socketEventProcessor = PublishProcessor.create()
            socketEventProcessor
        }

    constructor(@NonNull connectionUrl: String) {
        this.webSocketOnSubscribe = WebSocketOnSubscribe(connectionUrl)
    }

    constructor(@NonNull client: OkHttpClient, @NonNull connectionUrl: String) {
        this.webSocketOnSubscribe = WebSocketOnSubscribe(client, connectionUrl)
    }

    fun isConnected(): Boolean {
        webSocket?.let { return true }; return false
    }

    fun onOpen(): Flowable<SocketOpenEvent> {
        return eventSource
                .ofType(SocketOpenEvent::class.java)
                .doOnEach(RxWebSocketLogger("onOpen"))
    }

    fun onClosed(): Flowable<SocketClosedEvent> {
        return eventSource
                .ofType(SocketClosedEvent::class.java)
                .doOnEach(RxWebSocketLogger("onClosed"))
    }

    fun onClosing(): Flowable<SocketClosingEvent> {
        return eventSource
                .ofType(SocketClosingEvent::class.java)
                .doOnEach(RxWebSocketLogger("onClosing"))
    }

    fun onFailure(): Flowable<SocketFailureEvent> {
        return eventSource
                .ofType(SocketFailureEvent::class.java)
                .doOnEach(RxWebSocketLogger("onFailure"))
    }

    fun onTextMessage(): Flowable<SocketMessageEvent> {
        return eventSource
                .ofType(SocketMessageEvent::class.java)
                .filter(Predicate<SocketMessageEvent> { it.isText })
                .doOnEach(RxWebSocketLogger("onTextMessage"))
    }

    fun onBinaryMessage(): Flowable<SocketMessageEvent> {
        return eventSource
                .ofType(SocketMessageEvent::class.java)
                .filter { event -> !event.isText }
                .doOnEach(RxWebSocketLogger("onBinaryMessage"))
    }

    @Synchronized
    fun connect() {
        connectionDisposables = CompositeDisposable()
        val webSocketInstanceDisposable = eventSource
                .ofType(SocketOpenEvent::class.java)
                .firstElement()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(
                        { socketOpenEvent -> webSocket = socketOpenEvent.webSocket }
                ) { throwable ->
                    Log.e(TAG, throwable.message)
                    throwable.printStackTrace()
                }
        val connectionDisposable = Flowable.create(webSocketOnSubscribe, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(
                        { event -> socketEventProcessor.onNext(event) }
                ) { throwable ->
                    Log.e(TAG, throwable.message)
                    throwable.printStackTrace()
                }
        connectionDisposables!!.add(webSocketInstanceDisposable)
        connectionDisposables!!.add(connectionDisposable)
        disposables.add(connectionDisposable)
    }

    @Synchronized
    fun sendMessage(@Nullable payload: Any): Single<Boolean> {
        return Single.fromCallable {
            webSocket?.let { w ->
                val jsonBody = Gson().toJson(payload)
                Log.d(TAG, "sending message: $jsonBody")
                return@fromCallable w.send(jsonBody)
            }
            throw RuntimeException("WebSocket not connected!")
        }
    }

    @Synchronized
    fun close(): Single<Boolean> {
        return Single.fromCallable {
            webSocket?.let { w ->
                disposables.add(eventSource
                        .ofType(SocketClosedEvent::class.java)
                        .subscribe({ _ ->
                            connectionDisposables!!.clear()
                            disposables.clear()
                        }, {
                            throwable -> throwable.printStackTrace()
                        }))
                return@fromCallable w.close(1000, "Bye")
            }
            throw RuntimeException("WebSocket not connected!")
        }.doOnSuccess { success -> webSocket = null }
    }

    @Synchronized
    fun close(code: Int, @Nullable reason: String): Single<Boolean> {
        return Single.fromCallable {
            webSocket?.let { w ->
                disposables.add(eventSource
                        .ofType(SocketClosedEvent::class.java)
                        .subscribe({ _ ->
                            connectionDisposables!!.clear()
                            disposables.clear()
                        }, {
                            throwable -> throwable.printStackTrace()
                        }))
                return@fromCallable w.close(code, reason)
            }
            throw RuntimeException("WebSocket not connected!")
        }.doOnSuccess { success -> webSocket = null }
    }

    companion object {
        private val TAG = "RxWebSocket"
    }
}