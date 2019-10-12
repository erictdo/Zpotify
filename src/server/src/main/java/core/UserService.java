package main.java.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import main.java.model.User;
import main.java.utils.Deserializer;
import main.java.utils.Serializer;

import java.io.IOException;
import java.util.List;

/**
 * This class represents a dispatcher for Login information
 */
public class UserService extends Dispatcher {
    private static final int FRAGMENT_SIZE = 8192;
    private Deserializer deserializer;
    private List<User> userList;

    public UserService() {
        deserializer = new Deserializer();
        userList = deserializer.deserializeUsersFromJson();
    }

    /**
     * Refreshes User list
     */
    public void refreshUserList() {
        userList = deserializer.deserializeUsersFromJson();
    }

    /**
     * Updates the users.json file with the current userList
     */
    public void updateUserDatabase() {
        Serializer.updateUserJson(userList);
    }

    /**
     * Return User as JSON Object, or empty String if user is not valid
     *
     * @param username the username from client
     * @param password the password from client
     */
    public String login(String username, String password) throws IOException {
        User validUser = checkValidUser(username, password);
        if (validUser != null) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String userJsonString = gson.toJson(validUser);
            return userJsonString;
        } else {
            System.out.println("User credentials did not match in the database. Returning Empty Json Object");
            return (new JsonObject()).toString();
        }
    }

    /**
     * Searches for username in the userList
     *
     * @param name Username being searched
     * @return User if user exists; Null otherwise
     */
    public User checkValidUser(String name, String password) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getName().equals(name) && userList.get(i).getPassword().equals(password)) {
                return userList.get(i);
            }
        }
        return null;
    }


}