package com.appzorro.driverappcabscout.controller;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.HttpHandler;
import com.appzorro.driverappcabscout.view.PathMapActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vijay on 10/2/17.
 */

public class AcceptCustomerRequest {
    Context context;
    private static final String TAG = AcceptCustomerRequest.class.getSimpleName();

    public void AcceptCustomerRequest(Context context, String params) {
        this.context = context;
        new AcceptCustomerRequest.ExecuteApi(context).execute(params);
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
                    String message = response.getString("message");
                    String driver_status = response.getString("driver_status");

                    CSPreferences.putString(context, Constant.DRIVER_STATUS, driver_status);
                    EventBus.getDefault().post(new Event(Constant.ACCEPTBYDRIVER, message));
                    Intent intent = new Intent(context, PathMapActivity.class);
                    context.startActivity(intent);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                EventBus.getDefault().post(new Event(Constant.SERVER_ERROR, ""));

            }
        }
    }

}
