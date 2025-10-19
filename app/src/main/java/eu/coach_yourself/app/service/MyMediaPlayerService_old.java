package eu.coach_yourself.app.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.HashMap;
import java.util.Map;

import eu.coach_yourself.app.R;
import eu.coach_yourself.app.model.Playlist;

public class MyMediaPlayerService_old extends Service {
    private Utilities utils;
    private Handler mHandler = new Handler();
    private MediaPlayer mediaPlayer = null;
    public static boolean isPlaying = false;
    public static boolean isbackgroundPlay = false;

    private boolean isPause = false;
    public static String START_PLAY = "START_PLAY";
    public static String SEEK_TO = "SEEK_TO";
    public static String PREVIOUS_PLAY = "PREVIOUS_PAY";
    public static String NEXT_PLAY = "NEXT_PLAY";
    public static String STOPP_LAY = "STOP_PLAY";
    public static String PLAY_BaCKWARD = "PLAY_BaCKWARD";
    public static String PLAY_FORWARD = "PLAY_FORWARD";
    public static String BACKGROUND_PLAY = "PLAY_BACKGROUND";
    public static String BACKGROUND_STOP = "STOP_BACKGROUND";


    public static final String ACTION_PLAY = "com.mcakir.audio.player.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.mcakir.audio.player.ACTION_PAUSE";
    public static final String ACTION_STOP = "com.mcakir.audio.player.ACTION_STOP";
    public String filePath = "";
    public String songsName = "";

    public boolean buffering = true;
    public boolean completeSongs = false;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;
    private boolean onGoingCall = false;
    private TelephonyManager telephonyManager;
    private WifiManager.WifiLock wifiLock;
    private AudioManager audioManager;
    private AudioMediaNotificationManager notificationManager;
    private String status;
    private String strAppName;
    private String strLiveBroadcast;
    public static int currentPlaySongs = 0;
    public static Playlist commonPlaylistcontent;

    private MediaPlayer mediaPlayerBackgroundsongs = null;

    public static int currentBackgroundSongs = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        strAppName = getResources().getString(R.string.app_name);
        strLiveBroadcast = "On Air";
        onGoingCall = false;
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //notificationManager = new AudioMediaNotificationManager(this);
        mediaSession = new MediaSessionCompat(this, getClass().getSimpleName());
        transportControls = mediaSession.getController().getTransportControls();
        mediaSession.setActive(true);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "...")
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, strAppName)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, strLiveBroadcast)
                .build());
        mediaSession.setCallback(mediasSessionCallback);
        wifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mcScPAmpLock");
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        registerReceiver(becomingNoisyReceiver, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        utils = new Utilities();
        if (null != intent && null != intent.getExtras()) {
            if (intent.getBooleanExtra(START_PLAY, false)) {
                int postion = intent.getIntExtra("postion", 0);
                if (commonPlaylistcontent != null) {
                    Playlist mPlaylistcontent = commonPlaylistcontent;
                    if (currentPlaySongs == postion) {
                        filePath = mPlaylistcontent.getTrack_url();
                        songsName = mPlaylistcontent.getTitle();
                        if (isPlaying)
                            pause();
                        else {
                            notificationManager.updateSongs(songsName);
                            play();
                        }
                    } else {
                        currentPlaySongs = postion;
                        filePath = mPlaylistcontent.getTrack_url();
                        songsName = mPlaylistcontent.getTitle();
                        notificationManager.updateSongs(songsName);
                        notificationManager.updateSongs(songsName);

                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                            mediaPlayer = null;
                            isPlaying = false;
                            isPause = false;
                        }
                        completeSongs = false;
                        Intent intent1 = new Intent("my-event");
                        intent1.putExtra("isplay", isPlaying);
                        intent1.putExtra("buffering", buffering);
                        intent1.putExtra("currentDuration", "");
                        intent1.putExtra("totalDuration", "");
                        intent1.putExtra("completeSongs", completeSongs);
                        intent1.putExtra("progress", 0);
                        intent1.putExtra("currentPlaySongs", currentPlaySongs);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);
                        notificationManager.cancelNotify();
                        play();

                    }

                }

            } else if (intent.getBooleanExtra(STOPP_LAY, false)) {
                // unregisterReceiver(becomingNoisyReceiver);
                currentPlaySongs = 0;
                stop();
                notificationManager.cancelNotify();

            } else if (intent.getBooleanExtra(PREVIOUS_PLAY, false)) {
                stop();
            } else if (intent.getBooleanExtra(NEXT_PLAY, false)) {
                stopForNextSongs();
            } else if (intent.getBooleanExtra(SEEK_TO, false)) {
                if (mediaPlayer != null) {
                    boolean seekTuch = intent.getBooleanExtra("seekTuch", false);
                    if (seekTuch) {
                        mHandler.removeCallbacks(mUpdateTimeTask);
                    } else {
                        int seek = intent.getIntExtra("seek", 0);
                        int currentPosition = utils.progressToTimer(seek, getDuration());
                        seek(currentPosition);
                        mHandler.postDelayed(mUpdateTimeTask, 100);
                    }
                }

            } else if (intent.getBooleanExtra(PLAY_BaCKWARD, false)) {
                backAudio();
            } else if (intent.getBooleanExtra(PLAY_FORWARD, false)) {
                goaheadAudio();
            } else if (intent.getBooleanExtra(BACKGROUND_PLAY, false)) {
                currentBackgroundSongs = intent.getIntExtra("currentBackgroundSongs", 0);
                backgrounsongsPlay();
            } else if (intent.getBooleanExtra(BACKGROUND_STOP, false)) {
                currentBackgroundSongs = intent.getIntExtra("currentBackgroundSongs", 0);
                backgrounsongsStop();
            }

        }
        if (null != intent) {
            String action = intent.getAction();
            if (action != null && action.equalsIgnoreCase(ACTION_PLAY)) {
                transportControls.play();
            } else if (action != null && action.equalsIgnoreCase(ACTION_PAUSE)) {
                transportControls.pause();
            } else if (action != null && action.equalsIgnoreCase(ACTION_STOP)) {
                transportControls.stop();
            }
        }
        return Service.START_STICKY;
    }

    public void updateProgressBar() {
        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    public void seek(int msec) {
        if (mediaPlayer != null) {
            if (msec >= getDuration()) {
                msec = getDuration();
            } else if (msec <= 0) {
                msec = 0;
            }
            mediaPlayer.seekTo(msec);
        }
    }

    public void backAudio() {
        if (mediaPlayer != null) {
            totalDuration = mediaPlayer.getDuration();
            currentDuration = mediaPlayer.getCurrentPosition();
            int currentDurationValue = (int) (currentDuration - (30 * 1000));
            if (totalDuration >= currentDurationValue)
                mediaPlayer.seekTo(currentDurationValue);
            else
                mediaPlayer.seekTo((int) totalDuration);


        }


    }

    public void goaheadAudio() {
        if (mediaPlayer != null) {
            totalDuration = mediaPlayer.getDuration();
            currentDuration = mediaPlayer.getCurrentPosition();
            int currentDurationValue = (int) (currentDuration + (30 * 1000));
            if (totalDuration >= currentDurationValue)
                mediaPlayer.seekTo(currentDurationValue);
            else
                mediaPlayer.seekTo((int) totalDuration);


        }
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public void onDestroy() {
        stop();
        stopForeground(true);
    }

    long totalDuration, currentDuration;
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            totalDuration = mediaPlayer.getDuration();

            Log.e("totalDuration", "are" + totalDuration);

            currentDuration = mediaPlayer.getCurrentPosition();
            String total = utils.milliSecondsToTimer(totalDuration);
            String current = utils.milliSecondsToTimer(currentDuration);
            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
            Intent intent1 = new Intent("my-event");
            intent1.putExtra("isplay", isPlaying);
            intent1.putExtra("buffering", buffering);
            intent1.putExtra("currentDuration", current);
            intent1.putExtra("totalDuration", total);
            intent1.putExtra("progress", progress);
            intent1.putExtra("completeSongs", completeSongs);
            intent1.putExtra("currentPlaySongs", currentPlaySongs);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);
            mHandler.postDelayed(this, 100);
        }
    };

    private void play() {
        if (!isPlaying) {
            Log.e("songsName", songsName);
            Log.e("songsName", filePath);
            notificationManager.updateSongs(songsName);
            isPlaying = true;
            completeSongs = false;
            if (!isPause) {
                try {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "audio/mp3");
                    // change content type if necessary
                    headers.put("Accept-Ranges", "bytes");
                    headers.put("Status", "206");
                    headers.put("Cache-control", "no-cache");

                    Uri uri = Uri.parse(filePath);
                    mediaPlayer = new MediaPlayer();
                    // mediaPlayer.setDataSource(filePath);
                    mediaPlayer.setDataSource(getApplicationContext(), uri, headers);
                  //  mediaPlayer.prepareAsync();
                    mediaPlayer.prepare();
                } catch (Exception ex) {
                }
            }
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }});

            notificationManager.startNotify(PlaybackStatus.PLAYING);
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer arg0) {
                    completeSongs = true;
                    Intent intent1 = new Intent("my-event");
                    intent1.putExtra("isplay", isPlaying);
                    intent1.putExtra("buffering", buffering);
                    intent1.putExtra("currentDuration", "");
                    intent1.putExtra("totalDuration", "");
                    intent1.putExtra("completeSongs", completeSongs);
                    intent1.putExtra("progress", 0);
                    intent1.putExtra("currentPlaySongs", currentPlaySongs);
                    LocalBroadcastManager.getInstance(getApplicationContext())
                            .sendBroadcast(intent1);
                    currentPlaySongs = 0;
                    notificationManager.cancelNotify();
                    stop();


                }
            });
            mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                        buffering = true;
                    }
                    if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
                        buffering = false;
                    }
                    return false;
                }
            });
//            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mediaPlayer) {
//                    Log.e("time", mediaPlayer.getDuration() + " ..");
//                }
//            });
            isPause = false;
            updateProgressBar();
        }
    }

    public void LoadOtherBroadCast() {
        totalDuration = mediaPlayer.getDuration();
        currentDuration = mediaPlayer.getCurrentPosition();
        String total = utils.milliSecondsToTimer(totalDuration);
        String current = utils.milliSecondsToTimer(currentDuration);
        int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
        Intent intent1 = new Intent("my-event");
        intent1.putExtra("isplay", isPlaying);
        intent1.putExtra("buffering", buffering);
        intent1.putExtra("currentDuration", current);
        intent1.putExtra("totalDuration", total);
        intent1.putExtra("progress", progress);
        intent1.putExtra("completeSongs", completeSongs);
        intent1.putExtra("currentPlaySongs", currentPlaySongs);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(intent1);
    }

    private void pause() {
        if (isPlaying) {
            isPlaying = false;
            isPause = true;
            completeSongs = false;
            if (mediaPlayer != null) {
                mediaPlayer.pause();
                notificationManager.startNotify(PlaybackStatus.PAUSED);
                LoadOtherBroadCast();
                mHandler.removeCallbacks(mUpdateTimeTask);

            }
        }
    }

    private void stopForNextSongs() {
        if (isPlaying || isPause) {
            isPlaying = false;
            isPause = false;
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
            completeSongs = false;
            Intent intent1 = new Intent("my-event");
            intent1.putExtra("isplay", isPlaying);
            intent1.putExtra("buffering", buffering);
            intent1.putExtra("currentDuration", "");
            intent1.putExtra("totalDuration", "");
            intent1.putExtra("completeSongs", completeSongs);
            intent1.putExtra("progress", 0);
            intent1.putExtra("currentPlaySongs", currentPlaySongs);

            LocalBroadcastManager.getInstance(getApplicationContext())
                    .sendBroadcast(intent1);
        }
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    private void stop() {
        if (isPlaying || isPause) {
            isPlaying = false;
            isPause = false;
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                notificationManager.startNotify(PlaybackStatus.STOPPED);
                backgrounsongsStop();
            }
            completeSongs = false;
            Intent intent1 = new Intent("my-event");
            intent1.putExtra("isplay", isPlaying);
            intent1.putExtra("buffering", buffering);
            intent1.putExtra("currentDuration", "");
            intent1.putExtra("totalDuration", "");
            intent1.putExtra("completeSongs", completeSongs);
            intent1.putExtra("progress", 0);
            intent1.putExtra("currentPlaySongs", currentPlaySongs);
            LocalBroadcastManager.getInstance(getApplicationContext())
                    .sendBroadcast(intent1);

        }
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_OFFHOOK
                    || state == TelephonyManager.CALL_STATE_RINGING) {
                if (!isPlaying()) return;
                onGoingCall = true;
                pause();
            } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                if (!onGoingCall) return;
                onGoingCall = false;
                play();
            }
        }
    };

    private MediaSessionCompat.Callback mediasSessionCallback = new MediaSessionCompat.Callback() {
        @Override
        public void onPause() {
            super.onPause();
            pause();
        }

        @Override
        public void onStop() {
            super.onStop();

            if (isPlaying || isPause) {
                isPlaying = false;
                isPause = false;
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                completeSongs = false;
                Intent intent1 = new Intent("my-event");
                intent1.putExtra("isplay", isPlaying);
                intent1.putExtra("buffering", buffering);
                intent1.putExtra("currentDuration", "");
                intent1.putExtra("totalDuration", "");
                intent1.putExtra("completeSongs", completeSongs);
                intent1.putExtra("progress", 0);
                intent1.putExtra("currentPlaySongs", currentPlaySongs);

                LocalBroadcastManager.getInstance(getApplicationContext())
                        .sendBroadcast(intent1);
            }
            notificationManager.cancelNotify();
            mHandler.removeCallbacks(mUpdateTimeTask);

        }

        @Override
        public void onPlay() {
            super.onPlay();
            completeSongs = false;
            play();

        }
    };

    public void backgrounsongsPlay() {
        try {
            if (!isPlaying()) {
                return;
            }
            if (currentBackgroundSongs == 0) {
                backgrounsongsStop();
            } else {
                Uri mediaPath = null;
                if (mediaPlayerBackgroundsongs != null) {
                    mediaPlayerBackgroundsongs.release();
                    mediaPlayerBackgroundsongs = null;
                }
                if (currentBackgroundSongs == 1)
                    mediaPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.meer);
                else if (currentBackgroundSongs == 2)
                    mediaPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.wald);
                else if (currentBackgroundSongs == 3)
                    mediaPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.regen);
                else if (currentBackgroundSongs == 4)
                    mediaPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.musik);

                mediaPlayerBackgroundsongs = new MediaPlayer();
                mediaPlayerBackgroundsongs.setDataSource(getApplicationContext(), mediaPath);
                mediaPlayerBackgroundsongs.prepare();
                mediaPlayerBackgroundsongs.start();
                mediaPlayerBackgroundsongs.setLooping(true);
                isbackgroundPlay = true;
            }
        } catch (Exception ex) {
        }

    }

    public void backgrounsongsStop() {

        if (mediaPlayerBackgroundsongs != null) {
            mediaPlayerBackgroundsongs.stop();
            mediaPlayerBackgroundsongs.release();
            mediaPlayerBackgroundsongs = null;
        }
        isbackgroundPlay = false;
    }

    public static boolean isPlaying() {
        return isPlaying;
    }

    public static boolean isbackgroundPlay() {
        return isbackgroundPlay;
    }


    public MediaSessionCompat getMediaSession() {
        return mediaSession;
    }

    private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pause();
        }
    };

}
