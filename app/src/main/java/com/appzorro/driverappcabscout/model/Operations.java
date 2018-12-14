package com.appzorro.driverappcabscout.model;

import android.content.Context;
import android.util.Log;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.view.Activity.Logger;
import com.appzorro.driverappcabscout.view.Activity.PathmapActivity.view.PathMapActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

;
;

/**
 * Created by rishav on 17/1/17.
 */

public class Operations {

    private static final String TAG = Operations.class.getSimpleName();

    public static String getCabCompaniesTask(Context context, String cabalias) {

        String params = Config.companyaliasurl + "&company_id=" + cabalias;
        Log.e(TAG, "cabcompanieslist" + params);
        return params;
    }

    public static String simpleRegister(Context context, String companyid, String emailid, String password, String name,
                                        String devicetype, String devicetoken, String mobileno, String profilepic, String city,
                                        String zip, String driverlicence) {


        String parms = Config.simplesignupurl + "&company_id=" + companyid + "&email=" + emailid + "&password=" + password + "&name=" + name
                + "&device_token=" + devicetoken + "&device_type=" + devicetype + "&mobile=" + mobileno + "&profile_pic=" + profilepic
                + "&city=" + city + "&zip=" + zip + "&driver_license=" + driverlicence;
        Log.e("simple signup url", parms);
        return parms;

    }

    public static String getLocationId(Context context, String cabId) {
        String params = Config.cab_location_list + "&company_id=" + cabId;

        Log.e(TAG, "company_list_params-- " + params);

        return params;
    }

    public static String getLastStateOfRide(Context context, String rideId) {
        String params = Config.ride_last_state + "&ride_id=" + rideId;

        Log.e(TAG, "getLast__Ride_params-" + params);

        return params;
    }


    public static String distanceMatrix(Context context, double originlat, double originlng, double dest_lat, double destlng) {
        String params = "";
        try {
            JSONObject locationJsonObject = new JSONObject();
            locationJsonObject.put("origin", originlat + "," + originlng);
            locationJsonObject.put("destination", dest_lat + "," + destlng);
            params = "https://maps.googleapis.com/maps/api/distancematrix/" +
                    "json?origins=" + locationJsonObject.getString("origin") + "&destinations=" + locationJsonObject.getString("destination") + "&mode=driving&" +
                    "language=en-EN&sensor=false&key=" + context.getResources().getString(R.string.google_android_map_api_key_none);
            Log.d("distanceParams", params);
        } catch (JSONException e) {

        }

        Log.e(TAG, "distance_verifyparams-- " + params);

        return params;
    }


    // Login operation below
    public static String loginTask(Context context, String email, String password, String deviceToken) {
        String params = Config.login_url + "&email=" + email + "&password=" + password + "&device_token=" + deviceToken;

        Log.e(TAG, "loginparameters--" + params);
        return params;
    }

    public static String facebookLoginTask(Context context, String fb_id) {
        String params = Config.facebook_login_verify_url + fb_id;
        Log.e(TAG, "login_verify params-- " + params);
        return params;
    }


    public static String fbLoginParams(Context context, String company_id, String email, String name,
                                       String token, String mobile, String imageUrl, String fb_id, String drivinglince,
                                       String city, String zipcode) {
        /*String params = Config.fb_login_url+company_id+"&email="+email+"&password=WJBJvfHTRNT"+"&name="+name+"&device_token="+token+"&device_type=A"+"&mobile="+mobile+"&profileImage="+imageUrl+"&facebook_id="+fb_id;*/

        try {
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("company_id", company_id);
            postDataParams.put("email", email);
            postDataParams.put("password", "welcome");
            postDataParams.put("name", name);
            postDataParams.put("device_token", token);
            postDataParams.put("device_type", "A");
            postDataParams.put("mobile", mobile);
            postDataParams.put("profile_pic", imageUrl);
            postDataParams.put("facebook_id", fb_id);
            postDataParams.put("driver_license", drivinglince);
            postDataParams.put("city", city);
            postDataParams.put("zip", zipcode);
            String params = null;
            try {
                params = Utils.getPostDataString(postDataParams);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return params;

        } catch (Exception e) {
            e.printStackTrace();
        }
        //     Log.e(TAG, "fb_login params-- "+params);return params;} catch (JSONException e) {e.printStackTrace();}return null;}
        return null;
    }


    public static String simpleuserRegister(Context context, String LocId, String companyid, String emailid, String name, String devicetype, String devicetoken, String mobileno, String profilepic, String city, String zip, String driverlicence, String County, String State, String lat, String Long, String password) {
        String params = Config.registerUrl + "&location_id=" + LocId + "&company_id=" + companyid + "&email=" + emailid + "&name=" + name + "&device_token=" + devicetoken + "&device_type=" + devicetype + "&mobile=" + mobileno + "&profile_pic=" + profilepic + "&facebook_id=" + "" + "&driver_license=" + driverlicence +
                "&city=" + city + "&zip=" + zip + "&verified_status=" + "1" + "&country=" + County + "&state=" + State + "&latitude=" + lat + "&longitude=" + Long + "&password=" + password;
        Log.d("params_register", params);
        return params;
    }

    public static String nearestRoadlatlng(Context context, String latlng) {

        String parms = Config.nearestroadurl + latlng + "&key=" + context.getResources().getString(R.string.google_android_map_api_key_none);
        return parms;
    }


    public static String directionApi(Context context, double lat, double lng, double destination_lat, double dest_long) {

        String parms = "https://maps.googleapis.com/maps/api/directions/json?" +
                "mode=driving&"
                + "transit_routing_preference=less_driving&"
                + "origin=" + String.valueOf(lat) + "," + String.valueOf(lng) + "&"
                + "destination=" + String.valueOf(destination_lat) + "," + String.valueOf(dest_long) + "&"
                + "key=" + context.getResources().getString(R.string.google_android_map_api_key_none);
        Log.d("chkDirectionApi", parms);
        // String parms = Config.nearestroadurl + latlng + "&key=" + "AIzaSyD0f9QMvoH2EBf0qEyHO-afhPX3yluriu4";
        return parms;
    }


    public static String getUserDetail(Context context, String driverid) {
        String parms = Config.userdetail_url + driverid;
        Log.e("usedetailurlurl", parms);
        return parms;
    }

    /*  public static String getCustomerRequest(Context context, String driverid) {
          String parms = Config.customerrequesturl + driverid;
          Log.e("customerrequesturl", parms);
          return parms;
      }*/
    public static String getCustomerRequest(Context context, String driverid, String lati, String lng) {
        String parms = Config.customerrequesturl + driverid + "&lat=" + lati + "&long=" + lng;
        Log.e("customerrequesturl", parms);
        return parms;
    }

    public static String customer_status(Context context, String customer_id, String rideId) {

        String params = Config.customer_status + customer_id + "&ride_id=" + rideId;
        Log.e(TAG, "customer_status_params-- " + params);
        return params;
    }

    public static String acceptByDriver(Context context, String driverid, String requestid,
                                        String driverlat, String driverlng, String riderId, String poolType) {
        String parms = Config.acceptrequestbydriver + driverid + "&ride_request_id=" + requestid + "&latitude=" +
                driverlat + "&longitude=" + driverlng + "&customer_id=" + riderId + "&type=" + poolType;
        Log.e("driver_accept_url", parms);
        Logger.addRecordToLog(parms);
        return parms;
    }
    public static String getDailyEarn(Context context, String driverid) {

        String parms = Config.getdailyearning + driverid;
        Log.e("daily_earning", parms);
        //Logger.addRecordToLog(parms);
        return parms;
    }


    public static String rejectByDriver(String driverid, String requestid) {

        String parms = Config.rejectrequestbydriver + driverid + "&ride_request_id=" + requestid;
        Log.e("driver_accept_url", parms);
        return parms;
    }

    public static String laterBooking(Context context, String driverid) {

        String parms = Config.laterbookingurl + driverid;
        Log.e("later booking url", parms);
        return parms;
    }

    public static String changePassword(Context context, String driverid, String oldpassword, String newpassword) {

        String parms = Config.changepasswordurl + driverid + "&oldpassword=" + oldpassword + "&newpassword=" + newpassword;

        Log.e("changepasswordurl", parms);
        return parms;
    }

    //update image

    public static String updateProfilePic(Context context, String driverid, String img_base64) {

        String parms = Config.updateProfilePic + driverid + "&profile_pic=" + img_base64;

        Log.e("updateProfilePic_url", parms);
        return parms;
    }

    //End
    /* public static String updateProfile(Context context,String driverid,String profilepic,String name ,String mobile){

         String parms = Config.updateprofileurl+driverid+"&profile_pic="+profilepic+"&name="+name+"&mobile="+mobile;
         Log.e("update profile url",parms);
         return parms.replaceAll("\n","");
     }*/
    public static String updateProfile(Context context, String driverid, String name,
                                       String mobile) {
        /*String params = Config.fb_login_url+company_id+"&email="+email+"&password=WJBJvfHTRNT"+"&name="+name+"&device_token="+token+"&device_type=A"+"&mobile="+mobile+"&profileImage="+imageUrl+"&facebook_id="+fb_id;*/

        try {
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("driver_id", driverid);
            Log.d("driverId", driverid + " at profile time");
            postDataParams.put("name", name);
            postDataParams.put("mobile", mobile);
            String params = null;
            try {
                params = Utils.getPostDataString(postDataParams);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return params;

        } catch (Exception e) {
            e.printStackTrace();

        }
        //     Log.e(TAG, "fb_login params-- "+params);return params;} catch (JSONException e) {e.printStackTrace();}return null;}
        return null;
    }

    public static String changeComapnyName(Context context, String driverid, String cabalias) {

        String parms = Config.changecompanyurl + driverid + "&company_id=" + cabalias;
        Log.d("paramsChange", parms);
        return parms;

    }

    public static String updteProfile(Context context, String driverid, String name, String no, String licenseValidity, String licenseNo, String addrss, String zip) {
        String parms = Config.updateprofileurl + "&driver_id=" + driverid + "&name=" + name + "&mobile=" + no + "&address=" + addrss + "&zip=" + zip + "&license_validity=" + licenseValidity + "&license_number=" + licenseNo;
        Log.d("paramsChange", parms);
        return parms;

    }

    public static String reviewofDriver(Context context, String driverid) {

        String parms = Config.reviewcheckurl + driverid;
        Log.e("reviewcheckurl", parms);
        return parms;

    }

    public static String sendRatingtoCustomer(Context context, String driverid, String customerid, String rating, String feedback, String rideRequestId) {
        String parms = Config.ratingtocustomerurl + driverid + "&customer_id=" + customerid + "&rating=" + rating + "&feedback=" + feedback + "&ride_request_id=" + rideRequestId;
        Log.e("ratingurl", parms);
        return parms;
    }
    public static String sendDrivertStatus(Context context, String driverid, String status, String latitude, String longititude, String ts,String soc_id) {
        String parms = Config.onlineoffilne +"&driver_id="+ driverid + "&status=" + status + "&latitude=" + latitude + "&longitude=" + longititude + "&time=" + ts +"&socket_id="+soc_id;
        Log.e("onlineOffline", parms);
        return parms;
    }

    public static String sendDriverstatus(Context context, String driverid, String status, String latitude, String longititude, String ts) {
        try {
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("driver_id", driverid);
            postDataParams.put("status", status);
            postDataParams.put("latitude", latitude);
            postDataParams.put("longitude", longititude);
            postDataParams.put("time", ts);
            String params = null;
            try {
                params = Utils.getPostDataString(postDataParams);
                Log.d("paramsDriver", params + "");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return params;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;

    }

    public static String arrivedDriver(Context context, String driverid, String requestid, String riderId) {

        String parms = Config.arrivedurl + driverid + "&ride_request_id=" + requestid + "&customer_id=" + riderId;
        Log.e("arriveddriverurl", parms);
        return parms;
    }

    public static String startTrips(Context context, String driverid, String riderequestid, String time, String date,
                                    String pickupcordinate, String riderId) {

        String parms = Config.starttripsurl + driverid + "&ride_request_id=" + riderequestid + "&time=" + time +
                "&date=" + date + "&pickup_cordinates=" + pickupcordinate + "&customer_id=" + riderId;
        Log.e("starttripsurl ", parms);
        return parms;

    }

    public static String cancelTrips(Context context, String driverid, String requestid) {

        String parms = Config.canceltripsurl + driverid + "&ride_request_id=" + requestid;
        Log.e("cancel trips url", parms);
        return parms;
    }

    public static String stopRide(Context context, String driverid, String riderequestid, String time, String date,
                                  String dropcordinate, String riderId) {

        String parms = Config.stopurl + driverid + "&ride_request_id=" + riderequestid + "&time=" + time + "&date=" + date +
                "&drop_cordinates=" + dropcordinate + "&customer_id=" + riderId;
        Log.e("stoprideurl ", parms);
        return parms;
    }

    public static String collectCash(Context context, String driverid, String requestid, String customerid, String cash) {

        String parms = Config.collectcashurl + driverid + "&ride_request_id=" + requestid + "&customer_id=" + customerid + "&cash=" + cash;
        Log.e("collect_cash_url", parms);

        return parms;
    }

    public static String collectAmt(Context context, String companyId, String cartype, String dis, String tym, String customerid, String driverid, String rideid) {
        String parms = Config.collectamt + companyId + "&car_type=" + cartype + "&distance=" + dis + "&time=" + tym + "&customer_id=" + customerid + "&driver_id=" + driverid + "&ride_id=" + rideid;
        Log.e("collectAmt", parms);

        return parms;
    }

    public static String collectAmtDone(String driverId, String customerId, String ride_id, String amount) {
        String parms = Config.collectCashDone + driverId + "&customer_id=" + customerId + "&ride_id=" + ride_id + "&amount=" +
                amount;
        Log.e("collectAmt", parms);

        return parms;
    }

    // Send JsonArray(Lat long )

    public static String SendLatLng() {
        String parms = Config.collectamt;
        JSONObject jsonObject = null;
        jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < PathMapActivity.lattLngList.size(); i++) {
            try {
                JSONObject itemA = new JSONObject();
                itemA.put("name", "aaa");
                itemA.put("name", "aaa");
                jsonArray.put(itemA);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        try {
            jsonObject.put("array", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("Params", parms);

        return parms;
    }

    //End

    public static String tripsCompleted(Context context, String driverid, String date, String pageNo) {
        String parms = Config.triphistoryurl + driverid + "&date=" + date + "&pageNo=" + pageNo;
        Log.e("trip_history_url", parms);
        return parms;
    }

    public static String sendLocationurl(Context context, String lat, String lng, String driver_id) {


        String parms = Config.locationsendurl + "&latitude=" + lat + "&longitude=" + lng + "&driver_id=" + driver_id;

        Log.e("sendlocation_url", parms);
        return parms;
    }

    // user this service when customer select the payment method as a creditcard

    public static String sendCashAmount(Context context, String driverid, String requestid, String cash) {

        String parms = Config.sendAmountCard + driverid + "&ride_request_id+" + requestid + "&cash=" + cash;

        Log.e("creditcard payment", "url " + parms);
        return parms;
    }


}