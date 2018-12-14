package com.appzorro.driverappcabscout.controller;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.HttpHandler;
import com.appzorro.driverappcabscout.view.Activity.Logger;
import com.appzorro.driverappcabscout.view.Activity.PathmapActivity.view.PathMapActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

;

/**
 * Created by vijay on 10/2/17.
 */

public class AcceptCustomerRequest {
    Context context;
    private static final String TAG = AcceptCustomerRequest.class.getSimpleName();

    public void AcceptCustomerRequest(Context context, String params) {
        this.context = context;
        new ExecuteApi(context).execute(params);
    }

    public void RejectCustomer(Context context, String params) {
        this.context = context;
        new ExecuteRejectApi(context).execute(params);
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

            Log.e(TAG, "accept_by_driver--" + response);
            Logger.addRecordToLog(response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject response = jsonObject.getJSONObject("response");
                    int id = Integer.parseInt(response.getString("id"));
                    if(id>0)
                    {
                        String message = response.getString("message");
                        String driver_status = response.getString("driver_status");

                        CSPreferences.putString(context, Constant.DRIVER_STATUS, driver_status);
                        EventBus.getDefault().post(new Event(Constant.ACCEPTBYDRIVER, message));
                        Intent intent = new Intent(context, PathMapActivity.class);
                        context.startActivity(intent);

                    }
                    else
                    {
                        EventBus.getDefault().post(new Event(Constant.SERVER_ERROR, ""));
                    }


                } catch (JSONException e) {
                    EventBus.getDefault().post(new Event(Constant.SERVER_ERROR, ""));

                    e.printStackTrace();
                }
            } else {
                EventBus.getDefault().post(new Event(Constant.SERVER_ERROR, ""));

            }
        }
    }

    private class ExecuteRejectApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteRejectApi(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(strings[0]);
            Log.e(TAG, "reject_by_driver--" + response);
            EventBus.getDefault().post(new Event(Constant.DRIVERREJECTED, "Rejected"));


            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {


            }
        }
    }

}
