package com.appzorro.driverappcabscout.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appzorro.driverappcabscout.model.Beans.CustomerDetailBean;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.HttpHandler;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomerDetail {

    public static CustomerDetailBean customer_detailBean;


    private final String TAG = CustomerDetail.class.getSimpleName();

    public void customerDetail(Context context, String params) {
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
            Log.e(TAG, "customer_status_response--" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //{"response":{"message":"Success","status":"1","data":{"car_type0":"0","fare0":30.05,"car_type1":"1","fare1":50.5,"car_type2":"2","fare2":60.02}}}

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonObject1=jsonObject.getJSONObject("response");
                String message = jsonObject1.getString("message");

                if (message.equals("success")) {
                    Gson gson = new Gson();
                    customer_detailBean = gson.fromJson(s, CustomerDetailBean.class);
                    String s1=customer_detailBean.getResponse().getDetail().getCustomerId();

                 /*   JSONObject jsonObject2 = jsonObject.getJSONObject("detail");
                    String status = jsonObject1.getString("status");*/
                 // mContext.startActivity(new Intent(mContext, NotificatonDialog.class).addFlags((Intent.FLAG_ACTIVITY_NEW_TASK)));
                    EventBus.getDefault().post(new Event(Constant.NOTIFICATION_DIALOG, ""));

                } else
                    EventBus.getDefault().post(new Event(Constant.NOTIFICATION_DIALOG_FAILURE, ""));

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}
