package com.learntv.studybuddy.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();

    private static Retrofit retrofit = null;
    public static ApiInterface getClient(){
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.learntv.lk/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new Api().okHttpClient)
                    .build();
        }
        ApiInterface api = retrofit.create(ApiInterface.class);
        return api;
    }
}
