package main.java.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import main.java.model.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Deserializer {
  private InputStream MUSIC_FILE;
  private InputStream USER_FILE;

  /**
   * Constructor for the deserializer
   */
  public Deserializer () {
    USER_FILE = Deserializer.class.getResourceAsStream("/user.json");
    MUSIC_FILE = Deserializer.class.getResourceAsStream("/music.json");

  }

  /**
   * Returns and stores a dictionary of songs that are playable (i.e. song mp3
   * files that exist in the music directory).
   *
   * @return A dictionary of existing songs.
   */
  public ArrayList<Music> deserializeSongsFromJson() {
    ArrayList<Music> songs = new ArrayList<>();

    Gson gson = new Gson();
    BufferedReader br = new BufferedReader(new InputStreamReader(MUSIC_FILE));

    JsonArray jsonArray = gson.fromJson(br, JsonArray.class);

    for (JsonElement jsonElement : jsonArray) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      Release release = gson.fromJson(jsonObject.get("release"), Release.class);
      Artist artist = gson.fromJson(jsonObject.get("artist"), Artist.class);
      Song song = gson.fromJson(jsonObject.get("song"), Song.class);

      songs.add(new Music(release, artist, song));
    }

    return songs;
  }

  /**
   * Reads from user.json file and stores it to User ArrayList
   * 
   * @return ArrayList of Users from user.json file
   */
  public ArrayList<User> deserializeUsersFromJson() {
    ArrayList<User> users = new ArrayList<>();
    try {
      Gson gson = new Gson();
      //BufferedReader br = new BufferedReader(new InputStreamReader(USER_FILE));
      BufferedReader br = new BufferedReader(new FileReader(new File("user.json")));

      JsonArray jsonArray = gson.fromJson(br, JsonArray.class);
      if (jsonArray != null) {
        for (JsonElement jsonElement : jsonArray) {
          JsonObject jsonObject = jsonElement.getAsJsonObject();
          String name = gson.fromJson(jsonObject.get("name"), String.class);
          String password = gson.fromJson(jsonObject.get("password"), String.class);

          Type playlistType = new TypeToken<ArrayList<Playlist>>() {}.getType();
          ArrayList<Playlist> playlists = gson.fromJson(jsonObject.get("listOfPlaylists"), playlistType);

          users.add(new User(name, password, playlists));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return users;
  }
}