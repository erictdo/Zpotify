package com.example.a327lab1.controller;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a327lab1.R;
import com.example.a327lab1.model.Music;

import java.util.ArrayList;

/**
 * Class to display the list of music and info.
 */
public class UserMusicAdapter extends RecyclerView.Adapter<UserMusicAdapter.ViewHolder> {

    private static final String TAG = "UserMusicAdapter";

    private MediaPlayer mp;
    private ArrayList<Music> listOfMusic;
    private String playlistName;
    private String userName;
    private Context context;
    private String currentlyPlaying;

    /**
     * Constructor method. Shows the list of the music in the User's playlist.
     * @param context
     * @param listOfMusic
     */
    public UserMusicAdapter(Context context, ArrayList<Music> listOfMusic, String playlistName, String userName) {
        this.listOfMusic = listOfMusic;
        this.context = context;
        this.playlistName = playlistName;
        this.userName = userName;
    }

    /**
     * OnCreate view holder for UserMusicAdapter
     * @param parent parent view
     * @param viewType view type
     * @return view of the user's music
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_music_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    /**
     * Method to bind the ViewHolder in UserMusicAdapter
     * @param holder user music info
     * @param position position in the list
     */
    @Override
    public void onBindViewHolder(@NonNull UserMusicAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.title.setText(listOfMusic.get(position).getSongTitle());
        holder.artist.setText(listOfMusic.get(position).getArtistName());
        holder.date.setText(listOfMusic.get(position).getReleaseDate());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * Onclick for the user music.
             */
            public void onClick(View view) {
                //Play the music!
                Log.d(TAG, "onClick: clicked on: " + listOfMusic.get(position).getSongTitle());

                if (mp != null && !currentlyPlaying.equals(listOfMusic.get(position).getSongTitle())) {
                    mp.stop();
                    mp.release();
                    mp = MediaPlayer.create(context,R.raw.imperial);
                    mp.start();
                    currentlyPlaying = listOfMusic.get(position).getSongTitle();
                    Toast.makeText(context, currentlyPlaying + " is now playing.", Toast.LENGTH_SHORT).show();
                } else if (mp != null){
                    mp.stop();
                    mp.release();
                    mp = null;
                    Toast.makeText(context, "Stopped playing " + currentlyPlaying, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, listOfMusic.get(position).getSongTitle() + " is now playing.", Toast.LENGTH_SHORT).show();
                    mp = MediaPlayer.create(context,R.raw.imperial);
                    mp.start();
                    currentlyPlaying = listOfMusic.get(position).getSongTitle();
                    Toast.makeText(context, "Stopped playing " + currentlyPlaying, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Getter method for the list of music.
     * @return list of music
     */
    @Override
    public int getItemCount() {
        return listOfMusic.size();
    }

    /**
     * Method to remove a playlist at a specific position.
     * @param position of the playlist
     */
    public void removeAt(int position) {
        listOfMusic.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listOfMusic.size());
    }

    /**
     * Method to hold the recycle view of the user music.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener  {
        TextView title;
        TextView artist;
        TextView date;
        RelativeLayout parentLayout;

        /**
         * User music information
         * @param itemView item view of user music
         */
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_user_music_title);
            artist = itemView.findViewById(R.id.tv_user_music_artist);
            date = itemView.findViewById(R.id.tv_user_music_date);
            parentLayout = itemView.findViewById(R.id.parent_layout_user_music);

            itemView.setOnCreateContextMenuListener(this);
        }

        /**
         * Method for the onclick menu option.
         * @param menu menu
         * @param v view of the menu
         * @param menuInfo menu info
         */
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select The Action");
            MenuItem removeSong = menu.add(1, 1, 1, "Remove Song from Playlist");//groupId, itemId, order, title

            removeSong.setOnMenuItemClickListener(onEditMenu);
        }

        /**
         * On click listener for the user music menu.
         */
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case 1:
                        //Implement Remove song from playlist feature
                        Toast.makeText(context, "Implement Remove Song From Playlist function", Toast.LENGTH_SHORT).show();
                        int musicIndex = getAdapterPosition();
                        String musicID = listOfMusic.get(musicIndex).getRelease().getId();
                        UserJSONProcessor userJSONProcessor = new UserJSONProcessor(context);
                        userJSONProcessor.deleteMusicFromPlaylist(userName,playlistName,musicID);
                        removeAt(musicIndex);
                        break;
                }
                return true;
            }
        };
    }
}
