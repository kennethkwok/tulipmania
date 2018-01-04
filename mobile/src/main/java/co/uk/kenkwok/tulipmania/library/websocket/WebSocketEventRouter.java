package co.uk.kenkwok.tulipmania.library.websocket;


import co.uk.kenkwok.tulipmania.library.websocket.entities.SocketClosedEvent;
import co.uk.kenkwok.tulipmania.library.websocket.entities.SocketClosingEvent;
import co.uk.kenkwok.tulipmania.library.websocket.entities.SocketEvent;
import co.uk.kenkwok.tulipmania.library.websocket.entities.SocketFailureEvent;
import co.uk.kenkwok.tulipmania.library.websocket.entities.SocketMessageEvent;
import co.uk.kenkwok.tulipmania.library.websocket.entities.SocketOpenEvent;
import io.reactivex.FlowableEmitter;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * RxWebSocket library - simple Rx wrapper for OkHttp WebSockets
 * Source: https://github.com/TeamWanari/websocket-android
 */
public class WebSocketEventRouter extends WebSocketListener {

    private final FlowableEmitter<SocketEvent> emitter;

    public WebSocketEventRouter(FlowableEmitter<SocketEvent> emitter) {
        this.emitter = emitter;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        if (!emitter.isCancelled()) {
            emitter.onNext(new SocketOpenEvent(webSocket, response));
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        if (!emitter.isCancelled()) {
            emitter.onNext(new SocketMessageEvent(text));
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        if (!emitter.isCancelled()) {
            emitter.onNext(new SocketMessageEvent(bytes));
        }
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        if (!emitter.isCancelled()) {
            emitter.onNext(new SocketClosingEvent(code, reason));
        }
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        if (!emitter.isCancelled()) {
            emitter.onNext(new SocketClosedEvent(code, reason));
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        if (!emitter.isCancelled()) {
            emitter.onNext(new SocketFailureEvent(t, response));
        }
    }
}