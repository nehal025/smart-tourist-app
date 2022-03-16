package com.example.smarttourapp.utils;

import android.graphics.Bitmap;
import com.example.smarttourapp.model.CurrentLocation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class Global {

    //camera img
    public static Bitmap img;

    //profile name
    public static String name;

    //display address
    public static CurrentLocation currentAddress;

    //cost estimation address
    public static CurrentLocation fromAddress=new CurrentLocation();
    public static CurrentLocation toAddress=new CurrentLocation();

    //recommendations likes
    public static TreeMap<String,String> hotelLikes = new TreeMap<>();

    public static HashMap<String,Integer> restaurantLikes = new HashMap<>();

    //recommendations
    public static List<String> hotelRecommendation = new ArrayList<>();

    public static List<String> restaurantRecommendation = new ArrayList<>();



    public static  void clear() {
        restaurantLikes.clear();
        hotelLikes.clear();
        restaurantRecommendation.clear();
        hotelRecommendation.clear();

    }

    public static  void clear2() {
        hotelLikes.clear();
        hotelRecommendation.clear();

    }

}

