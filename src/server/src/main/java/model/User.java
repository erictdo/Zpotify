package main.java.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class User {
    @Expose
    private String name;
    @Expose
    private String password;
    @Expose
    private ArrayList<Playlist> listOfPlaylists;

    public User(String name, String password, ArrayList<Playlist> listOfPlaylists) {
        this.name = name;
        this.password = password;
        this.listOfPlaylists = listOfPlaylists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void deletePlaylist(String index) {
        this.listOfPlaylists.remove(index);
    }

}
