package com.example.smarttourapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hotel {

    public Hotel(String title, String img, String location, String info, String star, String price, Float rating, String reviews, String booknow,boolean save) {
        this.title = title;
        this.img = img;
        this.location = location;
        this.info = info;
        this.star = star;
        this.price = price;
        this.rating = rating;
        this.reviews = reviews;
        this.booknow = booknow;
        Save = save;

    }

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
    @SerializedName("star")
    @Expose
    private String star;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("rating")
    @Expose
    private Float rating;
    @SerializedName("reviews")
    @Expose
    private String reviews;
    @SerializedName("booknow")
    @Expose
    private String booknow;

    private boolean Save;

    public boolean isSave() {
        return Save;
    }

    public void setSave(boolean save) {
        Save = save;
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

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getBooknow() {
        return booknow;
    }

    public void setBooknow(String booknow) {
        this.booknow = booknow;
    }

}