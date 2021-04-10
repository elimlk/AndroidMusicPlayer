package com.example.androidmusicplayer;

import android.media.MediaPlayer;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;

public class Song implements Parcelable {
    private static int lastId = 0;
    private int id;
    private String name;
    private String artist;
    private String details;
    private String linkSong;
    private String linkPic;


    private int picResID;
    private float length;

    public Song(String name, String artist, String details, String linkSong,String linkPic) {


//        MediaPlayer player = new MediaPlayer();
//        try {
//            player.setDataSource(linkSong);
//            player.prepareAsync();
//            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    this.len=mp.getDuration();
//
//                }
//            });
//            this.length = len;
//            this.length=player.getDuration();
//            player.release();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        this.id = ++lastId;
        this.name = name;
        this.artist = artist;
        this.details = details;
        this.linkSong = linkSong;
        this.linkPic = linkPic;
        //this.picResID = getDa
    }

    protected Song(Parcel in) {
        id = in.readInt();
        name = in.readString();
        artist = in.readString();
        details = in.readString();
        linkSong = in.readString();
        linkPic = in.readString();
        picResID = in.readInt();
        length = in.readFloat();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public int getPicResID() {
        return picResID;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(artist);
        dest.writeString(details);
        dest.writeString(linkSong);
        dest.writeString(linkPic);
        dest.writeInt(picResID);
        dest.writeFloat(length);
    }
}
