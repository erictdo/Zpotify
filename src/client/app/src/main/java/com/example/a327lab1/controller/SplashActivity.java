package com.example.a327lab1.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.a327lab1.R;
import com.example.a327lab1.model.User;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Activity page on startup of the application.
 */
public class SplashActivity extends AppCompatActivity {

    /**
     * Method to startup the splash activity page.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final User user = new User (SplashActivity.this);

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (user.getName() != "") {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    i.putExtra("name", user.getName());
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        },2000);
    }

}
