package com.appzorro.driverappcabscout.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appzorro.driverappcabscout.model.Beans.GetCityBean;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.HttpHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vijay on 10/7/18.
 */

public class GetCityManager {
    private static final String TAG = GetCityManager.class.getSimpleName();
    public static ArrayList<GetCityBean> citylist;

    public void GetCityManager(Context context, String params) {

        new GetCityManager.ExecuteApi(context).execute(params);
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
            Log.e(TAG, "cityget" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            citylist = new ArrayList<>();
            String payment_method = "";
            String customer_id = "";


            if (s != null) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject response = jsonObject.getJSONObject("response");
                    JSONArray jsonArray = response.getJSONArray("location_list");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String loc_id = jsonObject1.getString("location_id");
                        String loc_name = jsonObject1.getString("location_name");

                        CSPreferences.putString(mContext, "LocationId", loc_id);
                        GetCityBean customerRequest = new GetCityBean(loc_id,loc_name);
                        citylist.add(customerRequest);
                    }
                //    EventBus.getDefault().post(new Event(Constant.GETCITY, ""));

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
