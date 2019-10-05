//Work in Progress
package com.example.a327lab1.models;

import android.support.annotation.NonNull;

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

    public String getSongTitle()
    {
        return song.getSongTitle();
    }

    public String getReleaseDate()
    {
        return release.getReleaseDate();
    }

    public String getArtistName()
    {
        return artist.getArtistName();
    }

    public Release getRelease() { return release; }

    public Artist getArtist() { return artist; }

    public Song getSong() { return song; }
}