package com.example.a327lab1.controller;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a327lab1.R;
import com.example.a327lab1.model.Playlist;
import com.example.a327lab1.model.User;
import com.example.a327lab1.rpc.Proxy;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Registration Activity for the application.
 */
public class RegisterActivity extends AppCompatActivity {
    private static Context cxt;

    private UserJSONProcessor userJSONProcessor;

    private EditText userName, userPassword, userPasswordConfirm;
    private Button registerButton;
    private TextView navToLogin;

    /**
     * Method to create and store a new user in JSON.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Bad coding
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initUIViews();

        //userJSONProcessor = new UserJSONProcessor(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get user input
                String name = userName.getText().toString();
                String password = userPassword.getText().toString();
                String passwordConfirm = userPasswordConfirm.getText().toString();

                JsonObject ret;
                Proxy proxy = new Proxy(RegisterActivity.this);
                String[] params = {
                        name,
                        password,
                        passwordConfirm
                };
                ret = proxy.synchExecution("register", params);

                //Below is bad coding, but it works.
                String responseJO = ret.get("ret").getAsString();

                if (ret == null){
                    Toast.makeText(RegisterActivity.this, "Something went wrong. Could not send request to server.", Toast.LENGTH_SHORT).show();
                } else if (responseJO.contains("true")) {
                    Toast.makeText(RegisterActivity.this, "Your account has been made. Please login.", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (responseJO.contains("false")) {
                    Toast.makeText(RegisterActivity.this, "This account has already been registered. Try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Invalid inputs. Try again.", Toast.LENGTH_SHORT).show();
                }

//                if (validate(name, password, passwordConfirm)){
//                    addUserToJSON(name, password);
//                    finish();
//                }
            }
        });

        navToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * UI view of the Register Page.
     */
    private void initUIViews() {
        userName = (EditText)findViewById(R.id.etUserName);
        userPassword = (EditText)findViewById(R.id.etUserPassword);
        userPasswordConfirm = (EditText)findViewById(R.id.etUserPasswordConfirm);
        registerButton = (Button)findViewById(R.id.btnRegister);
        navToLogin = (TextView)findViewById((R.id.tvNavToLogin));
    }

    /**
     * Validation method for the user registration info.
     * @param name username
     * @param password user's password
     * @param passwordConfirm password confirmation
     * @return validation of the user's registration info.
     */
    private boolean validate(String name, String password, String passwordConfirm){

        if (name.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            Toast.makeText(this, "There are missing fields. Please enter all details.", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(passwordConfirm)) {
            Toast.makeText(this, "Password inputs do not match. Please try again.", Toast.LENGTH_SHORT).show();
        } else if (userJSONProcessor.hasUserName(name)){
            Toast.makeText(this, "User already exists. Try different username", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Your account has been made. Please login.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    /**
     * Method to store user's info into JSON.
     * @param name username
     * @param password user's password
     */
    private void addUserToJSON(String name, String password) {
        User newUser = new User(name, password, new ArrayList<Playlist>());
        userJSONProcessor.addUser(newUser);
    }
}
