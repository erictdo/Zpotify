package com.example.a327lab1.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a327lab1.R;
import com.example.a327lab1.rpc.Proxy;
import com.google.gson.JsonObject;

/**
 * Class that displays a pop up of the playlist.
 */
public class AddPlaylistPopup extends AppCompatActivity {
    private UserJSONProcessor userJSONProcessor;

    private EditText newPlaylistName;
    private Button okBtn;
    private Button cancelbtn;

    /**
     * Method to initialize the state.
     * @param savedInstanceState state of the pop playlist creation.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_playlist_popup);

        //Bad coding
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initUIViews();

        //userJSONProcessor = new UserJSONProcessor(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int)(dm.widthPixels * .8);
        int height = (int)(dm.heightPixels * .3);

        getWindow().setLayout(width,height);

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * OnClick method for the pop up playlist creation.
             */
            public void onClick(View view) {
                String playlistName = newPlaylistName.getText().toString();
                if (!playlistName.isEmpty()) {
                    String userName = getIntent().getExtras().getString("name");

                    JsonObject ret = new JsonObject();
                    Proxy proxy = new Proxy(AddPlaylistPopup.this);
                    String[] params = {
                            userName,
                            playlistName
                    };
                    ret = proxy.synchExecution("addPlaylist", params);

                    String responseJO = ret.get("ret").getAsString();

                    if (ret == null) {
                        Toast.makeText(AddPlaylistPopup.this, "Something went wrong. Could not send request to server.", Toast.LENGTH_SHORT).show();
                    } else if (responseJO.contains("true")) {
                        Toast.makeText(AddPlaylistPopup.this, "Playlist, " + playlistName + " has been added.", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                    } else if (responseJO.contains("false")) {
                        Toast.makeText(AddPlaylistPopup.this, "Error: There's already a playlist with that name!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddPlaylistPopup.this, "Something went wrong. Server returned something weird.", Toast.LENGTH_SHORT).show();
                    }

                    //userJSONProcessor.addPlaylistToUser(userName, playlistName);
                    finish();

                } else {
                    Toast.makeText(AddPlaylistPopup.this, "Please enter playlist name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * UI view of the pop up of the add to playlist popup.
     */
    private void initUIViews() {
        newPlaylistName = findViewById(R.id.et_playlist_name_input);
        okBtn = findViewById(R.id.btnAddPlaylist_OK);
        cancelbtn = findViewById(R.id.btnAddPlaylist_CANCEL);
    }
}
