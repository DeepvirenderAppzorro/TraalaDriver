package com.appzorro.driverappcabscout.model;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by rishav on 17/1/17.
 */

public class Operations {
    private static final String TAG = Operations.class.getSimpleName();

    public static String getCabCompaniesTask(Context context, String cabalias) {
        String params = Config.companyaliasurl + cabalias;
        //  Log.e(TAG, "cab_companies list--"+params);
        return params;
    }

    /*public static String registrationTask(Context context, String email, String cab_id, String drivername, String password, String devicetoken,
                                          String drivNo, String drivLicence, String drivCity,String drivZip,String pofilepic) {

        String params = Config.signUp_url + "&email=" +email + "&company_id=" + cab_id +
                "&name=" +drivername + "&password=" +password + "&device_token=" + devicetoken + "&device_type=A" + "&mobile=" + drivNo + "&driver_license=" + drivLicence + "&city=" + drivCity +
                "&profile_pic="+pofilepic+ "&zip=" + drivZip;

        Log.e("Signup url", "registration params---" + params);

        return params;
    }
*/
    public static String simpleRegister(Context context, String companyid, String emailid, String password, String name,
                                        String devicetype, String devicetoken, String mobileno, String profilepic, String city,
                                        String zip, String driverlicence) {


        String parms = Config.simplesignupurl + "&company_id=" + companyid + "&email=" + emailid + "&password=" + password + "&name=" + name
                + "&device_token=" + devicetoken + "&device_type=" + devicetype + "&mobile=" + mobileno + "&profile_pic=" + profilepic
                + "&city=" + city + "&zip=" + zip + "&driver_license=" + driverlicence;
        Log.e("simple signup url", parms);
        return parms;

    }

    // Login operation below
    public static String loginTask(Context context, String email, String password, String deviceToken) {
        String params = Config.login_url + "&email=" + email + "&password=" + password + "&device_token=" + deviceToken;

        Log.e(TAG, "login parameters--" + params);
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


    public static String simpleuserRegister(Context context, String companyid, String emailid, String password, String name,
                                            String devicetype, String devicetoken, String mobileno, String profilepic, String city,
                                            String zip, String driverlicence) {
        /*String params = Config.fb_login_url+company_id+"&email="+email+"&password=WJBJvfHTRNT"+"&name="+name+"&device_token="+token+"&device_type=A"+"&mobile="+mobile+"&profileImage="+imageUrl+"&facebook_id="+fb_id;*/
        try {
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("company_id", companyid);
            postDataParams.put("email", emailid);
            postDataParams.put("password", password);
            postDataParams.put("name", name);
            postDataParams.put("device_type", devicetype);
            postDataParams.put("device_token", devicetoken);
            postDataParams.put("mobile", mobileno);
            postDataParams.put("profile_pic", profilepic);
            postDataParams.put("city", city);
            postDataParams.put("facebook_id", "");
            postDataParams.put("zip", zip);
            postDataParams.put("driver_license", driverlicence);
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


    public static String getUserDetail(Context context, String driverid) {
        String parms = Config.userdetail_url + driverid;
        Log.e("usedetail url url", parms);
        return parms;
    }

    public static String getCustomerRequest(Context context, String driverid) {

        String parms = Config.customerrequesturl + driverid;
        Log.e("customerrequest url", parms);
        return parms;
    }

    public static String acceptByDriver(Context context, String driverid, String requestid, String driverlat, String driverlng) {

        String parms = Config.acceptrequestbydriver + driverid + "&ride_request_id=" + requestid + "&latitude=" + driverlat + "&longitude=" + driverlng;
        Log.e("driver accept url", parms);
        return parms;
    }

    public static String laterBooking(Context context, String driverid) {

        String parms = Config.laterbookingurl + driverid;
        Log.e("later booking url", parms);
        return parms;
    }

    public static String changePassword(Context context, String driverid, String oldpassword, String newpassword) {

        String parms = Config.changepasswordurl + driverid + "&oldpassword=" + oldpassword + "&newpassword=" + newpassword;

        Log.e("change password url", parms);
        return parms;
    }

    /* public static String updateProfile(Context context,String driverid,String profilepic,String name ,String mobile){

         String parms = Config.updateprofileurl+driverid+"&profile_pic="+profilepic+"&name="+name+"&mobile="+mobile;
         Log.e("update profile url",parms);
         return parms.replaceAll("\n","");
     }*/
    public static String updateProfile(Context context, String driverid, String profile, String name,
                                       String mobile) {
        /*String params = Config.fb_login_url+company_id+"&email="+email+"&password=WJBJvfHTRNT"+"&name="+name+"&device_token="+token+"&device_type=A"+"&mobile="+mobile+"&profileImage="+imageUrl+"&facebook_id="+fb_id;*/

        try {
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("driver_id", driverid);
            postDataParams.put("profile_pic", profile);
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

        String parms = Config.changecompanyurl + driverid + "&cab_alias=" + cabalias;
        return parms;
    }

    public static String reviewofDriver(Context context, String driverid) {

        String parms = Config.reviewcheckurl + driverid;
        Log.e("review check url", parms);
        return parms;

    }
    public static String sendRatingtoCustomer(Context context, String driverid, String customerid, String rating, String feedback) {
        String parms = Config.ratingtocustomerurl + driverid + "&customer_id=" + customerid + "&rating=" + rating + "&feedback=" + feedback;
        Log.e("rating url", parms);
        return parms;
    }

    public static String sendDriverstatus(Context context, String driverid, String status,String latitude,String longititude) {

        String parms = Config.driveravilablityurl + driverid + "&status=" + status+"&latitude="+latitude+"&longitude="+longititude;
        Log.e("onl offline url",parms);
        return parms;

    }

    public static String arrivedDriver(Context context, String driverid, String requestid) {

        String parms = Config.arrivedurl + driverid + "&ride_request_id=" + requestid;
        Log.e("arrived driver url", parms);
        return parms;
    }

    public static String startTrips(Context context, String driverid, String riderequestid, String time, String date, String pickupcordinate) {

        String parms = Config.starttripsurl + driverid + "&ride_request_id=" + riderequestid + "&time=" + time + "&date=" + date + "&pickup_cordinates=" + pickupcordinate;
        Log.e("start trips url ", parms);
        return parms;

    }

    public static String cancelTrips(Context context, String driverid, String requestid) {

        String parms = Config.canceltripsurl + driverid + "&ride_request_id=" + requestid ;
        Log.e("cancel trips url", parms);
        return parms;
    }

    public static String stopRide(Context context, String driverid, String riderequestid, String time, String date, String dropcordinate) {

        String parms = Config.stopurl + driverid + "&ride_request_id=" + riderequestid + "&time=" + time + "&date=" + date+
        "&drop_cordinates=" + dropcordinate;
        Log.e("stop ride url ", parms);
        return parms;
    }
    public  static String collectCash(Context context,String driverid,String requestid,String cash){

        String parms = Config.collectcashurl+driverid+"&ride_request_id="+requestid+"&cash="+cash;
        Log.e("collect cash url",parms);
        return parms;
    }
    public static String tripsCompleted(Context context,String driverid,String date){

        String parms = Config.triphistoryurl+driverid+"&date="+date;
        Log.e("trip history url", parms);
        return parms;
    }

    public static String sendLocationurl(Context context,String lat, String lng,String customer_id,String driver_id){


        String parms = Config.locationsendurl+"&latitude="+lat+"&longitude="+lng+"&customer_id="+customer_id+"&driver_id="+driver_id;

        Log.e("sendlocation url",parms);
        return parms;
    }

}