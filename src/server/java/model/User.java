package com.example.a327lab1.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class User {
    @Expose
    private String name;
    @Expose
    private String password;
    @Expose
    private ArrayList<Playlist> listOfPlaylists;

    Context context;
    SharedPreferences sharedPreferences;

    public User(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
    }

    public User(String name, String password, ArrayList<Playlist> listOfPlaylists, Context context) {
        this.name = name;
        this.password = password;
        this.listOfPlaylists = listOfPlaylists;
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
    }

    public String getNameOnly() {
        return name;
    }

    public String getName() {
        name = sharedPreferences.getString("userdata","");
        return name;
    }

    public void setName(String name) {
        this.name = name;
        sharedPreferences.edit().putString("userdata","name").commit();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Playlist> getListOfPlaylists() {
        return listOfPlaylists;
    }

    public void setListOfPlaylists(ArrayList<Playlist> listOfPlaylists) {
        this.listOfPlaylists = listOfPlaylists;
    }

    public void addPlaylist(String name) {
        Playlist newPlaylist = new Playlist(name, new ArrayList<Music>());
        this.listOfPlaylists.add(newPlaylist);
    }

    public void deletePlaylist(int index) {
        this.listOfPlaylists.remove(index);
    }

    public void removeUser() {
        sharedPreferences.edit().clear().commit();
    }
}
