package eu.coach_yourself.app.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import eu.coach_yourself.app.MainActivity;
import eu.coach_yourself.app.utils.NotificationHelper;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String remainderText = "";
        if (intent != null && intent.hasExtra("remainderText")) {
            remainderText = intent.getStringExtra("remainderText");
        }
        Intent intentnew = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(context, 0, intentnew, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(context, 0, intentnew, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        NotificationHelper notificationHelper = new NotificationHelper(context, remainderText);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        nb.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = null;
        mNotificationManager = notificationHelper.getManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nb.setChannelId(NotificationHelper.channelID);
        }
//        MediaPlayer mp = MediaPlayer.create(context, R.raw.remindersec);
//        mp.start();
        mNotificationManager.notify(1, nb.build());


    }
}