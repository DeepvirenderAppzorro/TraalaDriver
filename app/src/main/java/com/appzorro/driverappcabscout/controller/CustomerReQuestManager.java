package com.appzorro.driverappcabscout.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appzorro.driverappcabscout.model.Beans.CustomerRequest;
import com.appzorro.driverappcabscout.model.Beans.CustomersRequestBean;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.HttpHandler;
import com.appzorro.driverappcabscout.view.Activity.NotificatonDialog;
import com.appzorro.driverappcabscout.view.Activity.PathmapActivity.model.CustomerListResponse;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vijay on 10/2/17.
 */

public class CustomerReQuestManager {

    private static final String TAG = CustomerReQuestManager.class.getSimpleName();
    public static ArrayList<CustomerRequest> requestlis = new ArrayList<>();
    public static CustomersRequestBean customersRequestBean;
    public static CustomerListResponse customerListResponse;
    public static String sourcelat = "", sourcelng = "", destlat = "", destlng = "";
    public static String driverFlag = "false";
    public String key = "";


    public void CustomerReQuestManager(Context context, String params, String key) {

        this.key = key;
        new ExecuteApi(context).execute(params);
    }

    public void CustomerRequestDetails(Context context, String params, String key) {
        this.key = key;
        new ExecuteDetailsApi(context).execute(params);
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
            Log.e(TAG, "customer_request_get--" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String name = "";
            String status = "";

            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject response = jsonObject.getJSONObject("response");
                    status = response.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        Gson gson = new Gson();
                        customersRequestBean = gson.fromJson(s, CustomersRequestBean.class);
                        customerListResponse = gson.fromJson(s, CustomerListResponse.class);
                        //   CSPreferences.putString(mContext, Constant.RIDER_ID, customer_id);
                        CSPreferences.putString(mContext, Constant.RIDER_NAME, name);
                        // CSPreferences.putString(mContext, Constant.CUSTOMER_TOKEN, aceessToken);

                        switch (key) {
                            case "no_event":
                                EventBus.getDefault().post(new Event(Constant.UPDATE_CUSTOMER_LIST, ""));
                                break;
                            case "no_msg":
                                Log.d("NoEvent", "True");
                                //Added by deep 3oct
                                // mContext.startActivity(new Intent(mContext, CashCollect.class));
                                break;

                            case "checkin_splash":
                                EventBus.getDefault().post(new Event(Constant.SPLASH_CHECKIN, ""));
                                break;
                            case "rating":
                                EventBus.getDefault().post(new Event(Constant.RATING_SCREEN, ""));
                                break;
                           /* case "firstTime":
                                EventBus.getDefault().post(new Event(Constant.RATING_SCREEN, ""));
                                break;*/
                            default:
                                EventBus.getDefault().post(new Event(Constant.CUSTOMERREQUEST, ""));
                                break;
                        }
                    } else {
                        customersRequestBean = null;
                        if (key.equals("checkin_splash"))
                            EventBus.getDefault().post(new Event(Constant.SPLASH_CHECKIN, ""));
                        else
                            EventBus.getDefault().post(new Event(Constant.SERVER_ERROR, ""));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    customersRequestBean = null;
                    if (key.equals("checkin_splash"))
                        EventBus.getDefault().post(new Event(Constant.SPLASH_CHECKIN, ""));
                    else
                        EventBus.getDefault().post(new Event(Constant.SERVER_ERROR, ""));
                    Log.e("exception request", e.toString());
                }
            } else {
                if (key.equals("checkin_splash"))
                    EventBus.getDefault().post(new Event(Constant.SPLASH_CHECKIN, ""));
                else
                    EventBus.getDefault().post(new Event(Constant.SERVER_ERROR, ""));
            }
        }
    }

    private class ExecuteDetailsApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteDetailsApi(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(strings[0]);
            Log.e(TAG, "customer_details_get--" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            boolean isDriverID = false;
            String ride_request_id = "";
            String distance = "";
            String poolorNot = "";
            String token = "";
            boolean showDialog = false;
            String driver_status = CSPreferences.readString(mContext, Constant.DRIVER_STATUS);

            try {
                JSONObject jsonObject1 = new JSONObject(s);
                JSONObject jsonObject = jsonObject1.getJSONObject("detail");
                String message = jsonObject1.getString("message");
                String vehicleType = jsonObject.getString("vehicle_type");
                token = jsonObject.getString("device_token");
                if (vehicleType.equals("4"))
                    poolorNot = "pool";
                if (message.equals("success")) {


                    if (jsonObject.has("ride_request_id") && isDriverID && !NotificatonDialog.isActive) {
                        ride_request_id = jsonObject.getString("ride_request_id");
                        if (jsonObject.has("distance")) {
                            distance = jsonObject.getString("distance");
                        }
                        CSPreferences.putString(mContext, Constant.DISTANCE, distance);
                        CSPreferences.putString(mContext, Constant.REQUEST_ID, ride_request_id);
                        showDialog = true;

                    } else if (jsonObject.has("ride_request_id") && isDriverID && NotificatonDialog.isActive) {
                        ride_request_id = jsonObject.getString("ride_request_id");
                        CSPreferences.putString(mContext, Constant.CUSTOMER_TOKEN, token);
                    }

                    if (!driver_status.equals("2") && key.equals("customer_request") && showDialog || poolorNot.equals("pool")) {
                        String customer_id = jsonObject.getString("customer_id");
                        CSPreferences.putString(mContext, Constant.CUSTOMER_TOKEN, token);
                        CSPreferences.putString(mContext, Constant.RIDER_ID, customer_id);

                        if (!driver_status.equals("2") && !poolorNot.equals("pool")) {
                            CSPreferences.putString(mContext, Constant.IS_POOLTYPE, "false");
                            //  sendNotification(message);
                           /* ModelManager.getInstance().getCustomerReQuestManager().CustomerReQuestManager(mContext, Operations.
                                    getCustomerRequest(mContext, CSPreferences.readString(mContext, "customer_id")), "no_msg");
*/
                        } else if (poolorNot.equals("pool")) {
                            CSPreferences.putString(mContext, Constant.IS_POOLTYPE, "true");
                            if (jsonObject.getString("driver_id").equals(CSPreferences.readString(mContext, "customer_id"))) {
                               /* ModelManager.getInstance().getCustomerReQuestManager().CustomerReQuestManager(mContext, Operations.
                                        getCustomerRequest(mContext, CSPreferences.readString(mContext, "customer_id")), "no_msg");*/
                            }
                        }
                    }
                }


                EventBus.getDefault().post(new Event(Constant.RIDE_DETAILS, ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
