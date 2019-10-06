package com.example.a327lab1.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.a327lab1.R;
import com.example.a327lab1.model.Music;
import com.example.a327lab1.model.Playlist;

import java.util.ArrayList;

/**
 * Activity class to add music to playlist.
 */
public class AddMusicToPlaylistActivity extends AppCompatActivity {

    private static final String TAG = "AddMusicToPlaylist";

    private UserJSONProcessor userJSONProcessor;

    private RecyclerView recyclerView;

    private String userName;
    private Music music;
    private ArrayList<Playlist> userListOfPlaylists;
    private ArrayList<String> playlistNames;

    /**
     * Save the instance of the music to playlisy.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music_to_playlist);

        userJSONProcessor = new UserJSONProcessor(this);

        initAttributes();
        initUIViews();
        initRecyclerView();
    }

    /**
     * Constructor method for add music to playlist.
     */
    private void initAttributes() {
        userName = getIntent().getExtras().getString("userName");
        music = (Music)getIntent().getSerializableExtra("music");
        userListOfPlaylists = userJSONProcessor.getListOfPlaylistsFromUser(userName);
        playlistNames = getPlaylistNames(userListOfPlaylists);
    }

    private void initUIViews() {
    }

    /**
     * Recycle view to add music to playlist.
     */
    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");

        RecyclerView recyclerView = findViewById(R.id.recycler_view_add_music_to_playlist);
        AddMusicToPlaylistAdapter adapter = new AddMusicToPlaylistAdapter(userName, playlistNames, music, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * List of playlist
     * @param list playlist
     * @return list of playlist
     */
    private ArrayList<String> getPlaylistNames(ArrayList<Playlist> list) {
        ArrayList<String> nameList = new ArrayList<String>();
        for (Playlist playlist : list) {
            nameList.add(playlist.getPlaylistName());
        }
        return nameList;
    }
}
