package java.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.model.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

public class Deserializer {
  public static final URL MUSIC_FILE = Deserializer.class.getResource("/music.json");
  public static final URL USER_FILE = Deserializer.class.getResource("/user.json");

  private List<Music> musicDatabase;
  private List<User> userDatabase;

  public Deserializer() {
    userDatabase = deserializeUsersFromJson();
    musicDatabase = deserializeSongsFromJson();
  }

  /**
   * Returns and stores a dictionary of songs that are playable (i.e. song mp3
   * files that exist in the music directory).
   *
   * @return A dictionary of existing songs.
   * @throws FileNotFoundException
   * @throws IOException
   */
  private List<Music> deserializeSongsFromJson() {
    List<Music> songs = new ArrayList<>();

    try {
      Gson gson = new Gson();
      BufferedReader br = new BufferedReader(new InputStreamReader(MUSIC_FILE.openStream()));

      JsonArray jsonArray = gson.fromJson(br, JsonArray.class);

      for (JsonElement jsonElement : jsonArray) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Release release = gson.fromJson(jsonObject.get("release"), Release.class);
        Artist artist = gson.fromJson(jsonObject.get("artist"), Artist.class);
        Song song = gson.fromJson(jsonObject.get("song"), Song.class);

        songs.add(new Music(release, artist, song));
      }

      return songs;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Reads from user.json file and stores it to User ArrayList
   * 
   * @return ArrayList of Users from user.json file
   */
  private List<User> deserializeUsersFromJson() {
    List<User> users = new ArrayList<>();

    try {
      Gson gson = new Gson();
      BufferedReader br = new BufferedReader(new InputStreamReader(USER_FILE.openStream()));

      JsonArray jsonArray = gson.fromJson(br, JsonArray.class);

      for (JsonElement jsonElement : jsonArray) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = gson.fromJson(jsonObject.get("name"), String.class);
        String password = gson.fromJson(jsonObject.get("password"), String.class);

        Type playlistType = new TypeToken<ArrayList<Playlist>>(){}.getType();
        ArrayList<Playlist> playlists = gson.fromJson(jsonObject.get("listOfPlaylists"), playlistType);

        users.add(new User(name, password, playlists));
      }

      return users;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Returns all music in the music list
   *
   * @return
   */
  public List<Music> getMusicDatabase() {
    return musicDatabase;
  }

  /**
   * Returns all users in the user list
   *
   * @return
   */
  public List<User> getUserDatabase() {
    return userDatabase;
  }
}