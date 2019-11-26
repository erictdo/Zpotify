package main.java.core;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import main.java.dfs.DFS;
import main.java.dfs.DFSCommand;
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
                dfs.lists();
                dfs.create("music");
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                var chunks = DFSCommand.getMusicJsonChunks("music.json", 10);

                System.out.println("Adding pages to music.json...");
                int i = 0;
                for (var chunk : chunks) {
                    String jsonStr = null;
                    try {
                        jsonStr = gson.toJson(chunk);
                        dfs.append("music", jsonStr);

                        System.out.println(String.format("Creating page [%d/%d]", ++i, chunks.size()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Done");
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
        Gson gson = new Gson();

        DFS.FileJson fileToSearch = null;
        ArrayList<SearchThread> searchThreads = new ArrayList<>();
        ArrayList<Music> musicResult = new ArrayList<>();

        try {
            DFS.FilesJson files = dfs.readMetaData();
            for (DFS.FileJson file : files.getFiles()) {
                if (file.getName().equals("music")) {
                    fileToSearch = file;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Search each page in each thread
        ArrayList<Thread> threadList = new ArrayList<Thread>();
        for (int i = 0; i < fileToSearch.getPages().size(); i++) {
            SearchThread t = new SearchThread(i, fileToSearch, dfs, text);
            Thread thread = new Thread(t);
            searchThreads.add(t);
            threadList.add(thread);
            threadList.get(i).start();
        }

        // Wait for all threads to finish
        for (int i = 0 ; i < threadList.size() ; i++) {
            threadList.get(i).join();
        }

        //Combine search results from each thread
        for (int i = 0; i < searchThreads.size(); i++) {
            musicResult.addAll(searchThreads.get(i).getResults());
        }

        return musicResult;
    }


}