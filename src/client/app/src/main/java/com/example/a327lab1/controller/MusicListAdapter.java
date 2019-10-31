package com.example.a327lab1.controller;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.os.Debug;
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

import com.example.a327lab1.R;
import com.example.a327lab1.model.Music;
import com.example.a327lab1.rpc.CECS327InputStream;
import com.example.a327lab1.rpc.Proxy;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Console;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Classs to show the recycle view of the music list.
 */

class AudioMediaSource extends MediaDataSource{

    private InputStream inputStream2;
    private CECS327InputStream inputStream;
    private String fileName;


    //private CECS327InputStream is;
    public AudioMediaSource(InputStream is)
    {
        inputStream2 = is;
    }

    public AudioMediaSource(String fileName, Context context)
    {
        try
        {
            inputStream = new CECS327InputStream(fileName, context);
        }
        catch(IOException reeeeee)
        {
            reeeeee.printStackTrace();
        }

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

class ByteArrayMediaDataSource extends MediaDataSource {

    private final byte[] data;

    public ByteArrayMediaDataSource(byte []data) {
        assert data != null;
        this.data = data;
    }
    @Override
    public int readAt(long position, byte[] buffer, int offset, int size) throws IOException {
        System.arraycopy(data, (int)position, buffer, offset, size);
        return size;
    }

    @Override
    public long getSize() throws IOException {
        return data.length;
    }

    @Override
    public void close() throws IOException {
        // Nothing to do here
    }
}

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    private static final String TAG = "MusicListAdapter";
    private MediaPlayer mp;
    private MediaDataSource mds;
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
//                    FileDescriptor fd = new FileDescriptor();
//                    AssetManager am = context.getAssets();
//                    AssetFileDescriptor afd = am.openFd("imperial.mp3");

                    //mds = new AudioMediaSource("imperial.mp3", context);
                    //mds = new AudioMediaSource(am.open("imperial.mp3"));
                    byte[] mp3ByteArray = getSongFromServer("imperial.mp3");
                    String filePath = "/data/data/com.example.a327lab1/files/musicfile.3gp";
                    File path=new File(filePath);
                    path.setReadable(true, false);

                    FileOutputStream fos = new FileOutputStream(path);
                    fos.write(mp3ByteArray);
                    fos.close();

                    mp = new MediaPlayer();

                    mds = new AudioMediaSource(filePath, context);

                    mp.setDataSource(mds);

                    mp.prepareAsync();

                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mp.release();
                        }
                    });

                    mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mp.start();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*
                if(mp != null)
                {
                    mp.stop();
                    mp.release();
                    mp.reset();
//                    mp = null;
                }
                else
                {
                    mp.prepareAsync();
                    mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mp.stop();
                            mp.release();
                            mp.reset();
//                            mp = null;
                        }
                    });
                }


                 */

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

    public byte[] getSongFromServer(String songID) {
        Proxy proxy = new Proxy(context);
        JsonObject ret;
        byte[] retByte = {};
        String[] array2 = {songID, "0"};
        do {
            ret = proxy.synchExecution("getSongFragment", array2);
            JsonElement jElement = new JsonParser().parse(ret.get("ret").getAsString());
            ret = jElement.getAsJsonObject();
            byte[] serverRet = ret.get("data").getAsString().getBytes();
            byte[] current = Arrays.copyOf(retByte, retByte.length);
            retByte = new byte[serverRet.length + current.length];
            System.arraycopy(current, 0, retByte, 0, current.length);
            System.arraycopy(serverRet, 0, retByte, current.length, serverRet.length);
            array2[1] = ret.get("currentIndex").getAsString();
        } while(ret.get("keepPulling").getAsString().equals("true"));
        return retByte;
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
