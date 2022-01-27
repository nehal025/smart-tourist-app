package com.example.smarttourapp.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttourapp.R;
import com.example.smarttourapp.ui.activities.DisplayPlace;
import com.example.smarttourapp.ui.activities.FoodList;
import com.example.smarttourapp.ui.activities.LocationClassifier;
import com.example.smarttourapp.ui.activities.PlaceList;
import com.example.smarttourapp.ui.activities.RestaurantList;
import com.example.smarttourapp.model.Place;
import com.example.smarttourapp.retrofit.RetrofitArrayApi;
import com.example.smarttourapp.ui.activities.HotelsList;
import com.example.smarttourapp.ui.adapters.FeaturedPlaceAdapter;
import com.example.smarttourapp.ui.adapters.HotelAdapter;
import com.example.smarttourapp.utils.Global;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment  {
    ArrayList<Place> featuredLocations = new ArrayList<>();

    TextView textView;
    private static final String ARG_LOCATION = "argLocation";

    RecyclerView featuredRecycler;
    FeaturedPlaceAdapter adapter;
    MaterialCardView getHotel;
    MaterialCardView getRestaurant;
    MaterialCardView getFood;
    MaterialCardView getPlace;
    ProgressBar progressBar;
    ConstraintLayout fragment_hide;


    public HomeFragment() {

    }

    public static HomeFragment newInstance(String location) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
         fragment_hide=view.findViewById(R.id.fragment_hide);

        textView = view.findViewById(R.id.text_location);
        featuredRecycler = view.findViewById(R.id.featured_recycler);
        getHotel=view.findViewById(R.id.getHotel);
        getRestaurant=view.findViewById(R.id.getRestaurant);
        getFood=view.findViewById(R.id.getFood);
        getPlace=view.findViewById(R.id.getPlace);
        progressBar= view.findViewById(R.id.fragment_progressbar);
        fragment_hide.setVisibility(View.INVISIBLE);

//        if (getArguments() != null) {
//             location = getArguments().getString(ARG_LOCATION);
//            textView.setText(location);
//        }

        getHotel.setOnClickListener(v -> {
            Intent myIntent = new Intent(getContext(), HotelsList.class);
            startActivity(myIntent);
        });



        getRestaurant.setOnClickListener(v -> {
            Intent myIntent = new Intent(getContext(), RestaurantList.class);
            startActivity(myIntent);
        });

        getFood.setOnClickListener(v -> {
            Intent myIntent = new Intent(getContext(), FoodList.class);
            startActivity(myIntent);
        });

        getPlace.setOnClickListener(v -> {
            Intent myIntent = new Intent(getContext(), PlaceList.class);
            startActivity(myIntent);
        });

        featuredRecycler();

        getRetrofit();


        return view;
    }

    private void featuredRecycler() {

        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public void onStart() {
        super.onStart();

    }


    private void getRetrofit() {

        String baseURL = "https://smart-tourist-app.herokuapp.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayApi service = retrofit.create(RetrofitArrayApi.class);
        Call<List<Place>> call = service.getFeaturedPlaces(true);


        call.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(@NonNull Call<List<Place>> call, @NonNull Response<List<Place>> response) {

                for (int i = 0; i < response.body().size(); i++) {
                    String id=response.body().get(i).getId();
                    String name=response.body().get(i).getName();

                    List<String> img = new ArrayList<>(response.body().get(i).getImg());
                    String location=response.body().get(i).getLocation();
                    String state=response.body().get(i).getState();
                    String info=response.body().get(i).getInfo();
                    Boolean featured=response.body().get(i).getFeatured();
                    Boolean ml=response.body().get(i).getMl();
                    featuredLocations.add(new Place(id,name,img,location,state,info,featured,ml));

                }

                adapter = new FeaturedPlaceAdapter(featuredLocations,getContext());
                featuredRecycler.setAdapter(adapter);
                initListener();
                load();

            }

            @Override
            public void onFailure(Call <List<Place>> call, Throwable t) {



            }
        });


    }


    public void load() {
        final Handler h =new Handler();
        Runnable  r= new Runnable() {

            public void run() {

                if(!Global.city.equals("")){
                    textView.setText(Global.city);
                    progressBar.setVisibility(View.GONE);

                    fragment_hide.setVisibility(View.VISIBLE);

                    h.removeCallbacksAndMessages(null);

                    return;
                }

                h.postDelayed(this, 100);
            }
        };

        h.postDelayed(r, 100);
    }


    private void initListener(){

        adapter.setOnItemClickListener(new FeaturedPlaceAdapter.OnItemClickListener() {

            @Override
            public void onClickBookNow(View view, int position) {

            }

            @Override
            public void onItemClick(int position) {
                Intent myIntent = new Intent(getActivity(), DisplayPlace.class);
                myIntent.putExtra("location", featuredLocations.get(position).getName());
                startActivity(myIntent);

            }
        });

    }

}