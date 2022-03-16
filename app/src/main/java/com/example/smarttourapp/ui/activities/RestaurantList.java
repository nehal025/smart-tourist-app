package com.example.smarttourapp.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.smarttourapp.R;
import com.example.smarttourapp.animation.MyBounceInterpolator;
import com.example.smarttourapp.model.ArrayListCategory;
import com.example.smarttourapp.model.Restaurant;
import com.example.smarttourapp.retrofit.RetrofitArrayApi;
import com.example.smarttourapp.ui.adapters.RestaurantAdapter;
import com.example.smarttourapp.utils.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Restaurant> restaurants = new ArrayList<>();
    RestaurantAdapter hotelAdapter;
    RelativeLayout errorLayout;
    ImageView errorImage;
    TextView errorTitle, errorMessage;
    Button btnRetry;
    RelativeLayout lottieContainer;
    LottieAnimationView lottie;
    ArrayList<String> list= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        recyclerView = findViewById(R.id.recyclerView_restaurant);
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

//        LoadJson(Global.city);
        Intent intent = getIntent();

        if (intent.hasExtra("location")) {
            LoadJson(getIntent().getExtras().getString("location"));
        } else {
            LoadJson(Global.currentAddress.getCity());
        }

    }


    public void LoadJson(String location){
        ArrayListCategory arrayListAge = new ArrayListCategory((ArrayList<String>) Global.restaurantRecommendation);

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
        Call<List<Restaurant>> call;

        if (getRec()) {
            call = service.getLiveRestaurantsWithStar(location, arrayListAge);

        }else {
            call = service.getLiveRestaurants(location);}



        call.enqueue(new Callback<List<Restaurant>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<Restaurant>> call, @NonNull Response<List<Restaurant>> response) {
                if (response.isSuccessful() ){

                    if (!restaurants.isEmpty()){
                        restaurants.clear();
                    }

                    for (int i = 0; i< Objects.requireNonNull(response.body()).size(); i++){

                        restaurants.add( new Restaurant(response.body().get(i).getTitle(),
                                response.body().get(i).getImg(),
                                response.body().get(i).getLocation(),
                                response.body().get(i).getInfo(),
                                response.body().get(i).getRating(),
                                response.body().get(i).getBookNow()
                        ));
                    }


                    hotelAdapter = new RestaurantAdapter(restaurants, getApplicationContext());
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
            public void onFailure(@NonNull Call<List<Restaurant>> call, @NonNull Throwable t) {
                lottie.cancelAnimation();
                lottieContainer.setVisibility(View.GONE);
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


        hotelAdapter.setOnItemClickListener(new RestaurantAdapter.OnItemClickListener() {


            @Override
            public void onClickBookNow(View view, int position) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(restaurants.get(position).getBookNow())));

            }

            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(restaurants.get(position).getBookNow())));


            }

            @Override
            public void onLike(View view, int position) {
                Toast.makeText(getApplicationContext(),restaurants.get(position).getInfo(),Toast.LENGTH_SHORT).show();
                ImageView imageView = view.findViewById(R.id.like_button);

                imageView.setImageResource(R.drawable.ic_like_button_pressed);

                final Animation myAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 20);
                myAnim.setInterpolator(interpolator);
                view.startAnimation(myAnim);
                String category =restaurants.get(position).getInfo();

                if (Global.restaurantLikes.containsKey(category)) {
                    Global.restaurantLikes.put(category, Global.restaurantLikes.get(category) + 1);
                } else {
                    Global.restaurantLikes.put(category, 1);
                }

            }
        });

    }

    private void saveMap(HashMap<String,Integer> inputMap) {
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        if (pSharedPref != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            pSharedPref.edit()
                    .remove("My_map")
                    .putString("My_map", jsonString)
                    .apply();
        }
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


    private  void clearLikes(){
        SharedPreferences preferences =getSharedPreferences("MyVariables",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        finish();

    }
    @Override
    protected void onPause() {
        if (getLoginStatus()) {
            clearLikes();
            Global.restaurantRecommendation.clear();

            saveMap(Global.restaurantLikes);
//            Toast.makeText(getApplicationContext(),loadMap().toString(),Toast.LENGTH_SHORT).show();

            if(Global.restaurantLikes.size()>0){
                int maxValueInMap=(Collections.max(Global.restaurantLikes.values()));
                int _50=maxValueInMap/2;


                for (Map.Entry<String, Integer> entry : Global.restaurantLikes.entrySet()) {

                    if (entry.getValue()>=_50) {
                        list.add(entry.getKey());
                    }
                }
//            Toast.makeText(getApplicationContext(),list.toString(),Toast.LENGTH_SHORT).show();
                Global.restaurantRecommendation.addAll(list);
                putRecommendations();

            }

        }
        super.onPause();

    }

    public Boolean getLoginStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        return sharedPreferences.getBoolean("loginStatus", false);
    }

    private void putRecommendations() {

        ArrayListCategory arrayListAge = new ArrayListCategory(list);


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

//                Log.d(TAG, "Success");
//                stopSelf();


            }

            @Override
            public void onFailure(@NonNull Call <ResponseBody> call, @NonNull Throwable t) {
//                Log.d(TAG, "failed");
//                stopSelf();
            }
        });

    }
    public String getToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        return sharedPreferences.getString("token", null);
    }

    public Boolean getRec() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        return sharedPreferences.getBoolean("rec", true);
    }
}