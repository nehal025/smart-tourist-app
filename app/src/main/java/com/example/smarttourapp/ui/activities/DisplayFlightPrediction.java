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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.smarttourapp.ExitService;
import com.example.smarttourapp.R;
import com.example.smarttourapp.animation.MyBounceInterpolator;
import com.example.smarttourapp.model.Flight;
import com.example.smarttourapp.model.Hotel;
import com.example.smarttourapp.model.PricePredictionFlight;
import com.example.smarttourapp.model.Recommendation;
import com.example.smarttourapp.retrofit.RetrofitArrayApi;
import com.example.smarttourapp.ui.adapters.FlightAdapter;
import com.example.smarttourapp.ui.adapters.HotelAdapter;
import com.example.smarttourapp.ui.adapters.HotelPreddictionAdapter;
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

public class DisplayFlightPrediction extends AppCompatActivity {
    TextView totalPrice;
    TextView hotelAvg;
    TextView flightAvg;
    TextView persons;
    TextView days;
    TextView livingCost;
    ProgressBar progressBar;


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Hotel> hotels = new ArrayList<>();

    RecyclerView recyclerViewFlight;
    RecyclerView.LayoutManager layoutManagerFlight;
    List<Flight> flights = new ArrayList<>();

    HotelPreddictionAdapter hotelAdapter;
    FlightAdapter flightAdapter;

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
        setContentView(R.layout.activity_display_flight_prediction);


        recyclerView = findViewById(R.id.recyclerView_hotel);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
       recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        recyclerViewFlight = findViewById(R.id.recyclerView_flight);
        layoutManagerFlight = new LinearLayoutManager(getApplicationContext());
        recyclerViewFlight.setLayoutManager(layoutManagerFlight);
        recyclerViewFlight.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFlight.setNestedScrollingEnabled(false);
        recyclerViewFlight.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        errorLayout = findViewById(R.id.errorLayout);
        errorImage = findViewById(R.id.errorImage);
        errorTitle = findViewById(R.id.errorTitle);
        errorMessage = findViewById(R.id.errorMessage);
        btnRetry = findViewById(R.id.btnRetry);

        lottie = findViewById(R.id.lottie);
        lottieContainer = findViewById(R.id.lottie_container);
        lottieContainer.setVisibility(View.VISIBLE);

        totalPrice=findViewById(R.id.totalprice);
        livingCost=findViewById(R.id.living_cost);
        hotelAvg=findViewById(R.id.HotelsAvg);
        flightAvg=findViewById(R.id.flight_price);
        days=findViewById(R.id.days);
        persons=findViewById(R.id.person);
        CollapsingToolbarLayout collapsingToolbarLayout =findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Travel Cost Estimations");
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        Intent intent = getIntent();
        String day = intent.getStringExtra("day");
        String star = intent.getStringExtra("star");
        String person = intent.getStringExtra("person");

       getRetrofitFlight(Global.fromAddress.getCity(),Global.toAddress.getCity(),Global.fromAddress.getDistrict(),Global.toAddress.getDistrict(),day,star,person);

    }


    private void getRetrofitFlight(String from ,String to,String fromDist ,String toDist,String day,String star,String person) {

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
        Call<PricePredictionFlight> call = service.getPredictionFlight(day,person,star,from,to,fromDist,toDist,true);


        call.enqueue(new Callback<PricePredictionFlight>() {
            @SuppressLint({"CheckResult", "NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<PricePredictionFlight> call, @NonNull Response<PricePredictionFlight> response) {



                if (response.isSuccessful()) {

                    if (!hotels.isEmpty()) {
                        hotels.clear();
                    }
                    if (!flights.isEmpty()) {
                        flights.clear();
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

                    for (int i = 0; i < response.body().getFlight().size(); i++) {

                        flights.add(new Flight(response.body().getFlight().get(i).getTitle(),
                                        response.body().getFlight().get(i).getImg(),
                                response.body().getFlight().get(i).getCost()
                                ,response.body().getFlight().get(i).getTime()));


                    }


                    hotelAdapter = new HotelPreddictionAdapter(hotels, getApplicationContext());
                    recyclerView.setAdapter(hotelAdapter);
                    hotelAdapter.notifyDataSetChanged();


                    flightAdapter = new FlightAdapter(flights, getApplicationContext());
                    recyclerViewFlight.setAdapter(flightAdapter);
                    flightAdapter.notifyDataSetChanged();
                    initListener();

                    lottieContainer.setVisibility(View.GONE);
                    lottie.cancelAnimation();
                    hotelAvg.setText("Hotels Avg Cost :"+response.body().getHotelsAvgCost());
                    flightAvg.setText("Flight Avg Cost :"+response.body().getFlightAvgCost());

                    totalPrice.setText("Total Cost :"+response.body().getTotalCost());
                    persons.setText("Person :"+response.body().getPerson());
                    days.setText("Days :"+response.body().getDay());
                    livingCost.setText("Living Cost :"+response.body().getLivingCost());


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
            public void onFailure(@NonNull Call <PricePredictionFlight> call, @NonNull Throwable t) {

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


                        Global.map.put(star, String.valueOf(Integer.parseInt(Objects.requireNonNull(Global.map.get(star))) + 1));
                        hotels.get(position).setSave(true);
                        Toast toast = Toast.makeText(DisplayFlightPrediction.this, Global.map.get(star), Toast.LENGTH_SHORT);
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
            intent.putExtra("_1star",Global.map.get("1"));
            intent.putExtra("_2star",Global.map.get("2"));
            intent.putExtra("_3star",Global.map.get("3"));
            intent.putExtra("_4star",Global.map.get("4"));
            intent.putExtra("_5star",Global.map.get("5"));

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
        _1star = Global.map.get("1");
        _2star = Global.map.get("2");
        _3star = Global.map.get("3");
        _4star = Global.map.get("4");
        _5star = Global.map.get("5");


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

                Global.map.put("1" ,response.body().get1star());
                Global.map.put("2" ,response.body().get2star());
                Global.map.put("3",response.body().get3star());
                Global.map.put("4",response.body().get4star());
                Global.map.put("5",response.body().get5star());
                Global.recommendation.clear();

                Global.recommendation = response.body().getRecommendation();

            }

            @Override
            public void onFailure(@NonNull Call<Recommendation> call, @NonNull Throwable t) {
                Toast toast = Toast.makeText(DisplayFlightPrediction.this, t.toString(), Toast.LENGTH_SHORT);
                toast.setMargin(50, 50);
                toast.show();
            }
        });

    }


    public Boolean getRec() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        return sharedPreferences.getBoolean("rec", true);
    }

}