package com.example.smarttourapp.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.smarttourapp.model.Place;
import com.example.smarttourapp.R;
import com.example.smarttourapp.retrofit.RetrofitArrayApi;
import com.example.smarttourapp.utils.*;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DisplayPlace extends AppCompatActivity {
    Place place;
    TextView info;
    TextView city;
    TextView state;
    ImageView img;
    String value;
    CoordinatorLayout coordinatorLayout;
    ProgressBar progressBar;

    MaterialCardView getHotel;
    MaterialCardView getRestaurant;
    MaterialCardView getFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_place);
        city=findViewById(R.id.city);
        info=findViewById(R.id.info);
        img=findViewById(R.id.img);
        state=findViewById(R.id.state);
        coordinatorLayout=findViewById(R.id.displayPlace);
        progressBar=findViewById(R.id.displayPlace_progressbar);

        getHotel=findViewById(R.id.getHotel);
        getRestaurant=findViewById(R.id.getRestaurant);
        getFood=findViewById(R.id.getFood);
        coordinatorLayout.setVisibility(View.INVISIBLE);
        value= getIntent().getExtras().getString("location");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.e("nehal", " response "+ "hi");
//        location.setText(value);
        getRetrofit(value);

        CollapsingToolbarLayout collapsingToolbarLayout=findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(value);

        getHotel.setOnClickListener(v -> {
            Intent myIntent = new Intent(DisplayPlace.this, HotelsList.class);
            myIntent.putExtra("location",place.getLocation() );

            startActivity(myIntent);
        });


        getRestaurant.setOnClickListener(v -> {
            Intent myIntent = new Intent(DisplayPlace.this, RestaurantList.class);
            myIntent.putExtra("location",place.getLocation() );

            startActivity(myIntent);
        });

        getFood.setOnClickListener(v -> {
            Intent myIntent = new Intent(DisplayPlace.this, FoodList.class);
            myIntent.putExtra("location",place.getState());
            startActivity(myIntent);
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    private void getRetrofit(String loc) {

        String baseURL = getResources().getString(R.string.link);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayApi service = retrofit.create(RetrofitArrayApi.class);
        Call<List<Place>> call = service.getPlace(loc);


        call.enqueue(new Callback<List<Place>>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(@NonNull Call<List<Place>> call, @NonNull Response<List<Place>> response) {

                if(!response.body().isEmpty()){
                    info.setText(response.body().get(0).getInfo());
                    city.setText(response.body().get(0).getLocation());
                    state.setText(response.body().get(0).getState());


                    String id=response.body().get(0).getId();
                    String name=response.body().get(0).getName();
                    List<String> imgList = new ArrayList<>(response.body().get(0).getImg());
                    String location=response.body().get(0).getLocation();
                    String state=response.body().get(0).getState();
                    String info=response.body().get(0).getInfo();
                    Boolean featured=response.body().get(0).getFeatured();
                    Boolean ml=response.body().get(0).getMl();
                    place= new Place(id,name,imgList,location,state,info,featured,ml);

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(Utils.getRandomDrawbleColor());
                    requestOptions.error(Utils.getRandomDrawbleColor());
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                    requestOptions.centerCrop();

                    Glide.with(getApplicationContext())
                            .load(response.body().get(0).getImg().get(0))
                            .apply(requestOptions)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                    holder.progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                    holder.progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(img);

                    coordinatorLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);


                }else{


                }
                }




            @Override
            public void onFailure(Call <List<Place>> call, Throwable t) {

                Toast toast = Toast.makeText(getApplicationContext(),
                        "This is a message displayed in a Toast",
                        Toast.LENGTH_SHORT);

                toast.show();

            }
        });


    }
}