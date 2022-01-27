package com.example.smarttourapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Restaurant {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("info")
    @Expose
    private String info;
    @SerializedName("rating")
    @Expose
    private Float rating;
    @SerializedName("bookNow")
    @Expose
    private String bookNow;



    public Restaurant(String title, String img, String location, String info, Float rating, String bookNow) {
        super();
        this.title = title;
        this.img = img;
        this.location = location;
        this.info = info;
        this.rating = rating;
        this.bookNow = bookNow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getBookNow() {
        return bookNow;
    }

    public void setBookNow(String bookNow) {
        this.bookNow = bookNow;
    }
}
