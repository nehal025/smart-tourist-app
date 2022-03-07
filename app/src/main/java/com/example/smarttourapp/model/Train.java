package com.example.smarttourapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Train {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("cost")
    @Expose
    private Integer cost;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("trainClass")
    @Expose
    private String trainClass;


    public Train(String title, Integer cost, String time, String trainClass) {
        super();
        this.title = title;
        this.cost = cost;
        this.time = time;
        this.trainClass = trainClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getTrainClass() {
        return trainClass;
    }

    public void setTrainClass(String trainClass) {
        this.trainClass = trainClass;
    }

}