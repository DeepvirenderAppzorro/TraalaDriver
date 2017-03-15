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
 * Created by Pankaj on 23/1/17.
 */
public class RegistrationManager {

    private final String TAG = RegistrationManager.class.getSimpleName();

    public void registerUser(Context context, String url,String params) {
        new ExecuteApi(context).execute(url,params);
    }
    private class ExecuteApi extends AsyncTask<String, String, String> {
        Context mContext;
        ExecuteApi(Context context) {
            mContext = context;
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.getResponse(strings[0],strings[1]);
            Log.e(TAG, "simple register--"+response);
            return response;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject response = jsonObject.getJSONObject("response");
                    int id = response.getInt("id");
                    String message = response.getString("message");

                    EventBus.getDefault().post(new Event(Constant.SIGNUPRESPONSE, String.valueOf(id) + "," + message));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                EventBus.getDefault().post(new Event(Constant.SERVER_ERROR,""));

            }
        }
    }
}