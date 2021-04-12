package com.example.androidmusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;

    boolean isPlaying = false;
    Boolean firstTime;
    ImageButton btnPlay;
    ImageButton btnNext;
    ImageButton btnBack;
    TextView tvSongTitle;
    ArrayList<Song> songs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("details",MODE_PRIVATE);
        firstTime = sp.getBoolean("firstTime",true);
        if (firstTime){
            initData();
        }
        loadData();


        RecyclerView recyclerView = findViewById(R.id.recyclerView_songs);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SongAdapter songAdapter = new SongAdapter(this,songs);
        songAdapter.setListener(new SongAdapter.SongListener() {
            @Override
            public void onSongClicked(int position, View view) {
                Toast toast = Toast.makeText(MainActivity.this,songs.get(position).getName(),Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onSongLongCliked(int position, View view) {
                songs.remove(position);
                songAdapter.notifyItemRemoved(position);
            }
        });
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
        Song song4 = new Song("bob4","Bob Dylan","details","https://www.syntax.org.il/xtra/bob.m4a","https://cdn.vox-cdn.com/thumbor/R9BcMgY3tn_Tl_TtgTkOU9YRlFM=/1400x1400/filters:format(jpeg)/cdn.vox-cdn.com/uploads/chorus_asset/file/13399919/dylan_hyden_getty_ringer.jpg");
        Song song5 = new Song("bob5","Bob Dylan","details","https://www.syntax.org.il/xtra/bob1.m4a","https://cdn.vox-cdn.com/thumbor/swceSx93LvCtw2-DYAjE5xBW5KY=/1400x1400/filters:format(jpeg)/cdn.vox-cdn.com/uploads/chorus_asset/file/9757317/BobDylan_Getty_Ringer.jpg");
        Song song6 = new Song("bob6","Bob Dylan","details","https://www.syntax.org.il/xtra/bob2.mp3","https://images-na.ssl-images-amazon.com/images/I/61-xzhcCLOL._SL1200_.jpg");
        Song song7 = new Song("bob7","Bob Dylan","details","https://www.syntax.org.il/xtra/bob.m4a","https://i1.sndcdn.com/artworks-000094489078-rpuzes-t500x500.jpg");
        Song song8 = new Song("bob8","Bob Dylan","details","https://www.syntax.org.il/xtra/bob1.m4a","https://m.media-amazon.com/images/I/51eA5COaHEL._SS500_.jpg");
        Song song9 = new Song("bob9","Bob Dylan","details","https://www.syntax.org.il/xtra/bob2.mp3","https://images-na.ssl-images-amazon.com/images/I/91i%2BepjraeL.jpg");
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
        intent.putExtra("songsList",songs);
        intent.putExtra("command","new_instance");
        //intent.putExtra("postionSong",)
        startService(intent);
    }

    private void nextSong(){
        Intent intent = new Intent(MainActivity.this,MediaService.class);
        intent.putExtra("songsList",songs);
        intent.putExtra("command","next");
        startService(intent);
    }

    private void prevSong(){
        Intent intent = new Intent(MainActivity.this,MediaService.class);
        intent.putExtra("songsList",songs);
        intent.putExtra("command","back");
        startService(intent);
    }

    private void stopMusic() {
        Intent intent = new Intent(this,MediaService.class);
        stopService(intent);

    }

    private void loadData()
    {
        try {
            FileInputStream fis  = openFileInput("person");
            ObjectInputStream ois  = new ObjectInputStream(fis);
            this.songs = (ArrayList<Song>) ois.readObject();
            ois.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("firstTime",false);
        FileOutputStream fos = null;
        try {
            fos = openFileOutput("songs",MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(songs);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}