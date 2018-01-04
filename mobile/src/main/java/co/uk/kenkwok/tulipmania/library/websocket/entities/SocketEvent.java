package co.uk.kenkwok.tulipmania.library.websocket.entities;

import co.uk.kenkwok.tulipmania.library.websocket.SocketEventTypeEnum;

/**
 * RxWebSocket library - simple Rx wrapper for OkHttp WebSockets
 * Source: https://github.com/TeamWanari/websocket-android
 */
public class SocketEvent {

    private final SocketEventTypeEnum type;

    public SocketEvent(SocketEventTypeEnum type) {
        this.type = type;
    }

    public SocketEventTypeEnum getType() {
        return type;
    }

    @Override
    public String toString() {
        return "SocketEvent{" +
                "type=" + type +
                '}';
    }
}