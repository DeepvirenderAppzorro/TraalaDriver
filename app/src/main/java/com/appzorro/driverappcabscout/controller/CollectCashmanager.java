package com.appzorro.driverappcabscout.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appzorro.driverappcabscout.model.Beans.CashCollect_bean;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.HttpHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vijay on 17/2/17.
 */

public class CollectCashmanager {

    private static final String TAG = CollectCashmanager.class.getSimpleName();
    public static ArrayList<CashCollect_bean> cash_list;


    public void CollectCashmanager(Context context, String params) {

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

            Log.e(TAG, "rating_submiturl--" +response);

            return response;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null){
                cash_list = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(s);

                JSONObject jsonObject1 = jsonObject.getJSONObject("response");

          //      int id = Integer.parseInt(jsonObject1.getString("id"));

                String message = jsonObject1.getString("message");
              JSONObject jsonObject2=jsonObject1.getJSONObject("fare");
                String baseFare=jsonObject2.getString("fare");
                    CashCollect_bean cash = new CashCollect_bean(baseFare);
                    cash_list.add(cash);
                    EventBus.getDefault().post(new Event(Constant.COLLECTCASH,message));

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
