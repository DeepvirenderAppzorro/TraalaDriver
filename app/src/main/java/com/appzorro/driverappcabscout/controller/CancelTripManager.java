package com.appzorro.driverappcabscout.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.HttpHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vijay on 16/2/17.
 */

public class CancelTripManager {

    private static final String TAG = CancelTripManager.class.getSimpleName();

    public void CancelTripManager(Context context, String params) {

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

            Log.e(TAG, "cancel trpis--" +response);

            return response;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject response = jsonObject.getJSONObject("response");
                    int id = Integer.parseInt(response.getString("id"));
                    String message = response.optString("message");
                    EventBus.getDefault().post(new Event(Constant.CANCELTRIP,""));


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
