package com.example.a327lab1.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
 * Adapter class to add music to playlist.
 */
public class AddMusicToPlaylistAdapter extends RecyclerView.Adapter<AddMusicToPlaylistAdapter.ViewHolder>{

    private static final String TAG = "AddMusicAdapter";

    private UserJSONProcessor userJSONProcessor;

    private String userName;
    private ArrayList<String> playlistNames;
    private Music music;
    private Context context;

    /**
     * Constructor for the music to playlist.
     * @param userName user
     * @param playlistNames playlist's name
     * @param music music
     * @param context
     */
    public AddMusicToPlaylistAdapter(String userName, ArrayList<String> playlistNames, Music music, Context context) {
        this.userName = userName;
        this.playlistNames = playlistNames;
        this.music = music;
        this.context = context;
        this.userJSONProcessor = new UserJSONProcessor(context);
    }

    /**
     * View holder method for music to playlist.
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_playlist_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    /**
     * BindViewHolder method to add music to playlist
     * @param holder viewholder
     * @param position position of the music into playlist.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        final String playlistName = playlistNames.get(position);

        holder.name.setText(playlistName);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to UserPlaylist screen
                Log.d(TAG, "onClick: clicked on: " + playlistName);

                Toast.makeText(context,  music.getSong().getTitle() + " has been added to " + playlistName, Toast.LENGTH_SHORT).show();

                //userJSONProcessor.addMusicToPlaylist(userName, playlistName, music);
                addMusicToPlaylist(userName, playlistName, music);

                ((AddMusicToPlaylistActivity)context).finish();
            }
        });
    }

    /**
     * Get te size of the playlist
     * @return playlist's size
     */
    @Override
    public int getItemCount() {
        return playlistNames.size();
    }

    /**
     * remove playlist's position
     * @param position playlist's position
     */
    public void removeAt(int position) {
        playlistNames.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, playlistNames.size());
    }

    /**
     * View Holder for the recycler view.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        RelativeLayout parentLayout;

        /**
         * View Holder for the emulator.
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.playlist_image);
            name = itemView.findViewById(R.id.playlist_name);
            parentLayout = itemView.findViewById(R.id.parent_layout_playlist);

            //itemView.setOnCreateContextMenuListener(this);
        }

    }

    public String addMusicToPlaylist(String userName, String playlistName, Music music) {
        Gson gson = new Gson();

        JsonObject ret = new JsonObject();

        Proxy proxy = new Proxy(context);
        String[] params = {
                userName,
                playlistName,
                music.getRelease().getId()
        };
        ret = proxy.synchExecution("addMusicToPlaylist", params);

        String responseJO = ret.get("ret").getAsString();

        return responseJO;
    }
}
