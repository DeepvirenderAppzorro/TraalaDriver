package com.appzorro.driverappcabscout.web;

import com.appzorro.driverappcabscout.view.Activity.HomeActivity.model.DriverStatusResponse;
import com.appzorro.driverappcabscout.view.Activity.PathmapActivity.model.CustomerListResponse;
import com.appzorro.driverappcabscout.view.Activity.loginActivity.model.LoginResponse;
import com.appzorro.driverappcabscout.view.Activity.loginActivity.model.ProfileResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by vijay on 29/10/18.
 */

public interface WebApi {
    //*** Login Api
    @FormUrlEncoded
    @POST("driver_api.php?action=driver_login")
    Call<LoginResponse> islogin(@Field("email") String rideId, @Field("password") String pathImg, @Field("device_token") String token);

    //*** UserDetail Api
    @FormUrlEncoded
    @POST("driver_api.php?action=user_detail")
    Call<ProfileResponse> getProfile(@Field("driver_id") String driverid);

    //*** getDriverStatus Api
    @FormUrlEncoded
    @POST("driver_api.php?action=driverStatus")
    Call<DriverStatusResponse> getStatus(@Field("driver_id") String driverid,@Field("status") String status,@Field("latitude") String lattitude,@Field("longitude") String longitude,@Field("time") String time,@Field("socket_id") String socket_id);

    //*** getCustomerList Api
    @FormUrlEncoded
    @POST("driver_api.php?action=get_customer_list")
    Call<CustomerListResponse> getCustomerList(@Field("driver_id") String driverid, @Field("lat") String lattitude, @Field("long") String longitude);


}
