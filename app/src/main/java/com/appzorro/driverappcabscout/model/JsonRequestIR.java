package com.appzorro.driverappcabscout.model;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.Random;

public class JsonRequestIR {

    public static JSONObject driverRequest(Context activity,Double currentlat, Double currentlang){
        //{latitude=30.7098011, longitude=76.6935132, ride_request_id=12,
        // message=157 has accepted the request, noti_type=driver_request}
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("latitude", String.valueOf(currentlat));
            jsonObject.put("longitude", String.valueOf(currentlang));
            jsonObject.put("message", CSPreferences.readString(activity, "user_name")+" has accepted the request");
            jsonObject.put("ride_request_id", CSPreferences.readString(activity, Constant.CUSTOMER_ID));
            jsonObject.put("noti_type", "driver_request");

        }catch (Exception e){
            Log.d("Exception",e.getMessage());
        }
        return jsonObject;
    }
    public static JSONObject jsonRequestForLatLong(Context context, Double lat, Double lng) {
        JSONObject jsonObject = null;
        Random rnd = new Random();
        int request_id = 1 + rnd.nextInt(999);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("id", String.valueOf(lat));
            jsonObject.put("ride_request_id", String.valueOf(lng));
            jsonObject.put("noti_type", "driver location");
            jsonObject.put("customer_id", CSPreferences.readString(context, Constant.CUSTOMER_ID));
        } catch (Exception e) {

        }
        return jsonObject;
    }

    public static JSONObject ArrivedjsonRequest(Context context) {
        JSONObject jsonObject = null;
        //message=geena sidhu rani has been arrived to your location, noti_type=driver_arrived
        Random rnd = new Random();
        int request_id = 1 + rnd.nextInt(999);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("noti_type", "driver_arrived");
            jsonObject.put("message", CSPreferences.readString(context, "user_name") + " has been arrived to your location");
            jsonObject.put("customer_id", CSPreferences.readString(context, Constant.CUSTOMER_ID));
        } catch (Exception e) {

        }
        return jsonObject;
    }


    public static JSONObject RideStartjsonRequest(Context context,String Message, String Noti_type) {
        JSONObject jsonObject = null;
        //{latitude=, Total Amount=, id=31, json3=, longitude=, ride_request_id=, message=Trip Has Started, noti_type=trip_started}

        Random rnd = new Random();
        int request_id = 1 + rnd.nextInt(999);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("noti_type", Noti_type);
            jsonObject.put("message", Message);
            jsonObject.put("id", CSPreferences.readString(context, "customer_id"));
            jsonObject.put("ride_request_id", CSPreferences.readString(context, Constant.REQUEST_ID));
            jsonObject.put("customer_id", CSPreferences.readString(context, Constant.CUSTOMER_ID));

        } catch (Exception e) {

        }
        return jsonObject;
    }
}
