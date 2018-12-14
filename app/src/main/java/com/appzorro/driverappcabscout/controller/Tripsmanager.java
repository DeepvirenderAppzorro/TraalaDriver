package com.appzorro.driverappcabscout.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appzorro.driverappcabscout.model.Beans.CompletedRideBean;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.HttpHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by vijay on 20/2/17.
 */

public class Tripsmanager {

    private static final String TAG = Tripsmanager.class.getSimpleName();
    public static ArrayList<CompletedRideBean> trips;

    public void Tripsmanager(Context context, String params) {

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
            Log.e(TAG, "trpshistoryurl--" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                trips = new ArrayList<>();
                String paymentTyp = "";
                String customername="";
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject res = jsonObject.getJSONObject("response");
                    String msg = res.getString("message");
                    String status = res.getString("status");

                    JSONArray jsonArray = res.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String requestid = jsonObject1.getString("ride_request_id");
                            String pickcordinate = jsonObject1.getString("pickup_cordinates");
                            String dropcordinate = jsonObject1.getString("drop_cordinates");
                            String startdate = jsonObject1.getString("start_date");
                            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                            Date date = null;
                            try {
                                date = inputFormat.parse(startdate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            System.out.println(outputFormat.format(date));
                            String startTym = jsonObject1.getString("start_time");
                            Log.d("time", startTym + " start tym in tripmanger");
                            String dropLoc = jsonObject1.getString("drop_location");
                            String pathImg = jsonObject1.getString("path_image");

                            String paymentType = jsonObject1.getString("payment_type");
                            if (paymentType.equalsIgnoreCase("0")) {
                                paymentTyp = "Cash";

                            } else if (paymentType.equalsIgnoreCase("1")) {
                                paymentTyp = "Credit";
                            } else if (paymentType.equalsIgnoreCase("2")) {
                                paymentTyp = "corporate acc";
                            }

                            String pickupLoc = jsonObject1.getString("pickup_location");
                            String enddate = jsonObject1.getString("end_date");
                            String endTym = jsonObject1.getString("end_time");

                            String feedBack = jsonObject1.getString("rating");
                            Log.d("f", feedBack);
                            String vehicleName = jsonObject1.getString("vehicle_category_name");

                            String totalamount = jsonObject1.getString("total_amount");
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("Customer Detail");
                           customername = jsonObject2.getString("name");
                            if(customername.equalsIgnoreCase("")||customername.equalsIgnoreCase("null"))
                            {
                                customername="N/A";
                            }
                            String id = jsonObject2.getString("id");
                            String profile = jsonObject2.getString("profile_pic");
                            String mobile = jsonObject2.getString("mobile");
                            String picsplit[] = pickcordinate.split(",");
                            String picklat = picsplit[picsplit.length - 2];
                            String picklng = picsplit[picsplit.length - 1];
                            String dropsplit[] = dropcordinate.split(",");
                            String droplat = dropsplit[dropsplit.length - 2];
                            String droplng = dropsplit[dropsplit.length - 1];
                            CompletedRideBean completedRideBean = new CompletedRideBean(requestid, picklat, picklng, droplat, droplng,
                                    outputFormat.format(date), startTym, enddate, endTym, totalamount, customername, Config.baserurl_image + profile, mobile, dropLoc, pickupLoc, feedBack, vehicleName, paymentTyp, Config.pathurl_image + pathImg);
                            trips.add(completedRideBean);


                        }
                        EventBus.getDefault().post(new Event(Constant.TRIPSHOSTORY, s));
                    } else {

                        EventBus.getDefault().post(new Event(Constant.BLANKLIST, ""));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                EventBus.getDefault().post(new Event(Constant.SERVER_ERROR, ""));

            }
        }
    }
}
