package main.java.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.model.Music;
import main.java.utils.Deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents a dispatcher for Login information
 */
public class MusicService extends Dispatcher {
    private static final int FRAGMENT_SIZE = 8192;
    private Deserializer deserializer;
    private ArrayList<Music> musicList;

    public MusicService() {
        deserializer = new Deserializer();
        musicList = deserializer.deserializeSongsFromJson();
    }

    /**
     * Gets all the songs from the song database
     */
    public String getMusicPageList(String search, String pageNumString, String pageSizeString) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        int pageNum = Integer.parseInt(pageNumString);
        int pageSize = Integer.parseInt(pageSizeString);

        List<Music> list;

        // Filter Music using search entry
        if (!search.isEmpty()) {
            Stream<Music> stream = musicList.stream()
                    .filter(m -> m.getSong().getTitle().toLowerCase().contains(search.toLowerCase())
                            || m.getArtist().getName().toLowerCase().contains(search.toLowerCase())
                            || m.getArtist().getTerms().toLowerCase().contains(search.toLowerCase()));

            list = stream.collect(Collectors.toList());

        } else {
            list = musicList;
        }

        // Get music within a page
        int startIndex = ((pageNum - 1) * pageSize) + 1;
        int endIndex;
        if (!list.isEmpty()) {
            if (((pageNum - 1) * pageSize) + pageSize < list.size()) { // Use startMusicIndex + pageSize as endIndex
                endIndex = ((pageNum - 1) * pageSize) + pageSize;
            } else { // Use list size as endIndex
                endIndex = list.size();
            }
        } else {
            return gson.toJson(new ArrayList<Music>()); //Return empty Music list
        }

        ArrayList<Music> filteredMusicList = new ArrayList<Music>(list.subList(startIndex-1,endIndex));

        // Convert to Json String and return the result
        String musicListJsonString = gson.toJson(filteredMusicList);

        return musicListJsonString;
    }

}