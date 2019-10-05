package com.example.a327lab1.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Song implements Serializable {
    @Expose
    private double key;
    @Expose
    private double mode_confidence;
    @Expose
    private double artist_mbtags_count;
    @Expose
    private double key_confidence;
    @Expose
    private double tatums_start;
    @Expose
    private int year;
    @Expose
    private double duration;
    @Expose
    private double hotttnesss;
    @Expose
    private double beats_start;
    @Expose
    private double time_signature_confidence;
    @Expose
    private String title;
    @Expose
    private double bars_confidence;
    @Expose
    private String id;
    @Expose
    private double bars_start;
    @Expose
    private String artist_mbtags;
    @Expose
    private double start_of_fade_out;
    @Expose
    private double tempo;
    @Expose
    private double end_of_fade_in;
    @Expose
    private double beats_confidence;
    @Expose
    private double tatums_confidence;
    @Expose
    private int mode;
    @Expose
    private double time_signature;
    @Expose
    private double loudness;

    public Song(double key, double mode_confidence, double artist_mbtags_count, double key_confidence, double tatums_start, int year, double duration, double hotttnesss, double beats_start, double time_signature_confidence, String title, double bars_confidence, String id, double bars_start, String artist_mbtags, double start_of_fade_out, double tempo, double end_of_fade_in, double beats_confidence, double tatums_confidence, int mode, double time_signature, double loudness) {
        this.key = key;
        this.mode_confidence = mode_confidence;
        this.artist_mbtags_count = artist_mbtags_count;
        this.key_confidence = key_confidence;
        this.tatums_start = tatums_start;
        this.year = year;
        this.duration = duration;
        this.hotttnesss = hotttnesss;
        this.beats_start = beats_start;
        this.time_signature_confidence = time_signature_confidence;
        this.title = title;
        this.bars_confidence = bars_confidence;
        this.id = id;
        this.bars_start = bars_start;
        this.artist_mbtags = artist_mbtags;
        this.start_of_fade_out = start_of_fade_out;
        this.tempo = tempo;
        this.end_of_fade_in = end_of_fade_in;
        this.beats_confidence = beats_confidence;
        this.tatums_confidence = tatums_confidence;
        this.mode = mode;
        this.time_signature = time_signature;
        this.loudness = loudness;
    }

    public String getSongTitle()
    {
        return title;
    }
}
