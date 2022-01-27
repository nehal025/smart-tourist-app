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
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.smarttourapp.R;
import com.example.smarttourapp.ui.camera.Camera;
import com.example.smarttourapp.ui.fragments.HomeFragment;
import com.example.smarttourapp.ui.fragments.NewsFragment;
import com.example.smarttourapp.ui.fragments.ProfileFragment;
import com.example.smarttourapp.ui.fragments.SaveFragment;
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
import com.google.android.material.navigation.NavigationBarView;
import com.nambimobile.widgets.efab.FabOption;

import java.util.List;
import java.util.Locale;


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


        HomeFragment homeFragment =  new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();


        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    selectedFragment = new HomeFragment();

                    break;
                case R.id.navigation_save:
                    selectedFragment = new SaveFragment();
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


    }


    @Override
    protected void onStart() {
        super.onStart();
        if (checkPermissions()) {
            syncLocation();
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
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
                    Global.city = addresses.get(0).getLocality();
                    Global.district = addresses.get(0).getSubAdminArea();
                    Global.State=addresses.get(0).getAdminArea();
                    Global.pinCode=addresses.get(0).getPostalCode();
                    Global.country = addresses.get(0).getCountryName();



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


    }




}


