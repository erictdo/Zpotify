package java.model;

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

    public double getKey() {
        return key;
    }

    public void setKey(double key) {
        this.key = key;
    }

    public double getMode_confidence() {
        return mode_confidence;
    }

    public void setMode_confidence(double mode_confidence) {
        this.mode_confidence = mode_confidence;
    }

    public double getArtist_mbtags_count() {
        return artist_mbtags_count;
    }

    public void setArtist_mbtags_count(double artist_mbtags_count) {
        this.artist_mbtags_count = artist_mbtags_count;
    }

    public double getKey_confidence() {
        return key_confidence;
    }

    public void setKey_confidence(double key_confidence) {
        this.key_confidence = key_confidence;
    }

    public double getTatums_start() {
        return tatums_start;
    }

    public void setTatums_start(double tatums_start) {
        this.tatums_start = tatums_start;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getHotttnesss() {
        return hotttnesss;
    }

    public void setHotttnesss(double hotttnesss) {
        this.hotttnesss = hotttnesss;
    }

    public double getBeats_start() {
        return beats_start;
    }

    public void setBeats_start(double beats_start) {
        this.beats_start = beats_start;
    }

    public double getTime_signature_confidence() {
        return time_signature_confidence;
    }

    public void setTime_signature_confidence(double time_signature_confidence) {
        this.time_signature_confidence = time_signature_confidence;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getBars_confidence() {
        return bars_confidence;
    }

    public void setBars_confidence(double bars_confidence) {
        this.bars_confidence = bars_confidence;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getBars_start() {
        return bars_start;
    }

    public void setBars_start(double bars_start) {
        this.bars_start = bars_start;
    }

    public String getArtist_mbtags() {
        return artist_mbtags;
    }

    public void setArtist_mbtags(String artist_mbtags) {
        this.artist_mbtags = artist_mbtags;
    }

    public double getStart_of_fade_out() {
        return start_of_fade_out;
    }

    public void setStart_of_fade_out(double start_of_fade_out) {
        this.start_of_fade_out = start_of_fade_out;
    }

    public double getTempo() {
        return tempo;
    }

    public void setTempo(double tempo) {
        this.tempo = tempo;
    }

    public double getEnd_of_fade_in() {
        return end_of_fade_in;
    }

    public void setEnd_of_fade_in(double end_of_fade_in) {
        this.end_of_fade_in = end_of_fade_in;
    }

    public double getBeats_confidence() {
        return beats_confidence;
    }

    public void setBeats_confidence(double beats_confidence) {
        this.beats_confidence = beats_confidence;
    }

    public double getTatums_confidence() {
        return tatums_confidence;
    }

    public void setTatums_confidence(double tatums_confidence) {
        this.tatums_confidence = tatums_confidence;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public double getTime_signature() {
        return time_signature;
    }

    public void setTime_signature(double time_signature) {
        this.time_signature = time_signature;
    }

    public double getLoudness() {
        return loudness;
    }

    public void setLoudness(double loudness) {
        this.loudness = loudness;
    }
}
