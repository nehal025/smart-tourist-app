package com.example.smarttourapp.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.smarttourapp.services.ExitService;
import com.example.smarttourapp.R;
import com.example.smarttourapp.animation.MyBounceInterpolator;
import com.example.smarttourapp.model.Hotel;
import com.example.smarttourapp.model.PricePredictionTrain;
import com.example.smarttourapp.model.Recommendation;
import com.example.smarttourapp.model.Train;
import com.example.smarttourapp.retrofit.RetrofitArrayApi;
import com.example.smarttourapp.ui.adapters.HotelPreddictionAdapter;
import com.example.smarttourapp.ui.adapters.TrainAdapter;
import com.example.smarttourapp.utils.Global;
import com.google.android.material.appbar.CollapsingToolbarLayout;

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

public class DisplayTrainPrediction extends AppCompatActivity {

    TextView totalPrice;
    TextView hotelAvg;
    TextView trainAvg;
    TextView persons;
    TextView days;
    TextView livingCost;
    TextView from;
    TextView to;


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Hotel> hotels = new ArrayList<>();
    HotelPreddictionAdapter hotelAdapter;
    RelativeLayout errorLayout;
    ImageView errorImage;
    TextView errorTitle, errorMessage;
    Button btnRetry;
    RelativeLayout lottieContainer;
    LottieAnimationView lottie;



    RecyclerView recyclerViewTrain;
    RecyclerView.LayoutManager layoutManagerTrain;
    List<Train> trains = new ArrayList<>();


    TrainAdapter trainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_train_prediction);

        recyclerView = findViewById(R.id.recyclerView_hotel);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        recyclerViewTrain = findViewById(R.id.recyclerView_train);
        layoutManagerTrain = new LinearLayoutManager(getApplicationContext());
        recyclerViewTrain.setLayoutManager(layoutManagerTrain);
        recyclerViewTrain.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTrain.setNestedScrollingEnabled(false);
        recyclerViewTrain.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        errorLayout = findViewById(R.id.errorLayout);
        errorImage = findViewById(R.id.errorImage);
        errorTitle = findViewById(R.id.errorTitle);
        errorMessage = findViewById(R.id.errorMessage);
        btnRetry = findViewById(R.id.btnRetry);

        lottie = findViewById(R.id.lottie);
        lottieContainer = findViewById(R.id.lottie_container);
        lottieContainer.setVisibility(View.VISIBLE);

        totalPrice=findViewById(R.id.total_price);
        hotelAvg=findViewById(R.id.HotelsAvg);
        trainAvg=findViewById(R.id.train_price);
        days=findViewById(R.id.days);
        persons=findViewById(R.id.person);
        livingCost=findViewById(R.id.living_cost);
        from=findViewById(R.id.from);
        to=findViewById(R.id.to);
        CollapsingToolbarLayout collapsingToolbarLayout =findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Travel Cost Estimations");
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        String day = intent.getStringExtra("day");
        String star = intent.getStringExtra("star");
        String person = intent.getStringExtra("person");

        from.setText(Global.fromAddress.getCity());
        to.setText(Global.toAddress.getCity());
        getRetrofitTrain(Global.fromAddress.getCity(),Global.toAddress.getCity(),day,star,person);




    }


    private void getRetrofitTrain(String from ,String to,String day,String star,String person) {

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
        Call<PricePredictionTrain> call = service.getPredictionTrain(day,person,star,from,to,true);


        call.enqueue(new Callback<PricePredictionTrain>() {
            @SuppressLint({"CheckResult", "SetTextI18n", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NonNull Call<PricePredictionTrain> call, @NonNull Response<PricePredictionTrain> response) {


                if (response.isSuccessful()) {

                    if (!hotels.isEmpty()) {
                        hotels.clear();
                    }
                    if (!trains.isEmpty()) {
                        trains.clear();
                    }
                    for (int i = 0; i < response.body().getHotels().size(); i++) {

                        hotels.add(new Hotel(response.body().getHotels().get(i).getTitle(),
                                response.body().getHotels().get(i).getImg(),
                                response.body().getHotels().get(i).getLocation(),
                                response.body().getHotels().get(i).getInfo(),
                                response.body().getHotels().get(i).getStar(),
                                String.valueOf(response.body().getHotels().get(i).getPrice()),
                                response.body().getHotels().get(i).getRating(),
                                response.body().getHotels().get(i).getReviews(),
                                response.body().getHotels().get(i).getBooknow()
                                , false
                        ));
                    }
                    for (int i = 0; i < response.body().getTrain().size(); i++) {

                        trains.add(new Train(response.body().getTrain().get(i).getTitle(),
                                response.body().getTrain().get(i).getCost(),
                                response.body().getTrain().get(i).getTime()
                                ,response.body().getTrain().get(i).getTrainClass()));


                    }

                    hotelAdapter = new HotelPreddictionAdapter(hotels, getApplicationContext());
                    recyclerView.setAdapter(hotelAdapter);
                    hotelAdapter.notifyDataSetChanged();

                    trainAdapter = new TrainAdapter(trains, getApplicationContext());
                    recyclerViewTrain.setAdapter(trainAdapter);
                    trainAdapter.notifyDataSetChanged();
                    initListener();

                    lottieContainer.setVisibility(View.GONE);
                    lottie.cancelAnimation();
                    hotelAvg.setText("Hotels Avg Cost :"+response.body().getHotelsAvgCost());
                    trainAvg.setText("Train Avg Cost :"+" Rs "+response.body().getTrainAvgCost());
                    totalPrice.setText("Total Cost :"+" Rs "+response.body().getTotalCost());
                    persons.setText("No of person :"+response.body().getPerson());
                    days.setText("No of days :"+response.body().getDay());
                    livingCost.setText("Living Cost :"+" Rs "+response.body().getLivingCost());

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
                            "Please Try Again!\n" +
                                    errorCode);


                }




            }




            @Override
            public void onFailure(@NonNull Call <PricePredictionTrain> call, @NonNull Throwable t) {

                Toast toast = Toast.makeText(getApplicationContext(),
                        t.getMessage(),
                        Toast.LENGTH_SHORT);

                toast.show();

            }
        });


    }


    private void showErrorMessage(int imageView, String title, String message) {

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

    private void initListener() {


        hotelAdapter.setOnItemClickListener(new HotelPreddictionAdapter.OnItemClickListener() {


            @Override
            public void onClickBookNow(View view, int position) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(hotels.get(position).getBooknow())));


            }

            @Override
            public void onLike(View view, int position) {
                ImageView imageView = view.findViewById(R.id.like_button);

                if (hotels.get(position).isSave()) {
                    imageView.setImageResource(R.drawable.ic_like_button);
                    hotels.get(position).setSave(false);


                } else {
                    if (getLoginStatus()) {

                        imageView.setImageResource(R.drawable.ic_like_button_pressed);

                        String star = String.valueOf(hotels.get(position).getStar());


                        Global.hotelLikes.put(star, String.valueOf(Integer.parseInt(Objects.requireNonNull(Global.hotelLikes.get(star))) + 1));
                        hotels.get(position).setSave(true);
                        Toast toast = Toast.makeText(DisplayTrainPrediction.this, Global.hotelLikes.get(star), Toast.LENGTH_SHORT);
                        toast.setMargin(50, 50);
                        toast.show();


                    }

                }
                final Animation myAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 20);
                myAnim.setInterpolator(interpolator);
                view.startAnimation(myAnim);

            }

            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(hotels.get(position).getBooknow())));

            }
        });


    }

    public Boolean getLoginStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        return sharedPreferences.getBoolean("loginStatus", false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        if (getLoginStatus()) {

            Intent intent = new Intent(Intent.ACTION_SYNC, null, this, ExitService.class);


            intent.putExtra("token1",getToken());
            intent.putExtra("_1star",Global.hotelLikes.get("1"));
            intent.putExtra("_2star",Global.hotelLikes.get("2"));
            intent.putExtra("_3star",Global.hotelLikes.get("3"));
            intent.putExtra("_4star",Global.hotelLikes.get("4"));
            intent.putExtra("_5star",Global.hotelLikes.get("5"));

            startService(intent);

        }
        super.onPause();

    }


    public String getToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        return sharedPreferences.getString("token", null);
    }

    private void putRecommendations() {

        String token1, _1star, _2star, _3star, _4star, _5star;
        token1 = getToken();
        _1star = Global.hotelLikes.get("1");
        _2star = Global.hotelLikes.get("2");
        _3star = Global.hotelLikes.get("3");
        _4star = Global.hotelLikes.get("4");
        _5star = Global.hotelLikes.get("5");


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
        Call<Recommendation> call = service.updateRecommendation(token1, _1star, _2star, _3star, _4star, _5star);

        call.enqueue(new Callback<Recommendation>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<Recommendation> call, @NonNull Response<Recommendation> response) {

                Global.hotelLikes.put("1" ,response.body().get1star());
                Global.hotelLikes.put("2" ,response.body().get2star());
                Global.hotelLikes.put("3",response.body().get3star());
                Global.hotelLikes.put("4",response.body().get4star());
                Global.hotelLikes.put("5",response.body().get5star());
                Global.hotelRecommendation.clear();

                Global.hotelRecommendation = response.body().getRecommendation();

            }

            @Override
            public void onFailure(@NonNull Call<Recommendation> call, @NonNull Throwable t) {
                Toast toast = Toast.makeText(DisplayTrainPrediction.this, t.toString(), Toast.LENGTH_SHORT);
                toast.setMargin(50, 50);
                toast.show();
            }
        });

    }


    public Boolean getRec() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        return sharedPreferences.getBoolean("rec", true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}