package main.java.core;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import main.java.dfs.DFS;
import main.java.model.Artist;
import main.java.model.Music;
import main.java.model.Release;
import main.java.model.Song;
import main.java.utils.Deserializer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
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

    private DFS dfs;

    public MusicService() {
        deserializer = new Deserializer();
        musicList = deserializer.deserializeSongsFromJson();
        try {
            if (dfs==null){
                dfs = new DFS(3000);
                dfs.join("127.0.0.1", 2000);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * Gets all the songs from the song database (For Assignment 2 - Server)
//     */
//    public String getMusicPageList(String search, String pageNumString, String pageSizeString) {
//        System.out.println("Getting music page list...");
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//
//        int pageNum = Integer.parseInt(pageNumString);
//        int pageSize = Integer.parseInt(pageSizeString);
//
//        List<Music> list;
//
//        // Filter Music using search entry
//        if (!search.isEmpty()) {
//            Stream<Music> stream = musicList.stream()
//                    .filter(m -> m.getSong().getTitle().toLowerCase().contains(search.toLowerCase())
//                            || m.getArtist().getName().toLowerCase().contains(search.toLowerCase())
//                            || m.getArtist().getTerms().toLowerCase().contains(search.toLowerCase()));
//
//            list = stream.collect(Collectors.toList());
//
//
//        } else {
//            list = musicList;
//        }
//
//        // Get music within a page
//        int startIndex = ((pageNum - 1) * pageSize) + 1;
//        int endIndex;
//        if (!list.isEmpty()) {
//            if (((pageNum - 1) * pageSize) + pageSize < list.size()) { // Use startMusicIndex + pageSize as endIndex
//                endIndex = ((pageNum - 1) * pageSize) + pageSize;
//            } else { // Use list size as endIndex
//                endIndex = list.size();
//            }
//        } else {
//            return gson.toJson(new ArrayList<Music>()); //Return empty Music list
//        }
//
//        ArrayList<Music> filteredMusicList = new ArrayList<Music>(list.subList(startIndex-1,endIndex));
//
//        // Convert to Json String and return the result
//        String musicListJsonString = gson.toJson(filteredMusicList);
//
//        return musicListJsonString;
//    }

    /**
     * Gets all the songs from the song database (For Assignment 3 - Chords)
     */
    public String getMusicPageList(String search, String pageNumString, String pageSizeString) {
        System.out.println("Getting music page list...");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        int pageNum = Integer.parseInt(pageNumString);
        int pageSize = Integer.parseInt(pageSizeString);

        List<Music> list;
        if (!search.isEmpty()) {
            try {
                list = search(search);
            } catch (Exception e) {
                e.printStackTrace();
                list = new ArrayList<Music>();
            }
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

    public List<Music> search(String text) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<Music> tracks = null;
        DFS.FileJson fileToSearch = null;
        ArrayList<SearchThread> searchThreads = new ArrayList<>();
        String songs = "";

        int j = 0;

        try {
            DFS.FilesJson files = dfs.readMetaData();
            DFS.PagesJson dir = null;
            for (DFS.FileJson file : files.getFiles()) {
                if (file.getName().equals("music.json")) {
                    fileToSearch = file;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < fileToSearch.getPages().size(); i++) {
            SearchThread t = new SearchThread(i, fileToSearch, dfs, text, j);
            Thread thread = new Thread(t);
            searchThreads.add(t);
            thread.start();
        }

        for (int i = 0; i < searchThreads.size(); i++) {
            songs += searchThreads.get(i).getResults();
        }

        System.out.println(songs);

        Type musicListType = new TypeToken<ArrayList<Music>>() {}.getType();
        ArrayList<Music> musicListResult = gson.fromJson(songs, musicListType);

        return musicListResult;
    }


}