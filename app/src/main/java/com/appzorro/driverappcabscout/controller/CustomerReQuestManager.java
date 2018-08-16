package com.appzorro.driverappcabscout.controller;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.appzorro.driverappcabscout.model.Beans.CustomerRequest;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.HttpHandler;
import com.appzorro.driverappcabscout.view.NotificatonDialog;

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

        new ExecuteApi(context).execute(params);
    }


    public void hitRequestApi(Context context, String params) {

        new ExecuteRideRequestApi(context).execute(params);
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
            requestlis = new ArrayList<>();
            String payment_method = "";
            String customer_id = "";
            String name = "";
            String status="";
            String profilpic = "";
            String pickupadd = "";
            String sourcelat = "";
            String sourcelng = "";
            String destlat = "";
            String destlng = "";
            String requestid = "";
            String mobilenumber = "";
            String price = "";
            String picloc="";
            String droploc="";
            String vehicleType="";

            if (s != null) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject response = jsonObject.getJSONObject("response");
                  status=  response.getString("status");
                  if(status.equalsIgnoreCase("1"))
                  {
                      JSONArray jsonArray = response.getJSONArray("data");
                      for (int i = 0; i < jsonArray.length(); i++) {

                          JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                          requestid = jsonObject1.getString("ride_request_id");
                          CSPreferences.putString(mContext, "request_id", requestid);
                          Log.e("request_id", requestid);
                          int id = Integer.parseInt(requestid);
                          if (id > 0) {

                              name = jsonObject1.getString("name");

                              customer_id = jsonObject1.getString("customer_id");
                              CSPreferences.putString(mContext, "CustomerId", customer_id);
                              Log.e("exception request", customer_id);
                              vehicleType = jsonObject1.getString("vehicle_type");
                              Log.d("VehicleType",vehicleType);

                              picloc = jsonObject1.getString("pickup_location");
                              droploc = jsonObject1.getString("drop_location");

                              profilpic = jsonObject1.getString("profile_pic");
                              pickupadd = jsonObject1.getString("pickup_cordinates");
                              price = jsonObject1.getString("price");

                              String[] picksplit = pickupadd.split(",");
                              sourcelat = picksplit[picksplit.length - 2];
                              sourcelng = picksplit[picksplit.length - 1];

                              String dropadd = jsonObject1.getString("drop_cordinates");
                              String[] dropsplit = dropadd.split(",");

                              destlat = dropsplit[dropsplit.length - 2];
                              destlng = dropsplit[dropsplit.length - 1];

                              if (jsonObject1.has("payment_type")) {
                                  payment_method = jsonObject1.getString("payment_type");
                                  CSPreferences.putString(mContext, Constant.PAYEMENT_METHOD, payment_method);
                              }
                              mobilenumber = jsonObject1.getString("mobile");

                           /* CustomerRequest customerRequest = new CustomerRequest(name, requestid, customer_id,
                                    Config.baserurl_image + profilpic, sourcelat, sourcelng,
                                    destlat, destlng, mobilenumber, payment_method);

                            requestlis.add(customerRequest);*/


                          } else {

                              String message = jsonObject1.getString("message");

                          }

                      }

                      CustomerRequest customerRequest = new CustomerRequest(name, requestid, customer_id,
                              Config.baserurl_image + profilpic, sourcelat, sourcelng,
                              destlat, destlng, mobilenumber, payment_method,price,vehicleType,picloc,droploc);

                      requestlis.add(customerRequest);
                      CSPreferences.putString(mContext, Constant.CUSTOMER_ID, customer_id);
                      CSPreferences.putString(mContext, Constant.RIDER_NAME, name);
                      CSPreferences.putString(mContext, Constant.RIDE_PRICE, price);
                      mContext.startActivity(new Intent(mContext, NotificatonDialog.class).addFlags((Intent.FLAG_ACTIVITY_NEW_TASK)));
                      EventBus.getDefault().post(new Event(Constant.CUSTOMERREQUEST, ""));
                  }
                  else
                  {
                      EventBus.getDefault().post(new Event(Constant.SERVER_ERROR, ""));

                  }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("exception request", e.toString());
                }
            } else {

                EventBus.getDefault().post(new Event(Constant.SERVER_ERROR, ""));
            }
        }
    }

    private class ExecuteRideRequestApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteRideRequestApi(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(strings[0]);
            Log.e(TAG, "customer__ride_request_get--" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            requestlis = new ArrayList<>();
            String payment_method = "";
            String customer_id = "";
            String name = "";
            String profilpic = "";
            String pickupadd = "";
            String sourcelat = "";
            String sourcelng = "";
            String destlat = "";
            String destlng = "";
            String requestid = "";
            String mobilenumber = "";
            String price = "";
            String picloc="";
            String droploc="";
            String vehicleType="";

            if (s != null) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String message1 = jsonObject.getString("message");


                    if (message1.equals("success")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("detail");
                        requestid = jsonObject1.getString("ride_request_id");
                        CSPreferences.putString(mContext, "request_id", requestid);
                        name = jsonObject1.getString("name");
                        price = jsonObject1.getString("price");
                        customer_id = jsonObject1.getString("customer_id");
                        Log.e("exception request", customer_id);
                        vehicleType = jsonObject1.getString("vehicle_type");
                        profilpic = jsonObject1.getString("profile_pic");
                        pickupadd = jsonObject1.getString("pickup_cordinates");

                        String[] picksplit = pickupadd.split(",");
                        sourcelat = picksplit[picksplit.length - 2];
                        sourcelng = picksplit[picksplit.length - 1];

                        picloc = jsonObject1.getString("pickup_location");
                        droploc = jsonObject1.getString("drop_location");


                        String dropadd = jsonObject1.getString("drop_cordinates");
                        String[] dropsplit = dropadd.split(",");

                        destlat = dropsplit[dropsplit.length - 2];
                        destlng = dropsplit[dropsplit.length - 1];

                        if (jsonObject1.has("payment_type")) {
                            payment_method = jsonObject1.getString("payment_type");
                            CSPreferences.putString(mContext, Constant.PAYEMENT_METHOD, payment_method);
                        }
                        mobilenumber = jsonObject1.getString("mobile");

                           /* CustomerRequest customerRequest = new CustomerRequest(name, requestid, customer_id,
                                    Config.baserurl_image + profilpic, sourcelat, sourcelng,
                                    destlat, destlng, mobilenumber, payment_method);

                            requestlis.add(customerRequest);*/


                    } else {

                        EventBus.getDefault().post(new Event(Constant.SERVER_ERROR, ""));

                    }

                    CustomerRequest customerRequest = new CustomerRequest(name, requestid, customer_id,
                            Config.baserurl_image + profilpic, sourcelat, sourcelng,
                            destlat, destlng, mobilenumber, payment_method,price,vehicleType,picloc,droploc);

                    requestlis.add(customerRequest);

                    CSPreferences.putString(mContext, Constant.CUSTOMER_ID, customer_id);
                    CSPreferences.putString(mContext, Constant.RIDER_NAME, name);
                    EventBus.getDefault().post(new Event(Constant.LAST_RIDE_STATE_SUCCCESS, ""));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("exception request", e.toString());
                }
            } else {

                EventBus.getDefault().post(new Event(Constant.SERVER_ERROR, ""));
            }
        }
    }

}
