package eu.coach_yourself.app.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import eu.coach_yourself.app.MainActivity;
import eu.coach_yourself.app.R;
public class AudioMediaNotificationManager {

    public static final int NOTIFICATION_ID = 555;
    private final String PRIMARY_CHANNEL = "PRIMARY_CHANNEL_ID_D";
    private final String PRIMARY_CHANNEL_NAME = "PRIMARY_D";
    private MyMediaPlayerService service;
    private String strAppName, strLiveBroadcast;
    private Resources resources;
    private NotificationManagerCompat notificationManager;

    public AudioMediaNotificationManager(MyMediaPlayerService service) {
        this.service = service;
        this.resources = service.getResources();
        strAppName = resources.getString(R.string.app_name);
        strLiveBroadcast = "On Air";
        notificationManager = NotificationManagerCompat.from(service);
    }

    public void startNotify(String playbackStatus) {
        Bitmap largeIcon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher);
        int icon = R.drawable.exo_icon_pause;
        Intent playbackAction = new Intent(service, MyMediaPlayerService.class);
        playbackAction.setAction(MyMediaPlayerService.ACTION_PAUSE);
        PendingIntent action ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            action = PendingIntent.getActivity(service, 1, playbackAction, PendingIntent.FLAG_MUTABLE);
        } else {
            action =PendingIntent.getService(service, 1, playbackAction, 0);
        }

        if (playbackStatus.equals(PlaybackStatus.PAUSED)) {
            icon = R.drawable.exo_icon_play;
            playbackAction.setAction(MyMediaPlayerService.ACTION_PLAY);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                action = PendingIntent.getActivity(service, 2, playbackAction, PendingIntent.FLAG_MUTABLE);
            } else {
                action = PendingIntent.getService(service, 2, playbackAction, 0);
            }
        }

        Intent stopIntent = new Intent(service, MyMediaPlayerService.class);
        stopIntent.setAction(MyMediaPlayerService.ACTION_STOP);

        PendingIntent stopAction ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            stopAction = PendingIntent.getActivity(service, 3, stopIntent, PendingIntent.FLAG_MUTABLE);
        } else {
            stopAction = PendingIntent.getService(service, 3, stopIntent, 0);
        }


        Intent intent = new Intent(service, MainActivity.class);
       // intent.setAction(Intent.ACTION_MAIN);
        //intent.addCategory(Intent.CATEGORY_LAUNCHER);
       // playbackAction.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
       // playbackAction.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

//        PendingIntent pendingIntent = PendingIntent.getActivity(service, 0, intent, 0);
        PendingIntent pendingIntent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(service, 0, intent, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(service, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }

        notificationManager.cancel(NOTIFICATION_ID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(PRIMARY_CHANNEL, PRIMARY_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            manager.createNotificationChannel(channel);
        }
        MediaSessionCompat.Token token = service.getMediaSession().getSessionToken();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(service, PRIMARY_CHANNEL)
                .setAutoCancel(false)
                .setContentTitle(strAppName)
                .setContentText(strLiveBroadcast)
                .setLargeIcon(largeIcon)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(icon, "pause", action)
                .addAction(R.drawable.exo_icon_stop, "stop", stopAction)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(token)
                        .setShowActionsInCompactView(0, 1)
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(stopAction))
                .setWhen(System.currentTimeMillis());

        service.startForeground(NOTIFICATION_ID, builder.build());
    }

    public void cancelNotify() {
        service.stopForeground(true);
    }

    public void updateSongs(String songs) {
        strLiveBroadcast = songs;
    }


}
