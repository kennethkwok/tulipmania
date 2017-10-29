package co.uk.kenkwok.tulipmania;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.widget.Toast;

import co.uk.kenkwok.tulipmania.ui.main.MainActivity;

/**
 * Created by kekwok on 21/04/2017.
 */

public class ReplyIntent extends IntentService {

    private Handler handler;

    public ReplyIntent() {
        super(ReplyIntent.class.getName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = null;
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            action = remoteInput.getCharSequence("resultsKey").toString();
        } else {
            //no input received (this shouldn’t happen). handle as appropriate
            action = "";
            return;
        }

        //dismiss the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(MainActivity.Companion.getNOTIFICATION_ID());

        final String actionString = action;
        final Context context = this;

        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Action selected: " + actionString, Toast.LENGTH_LONG).show();
            }
        });
    }
}
