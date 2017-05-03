package com.appzorro.driverappcabscout.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appzorro.driverappcabscout.model.Beans.CustomerRequest;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.HttpHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vijay on 10/2/17.
 */

public class CustomerReQuestManager {

    private static final String TAG = CustomerReQuestManager.class.getSimpleName();
    public static ArrayList<CustomerRequest> requestlis;

    public void CustomerReQuestManager(Context context, String params) {

        new CustomerReQuestManager.ExecuteApi(context).execute(params);
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
            Log.e(TAG, "customer request get--" +response);
            return response;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            requestlis = new ArrayList<>();
            if (s!=null) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String requestid = jsonObject1.getString("ride_request_id");
                        int id = Integer.parseInt(requestid);
                        if (id>0) {
                            String name = jsonObject1.getString("name");
                            String customer_id = jsonObject1.getString("customer_id");
                            String profilpic = jsonObject1.getString("profile_pic");
                            String pickupadd = jsonObject1.getString("pickup_cordinates");

                            String[] picksplit = pickupadd.split(",");
                            String sourcelat = picksplit[picksplit.length - 2];
                            String sourcelng = picksplit[picksplit.length - 1];

                            String dropadd = jsonObject1.getString("drop_cordinates");
                            String[] dropsplit = dropadd.split(",");

                            String destlat = dropsplit[dropsplit.length - 2];
                            String destlng = dropsplit[dropsplit.length - 1];

                            String payment_method = jsonObject1.getString("payment_type");
                            CSPreferences.putString(mContext,"payment_method",payment_method);

                            String mobilenumber = jsonObject1.getString("mobile");

                            CustomerRequest customerRequest = new CustomerRequest(name, requestid, customer_id,
                                    Config.baserurl_image+profilpic, sourcelat, sourcelng,
                                    destlat, destlng, mobilenumber,payment_method);

                            requestlis.add(customerRequest);

                            EventBus.getDefault().post(new Event(Constant.CUSTOMERREQUEST, ""));
                        }
                        else{

                            String message = jsonObject1.getString("message");

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("exception request",e.toString());
                }
            }
            else {

                EventBus.getDefault().post(new Event(Constant.SERVER_ERROR, ""));
            }
        }
    }

}
