package co.uk.kenkwok.tulipmania.service;

import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by kekwok on 20/04/2017.
 */

public class ListenerService extends WearableListenerService {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(ListenerService.class.getSimpleName(), "ListenerService onCreate()");
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(ListenerService.class.getSimpleName(), "Button click detected: " + messageEvent.getPath());
    }
}
