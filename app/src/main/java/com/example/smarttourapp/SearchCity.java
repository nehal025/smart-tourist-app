package com.example.smarttourapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.smarttourapp.model.CurrentLocation;
import com.example.smarttourapp.ui.activities.MainActivity;
import com.example.smarttourapp.ui.activities.SearchLocation;
import com.example.smarttourapp.ui.adapters.SearchAdapter;
import com.example.smarttourapp.utils.Global;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchCity extends AppCompatActivity {


    private Geocoder geocoder;
    SearchView searchView;
    RecyclerView searchRecycle;
    ArrayList<CurrentLocation> list;
    SearchAdapter searchAdapter;
    RecyclerView.LayoutManager layoutManager;
    FrameLayout frame;
    LinearLayout currentLocation;
    String operation;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc);
        currentLocation=findViewById(R.id.homeLocation);
        searchRecycle = findViewById(R.id.search_recycler);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        searchRecycle.setLayoutManager(layoutManager);
        searchRecycle.setItemAnimator(new DefaultItemAnimator());
        searchRecycle.setNestedScrollingEnabled(false);
        geocoder = new Geocoder(getApplicationContext());
        searchView = findViewById(R.id.searchView);
        frame = findViewById(R.id.frame);
        list = new ArrayList<>();
        searchView.setIconified(false);
        operation=getIntent().getStringExtra("operation");

        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(operation.equals("from")){
                    Global.fromAddress=new CurrentLocation(Global.currentAddress);
                }

                if(operation.equals("to")) {
                    Global.toAddress=new CurrentLocation(Global.currentAddress);
                }
                finish();
            }
        });

//        list.add(Global.currentAddress);
//        searchAdapter = new SearchAdapter(list, getApplicationContext());
//        searchRecycle.setAdapter(searchAdapter);
//        initListener();
        searchListener();

    }


    private  void searchListener(){

        searchView.setOnSearchClickListener(v -> {
            if(!list.isEmpty()) {
                list.clear();
                searchAdapter.notifyDataSetChanged();
                initListener();
                currentLocation.setVisibility(View.VISIBLE);
            }

        });

        searchView.setOnCloseListener(() -> {
            finish();
            return false;
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(!list.isEmpty()){
                    list.clear();
                    searchAdapter.notifyDataSetChanged();
                }
                currentLocation.setVisibility(View.GONE);

                try {
                    List<Address> addresses = geocoder.getFromLocationName(query, 5);

                    for (int i = 0; i < addresses.size(); i++) {

                        String city =addresses.get(i).getLocality();
                        String district=addresses.get(i).getSubAdminArea() ;
                        String state =addresses.get(i).getAdminArea();
                        String pinCode =addresses.get(i).getPostalCode();
                        String country =addresses.get(i).getCountryName();
                        String latitude =String.valueOf(addresses.get(i).getLatitude());
                        String longitude=String.valueOf(addresses.get(i).getLongitude());
                        String addressLine=addresses.get(i).getAddressLine(i);

                        if(city==null){
                            Toast.makeText(getApplicationContext(),"Please enter only city name",Toast.LENGTH_SHORT).show();
                        }else {
                            list.add(new CurrentLocation(city,district,state,pinCode,country,latitude,longitude,addressLine));
                            searchAdapter = new SearchAdapter(list, getApplicationContext());
                            searchRecycle.setAdapter(searchAdapter);
                            searchAdapter.notifyDataSetChanged();
                            initListener();
                        }

                    }



                } catch (IOException e) {
                    e.printStackTrace();
                }

                return false;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText.isEmpty()){
                    list.clear();
//                    list.add(Global.currentAddress);
//                    searchAdapter.notifyDataSetChanged();
//                    initListener();
                    currentLocation.setVisibility(View.VISIBLE);
                }

                if(newText.length()>0){
                    currentLocation.setVisibility(View.GONE);
                }

                return false;
            }
        });

    }


    private void initListener() {

        searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {

            @Override
            public void onClickBookNow(View view, int position) {

            }

            @Override
            public void onItemClick(int position) {
                if(operation.equals("from")){
                    Global.fromAddress=new CurrentLocation(list.get(position));
                }

                if(operation.equals("to")) {
                    Global.toAddress=new CurrentLocation(list.get(position));
                }
                finish();


            }
        });


    }
}