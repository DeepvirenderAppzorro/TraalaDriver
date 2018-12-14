package com.appzorro.driverappcabscout.view.Activity;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vijay on 15/6/18.
 */

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(" http://traala.com/cabscout/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClient())
                    .build();
        }
        return retrofit;
    }
    //get OkHttp instance
    private static OkHttpClient getOkHttpClient() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // builder.interceptors().add(getHeadersForApis());
        builder.interceptors().add(httpLoggingInterceptor);
        builder.readTimeout(90, TimeUnit.SECONDS);
        builder.connectTimeout(90, TimeUnit.SECONDS);
        return builder.build();
    }
}
