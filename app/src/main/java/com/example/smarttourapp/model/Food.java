package com.example.smarttourapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Food {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("img")
    @Expose
    private List<String> img;
    @SerializedName("info")
    @Expose
    private String info;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("featured")
    @Expose
    private Boolean featured;
    @SerializedName("ml")
    @Expose
    private Boolean ml;



    public Food(String id, String name, List<String> img, String info, String state, Boolean featured, Boolean ml) {
        super();
        this.id = id;
        this.name = name;
        this.img = img;
        this.info = info;
        this.state = state;
        this.featured = featured;
        this.ml = ml;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Boolean getMl() {
        return ml;
    }

    public void setMl(Boolean ml) {
        this.ml = ml;
    }
}
