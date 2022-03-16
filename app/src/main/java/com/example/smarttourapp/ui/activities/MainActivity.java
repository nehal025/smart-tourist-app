package com.example.smarttourapp.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.smarttourapp.R;
import com.example.smarttourapp.model.RecommendationRs;
import com.example.smarttourapp.model.CurrentLocation;
import com.example.smarttourapp.model.Recommendation;
import com.example.smarttourapp.retrofit.RetrofitArrayApi;
import com.example.smarttourapp.ui.camera.Camera;
import com.example.smarttourapp.ui.fragments.HomeFragment;
import com.example.smarttourapp.ui.fragments.NewsFragment;
import com.example.smarttourapp.ui.fragments.ProfileFragment;
import com.example.smarttourapp.ui.fragments.PredictionFragment;
import com.example.smarttourapp.utils.Global;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nambimobile.widgets.efab.FabOption;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest locationRequest;
    int LOCATION_REQUEST_CODE = 10001;
    Boolean alreadyExecuted = false;
    FabOption locationDetect;
    FabOption foodDetect;
    public static String globalLogin = "login";


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
//        bottomNav.setOnItemSelectedListener (navListener);
        bottomNav.setBackground(null);
        bottomNav.getMenu().getItem(2).setEnabled(false);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationDetect = findViewById(R.id.location_detect);
        foodDetect = findViewById(R.id.food_detect);


        locationDetect.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Camera.class);
            intent.putExtra("role", "locationDetect");
            startActivity(intent);
        });

        foodDetect.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Camera.class);
            intent.putExtra("role", "foodDetect");
            startActivity(intent);
        });

        Global.restaurantLikes = loadMap();


        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();


        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    selectedFragment = new HomeFragment();

                    break;
                case R.id.navigation_save:
                    selectedFragment = new PredictionFragment();
                    break;
                case R.id.navigation_news:
                    selectedFragment = new NewsFragment();
                    break;
                case R.id.navigation_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();
            }
            return true;
        });

        if(getLoginStatus()){
            getRecommendations(getToken());
            getRecommendationsRes(getToken());

        }


        if (checkPermissions()) {
            syncLocation();
        } else {
            requestPermissions();
        }



    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions() && !alreadyExecuted) {
            setLocation();

        }

    }

    @SuppressLint("MissingPermission")
    private void syncLocation() {
        if (isLocationEnabled()) {
            setLocation();
        } else {
            startGPS();
        }
    }

    private void startGPS() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(locationSettingsResponse -> setLocation());

        locationSettingsResponseTask.addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                ResolvableApiException apiException = (ResolvableApiException) e;
                try {
                    apiException.startResolutionForResult(MainActivity.this, 1001);
                } catch (IntentSender.SendIntentException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    @SuppressLint("MissingPermission")
    private void setLocation() {
        mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
            Location location = task.getResult();

            if (location == null) {
                requestNewLocationData();
            } else {
                try {

                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    String city = addresses.get(0).getLocality();
                    String district = addresses.get(0).getSubAdminArea();
                    String state = addresses.get(0).getAdminArea();
                    String pinCode = addresses.get(0).getPostalCode();
                    String country = addresses.get(0).getCountryName();
                    String latitude = String.valueOf(addresses.get(0).getLatitude());
                    String longitude = String.valueOf(addresses.get(0).getLongitude());
                    String addressLine = addresses.get(0).getAddressLine(0);
                    Global.currentAddress = new CurrentLocation(city, district, state, pinCode, country, latitude, longitude, addressLine);

                    if (!alreadyExecuted) {
                        alreadyExecuted = true;
                    }


                } catch (Exception ignored) {
                }
            }
        });
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        @SuppressWarnings("deprecation") LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }


    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            setLocation();
        }
    };


    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
    }


    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                syncLocation();
            }
        }
    }

//    @SuppressLint("NonConstantResourceId")
//    public final BottomNavigationView.OnNavigationItemSelectedListener navListener =
//            item -> {
//                Fragment selectedFragment = null;
//                switch (item.getItemId()) {
//                    case R.id.navigation_home:
//                        selectedFragment = HomeFragment.newInstance(displayLocation);
//                        break;
//                    case R.id.navigation_save:
//                        selectedFragment = new SaveFragment();
//                        break;
//                    case R.id.navigation_news:
//                        selectedFragment = NewsFragment.newInstance(district);
//                        break;
//                    case R.id.navigation_profile:
//                        selectedFragment = new ProfileFragment();
//                        break;
//                }
//
//                if (selectedFragment != null) {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                            selectedFragment).commit();
//                }
//                return true;
//            };

    public String getName() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        return sharedPreferences.getString("name", "user");
    }

    public Boolean getLoginStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        return sharedPreferences.getBoolean("loginStatus", false);
    }

    public void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        SharedPreferences preferences1 = getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = preferences1.edit();
        editor1.clear();
        editor1.apply();

        Global.clear();
        finish();

    }


    private void getRecommendations(String token) {
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
        Call<Recommendation> call = service.getRecommendedHotels(getToken());

        call.enqueue(new Callback<Recommendation>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<Recommendation> call, Response<Recommendation> response) {


                if (response.isSuccessful()) {
//                          Global.clear2();
//
//
                    Global.hotelLikes.put("1", response.body().get1star());
                    Global.hotelLikes.put("2", response.body().get2star());
                    Global.hotelLikes.put("3", response.body().get3star());
                    Global.hotelLikes.put("4", response.body().get4star());
                    Global.hotelLikes.put("5", response.body().get5star());

                    if(!Global.hotelRecommendation.isEmpty()){
                        Global.hotelRecommendation.clear();
                    }

                    Global.hotelRecommendation = response.body().getRecommendation();


                } else {

                }
            }

            @Override
            public void onFailure(@NonNull Call<Recommendation> call, @NonNull Throwable t) {
                Toast toast = Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT);
                toast.setMargin(50, 50);
                toast.show();
            }
        });

    }

    private void getRecommendationsRes(String token) {
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
        Call<RecommendationRs> call = service.getRecommendedRestaurants(getToken());

        call.enqueue(new Callback<RecommendationRs>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<RecommendationRs> call, @NonNull Response<RecommendationRs> response) {


                if (response.isSuccessful()) {


                    assert response.body() != null;
                    Global.restaurantRecommendation = response.body().getCategory();
//                    @SuppressLint("ShowToast") Toast toast = Toast.makeText(MainActivity.this, response.body().getCategory().toString(), Toast.LENGTH_SHORT);


                } else {

                }
            }

            @Override
            public void onFailure(@NonNull Call<RecommendationRs> call, @NonNull Throwable t) {
                Toast toast = Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT);
                toast.setMargin(50, 50);
                toast.show();
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


    public void recommendationOff() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("rec", false);
        editor.apply();
    }

    public void recommendationOn() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("rec", true);
        editor.apply();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private HashMap<String, Integer> loadMap() {
        HashMap<String, Integer> outputMap = new HashMap<>();
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return outputMap;
    }

    public void clearLikes() {
        SharedPreferences preferences = getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        finish();

    }

}


