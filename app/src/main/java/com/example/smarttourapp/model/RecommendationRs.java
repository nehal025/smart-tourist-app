package com.example.smarttourapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecommendationRs {

    @SerializedName("category")
    @Expose
    private List<String> category = null;

    public RecommendationRs(List<String> category) {
        super();
        this.category = category;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }
}
