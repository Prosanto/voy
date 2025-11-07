package eu.coach_yourself.app.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.io.File;

import eu.coach_yourself.app.R;
import eu.coach_yourself.app.model.Playlist;
import eu.coach_yourself.app.utils.ConstantFunctions;

public class MyMediaPlayerService extends Service {
    private Utilities utils;
    private Handler mHandler = new Handler();
    public static SimpleExoPlayer mediaPlayer = null;
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
    public static String PLAYER_VOLUME = "PLAYER_VOLUME";


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
    //  private AudioManager audioManager;
    private AudioMediaNotificationManager notificationManager;
    private String status;
    private String strAppName;
    private String strLiveBroadcast;
    public static int currentPlaySongs = 0;
    public static Playlist commonPlaylistcontent;

    private MediaPlayer mediaPlayerBackgroundsongs = null;

    public static int currentBackgroundSongs = 0;
    public float setvolume = (float) 0.5;
    private ComponentListener componentListener = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        strAppName = getResources().getString(R.string.app_name);
        strLiveBroadcast = "On Air";
        onGoingCall = false;

        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        PendingIntent pendingItent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, PendingIntent.FLAG_IMMUTABLE);


        //audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        notificationManager = new AudioMediaNotificationManager(this);
        // mediaSession = new MediaSessionCompat(this, getClass().getSimpleName());
        mediaSession = new MediaSessionCompat(this, getClass().getSimpleName(), null, pendingItent);
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

        if (componentListener == null)
            componentListener = new ComponentListener();

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
                        String fileName = ConstantFunctions.trackurlSplit(filePath);
                        File mFile = downloadAndUnzipContent(fileName);
                        if (mFile != null) {
                            filePath = mFile.getAbsolutePath();
                        }

                        songsName = mPlaylistcontent.getTitle();
                        if (isPlaying) {
                            pause();
                            backgrounsongsPaush();
                        } else {
                            notificationManager.updateSongs(songsName);
                            play();
                            backgrounsongsPlay();

                        }
                    } else {
                        currentPlaySongs = postion;
                        filePath = mPlaylistcontent.getTrack_url();
                        String fileName = ConstantFunctions.trackurlSplit(filePath);
                        File mFile = downloadAndUnzipContent(fileName);
                        if (mFile != null) {
                            filePath = mFile.getAbsolutePath();
                        }

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
                int newcurrentBackgroundSongs = intent.getIntExtra("currentBackgroundSongs", 0);

                if (currentBackgroundSongs != newcurrentBackgroundSongs)
                    backgrounsongsStop();

                currentBackgroundSongs = newcurrentBackgroundSongs;
                backgrounsongsPlay();
            } else if (intent.getBooleanExtra(BACKGROUND_STOP, false)) {
                currentBackgroundSongs = intent.getIntExtra("currentBackgroundSongs", 0);
                backgrounsongsStop();
            } else if (intent.getBooleanExtra(PLAYER_VOLUME, false)) {
                int replacesetvolume = intent.getIntExtra("setplayervolume", 0);
                float currentvolume = (float) replacesetvolume / 100;
                if (mediaPlayerBackgroundsongs != null) {
                    mediaPlayerBackgroundsongs.setVolume(currentvolume, currentvolume);
                    //mediaPlayer.setVolume(currentvolume);
                }
                setvolume = currentvolume;


                //backgrounsongsStop();
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
        return (int) mediaPlayer.getContentDuration();
    }

    @Override
    public void onDestroy() {
        stop();
        stopForeground(true);
        unregisterReceiver(becomingNoisyReceiver);
    }

    long totalDuration, currentDuration;
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            totalDuration = mediaPlayer.getContentDuration();
            if (0 > totalDuration)
                totalDuration = 0;

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
            notificationManager.updateSongs(songsName);
            isPlaying = true;
            completeSongs = false;
            if (!isPause) {
                if (mediaPlayer == null) {
                    mediaPlayer = new SimpleExoPlayer.Builder(getApplicationContext()).build();
//
//                    ExoTrackSelection.Factory adaptiveTrackSelectionFactory =
//                            new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
//                    mediaPlayer = ExoPlayerFactory.newSimpleInstance(this,
//                            new DefaultRenderersFactory(this),
//                            new DefaultTrackSelector(adaptiveTrackSelectionFactory),
//                            new DefaultLoadControl());
                }
                //  Uri uri = Uri.parse(filePath);
                // Uri uri = Uri.fromFile(new File(filePath));
//                MediaSource mediaSource = buildMediaSources(uri);

                MediaSource mediaSource = buildMediaSources(filePath);

                mediaPlayer.prepare(mediaSource, true, false);
                mediaPlayer.setPlayWhenReady(true);
                ///mediaPlayer.addListener(new Player.Listener() {});
                mediaPlayer.addListener(componentListener);
                PlaybackParameters playbackParameters = new PlaybackParameters(1.0f, 1.0f);
                mediaPlayer.setPlaybackParameters(playbackParameters);
            } else {
                if (mediaPlayer != null) {
                    mediaPlayer.setPlayWhenReady(true);
                }
            }

            notificationManager.startNotify(PlaybackStatus.PLAYING);
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
                mediaPlayer.setPlayWhenReady(false);
                notificationManager.startNotify(PlaybackStatus.PAUSED);
                LoadOtherBroadCast();
                mHandler.removeCallbacks(mUpdateTimeTask);

            }
            backgrounsongsStop();
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
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);
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
                    mediaPlayerBackgroundsongs.prepare();
                    mediaPlayerBackgroundsongs.start();
                    isbackgroundPlay = true;

                } else {

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
                    setAudioVolume();
                }

            }
        } catch (Exception ex) {
        }

    }

    public void backgrounsongsPaush() {
        if (mediaPlayerBackgroundsongs != null) {
            mediaPlayerBackgroundsongs.pause();
        }
        isbackgroundPlay = false;
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

    private MediaSource buildMediaSources(String filePath) {

        if (filePath.startsWith("http")) {
            Uri uri = Uri.parse(filePath);
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(
                    new DefaultDataSourceFactory(getApplicationContext(), "exoplayer-codelab")
            ).createMediaSource(MediaItem.fromUri(uri));

//            ExtractorMediaSource audioSource = new ExtractorMediaSource.Factory(
//                            new DefaultHttpDataSourceFactory("exoplayer-codelab")).
//                            createMediaSource(uri);
            return  mediaSource;

        } else {

            Uri uri = Uri.fromFile(new File(filePath));
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(
                    new DefaultDataSourceFactory(getApplicationContext(), "ua")
            ).createMediaSource(MediaItem.fromUri(uri));
            return  mediaSource;


//            return new ExtractorMediaSource(uri,
//                    new DefaultDataSourceFactory(this, "ua"),
//                    new DefaultExtractorsFactory(), null, null);
        }

    }

    public class ComponentListener implements Player.Listener {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case Player.STATE_ENDED:
                    Log.e("completeSongs.....","are"+completeSongs);
                    completeSongs = true;
                    Intent intent1 = new Intent("my-event");
                    intent1.putExtra("isplay", isPlaying);
                    intent1.putExtra("buffering", buffering);
                    intent1.putExtra("currentDuration", "");
                    intent1.putExtra("totalDuration", "");
                    intent1.putExtra("completeSongs", completeSongs);
                    intent1.putExtra("progress", 0);
                    intent1.putExtra("currentPlaySongs", currentPlaySongs);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);
                    currentPlaySongs = 0;
                    stop();
                    notificationManager.cancelNotify();

                    break;
                case Player.STATE_READY:
                    if (playWhenReady) {
                        isPlaying = true;
                        notificationManager.startNotify(PlaybackStatus.PLAYING);
                    } else {
                        isPlaying = false;
                        notificationManager.startNotify(PlaybackStatus.PAUSED);
                    }
                    break;
                case Player.STATE_BUFFERING:

                    break;
                case Player.STATE_IDLE:

                    break;


            }
        }

    }

    public void setAudioVolume() {
        if (mediaPlayerBackgroundsongs != null) {
            mediaPlayerBackgroundsongs.setVolume(setvolume, setvolume);
        }
    }

    public File downloadAndUnzipContent(String FolderName) {
        File d = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), getAlbumName());
        if (!d.exists()) {
            d.mkdirs();
        }
        String path = d.getAbsolutePath().concat("/").concat(FolderName);
        File myDir = new File(path);
        if (myDir.isFile()) {
            return myDir;
        } else {
            return null;
        }

//        File dir = null;
//        if (SDK_INT >= Build.VERSION_CODES.R) {
//            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
//            File myDir = new File(root + "/" + getAlbumName() + "/" + FolderName);
//            if (myDir.isFile()) {
//                dir = myDir;
//            }
//        } else {
//            String root = Environment.getExternalStorageDirectory().toString();
//            File myDir = new File(root + "/" + getAlbumName() + "/" + FolderName);
//            if (myDir.isFile()) {
//                dir = myDir;
//            }
//        }
//        return dir;
    }

    private String getAlbumName() {
        return "voy";
    }


}
