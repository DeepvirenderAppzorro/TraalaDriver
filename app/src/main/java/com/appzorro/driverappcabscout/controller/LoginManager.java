package com.appzorro.driverappcabscout.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.HttpHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import static com.appzorro.driverappcabscout.model.Constant.COMPANYID;

/**
 * Created by Pankaj on 24/1/17.
 */

public class LoginManager {

    private static final String TAG = LoginManager.class.getSimpleName();

    public void doLogin(Context context, String params) {

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

            Log.e(TAG, "logintimeresponse--" +response);

            return response;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null) {
                String companyID="";
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject response = jsonObject.getJSONObject("response");
                    int id = Integer.parseInt(response.getString("id"));
                    if (response.has("company_id")) {
                        companyID = response.getString("company_id");
                    }
                     Log.d("Driverid",id+"");
                    String message = response.getString("message");
                    if (id>0) {


                        CSPreferences.putString(mContext, "customer_id",String.valueOf(id));
                        CSPreferences.putString(mContext, COMPANYID,companyID);
                        EventBus.getDefault().post(new Event(Constant.LOGIN_STATUS, ""));
                    }
                    else{

                        EventBus.getDefault().post(new Event(Constant.LOGINERROR, message));
                    }

                } catch (JSONException e) {
                    EventBus.getDefault().post(new Event(Constant.SERVER_ERROR,""));

                    e.printStackTrace();
                }
            }else {

                EventBus.getDefault().post(new Event(Constant.SERVER_ERROR,""));
            }
        }
    }

}
