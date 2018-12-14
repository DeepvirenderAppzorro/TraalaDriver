package com.appzorro.driverappcabscout.model;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.Random;

public class JsonRequestIR {

    public static JSONObject driverRequest(Context activity,Double currentlat, Double currentlang,String deviceToken){
        //{latitude=30.7098011, longitude=76.6935132, ride_request_id=12,
        // message=157 has accepted the request, noti_type=driver_request}
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("latitude", String.valueOf(currentlat));
            jsonObject.put("longitude", String.valueOf(currentlang));
            jsonObject.put("message", CSPreferences.readString(activity, Constant.DRIVER_NAME+"")+" has accepted the request");
            jsonObject.put("ride_request_id", CSPreferences.readString(activity, Constant.RIDER_ID));
            jsonObject.put("noti_type", "driver_request");
            Log.d("jsondata",jsonObject+"");

        }catch (Exception e){
            Log.d("Exception",e.getMessage());
        }
        return jsonObject;
    }
    public static JSONObject rejectRequest(Context activity, String  customer_id, String driver_id, String ride_id, String noti_type, String poolType){

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("driver_id", driver_id);
            jsonObject.put("cutomerID", customer_id);
            jsonObject.put("ride_request_id", ride_id);
            jsonObject.put("poolType", poolType);
            jsonObject.put("driverSocketId",CSPreferences.readString(activity,Constant.SOCKET_ID));
            jsonObject.put("noti_type", noti_type);
            jsonObject.put("driver_index",Integer.parseInt(CSPreferences.readString(activity,"driver_index")));
            jsonObject.put("driverConsent", "reject");
            Log.d("jsondata",jsonObject+"");

        }catch (Exception e){
            Log.d("Exception",e.getMessage());
        }
        return jsonObject;
    }
    public static JSONObject accept(Context activity, String  customer_id, String driver_id, String ride_id, String noti_type, String poolType,String driverId,String lat,String lng){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("driverSocketId", CSPreferences.readString(activity,Constant.SOCKET_ID));
            jsonObject.put("driver_id",driverId);
            jsonObject.put("cutomerID", customer_id);
            jsonObject.put("ride_state", "ride_not_completed");
            jsonObject.put("ride_request_id", ride_id);
            jsonObject.put("lattitude", lat);
            jsonObject.put("longitude", lng);
            jsonObject.put("poolType", poolType);
            jsonObject.put("tracking", CSPreferences.readString(activity,Constant.REQUEST_TYPE));
            jsonObject.put("message", CSPreferences.readString(activity, Constant.DRIVER_NAME+"")+" has accepted the request");
            jsonObject.put("noti_type", noti_type);
            jsonObject.put("driverConsent", "accept");
            Log.d("jsondata",jsonObject+"");

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
            jsonObject.put("lattitude", String.valueOf(lat));
            jsonObject.put("longitude", String.valueOf(lng));
            jsonObject.put("noti_type", "driver location");

            jsonObject.put("bearing", String.valueOf(Utils.bearing));
            jsonObject.put("ride_state", "ride_not_completed");
            jsonObject.put("driverSocketId", CSPreferences.readString(context,Constant.SOCKET_ID));
            jsonObject.put("customer_id", CSPreferences.readString(context, Constant.RIDER_ID));
            Log.d("jsondata",jsonObject+"");

        } catch (Exception e) {

        }
        return jsonObject;
    }

    public static JSONObject jsonRequestForCash(Context context, String fare,String customerToken,String riderId) {
        JSONObject jsonObject = null;
        Random rnd = new Random();
        try {
            jsonObject = new JSONObject();
            jsonObject.put("noti_type", "cash_payment");
            jsonObject.put("message", "Base Fare");
            jsonObject.put("rider_id", riderId+"");
            jsonObject.put("amount", fare);
            jsonObject.put("ride_state", "ride_completed");
            Log.d("jsondata",jsonObject+"");


        } catch (Exception e) {

        }
        return jsonObject;
    }

    public static JSONObject ArrivedjsonRequest(Context context,String CUSTOMER_TOKEN,String customerID) {
        JSONObject jsonObject = null;
        //message=geena sidhu rani has been arrived to your location, noti_type=driver_arrived
        try {
            jsonObject = new JSONObject();
            jsonObject.put("noti_type", "driver_arrived");
            jsonObject.put("ride_state", "ride_not_completed");
            jsonObject.put("message", CSPreferences.readString(context, Constant.DRIVER_NAME+"") + " has been arrived to your location");
            jsonObject.put("customer_id", customerID);
            Log.d("jsondata",jsonObject+"");

        } catch (Exception e) {

        }
        return jsonObject;
    }

    public static JSONObject arriveTime(Context context,String time) {
        JSONObject jsonObject = null;
        //message=geena sidhu rani has been arrived to your location, noti_type=driver_arrived
        try {
            jsonObject = new JSONObject();
            jsonObject.put("noti_type", "arrive_time");
            jsonObject.put("time", time);
            jsonObject.put("ride_state", "ride_not_completed");
            jsonObject.put("message", CSPreferences.readString(context, Constant.DRIVER_NAME+"") + " has been arrived to your location");
            Log.d("jsondata",jsonObject+"");

        } catch (Exception e) {

        }
        return jsonObject;
    }

    public static JSONObject RideStartjsonRequest(Context context,String Message, String Noti_type, String customerToken,String ride_request_id,String customerId,String rideState,String lat,String lng) {
        JSONObject jsonObject = null;
        //{latitude=, Total Amount=, id=31, json3=, longitude=, ride_request_id=, message=Trip Has Started, noti_type=trip_started}

        Random rnd = new Random();
        int request_id = 1 + rnd.nextInt(999);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("noti_type", Noti_type);
            jsonObject.put("message", Message);
            jsonObject.put("ride_state", rideState);
            jsonObject.put("lattitude", lat);
            jsonObject.put("longitude", lng);
            jsonObject.put("id", CSPreferences.readString(context, "customer_id"));
            jsonObject.put("ride_request_id", ride_request_id);
            jsonObject.put("customer_id", customerId);
            Log.d("jsondata",jsonObject+"");

        } catch (Exception e) {

        }
        return jsonObject;
    }
    public static JSONObject RideStopjsonRequest(Context context,String Message, String Noti_type, String customerToken,String ride_request_id,String customerId,String rideState) {
        JSONObject jsonObject = null;
        //{latitude=, Total Amount=, id=31, json3=, longitude=, ride_request_id=, message=Trip Has Started, noti_type=trip_started}

        Random rnd = new Random();
        int request_id = 1 + rnd.nextInt(999);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("noti_type", Noti_type);
            jsonObject.put("message", Message);
            jsonObject.put("ride_state", rideState);
            jsonObject.put("id", CSPreferences.readString(context, "customer_id"));
            jsonObject.put("ride_request_id", ride_request_id);
            jsonObject.put("customer_id", customerId);
            Log.d("jsondata",jsonObject+"");

        } catch (Exception e) {

        }
        return jsonObject;
    }


    public static JSONObject jsonRequestForSingleDriver(Context context,String id) {
        CSPreferences.putString(context, Constant.DRIVER_STATUS, "5");
        JSONObject jsonObject = null;
        try {

            jsonObject = new JSONObject();
            jsonObject.put("driver_ID", id);
            jsonObject.put("noti_type", "customer_request");
            jsonObject.put("distance",CSPreferences.readString(context,"distanceCustomer"));
            jsonObject.put("customerID",CSPreferences.readString(context,Constant.RIDER_ID));
            jsonObject.put("poolType",CSPreferences.readString(context,"poolType"));
            jsonObject.put("message", "New ride request from " + CSPreferences.readString(context, Constant.RIDER_NAME));
            jsonObject.put("ride_request_id", CSPreferences.readString(context, Constant.REQUEST_ID));
            Log.d("jsondata",jsonObject+"");
        } catch (Exception e) {

        }
        return jsonObject;
    }

    public static JSONObject cancelCustomerDialog(Context context){
        CSPreferences.putString(context, Constant.DRIVER_STATUS, "5");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("RIDER_ID", CSPreferences.readString(context,Constant.RIDER_ID));
            jsonObject.put("noti_type", "cancel_dialog");
            jsonObject.put("ride_state", "ride_cancel_by_customer");
            Log.d("jsondata",jsonObject+"");
        } catch (Exception e) {

        }
        return jsonObject;


    }
}
