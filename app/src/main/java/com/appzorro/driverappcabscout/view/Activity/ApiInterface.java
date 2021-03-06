package com.appzorro.driverappcabscout.view.Activity;

import com.appzorro.driverappcabscout.model.Beans.CabCompanyBean;
import com.appzorro.driverappcabscout.model.Beans.Fare;
import com.appzorro.driverappcabscout.model.Beans.ModelForgot;
import com.appzorro.driverappcabscout.model.Beans.ModelGetVehicleType;
import com.appzorro.driverappcabscout.model.Beans.ModelUpdatePic;
import com.appzorro.driverappcabscout.model.Beans.SnapShotMapResponse;
import com.appzorro.driverappcabscout.model.UpdateDocument;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by vijay on 15/6/18.
 */

public interface ApiInterface {

    // Model API
    //  http://traala.com/cabscout/driver_api.php?action=rideStartByDriver&driver_id=250&ride_request_id=1&latitude=12.11&longitude=12.11
    @FormUrlEncoded
    @POST("driver_api.php?action=tripStarted")
    Call<ModelTrip> getmodeldemoresult(@Field("driver_id") String driver_id, @Field("ride_request_id") String ride_request_id, @Field("time") String tym, @Field("date") String Date, @Field("pickup_cordinates") String longitude);


    @GET("driver_api.php?action=company_list")
    Call<CabCompanyBean> GetCompanyList();
    //  http://traala.com/cabscout/driver_api.php?action=forgot_password&email=soberoi031@gmail.com
    @FormUrlEncoded
    @POST("driver_api.php?action=forgot_password")
    Call<ModelForgot> getForgotResponse(@Field("email") String email_id);


    @FormUrlEncoded
    @POST("driver_api.php?action=update_profile_pic")
    Call<ModelUpdatePic> updateProfilePic(@Field("driver_id") String driver_id, @Field("profile_pic") String profilePic);
    // ** Send googlemap snapshot

    @Multipart
    @POST("driver_api.php?action=update_document")
    Call<UpdateDocument> updateDocument(@Part("driver_id") String driver_id,@Part("type") String typ,@Part MultipartBody.Part image, @Part MultipartBody.Part image1, @Part("number") String number);
    // ** Send googlemap snapshot

    @FormUrlEncoded
    @POST("driver_api.php?action=path_image")
    Call<SnapShotMapResponse> snapShotGoogleMap(@Field("ride_id") String rideId, @Field("path_image") String pathImg);

    //** End


    @FormUrlEncoded
    @POST("driver_api.php?action=vehicle_type")
    Call<ModelGetVehicleType> getVehicleType(@Field("company_id") String comanyid);

    @FormUrlEncoded
    @POST("driver_api.php?action=amountpaid")
    Call<Fare.ModelCash23> getCash(@Field("company_id") String comanyid, @Field("car_type") String cartype, @Field("distance") String distance, @Field("time") String time, @Field("customer_id") String customer_id, @Field("driver_id") String driver_id, @Field("ride_id") String ride_id);

}
