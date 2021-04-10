package com.example.androidmusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    boolean isPlaying = false;
    ImageButton btnPlay;
    ImageButton btnNext;
    ImageButton btnBack;
    List<Song> songs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();

        RecyclerView recyclerView = findViewById(R.id.recyclerView_songs);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SongAdapter songAdapter = new SongAdapter(this,songs);
        recyclerView.setAdapter(songAdapter);

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

        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSong();
            }
        });

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevSong();
            }
        });


    }
    private void initData(){
        songs = new ArrayList<Song>();
        Song song1 = new Song("bob1","Bob Dylan","details","https://www.syntax.org.il/xtra/bob.m4a","https://i1.sndcdn.com/artworks-000094489078-rpuzes-t500x500.jpg");
        Song song2 = new Song("bob2","Bob Dylan","details","https://www.syntax.org.il/xtra/bob1.m4a","https://m.media-amazon.com/images/I/51eA5COaHEL._SS500_.jpg");
        Song song3 = new Song("bob3","Bob Dylan","details","https://www.syntax.org.il/xtra/bob2.mp3","https://miro.medium.com/max/4800/1*_EDEWvWLREzlAvaQRfC_SQ.jpeg");
        Song song4 = new Song("bob4","Bob Dylan","details","https://www.syntax.org.il/xtra/bob.m4a","https://i1.sndcdn.com/artworks-000094489078-rpuzes-t500x500.jpg");
        Song song5 = new Song("bob5","Bob Dylan","details","https://www.syntax.org.il/xtra/bob1.m4a","https://m.media-amazon.com/images/I/51eA5COaHEL._SS500_.jpg");
        Song song6 = new Song("bob6","Bob Dylan","details","https://www.syntax.org.il/xtra/bob2.mp3","https://miro.medium.com/max/4800/1*_EDEWvWLREzlAvaQRfC_SQ.jpeg");
        Song song7 = new Song("bob7","Bob Dylan","details","https://www.syntax.org.il/xtra/bob.m4a","https://i1.sndcdn.com/artworks-000094489078-rpuzes-t500x500.jpg");
        Song song8 = new Song("bob8","Bob Dylan","details","https://www.syntax.org.il/xtra/bob1.m4a","https://m.media-amazon.com/images/I/51eA5COaHEL._SS500_.jpg");
        Song song9 = new Song("bob9","Bob Dylan","details","https://www.syntax.org.il/xtra/bob2.mp3","https://miro.medium.com/max/4800/1*_EDEWvWLREzlAvaQRfC_SQ.jpeg");
        songs.add(song1);
        songs.add(song2);
        songs.add(song3);
        songs.add(song4);
        songs.add(song5);
        songs.add(song6);
        songs.add(song7);
        songs.add(song8);
        songs.add(song9);

    }

    private void playMusic() {
        Intent intent = new Intent(MainActivity.this,MediaService.class);
        intent.putParcelableArrayListExtra("songsList", (ArrayList<? extends Parcelable>) songs);
        intent.putExtra("command","new_instance");
        startService(intent);
    }

    private void nextSong(){
        Intent intent = new Intent(MainActivity.this,MediaService.class);
        intent.putParcelableArrayListExtra("songsList", (ArrayList<? extends Parcelable>) songs);
        intent.putExtra("command","next");
        startService(intent);
    }

    private void prevSong(){
        Intent intent = new Intent(MainActivity.this,MediaService.class);
        intent.putParcelableArrayListExtra("songsList", (ArrayList<? extends Parcelable>) songs);
        intent.putExtra("command","back");
        startService(intent);
    }

    private void stopMusic() {
        Intent intent = new Intent(this,MediaService.class);
        stopService(intent);

    }
}