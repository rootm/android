package com.example.muvindu.recyclerdemo.Model;

import android.graphics.Bitmap;
import android.media.Image;

/**
 * Created by Muvindu on 12/4/2016.
 */

public class Song {
    private long albumId;
    private  String data;
    private long id;
    private String songName;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    private String artist;
    private String album;
    private int duration;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }



    public Boolean getFavarite() {
        return favarite;
    }

    public void setFavarite(Boolean favarite) {
        this.favarite = favarite;
    }

    private Boolean favarite;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }





    public Song(){
        this.albumId=-1;
        this.id=-1;
        this.songName="";
        this.favarite=false;
        this.data="";
        this.artist="";
        this.album="";
        this.duration=0;
    }

}
