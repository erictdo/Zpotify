package main.java.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Music implements Serializable {
    @Expose
    private Release release;
    @Expose
    private Artist artist;
    @Expose
    private Song song;

    public Music(Release release, Artist artist, Song song) {
        this.release = release;
        this.artist = artist;
        this.song = song;
    }

    public Release getRelease() { return release; }

    public Artist getArtist() { return artist; }

    public Song getSong() { return song; }
}