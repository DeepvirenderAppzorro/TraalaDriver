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

import java.util.ArrayList;

/**
 * Created by vijay on 20/2/17.
 */

public class Tripsmanager {

    private static final String TAG = Tripsmanager.class.getSimpleName();
  public static  ArrayList<CompletedRideBean>trips;

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
            Log.e(TAG, "trps history url--" +response);
            return response;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null) {
                trips = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    if (jsonArray.length()>0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String requestid = jsonObject1.getString("ride_request_id");
                            String pickcordinate = jsonObject1.getString("pickup_cordinates");
                            String dropcordinate = jsonObject1.getString("drop_cordinates");
                            String startdate = jsonObject1.getString("start_date");
                            String enddate = jsonObject1.getString("end_datetime");
                            String totalamount = jsonObject1.getString("Total Amount");
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("Customer Detail");
                            String customername = jsonObject2.getString("name");
                            String profile = jsonObject2.getString("profile_pic");
                            String mobile = jsonObject2.getString("mobile");
                            String picsplit[] = pickcordinate.split(",");
                            String picklat = picsplit[picsplit.length - 2];
                            String picklng = picsplit[picsplit.length - 1];
                            String dropsplit[] = dropcordinate.split(",");
                            String droplat = dropsplit[dropsplit.length - 2];
                            String droplng = dropsplit[dropsplit.length - 1];
                            CompletedRideBean completedRideBean = new CompletedRideBean(requestid,picklat,picklng,droplat,droplng,
                                    startdate,enddate,totalamount,customername, Config.baserurl_image+profile,mobile);
                            trips.add(completedRideBean);

                            EventBus.getDefault().post(new Event(Constant.TRIPSHOSTORY,""));
                        }
                    }
                    else {

                        EventBus.getDefault().post(new Event(Constant.BLANKLIST,""));

                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                EventBus.getDefault().post(new Event(Constant.SERVER_ERROR,""));

            }
        }
    }
}
