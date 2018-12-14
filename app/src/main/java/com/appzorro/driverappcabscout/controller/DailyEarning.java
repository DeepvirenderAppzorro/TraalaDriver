package com.appzorro.driverappcabscout.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.HttpHandler;
import com.appzorro.driverappcabscout.view.Activity.Logger;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vijay on 24/10/18.
 */

public class DailyEarning {
    Context context;
    private static final String TAG = DailyEarning.class.getSimpleName();

    public void getDailyEarn(Context context, String params) {
        this.context = context;
        new DailyEarning.ExecuteApi(context).execute(params);
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

            Log.e(TAG, "daily_earn--" + response);
            Logger.addRecordToLog(response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String totAmt="";
            String date="";
            String time="";
            String totRides="";
            if (s != null) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject response = jsonObject.getJSONObject("response");
                    totAmt=  response.getString("total_amount");
                    if(totAmt.equalsIgnoreCase("null")||totAmt.equalsIgnoreCase(""))
                    {
                        totAmt="N/A";
                    }
                    totRides=  response.getString("total_rides");
                    time=  response.getString("total_time");
                    CSPreferences.putString(mContext, Constant.DLTOTALAMT, totAmt);
                    CSPreferences.putString(mContext, Constant.DLRIDES, totRides);
                    CSPreferences.putString(mContext, Constant.DLDATE, date);
                    CSPreferences.putString(mContext,  Constant.DLTIME, time);

                    EventBus.getDefault().post(new Event(Constant.DAILYEARN, ""));


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
