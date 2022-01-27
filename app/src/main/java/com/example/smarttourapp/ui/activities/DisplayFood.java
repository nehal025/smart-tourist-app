package com.example.smarttourapp.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.smarttourapp.R;
import com.example.smarttourapp.model.Food;
import com.example.smarttourapp.model.Place;
import com.example.smarttourapp.retrofit.RetrofitArrayApi;
import com.example.smarttourapp.utils.Utils;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DisplayFood extends AppCompatActivity {
    ArrayList<Food> food ;
    TextView info;
    TextView state;
    ImageView img;
    String value;
    CoordinatorLayout coordinatorLayout;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_food);
        info=findViewById(R.id.food_info);
        img=findViewById(R.id.food_img);
        state=findViewById(R.id.food_state);
        coordinatorLayout=findViewById(R.id.displayFood);
        progressBar=findViewById(R.id.displayFoodProgressbar);
        coordinatorLayout.setVisibility(View.INVISIBLE);
        value= getIntent().getExtras().getString("name");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.e("nehal", " response "+ "hi");
//        location.setText(value);
        getRetrofit(value);

        CollapsingToolbarLayout collapsingToolbarLayout=findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(value);



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    private void getRetrofit(String loc) {

        String baseURL = "https://smart-tourist-app.herokuapp.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayApi service = retrofit.create(RetrofitArrayApi.class);
        Call<List<Food>> call = service.getFood(loc);


        call.enqueue(new Callback<List<Food>>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(@NonNull Call<List<Food>> call, @NonNull Response<List<Food>> response) {

                if(!response.body().isEmpty()){
                    info.setText(response.body().get(0).getInfo());
                    state.setText(response.body().get(0).getState());

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
            public void onFailure(Call <List<Food>> call, Throwable t) {

                Toast toast = Toast.makeText(getApplicationContext(),
                        "This is a message displayed in a Toast",
                        Toast.LENGTH_SHORT);

                toast.show();

            }
        });


    }
}