package com.example.muvindu.recyclerdemo.Services;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;

import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.muvindu.recyclerdemo.Audio.music;
import com.example.muvindu.recyclerdemo.Fragments.songList_fragment;
import com.example.muvindu.recyclerdemo.Model.Song;
import com.example.muvindu.recyclerdemo.R;
import com.example.muvindu.recyclerdemo.UI.Main2Activity;
import com.example.muvindu.recyclerdemo.adapter.SongAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.muvindu.recyclerdemo.Services.serviceConnection.player;
import static com.example.muvindu.recyclerdemo.UI.Main2Activity.seekBar;
import static com.example.muvindu.recyclerdemo.UI.player_main.small_albumArt;
import static com.example.muvindu.recyclerdemo.adapter.SongAdapter.setText;
import static com.example.muvindu.recyclerdemo.adapter.SongAdapter.songList;

/**
 * Created by Muvindu on 12/14/2016.
 */

public class MediaService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,

        OnAudioFocusChangeListener {



    private static List<Song> songlist;



    public static String listType="none";
    private final IBinder iBinder = new LocalBinder();
    public MediaPlayer mediaPlayer;
    private String file;
    private int resumePosition = 0;
    private AudioManager audioManager;
    private int currentID = -1;


    private boolean shuffle=false;
    private boolean repeatOne =false;
    private boolean repeateAll=false;

    public static final String ACTION_PLAY = "com.valdioveliu.valdio.audioplayer.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.valdioveliu.valdio.audioplayer.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.valdioveliu.valdio.audioplayer.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.valdioveliu.valdio.audioplayer.ACTION_NEXT";
    public static final String ACTION_STOP = "com.valdioveliu.valdio.audioplayer.ACTION_STOP";

    //MediaSession
    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;

    //AudioPlayer notification ID
    private static final int NOTIFICATION_ID = 0116;


    public  int getCurrentID() {
        return currentID;
    }

    public static void setSonglist(List<Song> list,String type) {
        songlist = list;
        listType=type;
    }

    public static String getListType() {
        return listType;
    }

    public void setSongName(){
        Song song;
        song = songlist.get(currentID);
        setText(song.getSongName(),song.getAlbumId(),this.getApplicationContext(),0);
    }

    private BroadcastReceiver playNew = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                file = intent.getExtras().getString("filePath");
                currentID = intent.getExtras().getInt("id");
            } catch (NullPointerException e) {
                stopSelf();
            }

            if (file != null && file != "") {
                stop();
                // mediaPlayer.reset();
                initializePlayer();
            }
        }
    };

    private BroadcastReceiver stopAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            stop();
        }
    };

    private void register_playNewAudio() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(music.Broadcast_PLAY_NEW_AUDIO);
        registerReceiver(playNew, filter);
    }


    private void register_stopAudio() {
        IntentFilter filter = new IntentFilter(Main2Activity.Broadcast_stop);
        registerReceiver(stopAudio, filter);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            stop();
            mediaPlayer.release();
        }
        removeAudioFocus();
        unregisterReceiver(playNew);
        unregisterReceiver(stopAudio);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            file = intent.getExtras().getString("filePath");
            currentID = intent.getExtras().getInt("id");
        } catch (NullPointerException e) {
            stopSelf();
        }

        if (requestAudioFocus() == false) {
            //Could not gain focus
            stopSelf();
        }




                if (file != null && file != "") {
                    initMediaSession();
                    initializePlayer();
                }else{stopSelf();}


        MediaButtonReceiver.handleIntent(mediaSession,intent);
        buildNotification(PlaybackStatus.PLAYING);
        handleIncomingActions(intent);

        register_playNewAudio();
        register_stopAudio();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mediaPlayer == null) initializePlayer();
                else if (!mediaPlayer.isPlaying()) mediaPlayer.start();
                mediaPlayer.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mediaPlayer.isPlaying()) mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }


    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return true;
        }
        return false;
    }

    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager.abandonAudioFocus(this);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        stop();
        stopSelf();
        next();


    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        play();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    public class LocalBinder extends Binder {
        public MediaService getService() {
            return MediaService.this;
        }
    }

    private void initializePlayer() {
        try {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnInfoListener(this);

        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mediaPlayer.setDataSource(file);
            mediaPlayer.prepareAsync();
            buildNotification(PlaybackStatus.PLAYING);
        } catch (IOException e) {
            e.printStackTrace();
            stopSelf();
            Log.e("MYAPP", "exception1", e);
        }

    }





    private void play() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // seekBar.setProgress(0);


        }

    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();

        }
    }

    private void stop() {
        if (mediaPlayer != null) {
            resumePosition = 0;
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }

        }

    }


    public void resume() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(resumePosition);
            mediaPlayer.start();
        }

    }

    public void next(){

        try {
            stopSelf();
            stop();
            Song song;
            mediaPlayer.reset();
            if (currentID<(songlist.size()-1)){
                ++currentID;
            }else if (currentID==(songlist.size()-1)){
                currentID=0;
            }
            song = songlist.get(currentID);
            file = song.getData();
            setText(song.getSongName(),song.getAlbumId(),this.getApplicationContext(),1);

            // mediaPlayer.reset();
            initializePlayer();

            Log.e("MYAPP", "currentID " +currentID) ;
        } catch (Exception e) {
            Log.e("MYAPP", "exception", e);
        }

    }
public void previous(){
    stopSelf();
    stop();
    Song song;
    mediaPlayer.reset();
    if (currentID==0){
        currentID=(songlist.size()-1);
    }else{
        --currentID;
    }
    song = songlist.get(currentID);
    file = song.getData();
    setText(song.getSongName(),song.getAlbumId(),this.getApplicationContext(),2);

    // mediaPlayer.reset();
    initializePlayer();


}

    public boolean isRepeateAll() {
        return repeateAll;
    }

    public void setRepeateAll(boolean repeateAll) {
        this.repeateAll = repeateAll;
    }

    public boolean isRepeatOne() {
        return repeatOne;
    }

    public void setRepeatOne(boolean repeatOne) {
        this.repeatOne = repeatOne;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    public void txt(){
        transportControls.pause();
    }

private void initMediaSession(){

    mediaSession=new MediaSessionCompat(getApplicationContext(),"Session Tg");
    transportControls = mediaSession.getController().getTransportControls();
    //set MediaSession -> ready to receive media commands
    mediaSession.setActive(true);
    mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS|MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

    updateMetaData();

    mediaSession.setCallback(new MediaSessionCompat.Callback() {
        @Override
        public void onPlay() {
            super.onPlay();
            play();
            buildNotification(PlaybackStatus.PLAYING);
        }

        @Override
        public void onPause() {
            super.onPause();
            pause();
            buildNotification(PlaybackStatus.PAUSED);
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
            next();

        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            previous();

        }

        @Override
        public void onStop() {
            super.onStop();
            stop();
        }
    });

}

    private void updateMetaData() {
        final Song song=songlist.get(currentID);
        Bitmap albumArt = BitmapFactory.decodeResource(getResources(),
                R.drawable.albumartxxx); //replace with medias albumArt
        // Update the current metadata
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, song.getArtist())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, song.getAlbum())
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, song.getSongName())
                .build());
    }
    private void buildNotification(PlaybackStatus playbackStatus) {
        final Song song=songlist.get(currentID);
        int notificationAction = android.R.drawable.ic_media_pause;//needs to be initialized
        PendingIntent play_pauseAction = null;

        //Build a new notification according to the current state of the MediaPlayer
        if (playbackStatus == PlaybackStatus.PLAYING) {
            notificationAction = android.R.drawable.ic_media_pause;
            //create the pause action
            play_pauseAction = playbackAction(1);
        } else if (playbackStatus == PlaybackStatus.PAUSED) {
            notificationAction = android.R.drawable.ic_media_play;
            //create the play action
            play_pauseAction = playbackAction(0);
        }

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.albumartxxx); //replace with your own image

        // Create a new Notification
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setShowWhen(false)
                // Set the Notification style
                .setStyle(new NotificationCompat.MediaStyle()
                        // Attach our MediaSession token
                        .setMediaSession(mediaSession.getSessionToken())
                        // Show our playback controls in the compact notification view.
                        .setShowActionsInCompactView(0, 1, 2))

                // Set the Notification color
                .setColor(getResources().getColor(R.color.colorPrimary))
                // Set the large and small icons
                .setLargeIcon(largeIcon)
                .setSmallIcon(android.R.drawable.stat_sys_headset)
                // Set Notification content information
                .setContentText(song.getArtist())
                .setContentTitle(song.getAlbum())
                .setContentInfo(song.getSongName())
                // Add playback actions

                .addAction(android.R.drawable.ic_media_previous, "previous", playbackAction(3))
                .addAction(notificationAction, "pause", play_pauseAction)
                .addAction(android.R.drawable.ic_media_next, "next", playbackAction(2));

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void removeNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private PendingIntent playbackAction(int actionNumber) {
        Intent playbackAction = new Intent(this, MediaService.class);
        switch (actionNumber) {
            case 0:
                // Play
                playbackAction.setAction(ACTION_PLAY);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 1:
                // Pause
                playbackAction.setAction(ACTION_PAUSE);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 2:
                // Next track
                playbackAction.setAction(ACTION_NEXT);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 3:
                // Previous track
                playbackAction.setAction(ACTION_PREVIOUS);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            default:
                break;
        }
        return null;
    }

    private void handleIncomingActions(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
            transportControls.play();
        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
            transportControls.pause();
        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
            transportControls.skipToNext();
        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
            transportControls.skipToPrevious();
        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
            transportControls.stop();
        }
    }
}
