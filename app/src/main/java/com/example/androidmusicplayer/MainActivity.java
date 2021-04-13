package com.example.androidmusicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements AddSongFragment.AddSongEventListener {
    SharedPreferences sp;
    final String addSongFragTag = "addSongFragTag";
    boolean isPlaying = false;
    Boolean firstTimeRun;
    Boolean firstRun = true;
    ImageButton btnPlay;
    ImageButton btnPause;
    ImageButton btnNext;
    ImageButton btnBack;
    ImageButton btnAddSong;
    TextView tvSongTitle;
    ArrayList<Song> songs;
    SongAdapter songAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("details",MODE_PRIVATE);
        firstTimeRun = sp.getBoolean("firstTime",true);
        if (firstTimeRun){
            initData();
        }else
            loadData();
        RecyclerView recyclerView = findViewById(R.id.recyclerView_songs);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songAdapter = new SongAdapter(this,songs);
        songAdapter.setListener(new SongAdapter.SongListener() {
            @Override
            public void onSongClicked(int position, View view) {
                new DetailsSongFrafment(songs.get(position)).show(getSupportFragmentManager(),"SongFragDetailsTag");

            }

            @Override
            public void onSongLongCliked(int position, View view) {

            }
        });

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN|ItemTouchHelper.UP,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAbsoluteAdapterPosition();
                int toPosition = target.getAbsoluteAdapterPosition();
                Collections.swap(songs, fromPosition, toPosition);
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT | direction == ItemTouchHelper.RIGHT){
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Delete song")
                            .setMessage("Really?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    songs.remove(viewHolder.getAbsoluteAdapterPosition());
                                    songAdapter.notifyItemRemoved(viewHolder.getAbsoluteAdapterPosition());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    songAdapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(songAdapter);

        btnPlay = findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    if(firstRun){
                        Intent intent = new Intent(MainActivity.this,MediaService.class);
                        intent.putExtra("songsList",songs);
                        intent.putExtra("command","new_instance");
                        startService(intent);
                        firstRun=false;
                    }else{
                        Intent intent = new Intent(MainActivity.this,MediaService.class);
                        intent.putExtra("songsList",songs);
                        intent.putExtra("command","play");
                        startService(intent);
                    }
                }
            }
        });

        btnPause = findViewById(R.id.btn_pause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MediaService.class);
                intent.putExtra("songsList",songs);
                intent.putExtra("command","pause");
                startService(intent);
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

        btnAddSong = findViewById(R.id.btn_addSong);
        btnAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddSongFragment().show(getSupportFragmentManager(),addSongFragTag);
                //Toast toast = Toast.makeText(MainActivity.this,"add song",Toast.LENGTH_SHORT);
                //toast.show();
            }
        });
    }

    @Override
    protected void onPause() {
        saveData();
        super.onPause();

    }

    @Override
    public void addSong(Song s) {
        songs.add(s);
        songAdapter.notifyDataSetChanged();

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

    private void initData(){
        songs = new ArrayList<Song>();
        Song song1 = new Song("One More Cup of Coffee","Bob Dylan","released on January 5, 1976 by Columbia Records.","https://www.syntax.org.il/xtra/bob.m4a","https://i1.sndcdn.com/artworks-000094489078-rpuzes-t500x500.jpg");
        Song song2 = new Song("Sara","Bob Dylan","released on January 5, 1976 by Columbia Records","https://www.syntax.org.il/xtra/bob1.m4a","https://m.media-amazon.com/images/I/51eA5COaHEL._SS500_.jpg");
        Song song3 = new Song("The Man In ME","Bob Dylan","released on 1970 by Columbia Records","https://www.syntax.org.il/xtra/bob2.mp3","https://miro.medium.com/max/4800/1*_EDEWvWLREzlAvaQRfC_SQ.jpeg");
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

    private void loadData() {
        try {
            FileInputStream fis  = openFileInput("songs");
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

    private void saveData() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("firstTime",false);
        editor.commit();
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