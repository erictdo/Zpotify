package main.java.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import main.java.model.Playlist;
import main.java.model.User;
import main.java.utils.Deserializer;
import main.java.utils.Serializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a dispatcher for Login information
 */
public class UserService extends Dispatcher {
    private static final int FRAGMENT_SIZE = 8192;
    private Deserializer deserializer;
    private ArrayList<User> userList;

    public UserService() {
        deserializer = new Deserializer();
        userList = deserializer.deserializeUsersFromJson();
    }

    /**
     * Return User as JSON Object, or empty String if user is not valid
     *
     * @param username the username from client
     * @param password the password from client
     */
    public String login(String username, String password) throws IOException {
        System.out.println("Logging in: Checking if User exists...");

        User validUser = checkValidUser(username, password);
        if (validUser != null) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String userJsonString = gson.toJson(validUser);
            JsonObject userJO = new JsonObject();
            userJO.add("user", userJO);
            return userJO.toString();
        } else {
            System.out.println("User credentials were not found in the database. Returning Empty Json Object");
            return (new JsonObject()).toString();
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
        System.out.println("Checking if User exists...");

        boolean userExists = checkHasUser(name);
        if (userExists) {
            System.out.println("User does not exist. Creating account: " + name);

            User newUser = new User(name, password, new ArrayList<Playlist>());
            userList.add(newUser);
            updateUserDatabase();

            JsonObject isRegisteredJO = new JsonObject();
            isRegisteredJO.addProperty("IsRegistered", true);

            System.out.println("User successfully created: " + name);
            return isRegisteredJO.toString();

        } else {
            System.out.println("User already exists. Returning empty Object");
            return (new JsonObject()).toString();
        }
    }

    /**
     * Adds a new playlist to the user's account
     * @param userName      User's name
     * @param playlistName  Playlist name being added
     * @return
     */
    public String addPlaylist(String userName, String playlistName) {
        System.out.println("Adding Playlist" + playlistName + " to " + userName + "'s profile");
        for (int i = 0 ; i < userList.size() ; i++) {
            if (userList.get(i).getName().equals(userName)) {
                userList.get(i).addPlaylist(playlistName);
                JsonObject playlistJO = new JsonObject();
                playlistJO.add("playlist", playlistJO);
                return playlistJO.toString();
            }
        }
        return (new JsonObject()).toString();
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
    private User checkValidUser(String name, String password) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getName().equals(name) && userList.get(i).getPassword().equals(password)) {
                return userList.get(i);
            }
        }
        return null;
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