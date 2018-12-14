package com.appzorro.driverappcabscout.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.HttpHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vijay on 9/2/17.
 */

public class UserDetailManager {

    private static final String TAG = UserDetailManager.class.getSimpleName();

    public void UserDetailManager(Context context, String params) {

        new ExecuteApi(context).execute(params);
    }

    private class ExecuteApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApi(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(strings[0]);

            Log.e(TAG, "userupdatedetail--" + response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                String address = "";
                String zip = "";
                String locatioName = "";
                String licenseValidaity = "";
                String licenseNo = "";

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject response = jsonObject.getJSONObject("response");
                    String email = response.getString("email");
                    String name = response.getString("name");
                    String mobile = response.getString("mobile");
                    String adahrno = response.getString("adhar_no");
                    String aFImage = response.getString("adhaar_front");
                    String aBImage = response.getString("adhaar_back");

                    CSPreferences.putString(mContext, "AdharFImage", Config.card_image_url + "" + aFImage);
                    CSPreferences.putString(mContext, "AdharBImage", Config.card_image_url + "" + aBImage);
                    String locationId = response.getString("location_id");
                    String profile_pic = response.getString("profile_pic");
                    CSPreferences.putString(mContext, "user_email", email);
                    CSPreferences.putString(mContext, "profile_pic", Config.baserurl_image + "" + profile_pic);
                    address = response.getString("address");
                    zip = response.getString("zip_code");
                    licenseNo = response.getString("driver_license");
                    locatioName = response.getString("location_name");
                    licenseValidaity = response.getString("license_validity");
                    if (email.equalsIgnoreCase("null") || email.equalsIgnoreCase("")) {
                        email = "N/A";
                    }
                    if (adahrno.equalsIgnoreCase("null") || adahrno.equalsIgnoreCase("")) {
                        adahrno = "N/A";
                    }
                    if (name.equalsIgnoreCase("null") || name.equalsIgnoreCase("")) {
                        name = "N/A";
                    }
                    if (mobile.equalsIgnoreCase("null") || mobile.equalsIgnoreCase("")) {
                        mobile = "N/A";
                    }
                    if (address.equalsIgnoreCase("null") || address.equalsIgnoreCase("")) {
                        address = "N/A";
                    }
                    if (zip.equalsIgnoreCase("null") || zip.equalsIgnoreCase("")) {
                        zip = "N/A";
                    }
                    if (locatioName.equalsIgnoreCase("null") || locatioName.equalsIgnoreCase("")) {
                        locatioName = "N/A";
                    }
                    if (licenseValidaity.equalsIgnoreCase("null") || licenseValidaity.equalsIgnoreCase("")) {
                        licenseValidaity = "N/A";
                    }
                    CSPreferences.putString(mContext, "AdharNo", adahrno);
                    CSPreferences.putString(mContext, "user_email", email);
                    CSPreferences.putString(mContext, "location_Id", locationId);
                    CSPreferences.putString(mContext, "user_name", name);
                    CSPreferences.putString(mContext, "user_mobile", mobile);
                    CSPreferences.putString(mContext, "driverAddress", address);
                    CSPreferences.putString(mContext, "driverZip", zip);
                    CSPreferences.putString(mContext, "driverLicense", licenseNo);
                    CSPreferences.putString(mContext, "driverLocation", locatioName);
                    CSPreferences.putString(mContext, "LicenseValidity", licenseValidaity);
                    EventBus.getDefault().post(new Event(Constant.USERDETAILSTAUS, ""));

                } catch (JSONException e) {
                    EventBus.getDefault().post(new Event(Constant.SERVER_ERROR, ""));

                    e.printStackTrace();
                }

            } else {
                EventBus.getDefault().post(new Event(Constant.SERVER_ERROR, ""));
            }


        }
    }
}
