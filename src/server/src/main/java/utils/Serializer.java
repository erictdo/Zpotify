package main.java.utils;

import com.google.gson.*;
import main.java.model.User;

import java.io.*;
import java.net.URL;
import java.util.List;

public class Serializer {
    //public static final URL USER_FILE_PATH = Serializer.class.getResource("/user.json");

    public static void updateUserJson(List<User> users) {

        try {
            //PrintWriter outputWriter = new PrintWriter(new FileWriter(USER_FILE_PATH.getFile()));
            PrintWriter outputWriter = new PrintWriter(new FileWriter(new File("user.json")));

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            gson.toJson(users, outputWriter);

            outputWriter.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}