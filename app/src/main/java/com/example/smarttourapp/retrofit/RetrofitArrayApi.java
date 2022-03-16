package com.example.smarttourapp.retrofit;


import com.example.smarttourapp.model.RecommendationRs;
import com.example.smarttourapp.model.ArrayListCategory;
import com.example.smarttourapp.model.Food;
import com.example.smarttourapp.model.PricePredictionFlight;
import com.example.smarttourapp.model.PricePredictionTrain;
import com.example.smarttourapp.model.Recommendation;
import com.example.smarttourapp.model.Restaurant;
import com.example.smarttourapp.model.RegisterBody;
import com.example.smarttourapp.model.Hotel;
import com.example.smarttourapp.model.LoginResponse;
import com.example.smarttourapp.model.Place;
import com.example.smarttourapp.model.ThingsToDo;
import com.example.smarttourapp.model.news.News;


import java.util.List;

import okhttp3.ResponseBody;
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

    @GET("/hotels/live/")
    Call <List<Hotel>>getLiveHotelWithStar(@Query("location")String name, @Query("star")List<String> recommendation);

    @GET("/restaurants/live/")
    Call <List<Restaurant>>getLiveRestaurants(@Query("location")String name);

    @POST("/restaurants/live/")
    Call <List<Restaurant>>getLiveRestaurantsWithStar(@Query("location")String name,@Body ArrayListCategory arrayListCategory);


    @GET("/places/live/")
    Call <List<ThingsToDo>>getThingsToDo(@Query("location")String name);

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

    @POST("users/recommendation/hotel")
    Call<Recommendation> updateRecommendation(@Query("token") String token,
                                             @Query("_1star") String _1star,
                                             @Query("_2star") String _2star,
                                             @Query("_3star") String _3star,
                                             @Query("_4star") String _4star,
                                             @Query("_5star") String _5star
                                            );


    @GET("users/recommendation/hotel")
    Call <Recommendation>getRecommendedHotels( @Query("token") String token);


    @GET("/predictions/")
    Call <PricePredictionTrain>getPredictionTrain(@Query("day") String day,
                                                   @Query("person") String person,
                                                   @Query("star") String star,
                                                   @Query("fromCity") String fromCity,
                                                   @Query("toCity") String toCity,
                                                   @Query("trainBool") Boolean trainBool);
    @GET("/predictions/")
    Call <PricePredictionFlight>getPredictionFlight(@Query("day") String day,
                                                    @Query("person") String person,
                                                    @Query("star") String star,
                                                    @Query("fromCity") String fromCity,
                                                    @Query("toCity") String toCity,
                                                    @Query("fromDist") String fromDist,
                                                    @Query("toDist") String toDist,
                                                    @Query("flightBool") Boolean flightBool);



    @POST("users/recommendation/restaurants/")
    Call<ResponseBody> postCategoryRes(@Body ArrayListCategory arrayListCategory ,@Query("token")String token);

    @GET("users/recommendation/restaurants/")
    Call <RecommendationRs>getRecommendedRestaurants(@Query("token") String token);


    @POST("/users/recommendation/clear")
    Call<ResponseBody> clearRecommendation(@Query("token") String token);
}