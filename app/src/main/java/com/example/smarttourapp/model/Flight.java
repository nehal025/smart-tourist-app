package com.example.smarttourapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Flight {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("cost")
    @Expose
    private Integer cost;
    @SerializedName("time")
    @Expose
    private String time;


    public Flight(String title, String img, Integer cost, String time) {
        super();
        this.title = title;
        this.img = img;
        this.cost = cost;
        this.time = time;
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

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}