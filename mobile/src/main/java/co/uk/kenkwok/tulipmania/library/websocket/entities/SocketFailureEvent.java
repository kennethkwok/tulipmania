package co.uk.kenkwok.tulipmania.library.websocket.entities;

import co.uk.kenkwok.tulipmania.library.websocket.SocketEventTypeEnum;
import okhttp3.Response;

/**
 * RxWebSocket library - simple Rx wrapper for OkHttp WebSockets
 * Source: https://github.com/TeamWanari/websocket-android
 */
public class SocketFailureEvent extends SocketEvent {

    private final Throwable throwable;
    private final Response response;

    public SocketFailureEvent(Throwable throwable, Response response) {
        super(SocketEventTypeEnum.FAILURE);
        this.throwable = throwable;
        this.response = response;
    }

    public Throwable getException() {
        return throwable;
    }

    public Response getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "SocketFailureEvent{" +
                "exception=" + throwable.getMessage() +
                '}';
    }
}