package co.uk.kenkwok.tulipmania.library.websocket.entities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import co.uk.kenkwok.tulipmania.library.websocket.SocketEventTypeEnum;
import okio.ByteString;

/**
 * RxWebSocket library - simple Rx wrapper for OkHttp WebSockets
 * Source: https://github.com/TeamWanari/websocket-android
 */
public class SocketMessageEvent extends SocketEvent {

    private final String text;
    private final ByteString bytes;

    public SocketMessageEvent(@NonNull String message) {
        super(SocketEventTypeEnum.MESSAGE);
        this.text = message;
        this.bytes = null;
    }

    public SocketMessageEvent(@NonNull ByteString bytes) {
        super(SocketEventTypeEnum.MESSAGE);
        this.text = null;
        this.bytes = bytes;
    }

    @Nullable
    public String getText() {
        return text;
    }

    @Nullable
    public ByteString getBytes() {
        return bytes;
    }

    public boolean isText() {
        return bytes == null;
    }

    @Override
    public String toString() {
        return "SocketMessageEvent{" +
                "text='" + text + '\'' +
                ", bytes=" + bytes +
                '}';
    }
}