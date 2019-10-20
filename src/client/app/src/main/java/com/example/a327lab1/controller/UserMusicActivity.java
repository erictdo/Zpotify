package com.example.a327lab1.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.a327lab1.R;
import com.example.a327lab1.model.Music;
import com.example.a327lab1.model.Playlist;
import com.example.a327lab1.rpc.Proxy;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Shows the music information  in the User's playlist.
 */
public class UserMusicActivity extends AppCompatActivity {

    private static final String TAG = "UserMusicActivity";

    //private UserJSONProcessor userJSONProcessor;

    private String userName;
    private String playlistName;
    private ArrayList<Music> userMusicList;

    /**
     * Text view of the music info.
     */
    public TextView title, year, artist;

    /**
     * Method to start up user music page.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_music);

        //userJSONProcessor = new UserJSONProcessor(this);

        initAttributes();
        initRecyclerView();
    }

    /**
     * Method to initialize attributes of the user music activity.
     */
    private void initAttributes() {
        userName = getIntent().getExtras().getString("userName");
        playlistName = getIntent().getExtras().getString("playlistName");
        //userMusicList = userJSONProcessor.getListOfMusicFromPlaylist(userName, playlistName);
        userMusicList = getUserPlaylist();
    }

    /**
     * Recycle View of the user music list.
     */
    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: initRecyclerView");

        RecyclerView recyclerView = findViewById(R.id.recycler_view_user_music);
        UserMusicAdapter adapter = new UserMusicAdapter(this, userMusicList, playlistName, userName);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
    }

    private ArrayList<Music> getUserPlaylist() {
        JsonObject ret = new JsonObject();
        Proxy proxy = new Proxy(this);
        String[] params = {
                userName,
                playlistName
        };
        ret = proxy.synchExecution("getUserPlaylist", params);

        String responseJO = ret.get("ret").getAsString();

        Type musicListType = new TypeToken<ArrayList<Music>>() {}.getType();
        ArrayList<Music> userMusicList = new Gson().fromJson(responseJO, musicListType);

        return userMusicList;
    }
}
