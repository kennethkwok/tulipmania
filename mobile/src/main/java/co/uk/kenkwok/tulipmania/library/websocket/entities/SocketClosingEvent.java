package co.uk.kenkwok.tulipmania.library.websocket.entities;

import co.uk.kenkwok.tulipmania.library.websocket.SocketEventTypeEnum;

/**
 * RxWebSocket library - simple Rx wrapper for OkHttp WebSockets
 * Source: https://github.com/TeamWanari/websocket-android
 */
public class SocketClosingEvent extends SocketEvent {

    private final int code;
    private final String reason;

    public SocketClosingEvent(int code, String reason) {
        super(SocketEventTypeEnum.CLOSING);
        this.code = code;
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "SocketClosingEvent{" +
                "code=" + code +
                ", reason='" + reason + '\'' +
                '}';
    }
}