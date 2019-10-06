package com.example.a327lab1.controller;

import android.content.Context;

import com.example.a327lab1.model.Music;
import com.example.a327lab1.model.Playlist;
import com.example.a327lab1.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class UserJSONProcessor {
    private static final String USER_FILE_NAME = "users.json";

    /** Activity's context */
    private Context context;
    /** ArrayList of users */
    private ArrayList<User> listOfUsers;

    /**
     * Constructor for UserJSONProcessor
     * @param context   Activity's context
     */
    public UserJSONProcessor(Context context) {
        this.context = context;
        this.listOfUsers = deserializeUsersFromJSON();
    }

    /**
     * Reads from user.json file and stores it to User ArrayList
     * @return  ArrayList of Users from user.json file
     */
    private ArrayList<User> deserializeUsersFromJSON() {
        if (fileExists()) {

            //Read from user JSON
            StringBuilder userSB = new StringBuilder();
            try {
                File file = new File(context.getFilesDir(), USER_FILE_NAME);
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader br = new BufferedReader(inputStreamReader);
                String line;
                while ((line = br.readLine()) != null) {
                    userSB.append(line);
                    userSB.append("\n");
                }
                br.close();
            } catch (Exception e) {
                System.out.println(e);
            }

            //Convert to StringBuilder to ArrayList<User>
            Gson gson = new Gson();
            Type userType = new TypeToken<ArrayList<User>>(){}.getType();
            listOfUsers = gson.fromJson(userSB.toString(), userType);
        } else {
            listOfUsers = new ArrayList<User>();
        }
        return listOfUsers;
    }

    /**
     * Updates user.json file
     */
    private void writeUsersToJSON() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        String json = gson.toJson(listOfUsers);

        try {

            FileOutputStream fileOut = context.openFileOutput(USER_FILE_NAME, context.MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileOut);
            outputWriter.write(json);
            outputWriter.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Checks if user.json file exists
     * @return  True if file exists; False otherwise
     */
    private boolean fileExists() {
        File file = context.getFileStreamPath(USER_FILE_NAME);
        return !(file == null || !file.exists());
    }

    /**
     * Searches for username in the user.json file
     * @param name  Username being searched
     * @return  True if user exists; False otherwise
     */
    public boolean hasUserName(String name) {
        for (User myUser : listOfUsers) {
            if (myUser.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds playlist to user's account
     * @param userName  User
     * @param playlistName  Playlist being added
     */
    public void addPlaylistToUser(String userName, String playlistName) {
        for (int i = 0 ; i < listOfUsers.size() ; i++) {
            if ( listOfUsers.get(i).getName().equals(userName) ) {
                listOfUsers.get(i).addPlaylist(playlistName);
            }
        }
        writeUsersToJSON();
    }

    /**
     * Method that deletes playlist from user's account
     * @param userName  User
     * @param playlistName  Playlist being deleted
     */
    public void deletePlaylistFromUser(String userName, String playlistName) {
        for (int i = 0 ; i < listOfUsers.size() ; i++) {
            if ( listOfUsers.get(i).getName().equals(userName) ) {
                listOfUsers.get(i).deletePlaylist(i);
            }
        }
        writeUsersToJSON();
    }

    /**
     * Get list of playlist from user.
     * @param userName User's name
     * @return List of playlists from user
     */
    public ArrayList<Playlist> getListOfPlaylistsFromUser(String userName) {
        for (int i = 0 ; i < listOfUsers.size() ; i++) {
            if ( listOfUsers.get(i).getName().equals(userName) ) {
                User user = getUser(userName);
                return user.getListOfPlaylists();
            }
        }
        return new ArrayList<Playlist>();
    }

    /**
     * Adds music to user's playlist
     * @param userName      User's name
     * @param playlistName  Playlist name
     * @param music         Music object being added
     */
    public void addMusicToPlaylist(String userName, String playlistName, Music music) {
        for (int i = 0 ; i < listOfUsers.size() ; i++) {
            if (listOfUsers.get(i).getName().equals(userName)) {
                for (int j = 0 ; j < listOfUsers.get(i).getListOfPlaylists().size() ; j++) {
                    if (listOfUsers.get(i).getListOfPlaylists().get(j).getPlaylistName().equals(playlistName)) {
                        listOfUsers.get(i).getListOfPlaylists().get(j).addMusicToPlaylist(music);
                    }
                }
            }
        }
        writeUsersToJSON();
    }

    /**
     * Gets list of music from the user's playlist
     * @param userName      User's name
     * @param playlistName  Name of User's playlist
     * @return  List of music
     */
    public ArrayList<Music> getListOfMusicFromPlaylist(String userName, String playlistName) {
        for (int i = 0 ; i < listOfUsers.size() ; i++) {
            if (listOfUsers.get(i).getName().equals(userName)) {
                for (int j = 0 ; j < listOfUsers.get(i).getListOfPlaylists().size() ; j++) {
                    if (listOfUsers.get(i).getListOfPlaylists().get(j).getPlaylistName().equals(playlistName)) {
                        return listOfUsers.get(i).getListOfPlaylists().get(j).getListOfMusic();
                    }
                }
            }
        }
        return new ArrayList<Music>();
    }

    /**
     * Deletes music from playlist
     * @param userName  Username
     * @param playlistName  Playlist Name
     * @param musicID   Music (Released) ID
     */
    public void deleteMusicFromPlaylist(String userName, String playlistName, String musicID) {
        for (int i = 0 ; i < listOfUsers.size() ; i++) {
            if (listOfUsers.get(i).getName().equals(userName)) {
                for (int j = 0 ; j < listOfUsers.get(i).getListOfPlaylists().size() ; j++) {
                    if (listOfUsers.get(i).getListOfPlaylists().get(j).getPlaylistName().equals(playlistName)) {
                        for (int k = 0 ; k < listOfUsers.get(i).getListOfPlaylists().get(j).getListOfMusic().size() ; k++) {
                            if (listOfUsers.get(i).getListOfPlaylists().get(j).getListOfMusic().get(k).getRelease().getId().equals(musicID)) {
                                listOfUsers.get(i).getListOfPlaylists().get(j).deleteMusicFromPlaylist(k);
                            }
                        }
                    }
                }
            }
        }
        writeUsersToJSON();
    }

    /**
     * Renames playlist
     * @param userName     Username
     * @param playlistName  PlaylistName
     * @param newPlaylistName
     */
    public void renamePlaylistName(String userName, String playlistName, String newPlaylistName) {
        for (int i = 0 ; i < listOfUsers.size() ; i++) {
            if (listOfUsers.get(i).getName().equals(userName)) {
                for (int j = 0 ; j < listOfUsers.get(i).getListOfPlaylists().size() ; j++) {
                    if (listOfUsers.get(i).getListOfPlaylists().get(j).getPlaylistName().equals(playlistName)) {
                        listOfUsers.get(i).getListOfPlaylists().get(j).setPlaylistName(newPlaylistName);
                    }
                }
            }
        }
    }

    /**
     * Getter method for listOfUsers
     * @return  Array of users
     */
    public ArrayList<User> getListOfUsers() {
        return listOfUsers;
    }

    /**
     * Setter method for listOfUsers
     * @param listOfUsers   Array of users
     */
    public void setListOfUsers(ArrayList<User> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }

    /**
     * Adds user to listOfUsers and writes to userJSON file
     * @param user  User being added to our system
     */
    public void addUser(User user) {
        this.listOfUsers.add(user);
        writeUsersToJSON();
    }

    /**
     * Gets user object based on the username
     * @param name  Username
     */
    public User getUser(String name) {
        for (int i = 0 ; i < listOfUsers.size() ; i++) {
            if (listOfUsers.get(i).getName().equals(name)) {
                return listOfUsers.get(i);
            }
        }
        return null;
    }

}
