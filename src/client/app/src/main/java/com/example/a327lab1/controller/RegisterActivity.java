package com.example.a327lab1.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a327lab1.R;
import com.example.a327lab1.model.Playlist;
import com.example.a327lab1.model.User;

import java.util.ArrayList;

/**
 * Registration Activity for the application.
 */
public class RegisterActivity extends AppCompatActivity {

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

        initUIViews();

        userJSONProcessor = new UserJSONProcessor(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get user input
                String name = userName.getText().toString();
                String password = userPassword.getText().toString();
                String passwordConfirm = userPasswordConfirm.getText().toString();

                if (validate(name, password, passwordConfirm)){
                    addUserToJSON(name, password);
                    finish();
                }
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
