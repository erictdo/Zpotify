package com.example.a327lab1.controller;

import com.example.a327lab1.rpc.CECS327InputStream;

import java.io.IOException;
import java.io.InputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class CustomMediaPlayer {

    public static void mp3play(String file) {
        try {
            // It uses CECS327InputStream as InputStream to play the song
            InputStream is = new CECS327InputStream(file);
            Player mp3player = new Player(is);
            mp3player.play();
        }
        catch (JavaLayerException exception)
        {
            exception.printStackTrace();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    public static void mp3play(InputStream is) {
        try {
            // It uses CECS327InputStream as InputStream to play the song
            Player mp3player = new Player(is);
            mp3player.play();
        } catch (JavaLayerException exception) {
            exception.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
