package com.example.smarttourapp.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Place {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("img")
    @Expose
    private List<String> img = null;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("info")
    @Expose
    private String info;
    @SerializedName("featured")
    @Expose
    private Boolean featured;
    @SerializedName("ml")
    @Expose
    private Boolean ml;





    public Place(String id, String name, List<String> img, String location, String state, String info, Boolean featured, Boolean ml) {
        super();
        this.id = id;
        this.name = name;
        this.img = img;
        this.location = location;
        this.state = state;
        this.info = info;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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