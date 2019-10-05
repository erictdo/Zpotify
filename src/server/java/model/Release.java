package com.example.a327lab1.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Release implements Serializable {
    @Expose
    private String id;
    @Expose
    private String name;

    public Release(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getReleaseDate()
    {
        return name;
    }

    public String getId() { return id; }
}
