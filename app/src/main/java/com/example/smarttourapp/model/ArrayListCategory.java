package com.example.smarttourapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ArrayListCategory {
    @SerializedName("category")
    @Expose
    private ArrayList<String> category;
    public ArrayListCategory(ArrayList<String> category) {
        this.category=category;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }
}
