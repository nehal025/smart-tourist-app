package com.example.smarttourapp.services;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smarttourapp.R;
import com.example.smarttourapp.model.ArrayListCategory;
import com.example.smarttourapp.retrofit.RetrofitArrayApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExitServiceRes extends IntentService {

    ArrayList<String> category = new ArrayList<>();

    private static final String TAG ="Servicenehal" ;

    public ExitServiceRes() {
        super(ExitServiceRes.class.getName());
    }

    private void putRecommendations() {

        ArrayListCategory arrayListAge = new ArrayListCategory(category);


        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        String baseURL = getResources().getString(R.string.link);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayApi service = retrofit.create(RetrofitArrayApi.class);
        Call<ResponseBody> call = service.postCategoryRes(arrayListAge,getToken());

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call <ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "Success");
//                stopSelf();


            }

            @Override
            public void onFailure(@NonNull Call <ResponseBody> call, @NonNull Throwable t) {
                Log.d(TAG, "failed");
//                stopSelf();
            }
        });

    }

    public String getToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        return sharedPreferences.getString("token", null);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Service Started!");
        int maxValueInMap=(Collections.max(loadMap().values()));
        int _50=maxValueInMap/2;


        for (Map.Entry<String, Integer> entry :loadMap().entrySet()) {
            if (entry.getValue()>=_50) {
                category.add(entry.getKey());
            }
        }

        putRecommendations();
    }
    private HashMap<String,Integer> loadMap() {
        HashMap<String,Integer> outputMap = new HashMap<>();
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        try {
            if (pSharedPref != null) {
                String jsonString = pSharedPref.getString("My_map", (new JSONObject()).toString());
                if (jsonString != null) {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    Iterator<String> keysItr = jsonObject.keys();
                    while (keysItr.hasNext()) {
                        String key = keysItr.next();
                        Integer value = jsonObject.getInt(key);
                        outputMap.put(key, value);
                    }
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        return outputMap;
    }
}
