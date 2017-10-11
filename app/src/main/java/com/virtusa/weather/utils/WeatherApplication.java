package com.virtusa.weather.utils;

import android.app.Application;

import com.virtusa.weather.retrofit.RetroClient;

/**
 * Created by nidhiparekh on 10/11/17.
 */
public class WeatherApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        RetroClient.CreateRetroClient();
        PreferenceUtil.getInstance(getApplicationContext());
    }
}
