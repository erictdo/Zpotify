package com.example.a327lab1.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a327lab1.R;
import com.example.a327lab1.model.Playlist;
import com.example.a327lab1.rpc.Proxy;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Class to show activity's of the user interface play list.
 */
public class PlaylistFragment extends Fragment {

    private static final String TAG = "PlaylistFragment";

    private UserJSONProcessor userJSONProcessor;

    private PlaylistAdapter adapter;

    private String userName;
    private ArrayList<Playlist> listOfPlaylists;
    private ArrayList<String> playlistNames;
    private String newPlaylistName;

    private FloatingActionButton addPlaylistBtn;

    /**
     * OnCreateView method for the playlist fragements.
     * @param inflater inflater view
     * @param container view container
     * @param savedInstanceState state of the emulator
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        initAttributes();
        initUIViews(view);
        initRecyclerView(view);

        addPlaylistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),AddPlaylistPopup.class);
                i.putExtra("name", userName);
                startActivityForResult(i, 10001);
            }
        });
        return view;

//        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback( ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
//                int position_dragged = dragged.getAdapterPosition();
//                int position_target = target.getAdapterPosition();
//
//                Collections.swap(listOfPlaylists, position_dragged, position_target);
//                Collections.swap(playlistNames, position_dragged, position_target);
//
//                adapter.notifyItemMoved(position_dragged, position_target);
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
//
//            }
//        })
    }

    /**
     * View attributes of the music list.
     */
    private void initAttributes() {
        //userJSONProcessor = new UserJSONProcessor(getContext());
        userName = getActivity().getIntent().getExtras().getString("name");
        //listOfPlaylists = userJSONProcessor.getListOfPlaylistsFromUser(userName);
        listOfPlaylists = getListOfPlaylists(userName);
        playlistNames = getPlaylistNames(listOfPlaylists);
    }

    /**
     * UI view of the playlist.
     * @param view playlist page list
     */
    private void initUIViews(View view) {
        addPlaylistBtn = view.findViewById(R.id.btnAddPlaylist);
    }

    /**
     * recycle view of the music list.
     * @param view id of the music list
     */
    private void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: init recyclerview.");

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_playlist);
        adapter = new PlaylistAdapter( userName, playlistNames, view.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager( view.getContext()));
    }

    /**
     * Getter method for the playlist name.
     * @param list of playlist names
     * @return playlist names
     */
    private ArrayList<String> getPlaylistNames(ArrayList<Playlist> list) {
        ArrayList<String> nameList = new ArrayList<String>();
        for (Playlist playlist : list) {
            nameList.add(playlist.getPlaylistName());
        }
        return nameList;
    }

    private ArrayList<Playlist> getListOfPlaylists(String userName) {
        JsonObject ret = new JsonObject();
        Proxy proxy = new Proxy(getContext());
        String[] params = {
                userName
        };
        ret = proxy.synchExecution("getListOfPlaylists", params);

        String responseJO = ret.get("ret").getAsString();

        Type playlistType = new TypeToken<ArrayList<Playlist>>() {}.getType();
        ArrayList<Playlist> retPlaylist = new Gson().fromJson(responseJO, playlistType);

        return retPlaylist;
    }

    /**
     * Activity result method to reset the fragment after getting result from playlist popup.
     * @param requestCode code request
     * @param resultCode code result
     * @param data intent data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 10001) && (resultCode == Activity.RESULT_OK)) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }
}
