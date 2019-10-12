package main.java.utils;

import com.google.gson.*;
import main.java.model.User;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class Serializer {
    public static void updateUserJson(List<User> users) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        String json = gson.toJson(users);
        try {
            FileOutputStream fileOut = new FileOutputStream("/user.json");
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileOut);
            outputWriter.write(json);
            outputWriter.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}