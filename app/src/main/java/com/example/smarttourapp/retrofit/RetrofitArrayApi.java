package com.example.smarttourapp.retrofit;


import com.example.smarttourapp.model.Food;
import com.example.smarttourapp.model.Restaurant;
import com.example.smarttourapp.model.RegisterBody;
import com.example.smarttourapp.model.Hotel;
import com.example.smarttourapp.model.LoginResponse;
import com.example.smarttourapp.model.Place;
import com.example.smarttourapp.model.news.News;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitArrayApi {
    //news
    @GET("everything")
    Call<News> getNewsSearch(
            @Query("q") String keyword,
            @Query("apiKey") String apiKey,
            @Query("domains") String domains
    );



    @GET("/hotels/live/")
    Call <List<Hotel>>getLiveHotel(@Query("location")String name);

    @GET("/restaurants/live/")
    Call <List<Restaurant>>getLiveRestaurants(@Query("location")String name);


    @GET("/places/")
    Call <List<Place>>getPlace(@Query("name")String name);


    @GET("/places/")
    Call <List<Place>>getPlaceByState(@Query("state")String State);

    @GET("/places/")
    Call <List<Place>>getRecommendedPlace(@Query("state")String name);

    @GET("places/")
    Call <List<Place>>getFeaturedPlaces(@Query("featured")Boolean featured);

    @GET("/food/")
    Call <List<Food>>getFoodByState(@Query("state")String name);

    @GET("/food/")
    Call <List<Food>>getFood(@Query("name")String name);

    @POST("/users/register")
    Call<LoginResponse> registerUser(@Body RegisterBody RegisterBody);


    @POST("/users/login")
    Call<LoginResponse> loginUser(@Body RegisterBody RegisterBody);

    @POST("users/logout")
    Call<LoginResponse> logoutUser(@Body RegisterBody RegisterBody);

    @POST("users/recommendation")
    Call<LoginResponse> updateRecommendation(@Body RegisterBody RegisterBody);


    @GET("users/recommendation/")
    Call <List<Place>>getRecommendation(@Body RegisterBody RegisterBody);

}