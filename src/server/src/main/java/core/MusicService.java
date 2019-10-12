package main.java.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.model.Music;
import main.java.utils.Deserializer;

import java.io.IOException;
import java.util.List;

/**
 * This class represents a dispatcher for Login information
 */
public class MusicService extends Dispatcher {
    private static final int FRAGMENT_SIZE = 8192;
    private Deserializer deserializer;
    private List<Music> musicList;

    public MusicService() {
        deserializer = new Deserializer();
        musicList = deserializer.deserializeSongsFromJson();
    }

    /**
     * Return Song as JSON Object
     */
    public String getAllMusic() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String musicListJsonString = gson.toJson(musicList);
        return musicListJsonString;
    }
}