package com.example.smarttourapp.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.smarttourapp.R;
import com.example.smarttourapp.model.Place;
import com.example.smarttourapp.model.Restaurant;
import com.example.smarttourapp.model.ThingsToDo;
import com.example.smarttourapp.retrofit.RetrofitArrayApi;
import com.example.smarttourapp.ui.adapters.PlaceAdapter;
import com.example.smarttourapp.ui.adapters.RestaurantAdapter;
import com.example.smarttourapp.utils.Global;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<ThingsToDo> places = new ArrayList<>();
    PlaceAdapter placeAdapter;
    RelativeLayout errorLayout;
    ImageView errorImage;
    TextView errorTitle, errorMessage;
    Button btnRetry;
    RelativeLayout lottieContainer;
    LottieAnimationView lottie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);
        recyclerView = findViewById(R.id.recyclerView_place);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        errorLayout = findViewById(R.id.errorLayout);
        errorImage = findViewById(R.id.errorImage);
        errorTitle = findViewById(R.id.errorTitle);
        errorMessage = findViewById(R.id.errorMessage);
        btnRetry = findViewById(R.id.btnRetry);
        lottie = findViewById(R.id.lottie);
        lottieContainer = findViewById(R.id.lottie_container);
        lottieContainer.setVisibility(View.VISIBLE);

        loadJson(Global.currentAddress.getCity());

    }

    public void loadJson(String location){

        errorLayout.setVisibility(View.GONE);
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
        Call<List<ThingsToDo>> call = service.getThingsToDo(location);

        call.enqueue(new Callback<List<ThingsToDo>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<ThingsToDo>> call, @NonNull Response<List<ThingsToDo>> response) {
                if (response.isSuccessful() ){

                    if (!places.isEmpty()){
                        places.clear();
                    }

                    for (int i = 0; i< Objects.requireNonNull(response.body()).size(); i++){

                        String title=response.body().get(i).getTitle();
                        String img = response.body().get(i).getImg();
                        String location=response.body().get(i).getLocation();
                        String info=response.body().get(i).getInfo();

                        places.add( new ThingsToDo(title,img,location,info));
                    }


                    placeAdapter = new PlaceAdapter(places, getApplicationContext());
                    recyclerView.setAdapter(placeAdapter);
                    placeAdapter.notifyDataSetChanged();
                    initListener();
                    lottieContainer.setVisibility(View.GONE);
                    lottie.cancelAnimation();



                } else {
                    lottieContainer.setVisibility(View.GONE);
                    lottie.cancelAnimation();

                    lottieContainer.setVisibility(View.GONE);
                    String errorCode;
                    switch (response.code()) {
                        case 404:
                            errorCode = "404 not found";
                            break;
                        case 500:
                            errorCode = "500 server broken";
                            break;
                        default:
                            errorCode = "unknown error";
                            break;
                    }

                    showErrorMessage(
                            R.drawable.no_result,
                            "No Result",
                            "Please Try Again!\n"+
                                    errorCode);


                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ThingsToDo>> call, @NonNull Throwable t) {
                lottieContainer.setVisibility(View.GONE);
                lottie.cancelAnimation();
                showErrorMessage(R.drawable.oops, "Oops..",

                        t.toString());
            }
        });

    }
    private void showErrorMessage(int imageView, String title, String message){

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }

        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        btnRetry.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        });
    }

    private void initListener(){


        placeAdapter.setOnItemClickListener(new PlaceAdapter.OnItemClickListener() {


            @Override
            public void onClickBookNow(View view, int position) {

            }

            @Override
            public void onItemClick(int position) {
//                Intent myIntent = new Intent(PlaceList.this, DisplayPlace.class);
//                myIntent.putExtra("location", places.get(position).getTitle());
//                startActivity(myIntent);
            }
        });

    }
}