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

/** Created by Pankaj on 22/1/17. **/
public class CabCompaniesManager {
    private final String TAG = CabCompaniesManager.class.getSimpleName();

    //public static final HashMap<Integer, String> cabCompaniesList = new HashMap<>();

    public void getCabCompanies(Context context, String params) {
        new ExecuteApi(context).execute(params);
    }
    private class ExecuteApi extends AsyncTask<String, String, String> {
        Context mContext;
        ExecuteApi(Context context) {
            mContext = context;
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(strings[0]);
            Log.e(TAG, "companies list--"+response);
            return response;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                    int id= Integer.parseInt(jsonObject1.getString("id"));
                    if (id>0){

                        EventBus.getDefault().post(new Event(Constant.CAB_COMPANIES_SUCCESS,String.valueOf(id)));
                    }
                    else {

                        String message = jsonObject1.getString("message");
                        EventBus.getDefault().post(new Event(Constant.COMPANYNOTREGISTERD,message));
                    }

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