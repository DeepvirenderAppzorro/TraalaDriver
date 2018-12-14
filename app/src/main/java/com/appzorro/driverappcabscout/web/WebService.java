package com.appzorro.driverappcabscout.web;

import com.appzorro.driverappcabscout.view.Activity.HomeActivity.model.DriverStatusResponse;
import com.appzorro.driverappcabscout.view.Activity.PathmapActivity.model.CustomerListResponse;
import com.appzorro.driverappcabscout.view.Activity.loginActivity.model.LoginResponse;
import com.appzorro.driverappcabscout.view.Activity.loginActivity.model.ProfileResponse;
import com.appzorro.driverappcabscout.web.handler.GetCustomerListHandler;
import com.appzorro.driverappcabscout.web.handler.GetLoginHandler;
import com.appzorro.driverappcabscout.web.handler.GetProfileHandler;
import com.appzorro.driverappcabscout.web.handler.GetStatusHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vijay on 29/10/18.
 */

public class WebService {
    static WebService sInstance;
    private final WebApi retrofit;
    OkHttpClient client;
    Gson gson;

    public WebService() {
        sInstance = this;

        gson = new GsonBuilder()
                .setLenient()
                .create();


        retrofit = new Retrofit.Builder()

                .baseUrl("http://traala.com/cabscout/").client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(WebApi.class);

    }
    public static WebService getInstance() {
        return sInstance;
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

    //*** Login Api

    public void getLogin(final GetLoginHandler loginHandler, String email,String pwd,String token) {
        retrofit.islogin(email,pwd,token).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.body() != null) {
                    loginHandler.onSuccess(response.body());

                } else {
                    try {
                        loginHandler.onFail(response.errorBody().string().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginHandler.onFail(t.toString());
            }
        });
    }
    //End

    //***Get UserDetail Api

    public void getProfile(final GetProfileHandler profileHandler, String id) {
        retrofit.getProfile(id).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {

                if (response.body() != null) {
                    profileHandler.onSuccess(response.body());

                } else {
                    try {
                        profileHandler.onFail(response.errorBody().string().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                profileHandler.onFail(t.toString());
            }
        });
    }
    //End

    //***Get DriverStatus Api

    public void getDriverStatus(final GetStatusHandler getStatusHandler, String id,String status, String latitude, String longitude, String time,String socket_id) {
        retrofit.getStatus(id,status,latitude,longitude,time,socket_id).enqueue(new Callback<DriverStatusResponse>() {
            @Override
            public void onResponse(Call<DriverStatusResponse> call, Response<DriverStatusResponse> response) {

                if (response.body() != null) {
                    getStatusHandler.onSuccess(response.body());

                } else {
                    try {
                        getStatusHandler.onFail(response.errorBody().string().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DriverStatusResponse> call, Throwable t) {
                getStatusHandler.onFail(t.toString());
            }
        });
    }

    //End

//***Get CustomerList Api

    public void getCustomerList(final GetCustomerListHandler getCustomerListHandler, String id, String latitude, String longitude, String Key) {
        retrofit.getCustomerList(id,latitude,longitude).enqueue(new Callback<CustomerListResponse>() {
            @Override
            public void onResponse(Call<CustomerListResponse> call, Response<CustomerListResponse> response) {

                if (response.body() != null) {
                    getCustomerListHandler.onSuccess(response.body());

                } else {
                    try {
                        getCustomerListHandler.onFail(response.errorBody().string().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerListResponse> call, Throwable t) {
                getCustomerListHandler.onFail(t.toString());
            }
        });
    }
    //***End
}
