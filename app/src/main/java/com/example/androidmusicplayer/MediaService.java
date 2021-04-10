package com.example.androidmusicplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private MediaPlayer player = new MediaPlayer();
    //ArrayList<Song> songs;
    int currentPlaying = 0;
    final int NOTIF_ID = 1;
    List<Song> songs = new ArrayList<Song>();


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


        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);


        String channelId = "channel_id";
        String channelName = "Some channel";

        if(Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channelId);

        builder.setSmallIcon(android.R.drawable.ic_media_play);
        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.music_notif);

        Intent playIntent = new Intent(this,MediaService.class);
        playIntent.putExtra("command","play");
        PendingIntent playPendingIntent  = PendingIntent.getService(this,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn_play,playPendingIntent);
        
        Intent pauseIntent = new Intent(this,MediaService.class);
        pauseIntent.putExtra("command","pause");
        PendingIntent pausePendingIntent  = PendingIntent.getService(this,1,pauseIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn_notif_pause,pausePendingIntent);
        
        Intent nextIntent = new Intent(this,MediaService.class);
        nextIntent.putExtra("command","next");
        PendingIntent nextPendingIntent  = PendingIntent.getService(this,2,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn_notif_next,nextPendingIntent);

        Intent backIntent = new Intent(this,MediaService.class);
        backIntent.putExtra("command","back");
        PendingIntent backPendingIntent  = PendingIntent.getService(this,3,backIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn_notif_back,backPendingIntent);

        Intent closeIntent = new Intent(this,MediaService.class);
        closeIntent.putExtra("command","close");
        PendingIntent closePendingIntent  = PendingIntent.getService(this,4,closeIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn_notif_close,closePendingIntent);

        builder.setContent(remoteViews);
        startForeground(NOTIF_ID,builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String command  = intent.getStringExtra("command");
        songs = intent.getParcelableArrayListExtra("songsList");

        switch (command) {
            case "new_instance":
                if (!player.isPlaying()) {
                    try {
                        player.setDataSource(songs.get(currentPlaying).getLinkSong()); //songs.get(currentPlaying));
                        player.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "play":
                if (!player.isPlaying())
                    player.start();
                break;
            case "next":
                if (player.isPlaying())
                    player.stop();
                playSong(true);
                break;
            case "back":
                if (player.isPlaying())
                    player.stop();
                playSong(false);
                break;
            case "pause":
                if (player.isPlaying())
                    player.pause();
                break;
            case "close":
                stopSelf();

        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void playSong(boolean isNext)  {
        if(isNext) {
            currentPlaying++;
            if (currentPlaying == songs.size()-1)
                currentPlaying = 0;
        }
        else {
            currentPlaying--;
            if(currentPlaying < 0)
                currentPlaying = songs.size() - 1;
        }
        player.reset();
        try {
            player.setDataSource(songs.get(currentPlaying).getLinkSong());
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        playSong(true);
//        currentPlaying++;
////        if(currentPlaying == songsSize)
////            currentPlaying = 1;
//        player.reset();
//        try {
//            player.setDataSource("https://www.syntax.org.il/xtra/bob.m4a");
//            player.prepareAsync();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        player.start();

    }
}
