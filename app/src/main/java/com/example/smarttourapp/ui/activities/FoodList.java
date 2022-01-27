package com.example.smarttourapp.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttourapp.R;
import com.example.smarttourapp.model.Food;
import com.example.smarttourapp.model.Place;
import com.example.smarttourapp.retrofit.RetrofitArrayApi;
import com.example.smarttourapp.ui.adapters.FoodAdapter;
import com.example.smarttourapp.ui.adapters.PlaceAdapter;
import com.example.smarttourapp.utils.Global;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Food> food = new ArrayList<>();
    FoodAdapter foodAdapter;
    RelativeLayout errorLayout;
    ImageView errorImage;
    TextView errorTitle, errorMessage;
    Button btnRetry;
    RelativeLayout lottieContainer;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        progressBar=findViewById(R.id.food_list_progressbar);
        recyclerView = findViewById(R.id.recyclerView_food);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        errorLayout = findViewById(R.id.errorLayout);
        errorImage = findViewById(R.id.errorImage);
        errorTitle = findViewById(R.id.errorTitle);
        errorMessage = findViewById(R.id.errorMessage);
        btnRetry = findViewById(R.id.btnRetry);
//        loadJson(Global.State);

        Intent intent = getIntent();

        if (intent.hasExtra("location")) {
            loadJson(getIntent().getExtras().getString("location"));
        } else {
            loadJson(Global.State);
        }
    }

    public void loadJson(String location){

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
        Call<List<Food>> call = service.getFoodByState(location);

        call.enqueue(new Callback<List<Food>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<Food>> call, @NonNull Response<List<Food>> response) {
                if (response.isSuccessful() ){

                    if (!food.isEmpty()){
                        food.clear();
                    }

                    for (int i = 0; i< Objects.requireNonNull(response.body()).size(); i++){
                        String id=response.body().get(i).getId();
                        String name=response.body().get(i).getName();

                        List<String> img = new ArrayList<>(response.body().get(i).getImg());
                        String state=response.body().get(i).getState();
                        String info=response.body().get(i).getInfo();
                        Boolean featured=response.body().get(i).getFeatured();
                        Boolean ml=response.body().get(i).getMl();
                        food.add( new Food(id,name,img,info,state,featured,ml));
                    }


                    foodAdapter = new FoodAdapter(food, getApplicationContext());
                    recyclerView.setAdapter(foodAdapter);
                    foodAdapter.notifyDataSetChanged();
                    initListener();
                    progressBar.setVisibility(View.GONE);



                } else {
                    progressBar.setVisibility(View.GONE);

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
            public void onFailure(@NonNull Call<List<Food>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
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


        foodAdapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {


            @Override
            public void onClickBookNow(View view, int position) {

            }

            @Override
            public void onItemClick(int position) {
                Intent myIntent = new Intent(FoodList.this, DisplayFood.class);
                myIntent.putExtra("name", food.get(position).getName());
                startActivity(myIntent);
            }
        });

    }
}