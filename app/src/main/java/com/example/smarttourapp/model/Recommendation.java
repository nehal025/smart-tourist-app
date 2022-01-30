package com.example.smarttourapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recommendation {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("_1star")
    @Expose
    private String _1star;
    @SerializedName("_2star")
    @Expose
    private String _2star;
    @SerializedName("_3star")
    @Expose
    private String _3star;
    @SerializedName("_4star")
    @Expose
    private String _4star;
    @SerializedName("_5star")
    @Expose
    private String _5star;
    @SerializedName("recommendation")
    @Expose
    private List<String> recommendation = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Recommendation() {
    }


    public Recommendation(String id, String username, String _1star, String _2star, String _3star, String _4star, String _5star, List<String> recommendation) {
        super();
        this.id = id;
        this.username = username;
        this._1star = _1star;
        this._2star = _2star;
        this._3star = _3star;
        this._4star = _4star;
        this._5star = _5star;
        this.recommendation = recommendation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String get1star() {
        return _1star;
    }

    public void set1star(String _1star) {
        this._1star = _1star;
    }

    public String get2star() {
        return _2star;
    }

    public void set2star(String _2star) {
        this._2star = _2star;
    }

    public String get3star() {
        return _3star;
    }

    public void set3star(String _3star) {
        this._3star = _3star;
    }

    public String get4star() {
        return _4star;
    }

    public void set4star(String _4star) {
        this._4star = _4star;
    }

    public String get5star() {
        return _5star;
    }

    public void set5star(String _5star) {
        this._5star = _5star;
    }

    public List<String> getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(List<String> recommendation) {
        this.recommendation = recommendation;
    }
}
