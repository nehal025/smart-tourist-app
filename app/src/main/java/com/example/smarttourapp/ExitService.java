package com.example.smarttourapp;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smarttourapp.model.Recommendation;
import com.example.smarttourapp.retrofit.RetrofitArrayApi;
import com.example.smarttourapp.ui.activities.HotelsList;
import com.example.smarttourapp.utils.Global;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExitService extends IntentService {

    String token1,_1star,_2star,_3star,_4star,_5star;

    private static final String TAG ="Servicenehal" ;

    public ExitService() {
        super(ExitService.class.getName());
    }

    private void putRecommendations() {



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
        Call<Recommendation> call = service.updateRecommendation(token1,_1star,_2star,_3star,_4star,_5star);

        call.enqueue(new Callback<Recommendation>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call <Recommendation> call, Response<Recommendation> response) {
                Log.d(TAG, "Success");
//                stopSelf();

            }

            @Override
            public void onFailure(@NonNull Call <Recommendation> call, @NonNull Throwable t) {
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
        token1=intent.getStringExtra("token1");
        _1star=intent.getStringExtra("_1star");
        _2star=intent.getStringExtra("_2star");
        _3star=intent.getStringExtra("_3star");
        _4star=intent.getStringExtra("_4star");
        _5star=intent.getStringExtra("_5star");

        putRecommendations();
    }
}
