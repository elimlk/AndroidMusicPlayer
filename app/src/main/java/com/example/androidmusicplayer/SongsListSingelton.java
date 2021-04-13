package com.example.androidmusicplayer;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class SongsListSingelton {

    private static SongsListSingelton songsListSingelton =null;
    public ArrayList<Song> songs;
    public int currentPlayGlobal;
    TextView textView;
    private SongsListSingelton()
    {
        songs = new ArrayList<Song>();
        currentPlayGlobal = -1;
    }

    public static SongsListSingelton getInstance()
    {
        if(songsListSingelton == null)
            songsListSingelton = new SongsListSingelton();
        return songsListSingelton;
    }

    public void updateTitleSong(){
        textView.setText(songs.get(currentPlayGlobal).getName());
    }

    public void updateTextView(TextView _textView) {
        textView = _textView;
    }
}
