package com.example.androidmusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    boolean isPlaying = false;
    Button btnPlay;

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
                    btnPlay.setText("play"); // change text from hard coded to "values/strings"
                } else {
                    playMusic();
                    btnPlay.setText("Stop"); // change text from hard coded to "values/strings"
                }
                isPlaying = !isPlaying;
            }
        });


    }

    private void playMusic() {
        Intent intent = new Intent(this,MediaService.class);
        intent.putExtra("songLink","https://www.syntax.org.il/xtra/bob.m4a");
        startService(intent);
    }

    private void stopMusic() {
        Intent intent = new Intent(this,MediaService.class);
        stopService(intent);

    }
}