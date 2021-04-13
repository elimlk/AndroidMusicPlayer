package com.example.androidmusicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
    SongAdapter songAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("details",MODE_PRIVATE);
        firstTimeRun = sp.getBoolean("firstTime",true);
        tvSongTitle = findViewById(R.id.tv_songTitle);
        SongsListSingelton.getInstance().updateTextView(tvSongTitle);
        //songs = SongsListSingelton.getInstance().songs;
        if (firstTimeRun){
            initData();
        }else
            loadData();
        RecyclerView recyclerView = findViewById(R.id.recyclerView_songs);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songAdapter = new SongAdapter(this,SongsListSingelton.getInstance().songs);
        songAdapter.setListener(new SongAdapter.SongListener() {
            @Override
            public void onSongClicked(int position, View view) {
                Song clickedSong = SongsListSingelton.getInstance().songs.get(position);

                FragmentManager fm = getSupportFragmentManager();
                DetailsSongFrafment songDetailsFragment = DetailsSongFrafment.newInstance(clickedSong);
                songDetailsFragment.show(fm, "fragment_Details");
            }

            @Override
            public void onSongLongCliked(int position, View view) {

            }

            @Override
            public void onSongPlayed(int postion, View view) {
                view.findViewById(R.id.iv_playFlag).setVisibility(View.VISIBLE);
            }
        });

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN|ItemTouchHelper.UP,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAbsoluteAdapterPosition();
                int toPosition = target.getAbsoluteAdapterPosition();
                Collections.swap(SongsListSingelton.getInstance().songs, fromPosition, toPosition);
                if (SongsListSingelton.getInstance().currentPlayGlobal == fromPosition)
                    SongsListSingelton.getInstance().currentPlayGlobal = toPosition;
                if (SongsListSingelton.getInstance().currentPlayGlobal == toPosition)
                    SongsListSingelton.getInstance().currentPlayGlobal = fromPosition;

                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
                return false;
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                if(actionState == ItemTouchHelper.ACTION_STATE_DRAG)
                    viewHolder.itemView.setAlpha(0.65f);
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                viewHolder.itemView.setAlpha(1f);
                super.clearView(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT | direction == ItemTouchHelper.RIGHT){
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Delete song")
                            .setMessage("Really?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    SongsListSingelton.getInstance().songs.remove(viewHolder.getAbsoluteAdapterPosition());
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
                        intent.putExtra("songsList",SongsListSingelton.getInstance().songs);
                        intent.putExtra("command","new_instance");
                        startService(intent);
                        firstRun=false;
                    }else{
                        Intent intent = new Intent(MainActivity.this,MediaService.class);
                        intent.putExtra("songsList",SongsListSingelton.getInstance().songs);
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
                intent.putExtra("songsList",SongsListSingelton.getInstance().songs);
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
                //new AddSongFragment().show(getSupportFragmentManager(),addSongFragTag);
                FragmentManager fm = getSupportFragmentManager();
                AddSongFragment addSongDialogFragment = AddSongFragment.newInstance("Add song fragment");
                addSongDialogFragment.show(fm, "fragment_add_song");
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
        SongsListSingelton.getInstance().songs.add(s);
        songAdapter.notifyDataSetChanged();

    }

    private void nextSong(){
        Intent intent = new Intent(MainActivity.this,MediaService.class);
        intent.putExtra("songsList",SongsListSingelton.getInstance().songs);
        intent.putExtra("command","next");
        startService(intent);
    }

    private void prevSong(){
        Intent intent = new Intent(MainActivity.this,MediaService.class);
        intent.putExtra("songsList",SongsListSingelton.getInstance().songs);
        intent.putExtra("command","back");
        startService(intent);
    }

    private void initData(){

        Song song1 = new Song("One More Cup of Coffee","Bob Dylan","released on January 5, 1976 by Columbia Records.","https://www.syntax.org.il/xtra/bob.m4a","https://i1.sndcdn.com/artworks-000094489078-rpuzes-t500x500.jpg");
        Song song2 = new Song("Sara","Bob Dylan","released on January 5, 1976 by Columbia Records","https://www.syntax.org.il/xtra/bob1.m4a","https://m.media-amazon.com/images/I/51eA5COaHEL._SS500_.jpg");
        Song song3 = new Song("The Man In ME","Bob Dylan","released on 1970 by Columbia Records","https://www.syntax.org.il/xtra/bob2.mp3","https://miro.medium.com/max/4800/1*_EDEWvWLREzlAvaQRfC_SQ.jpeg");
        Song song4 = new Song("Like A Rolling Stone","Bob Dylan","details","https://drive.google.com/uc?id=1BoAYz801M29wK8YFmjRPcAnWb5xF5LW0&export=download","https://townsquare.media/site/295/files/2015/06/Like-a-Rolling-Stone.jpg");
        Song song5 = new Song("Hey Jude(Live)","The Beatles","details","https://drive.google.com/uc?id=1i5LER7DUNzlMnfk6PEJ6d671XWzW64Jb&export=download","https://m.media-amazon.com/images/M/MV5BZDRiNjliMzYtMzJjNi00ZmQ3LWIyYTAtMGVkMDE4ODVjYjI4XkEyXkFqcGdeQXVyMjUyNDk2ODc@._V1_.jpg");
        Song song6 = new Song("Three Little Birds (Official Video)","Bob Marley & The Wailers","details","https://drive.google.com/uc?id=1TDqnpzCwilf5WfxKSztfyAM6dyZmhwgq&export=download","https://yosmusic.com/wp-content/uploads/2020/12/Bob-Marley-the-Wailers.jpg");
        Song song7 = new Song(" No Women No Cry (Original)","Bob Marley","details","https://drive.google.com/uc?id=1PqbVgi41EpQCJZ3eL5JEBq7T58xC1zoG&export=download","https://www.bobmarley1love.org/wp-content/uploads/2019/03/bob-marley.jpg");
        Song song8 = new Song("What A Wonderful World","Louis Armstrong","details"," https://drive.google.com/uc?id=1okbrwvvDXnEXybJ1MpI7v1rMVJ7PUumx&export=download","https://img.discogs.com/FKnQLXzALNRvHBPu45SW1AaVouA=/fit-in/300x300/filters:strip_icc():format(jpeg):mode_rgb():quality(40)/discogs-images/R-4262037-1360059604-4319.jpeg.jpg");
        Song song9 = new Song("Lay, Lady, Lay","Bob Dylan","details","https://drive.google.com/uc?id=1oztQVyBLQWKb09vNFDsowU3XW2Byd7Fx&export=download","https://img.discogs.com/j02pY0LV-oYpRlbzajg56zmetac=/fit-in/600x600/filters:strip_icc():format(jpeg):mode_rgb():quality(90)/discogs-images/R-3796821-1344788534-2350.jpeg.jpg");


        //https://drive.google.com/uc?id=1BoAYz801M29wK8YFmjRPcAnWb5xF5LW0&export=download
        //https://drive.google.com/file/d/1BoAYz801M29wK8YFmjRPcAnWb5xF5LW0/view?usp=sharing
        //https://drive.google.com/uc?id=1i5LER7DUNzlMnfk6PEJ6d671XWzW64Jb&export=download
        //https://drive.google.com/uc?id=1TDqnpzCwilf5WfxKSztfyAM6dyZmhwgq&export=download
        //https://drive.google.com/uc?id=1PqbVgi41EpQCJZ3eL5JEBq7T58xC1zoG&export=download
        //https://drive.google.com/uc?id=1okbrwvvDXnEXybJ1MpI7v1rMVJ7PUumx&export=download
        //https://drive.google.com/uc?id=1oztQVyBLQWKb09vNFDsowU3XW2Byd7Fx&export=download

        SongsListSingelton.getInstance().songs.add(song1);
        SongsListSingelton.getInstance().songs.add(song2);
        SongsListSingelton.getInstance().songs.add(song3);
        SongsListSingelton.getInstance().songs.add(song4);
        SongsListSingelton.getInstance().songs.add(song5);
        SongsListSingelton.getInstance().songs.add(song6);
        SongsListSingelton.getInstance().songs.add(song7);
        SongsListSingelton.getInstance().songs.add(song8);
        SongsListSingelton.getInstance().songs.add(song9);

    }

    private void loadData() {
        try {
            FileInputStream fis  = openFileInput("songs");
            ObjectInputStream ois  = new ObjectInputStream(fis);
            SongsListSingelton.getInstance().songs = (ArrayList<Song>) ois.readObject();
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
            oos.writeObject(SongsListSingelton.getInstance().songs);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}