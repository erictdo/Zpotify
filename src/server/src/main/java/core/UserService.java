package main.java.core;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import main.java.model.Music;
import main.java.model.Playlist;
import main.java.model.User;
import main.java.utils.Deserializer;
import main.java.utils.Serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * This class represents a dispatcher for Login information
 */
public class UserService extends Dispatcher {
    private static final int FRAGMENT_SIZE = 8192;
    private Deserializer deserializer;
    private ArrayList<User> userList;
    private ArrayList<Music> musicList;

    public UserService() {
        deserializer = new Deserializer();
        userList = deserializer.deserializeUsersFromJson();
        musicList = deserializer.deserializeSongsFromJson();
    }

    /**
     * Return User as JSON Object, or empty String if user is not valid
     *
     * @param username the username from client
     * @param password the password from client
     */
    public String login(String username, String password) throws IOException {
        System.out.println("Logging in: Checking if User exists...");
        JsonObject responseJO = new JsonObject();

        boolean validUser = checkValidUser(username, password);
        if (validUser) {
            System.out.println("Validated Credentials. Returning True.");
            responseJO.addProperty("isLogin", true);
            return responseJO.toString();
        } else {
            System.out.println("User is not registered. Returning False.");
            responseJO.addProperty("isLogin", false);
            return responseJO.toString();
        }
    }

    /**
     * Registers a new user into the user database
     * @param name              User name
     * @param password          User password
     * @param passwordConfirm   Repeated user password
     * @return
     */
    public String register(String name, String password, String passwordConfirm) {
        System.out.println("Registering a new account...");
        JsonObject responseJO = new JsonObject();

        boolean userExists = checkHasUser(name);
        if (!userExists) {
            User newUser = new User(name, password, new ArrayList<Playlist>());
            userList.add(newUser);
            updateUserDatabase();

            System.out.println("User successfully registered: " + name);
            responseJO.addProperty("isRegistered", true);
            return responseJO.toString();
        } else {
            System.out.println("Error: User is already registered.");
            responseJO.addProperty("isRegistered", false);
            return responseJO.toString();
        }
    }

    public String getListOfPlaylists(String userName) {
        System.out.println("Getting list of user's playlists");
        JsonObject responseJO = new JsonObject();
        Gson gson = new Gson();

        for (int i = 0 ; i < userList.size() ; i++) {
            if (userList.get(i).getName().equals(userName)) {
                String playlistJO = gson.toJson(userList.get(i).getListOfPlaylists());
                return playlistJO;
            }
        }
        responseJO.addProperty("userNotFound", false);
        return responseJO.toString();
    }

    /**
     * Adds a new playlist to the user's account
     * @param userName      User's name
     * @param playlistName  Playlist name being added
     * @return
     */
    public String addPlaylist(String userName, String playlistName) {
        System.out.println("Adding Playlist" + playlistName + " to " + userName + "'s profile");
        JsonObject responseJO = new JsonObject();

        for (int i = 0 ; i < userList.size() ; i++) {
            if (userList.get(i).getName().equals(userName)) {
                userList.get(i).addPlaylist(playlistName);
                updateUserDatabase();

                responseJO.addProperty("addedPlaylist", true);
                return responseJO.toString();
            }
        }
        responseJO.addProperty("userNotFound", false);
        return responseJO.toString();
    }

    public String addMusicToPlaylist(String userName, String playlistName, String musicID) {
        System.out.println("Adding Music to " + userName + "'s playlist, " + playlistName);
        JsonObject responseJO = new JsonObject();

        Gson gson = new Gson();

        for (int i = 0 ; i < userList.size() ; i++) {
            if (userList.get(i).getName().equals(userName)) {
                for (int j = 0 ; j < userList.get(i).getListOfPlaylists().size() ; j++) {
                    if (userList.get(i).getListOfPlaylists().get(j).getPlaylistName().equals(playlistName)) {
                        for (int k = 0 ; k < musicList.size() ; k++) {
                            if (musicList.get(k).getRelease().getId().equals(musicID)) {
                                userList.get(i).getListOfPlaylists().get(j).addMusicToPlaylist(musicList.get(k));
                            }
                        }
                        updateUserDatabase();

                        responseJO.addProperty("addedMusicToPlaylist", true);
                        return responseJO.toString();
                    }
                }
            }
        }
        responseJO.addProperty("addedMusicToPlaylist", false);
        return responseJO.toString();
    }

    public String getUserPlaylist(String userName, String playlistName) {
        Gson gson = new Gson();
        JsonObject responseJO = new JsonObject();

        for (int i = 0 ; i < userList.size() ; i++) {
            if (userList.get(i).getName().equals(userName)) {
                for (int j = 0 ; j < userList.get(i).getListOfPlaylists().size() ; j++) {
                    if (userList.get(i).getListOfPlaylists().get(j).getPlaylistName().equals(playlistName)) {
                        String userPlaylistJO = gson.toJson(userList.get(i).getListOfPlaylists().get(j).getListOfMusic());
                        return userPlaylistJO;
                    }
                }
            }
        }
        responseJO.addProperty("getUserPlaylist", false);
        return responseJO.toString();
    }

    /** ~ Helper methods are below ~ */

    /**
     * Updates the users.json file with the current userList
     */
    private void updateUserDatabase() {
        Serializer.updateUserJson(userList);
    }

    /**
     * Searches for username in the userList
     *
     * @param name  Username being searched
     * @return      User if user exists; Null otherwise
     */
    private boolean checkValidUser(String name, String password) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getName().equals(name) && userList.get(i).getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if User exists in the user list
     * @param name  User name being checked
     * @return      True if user exists; false otherwise
     */
    private boolean checkHasUser(String name) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the user object from passing in username
     * @param userName      User name
     * @return              User object
     */
    public User getUser(String userName) {
        for (int i = 0 ; i < userList.size() ; i++) {
            if (userList.get(i).getName().equals(userName)) {
                return userList.get(i);
            }
        }
        return null;
    }

}