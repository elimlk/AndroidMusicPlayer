package com.example.androidmusicplayer;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.IOException;

public class MediaService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private MediaPlayer player = new MediaPlayer();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player.setOnCompletionListener(this);
        player.setOnPreparedListener(this);
        player.reset();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String songLink = intent.getStringExtra("songLink");
        try {
            player.setDataSource(songLink);
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            if (player.isPlaying())
                player.stop();
            player.release();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopSelf();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        player.start();
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(android.R.drawable.ic_media_play).setContentTitle("Playing Music").setContentText("play 'song Name' enjoy");
        startForeground(01,builder.build());
    }
}
