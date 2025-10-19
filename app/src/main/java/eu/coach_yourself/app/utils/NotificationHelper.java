package eu.coach_yourself.app.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import eu.coach_yourself.app.R;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "vay_app_alarm";
    public static final String channelName = "Voy App";
    private NotificationManager mManager;
    private String remainderText = "";

    public NotificationHelper(Context base, String remainderText) {
        super(base);
        this.remainderText = remainderText;

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createChannel();
//        }
    }

//    @TargetApi(Build.VERSION_CODES.O)
//    private void createChannel() {
//        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
//        getManager().createNotificationChannel(channel);
//    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.remindersec);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH);
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            channel.setSound(sound, att);

            if (mManager != null) {
                mManager.createNotificationChannel(channel);
            }
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.remindersec);

        if (remainderText.equalsIgnoreCase(""))
            remainderText = "In der Wiederholung liegt die Kraft..";

        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Reminder!")
                .setSound(sound)
                .setContentText("\n" +
                        remainderText)
                .setSmallIcon(R.drawable.ic_launcher_foreground);
    }
}
