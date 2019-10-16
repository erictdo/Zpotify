package com.example.a327lab1.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a327lab1.R;
import com.example.a327lab1.model.User;
import com.example.a327lab1.rpc.Proxy;
import com.google.gson.JsonObject;

/**
 * Login Class Activity.
 */
public class LoginActivity extends AppCompatActivity {

    private UserJSONProcessor userJSONProcessor;

    private EditText userName, userPassword;
    private Button loginButton;
    private TextView navToRegister;

    /**
     * Method to create a new registered user.
     * @param savedInstanceState instance of the current state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Bad coding
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initUIViews();

        userJSONProcessor = new UserJSONProcessor(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get user input
                String name = userName.getText().toString();
                String password = userPassword.getText().toString();

                JsonObject ret;
                Proxy proxy = new Proxy(LoginActivity.this);
                String[] params = {
                        name,
                        password
                };
                ret = proxy.synchExecution("login", params);

                //Below is bad coding, but it works.
                String responseJO = ret.get("ret").getAsString();

                if (ret == null) {
                    Toast.makeText(LoginActivity.this, "Something went wrong. Could not send request to server.", Toast.LENGTH_SHORT).show();
                } else if (responseJO.contains("true")) {
                    Toast.makeText(LoginActivity.this, "Logging in.", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra("name", name);
                    startActivity(i);

                    finish();
                } else if (responseJO.contains("false")) {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials. Try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Something went wrong. Server returned something weird.", Toast.LENGTH_SHORT).show();
                }

//                if (validateLoginCredentials(name, password)){
//                    User user = userJSONProcessor.getUser(name);
//
//                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                    i.putExtra("name", name);
//                    startActivity(i);
//
//                    finish();
//                }
            }
        });

        navToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

    }

    /**
     * UI view of Login.
     */
    private void initUIViews() {
        userName = (EditText)findViewById(R.id.etUserName);
        userPassword = (EditText)findViewById(R.id.etUserPassword);
        loginButton = (Button)findViewById(R.id.btnLogin);
        navToRegister = (TextView)findViewById((R.id.tvNavToRegister));
    }

    /**
     * Method to validate login credential.
     * @param name  Username
     * @param password  User password
     * @return validated credentials
     */
    private boolean validateLoginCredentials(String name, String password){
        boolean result = false;

        if (name.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "There are missing fields. Please enter all details.", Toast.LENGTH_SHORT).show();
        } else if (userJSONProcessor.hasUserName(name)){
            result = true;
        } else {
            Toast.makeText(this, "Username or password is invalid. Please try again.", Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    /**
     *  Method to resume the app after validation.
     */
    @Override
    public void onResume()
    {
        super.onResume();
        userJSONProcessor = new UserJSONProcessor(this);
    }
}
