package com.example.a327lab1.controller;

import android.content.Context;
import android.content.res.Resources;

import com.example.a327lab1.R;
import com.example.a327lab1.model.Music;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class that processess the music list from JSON file.
 */
public class MusicJSONProcessor {
    private Context context;
    private ArrayList<Music> listOfMusic;

    public MusicJSONProcessor(Context context) {
        this.context = context;
        this.listOfMusic = getMusicFromJSON();
    }

    /**
     * Method to get music list from the JSON  file.
     * @return music list in the JSON file
     */
    private ArrayList<Music> getMusicFromJSON() {
        Resources res = context.getResources();
        InputStream is = res.openRawResource(R.raw.music);
        Scanner scanner = new Scanner(is);

        StringBuilder musicSB = new StringBuilder();
        while (scanner.hasNextLine()) {
            musicSB.append(scanner.nextLine());
        }

        Gson gson = new Gson();

        Type musicType = new TypeToken<ArrayList<Music>>() {}.getType();
        listOfMusic = gson.fromJson(musicSB.toString(), musicType);

        return listOfMusic;
    }

    /**
     * Get method for the ListOfMusic.
     * @return List of music
     */
    public ArrayList<Music> getListOfMusic() {
        return listOfMusic;
    }

    /**
     * Set method for the ListOfMusic.
     * @param listOfMusic List of Music
     */
    public void setListOfMusic(ArrayList<Music> listOfMusic) {
        this.listOfMusic = listOfMusic;
    }
}
