package com.example.a327lab1.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Playlist {
    @Expose
    private String playlistName;
    @Expose
    private ArrayList<Music> listOfMusic;

    public Playlist(String playlistName, ArrayList<Music> listOfMusic) {
        this.playlistName = playlistName;
        this.listOfMusic = listOfMusic;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public ArrayList<Music> getListOfMusic() {
        return listOfMusic;
    }

    public void setListOfMusic(ArrayList<Music> listOfMusic) {
        this.listOfMusic = listOfMusic;
    }

    public void addMusicToPlaylist(Music music) {
        listOfMusic.add(music);
    }

    public void deleteMusicFromPlaylist(int index) { listOfMusic.remove(index); }
}
