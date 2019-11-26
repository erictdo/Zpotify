package com.example.a327lab1.controller;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
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
 * Activity's user interface of the Music List.
 */
public class MusicListFragment extends Fragment {

    public static final String TAG = "MusicListFragment";
    public static final int pageSize = 20;

    private RecyclerView recyclerView;

    private TextView totalMusic;
    private TextView pageNumber;
    private ImageView leftBtn;
    private ImageView rightBtn;
    private SearchView searchEntry;

    private ArrayList<Music> musicList;
    private int pageNum;
    private String search;
    private String userName;

    /**
     * OnCreateView method for the music list fragements.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_music_list, container, false);

        initUIViews(view);
        initAttributes();
        initRecyclerView(view);
        updateRecyclerView(view);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pageNum > 1) {
                    pageNum--;
                    musicList = getMusicPageList();
                    updateRecyclerView(view);
                    updatePageNumberView();
                }
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicList.size() == pageSize) {
                    pageNum++;
                    musicList = getMusicPageList();
                    updateRecyclerView(view);
                    updatePageNumberView();
                }
            }
        });

        searchEntry.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                search = s;
                pageNum = 1;
                musicList = getMusicPageList();
                updateRecyclerView(view);
                updatePageNumberView();
                searchEntry.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return view;
    }

    /**
     * View attributes of the music list.
     */
    private void initAttributes() {
        userName = getActivity().getIntent().getExtras().getString("name");
        //musicList = musicJSONProcessor.getListOfMusic();
        search = "";
        pageNum = 1;
        musicList = getMusicPageList();
        //totalMusic.setText(String.valueOf(musicList.size()));
    }

    /**
     * UI view of the music list.
     * @param view music page list
     */
    private void initUIViews(View view) {
        pageNumber = view.findViewById(R.id.tv_page_number);
        leftBtn = view.findViewById(R.id.iv_left_page);
        rightBtn = view.findViewById(R.id.iv_right_page);
        searchEntry = view.findViewById(R.id.search_music);
        totalMusic = view.findViewById(R.id.tv_total_songs);
    }

    /**
     * recycle view of the music list.
     * @param view id of the music list
     */
    private void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: initRecyclerView");

        recyclerView = view.findViewById(R.id.recycler_view_music_list);
    }

    /**
     * update recycle view method.
     * @param view view of the music list
     */
    private void updateRecyclerView(View view) {
        MusicListAdapter adapter = new MusicListAdapter(getContext(), musicList, userName);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager((new LinearLayoutManager(getContext())));
    }

    /**
     * Method to update the page number
     */
    private void updatePageNumberView() {
        int startPageNumber = ((pageNum - 1) * pageSize) + 1;
        int endPageNumber = ((pageNum - 1) * pageSize) + pageSize;
        String pageNumberString = startPageNumber + " - " + endPageNumber;
        pageNumber.setText(pageNumberString);
        //totalMusic.setText(getNumOfSongs());
    }

    /**
     * Getter method of the music search list.
     * @param search search list
     * @return the search result of the music list
     */
    private ArrayList<Music> getSearchMusicList(String search) {
        ArrayList<Music> searchArray = new ArrayList<Music>();
        for (int i = 0 ; i < musicList.size() ; i++) {
            if (musicList.get(i).getArtist().getName().contains(search) ||
                    musicList.get(i).getSong().getTitle().contains(search) ||
                    musicList.get(i).getArtist().getTerms().contains(search)) {
                searchArray.add(musicList.get(i));
            }
        }
        return searchArray;
    }

    private ArrayList<Music> getMusicPageList() {
        JsonObject ret = new JsonObject();
        Proxy proxy = new Proxy(getContext());
        String[] params = {
                search,
                String.valueOf(pageNum),
                String.valueOf(pageSize)
        };
        ret = proxy.synchExecution("getMusicPageList", params);

        String responseJO = ret.get("ret").getAsString();

        Type musicListType = new TypeToken<ArrayList<Music>>() {}.getType();
        ArrayList<Music> retMusicList = new Gson().fromJson(responseJO, musicListType);

        return retMusicList;
    }

}
