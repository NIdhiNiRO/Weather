package com.virtusa.weather.retrofit;


import com.virtusa.weather.model.weatherModel;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by nidhiparekh on 10/11/17.
 */
public interface ServiceApi {

   //service API Call
    @GET("/data/2.5/weather")
    Call<weatherModel> GetWeatherDetails(@Query("q") String query, @Query("appid") String appid);
}
