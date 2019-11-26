package com.example.a327lab1.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a327lab1.R;

/**
 * Main Activity that display the home page of the application.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navView;
    private DrawerLayout drawer;
    private TextView userName;
    private Toolbar toolbar;

    public UserJSONProcessor userJSONProcessor;

    /**
     * Method to display the home page.
     * @param savedInstanceState instance of the current state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUIViews();

        String name = getIntent().getExtras().getString("name");
        userName.setText(name);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Open Playlist Screen by default
        if (savedInstanceState == null) {
            toolbar.setTitle("Playlists");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new PlaylistFragment()).commit();
            navView.setCheckedItem(R.id.nav_playlist);
        }
    }

    /**
     * Navigation method on the home page.
     * @param menuItem item's in the menu
     * @return menu items of the home page
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_playlist:
                toolbar.setTitle("Playlists");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PlaylistFragment()).commit();
                break;
            case R.id.nav_musiclist:
                toolbar.setTitle("Song List");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MusicListFragment()).commit();
                break;
            case R.id.nav_logout:
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                Toast.makeText(this,"You have been logged out", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Method to back press from the main activity.
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * View method for the user info in Main Activity.
     */
    private void initUIViews() {
        navView = (NavigationView)findViewById(R.id.nav_view) ;
        navView.setNavigationItemSelectedListener(this);
        View headerView = navView.getHeaderView(0);

        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        userName = (TextView)headerView.findViewById(R.id.tvUserName);
    }

    /**
     * Method to resume main activity.
     */
    @Override
    public void onResume()
    {
        super.onResume();
        userJSONProcessor = new UserJSONProcessor(this);
    }

}
