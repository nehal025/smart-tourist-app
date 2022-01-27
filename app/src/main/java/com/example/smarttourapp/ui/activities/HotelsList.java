package com.example.smarttourapp.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.smarttourapp.model.Hotel;
import com.example.smarttourapp.R;
import com.example.smarttourapp.model.news.Article;
import com.example.smarttourapp.retrofit.RetrofitArrayApi;
import com.example.smarttourapp.ui.adapters.HotelAdapter;
import com.example.smarttourapp.ui.adapters.NewsAdapter;
import com.example.smarttourapp.utils.Global;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HotelsList extends AppCompatActivity  {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Hotel> hotels = new ArrayList<>();
    HotelAdapter hotelAdapter;
    TextView topHeadline;
    RelativeLayout errorLayout;
    ImageView errorImage;
    TextView errorTitle, errorMessage;
    Button btnRetry;
    RelativeLayout lottieContainer;

    LottieAnimationView lottie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels_list);

        recyclerView = findViewById(R.id.recyclerView_hotel);
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

        Intent intent = getIntent();

        if (intent.hasExtra("location")) {
            LoadJson(getIntent().getExtras().getString("location"));
        } else {
            LoadJson(Global.city);
        }

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

    public void LoadJson(String location){

        errorLayout.setVisibility(View.GONE);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        String baseURL = "https://smart-tourist-app.herokuapp.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayApi service = retrofit.create(RetrofitArrayApi.class);
        Call<List<Hotel>> call = service.getLiveHotel(location);

        call.enqueue(new Callback<List<Hotel>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if (response.isSuccessful() ){

                    if (!hotels.isEmpty()){
                        hotels.clear();
                    }

                    for (int i=0; i<response.body().size();i++){

                        hotels.add( new Hotel(response.body().get(i).getTitle(),
                                response.body().get(i).getImg(),
                                response.body().get(i).getLocation(),
                                response.body().get(i).getInfo(),
                                response.body().get(i).getStar(),
                                response.body().get(i).getPrice(),
                                response.body().get(i).getRating(),
                                response.body().get(i).getReviews(),
                                response.body().get(i).getBooknow()
                        ));
                    }


                    hotelAdapter = new HotelAdapter(hotels, getApplicationContext());
                    recyclerView.setAdapter(hotelAdapter);
                    hotelAdapter.notifyDataSetChanged();
                    initListener();

                    lottieContainer.setVisibility(View.GONE);
                    lottie.cancelAnimation();



                } else {

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
            public void onFailure(@NonNull Call<List<Hotel>> call, @NonNull Throwable t) {
                lottie.cancelAnimation();
                lottieContainer.setVisibility(View.GONE);
                showErrorMessage(R.drawable.oops, "Oops..",

                        "Server Connection error");
            }
        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {


    }


    private void initListener(){


        hotelAdapter.setOnItemClickListener(new HotelAdapter.OnItemClickListener() {


            @Override
            public void onClickBookNow(View view, int position) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(hotels.get(position).getBooknow())));

            }

            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(hotels.get(position).getBooknow())));

            }
        });

}


}