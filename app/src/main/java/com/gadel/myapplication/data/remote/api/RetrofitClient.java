package com.gadel.myapplication.data.remote.api;

import com.gadel.myapplication.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private ErpApiService myApi;

    private RetrofitClient() {
        // BuildConfig.BASE_URL jalará automáticamente la IP 192.168.1.15 que configuramos en build.gradle
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(ErpApiService.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public ErpApiService getApi() {
        return myApi;
    }
}