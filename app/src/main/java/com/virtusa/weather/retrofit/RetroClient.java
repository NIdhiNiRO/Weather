package com.virtusa.weather.retrofit;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.lang.reflect.Modifier;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


/**
 * Created by nidhiparekh on 10/11/17. **/
public class RetroClient {

    //Base Api URL
    public static final String BASE_URL="http://api.openweathermap.org/";
    public static final String IMAGE_URL="http://openweathermap.org/img/w/";
    private static RetroClient restClient;
    private ServiceApi serviceApi;


    public static void CreateRetroClient() {

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson sExposeGson = builder.create();
        restClient = new RetroClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getOKHttpClient())
                .addConverterFactory(GsonConverterFactory.create(sExposeGson)).build();
        restClient.serviceApi = retrofit.create(ServiceApi.class);
    }

    public static RetroClient getClient() {
        return restClient;
    }

    public static ServiceApi getServiceApis() {
        return restClient.serviceApi;
    }

    public static OkHttpClient getOKHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(logging);
        okHttpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request(); //Current Request
                Response response = chain.proceed(originalRequest); //Get response of the request
              /**//** DEBUG STUFF *//*
                if (BuildConfig.DEBUG) {
                    //I am logging the response body in debug mode. When I do this I consume the response (OKHttp only lets you do this once) so i have re-build a new one using the cached body
                    String bodyString = response.body().string();
                    Constant.logD(String.format("Sending request %s with headers %s ", originalRequest.url(), originalRequest.headers()));
                    Constant.logD(String.format("Got response HTTP %s %s \n\n with body %s \n\n with headers %s ", response.code(), response.message(), bodyString, response.headers()));
                    response = response.newBuilder().body(ResponseBody.create(response.body().contentType(), bodyString)).build();
                }*/
                return response;
            }
        });
        return okHttpClient;
    }

}
