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

                Toast.makeText(context,  music.getSongTitle() + " has been added to " + playlistName, Toast.LENGTH_SHORT).show();

                userJSONProcessor.addMusicToPlaylist(userName, playlistName, music);

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

//        @Override
//        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            menu.setHeaderTitle("Select The Action");
//            MenuItem rename = menu.add(1, 1, 1, "Rename");//groupId, itemId, order, title
//            MenuItem delete = menu.add(1, 2, 2, "Delete");
//
//            rename.setOnMenuItemClickListener(onEditMenu);
//            delete.setOnMenuItemClickListener(onEditMenu);
//        }
//
//        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//                switch (item.getItemId()) {
//                    case 1:
//                        //Implement Rename feature
//                        Toast.makeText(context, "Implement Rename function", Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case 2:
//                        int playlistIndex = getAdapterPosition();
//                        String playlistName = playlistNames.get(playlistIndex);
//                        UserJSONProcessor userJSONProcessor = new UserJSONProcessor(context);
//                        userJSONProcessor.deletePlaylistFromUser(userName,playlistName);
//                        removeAt(playlistIndex);
//                        break;
//                }
//                return true;
//            }
//        };
    }
}
