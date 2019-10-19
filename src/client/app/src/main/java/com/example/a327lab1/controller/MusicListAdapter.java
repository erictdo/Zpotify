package com.example.a327lab1.controller;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaDataSource;
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
import com.example.a327lab1.rpc.CECS327InputStream;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
/**
 * Classs to show the recycle view of the music list.
 */

class AudioMediaSource extends MediaDataSource{

    private InputStream inputStream;
    private String fileName;
    //private CECS327InputStream is;

    public AudioMediaSource(InputStream fileName){


        inputStream = fileName;
    }

    @Override
    public int readAt(long l, byte[] bytes, int i, int i1) throws IOException {
        return inputStream.read(bytes, i, i1);
    }

    @Override
    public long getSize() throws IOException {
        return 0;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    private static final String TAG = "MusicListAdapter";
    private MediaPlayer mp;
    private ArrayList<Music> listOfMusic;
    private String userName;
    private Context context;
    private String currentlyPlaying;

    /**
     * Constructor method for the Music List Adapter.
     * @param context music context
     * @param listOfMusic music list
     * @param userName user
     */
    public MusicListAdapter(Context context, ArrayList<Music> listOfMusic, String userName) {
        this.listOfMusic = listOfMusic;
        this.userName = userName;
        this.context = context;
        this.currentlyPlaying = "";
    }

    /**
     * OnCreate view holder for MusicListAdapter
     * @param parent parent view
     * @param viewType view type
     * @return the view of the music list.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_music_item, parent, false);
        MusicListAdapter.ViewHolder holder = new MusicListAdapter.ViewHolder(view);
        return holder;
    }

    /**
     * Method to bind the ViewHolder in MusicListAdapter
     * @param holder music info
     * @param position position in the list
     */
    @Override
    public void onBindViewHolder(@NonNull MusicListAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.title.setText(listOfMusic.get(position).getSong().getTitle());
        holder.artist.setText(listOfMusic.get(position).getArtist().getName());
        holder.terms.setText(listOfMusic.get(position).getArtist().getTerms());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Play the music!
                Log.d(TAG, "onClick: clicked on: " + listOfMusic.get(position).getSong().getTitle());

                try {
                    FileDescriptor fd = new FileDescriptor();
                    AssetManager am = context.getAssets();
                    AssetFileDescriptor afd = am.openFd("imperial.mp3");

                    InputStream is = new CECS327InputStream("imperial.mp3");
                    mp = new MediaPlayer();

//                    MediaDataSource mds = new AudioMediaSource(am.open("imperial.mp3"));

                    MediaDataSource mds = new AudioMediaSource(is);

                    mp.setDataSource(mds);
                    mp.prepareAsync();
                    mp.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*if (mp != null && !currentlyPlaying.equals(listOfMusic.get(position).getSong().getTitle())) {
                    mp.stop();
                    mp.release();
                    mp = MediaPlayer.create(context,R.raw.imperial);
                    mp.start();
                    currentlyPlaying = listOfMusic.get(position).getSong().getTitle();
                    Toast.makeText(context, currentlyPlaying + " is now playing.", Toast.LENGTH_SHORT).show();
                } else if (mp != null){
                    mp.stop();
                    mp.release();
                    mp = null;
                    Toast.makeText(context, "Stopped playing " + currentlyPlaying, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, listOfMusic.get(position).getSong().getTitle() + " is now playing.", Toast.LENGTH_SHORT).show();
                    mp = MediaPlayer.create(context,R.raw.imperial);
                    mp.start();
                    currentlyPlaying = listOfMusic.get(position).getSong().getTitle();
                    Toast.makeText(context, "Stopped playing " + currentlyPlaying, Toast.LENGTH_SHORT).show();
                }*/

            }
        });
    }

    /**
     * Get method to get the count of items.
     * @return List of music
     */
    @Override
    public int getItemCount() {
        return listOfMusic.size();
    }

    /**
     * View Holder for the Music List Adapter that extends from the Recycle View.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView title;
        TextView artist;
        TextView terms;
        RelativeLayout parentLayout;

        /**
         * View Holder for the Music List Adapter.
         *
         * @param itemView music information
         */
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_music_title);
            artist = itemView.findViewById(R.id.tv_music_artist);
            terms = itemView.findViewById(R.id.tv_music_terms);
            parentLayout = itemView.findViewById(R.id.parent_layout_music);

            itemView.setOnCreateContextMenuListener(this);
        }

        /**
         * Method to create a pop up menu to add songs.
         *
         * @param menu     menu of the pop up
         * @param v        view of the menu
         * @param menuInfo menu info
         */
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select The Action");
            MenuItem addSong = menu.add(1, 1, 1, "Add Song to Playlist");//groupId, itemId, order, title

            addSong.setOnMenuItemClickListener(onEditMenu);
        }

        /**
         * OnClick Listener for the menu item to add music to playlist activity.
         */
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case 1:
                        //Implement Add song to Playlist Feature
                        Intent i = new Intent(context, AddMusicToPlaylistActivity.class);
                        i.putExtra("userName", userName);
                        i.putExtra("music", listOfMusic.get(getLayoutPosition()));
                        context.startActivity(i);
                        break;
                }
                return true;
            }
        };
    }
}
