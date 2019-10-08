package java.model;

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

    public double getTerms_freq() {
        return terms_freq;
    }

    public void setTerms_freq(double terms_freq) {
        this.terms_freq = terms_freq;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFamiliarity() {
        return familiarity;
    }

    public void setFamiliarity(double familiarity) {
        this.familiarity = familiarity;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getSimilar() {
        return similar;
    }

    public void setSimilar(String similar) {
        this.similar = similar;
    }

    public double getHotttnesss() {
        return hotttnesss;
    }

    public void setHotttnesss(double hotttnesss) {
        this.hotttnesss = hotttnesss;
    }
}
