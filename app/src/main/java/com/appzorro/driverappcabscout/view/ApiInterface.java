package com.appzorro.driverappcabscout.view;

import com.appzorro.driverappcabscout.model.Beans.CabCompanyBean;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

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

}
