package com.example.androidmusicplayer;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class Song implements Serializable {
    private static int lastId = 0;
    private int id;
    private String name;
    private String artist;
    private String details;
    private String linkSong;
    private String linkPic;
    private float length;

    public Song(String name, String artist, String details, String linkSong,String linkPic) {
        this.id = ++lastId;
        this.name = name;
        this.artist = artist;
        this.details = details;
        this.linkSong = linkSong;
        this.linkPic = linkPic;
//        Thread thread = new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                length =calcDuration();
//            }
//        };
//        thread.start();


    }



    public float calcDuration(){


        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(linkSong, new HashMap<String, String>());
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int millSecond = Integer.parseInt(durationStr);
        float seconds = (millSecond/1000)%60;
        int min = (millSecond/1000)/60;
        return (min+(seconds/100));

    }


    public int getId() { return id; }

    public String getLinkSong() {
        return linkSong;
    }

    public void setLinkSong(String linkSong) {
        this.linkSong = linkSong;
    }

    public String getLinkPic() {
        return linkPic;
    }

    public void setLinkPic(String linkPic) {
        this.linkPic = linkPic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

}
