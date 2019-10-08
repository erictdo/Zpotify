package java.util;

import com.google.gson.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.model.Playlist;
import java.model.User;
import java.net.URL;

public class Serializer {
    public void updateUserJson(List<User> users) {
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