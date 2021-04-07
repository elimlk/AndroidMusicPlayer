package com.example.androidmusicplayer;

public class Song {
    private static int lastId = 0;
    private int id;
    private String name;
    private String artist;
    private String details;
    private String linkSong;
    private String linkPic;
    private float length;

    public Song(String name, String artist, String details, String linkSong,String linkPic, float length) {
        this.id = ++lastId;
        this.name = name;
        this.artist = artist;
        this.details = details;
        this.linkSong = linkSong;
        this.linkPic = linkPic;
        this.length = length;
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
