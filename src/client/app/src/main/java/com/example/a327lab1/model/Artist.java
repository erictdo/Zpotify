package com.example.a327lab1.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Artist implements Serializable {
    @Expose
    private double terms_freq;
    @Expose
    private String terms;
    @Expose
    private String name;
    @Expose
    private double familiarity;
    @Expose
    private double longitude;
    @Expose
    private String id;
    @Expose
    private String location;
    @Expose
    private double latitude;
    @Expose
    private String similar;
    @Expose
    private double hotttnesss;

    public Artist(double terms_freq, String terms, String name, double familiarity, double longitude, String id, String location, double latitude, String similar, double hotttnesss) {
        this.terms_freq = terms_freq;
        this.terms = terms;
        this.name = name;
        this.familiarity = familiarity;
        this.longitude = longitude;
        this.id = id;
        this.location = location;
        this.latitude = latitude;
        this.similar = similar;
        this.hotttnesss = hotttnesss;
    }

    public String getArtistName()
    {
        return name;
    }
    public String getTerms()
    {

        return terms;
    }
}
