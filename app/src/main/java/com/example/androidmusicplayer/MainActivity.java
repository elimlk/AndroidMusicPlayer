package com.example.androidmusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    boolean isPlaying = false;
    ImageButton btnPlay;
//    ArrayList<Song> songs = new ArrayList<Song>();
//    Song song1 = new Song("bob1","bob","details","https://www.syntax.org.il/xtra/bob.m4a","https://",5.34);
//    songs.add(song1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        btnPlay = findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    stopMusic();
                    btnPlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                } else {
                    playMusic();
                    btnPlay.setImageResource(R.drawable.ic_baseline_stop_circle_24);
                }
                isPlaying = !isPlaying;
            }
        });


    }
    private void initData(){
        //Song song = new Song("bob1","bob Dylen","details",)

    }

    private void playMusic() {
        Intent intent = new Intent(MainActivity.this,MediaService.class);
        intent.putExtra("songLink",getString(R.string.lnk_bob1));
        intent.putExtra("command","new_instance");
        startService(intent);
    }

    private void stopMusic() {
        Intent intent = new Intent(this,MediaService.class);
        stopService(intent);

    }
}