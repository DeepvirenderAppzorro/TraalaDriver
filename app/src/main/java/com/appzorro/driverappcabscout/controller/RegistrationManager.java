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
            Log.e(TAG, "simpleregister--"+response);
            return response;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String companyID="";
            String message="";
            if(s!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject response = jsonObject.getJSONObject("response");
                    int id = response.getInt("id");
                    if (response.has("company_id")) {
                         companyID = response.getString("company_id");
                    }
                     message = response.getString("message");

                    if (id>0){
                        CSPreferences.putString(mContext, "customer_id",String.valueOf(id));
                        CSPreferences.putString(mContext, COMPANYID,companyID);
                        EventBus.getDefault().post(new Event(Constant.SIGNUPRESPONSE,message));

                    }
                    else {


                        EventBus.getDefault().post(new Event(Constant.ERROR,message));

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post(new Event(Constant.ERROR,message));
                }
            }
            else {


                EventBus.getDefault().post(new Event(Constant.SERVER_ERROR,message));

            }
        }
    }
}