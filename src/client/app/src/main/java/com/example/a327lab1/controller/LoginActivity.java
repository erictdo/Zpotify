package com.example.a327lab1.controller;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a327lab1.R;
import com.example.a327lab1.model.User;
import com.example.a327lab1.rpc.Proxy;
import com.example.a327lab1.data.Session;
import com.google.gson.JsonObject;

/**
 * Login Class Activity.
 */
public class LoginActivity extends AppCompatActivity {

    private UserJSONProcessor userJSONProcessor;
    boolean login = false;
    static Session session;
    private EditText userName, userPassword;
    private Button loginButton;
    private TextView navToRegister;
    AssetManager am;
    public static Context cxt;
    /**
     * Method to create a new registered user.
     * @param savedInstanceState instance of the current state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUIViews();

        cxt = getApplicationContext();
        am = cxt.getAssets();

//        userJSONProcessor = new UserJSONProcessor(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObject ret;
                Proxy proxy = new Proxy(cxt);
                String[] array = {  userName.getText().toString(),
                        userPassword.getText().toString()};
                ret = proxy.synchExecution("Login", array);
                if(ret.size() > 0) {
                    login = true;
                    session.setUsername(userName.getText().toString());
                    session.setPassword(userPassword.getText().toString());
                    session.setUser(getUserFromServer());
                    session.setLoginTrue("Login");
                }
                //Get user input
                String name = userName.getText().toString();
                String password = userPassword.getText().toString();

                if (session.getLogin()){
//                User user = userJSONProcessor.getUser(name);

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra("name", name);
                    startActivity(i);

                    finish();
                }
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
     * If the user has the correct credentials, then go to main app with bottom navigation
     *
     */
    public void signIn(boolean correctInput) {
        if (correctInput) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Username or password is incorrect", Toast.LENGTH_LONG).show();
        }
    }

    public static String getUserFromServer() {
        Proxy proxy = new Proxy(cxt);
        String[] array = {  session.getUsername()   };
        JsonObject ret = proxy.synchExecution("getUser", array);
        return ret.toString();
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
//    private boolean validateLoginCredentials(String name, String password){
//        boolean result = false;
//
//        if (name.isEmpty() || password.isEmpty()) {
//            Toast.makeText(this, "There are missing fields. Please enter all details.", Toast.LENGTH_SHORT).show();
//        } else if (userJSONProcessor.hasUserName(name)){
//            result = true;
//        } else {
//            Toast.makeText(this, "Username or password is invalid. Please try again.", Toast.LENGTH_SHORT).show();
//        }
//
//        return result;
//    }

    /**
     *  Method to resume the app after validation.
     */
    @Override
    public void onResume()
    {
        super.onResume();
//        userJSONProcessor = new UserJSONProcessor(this);
    }
}
