package com.appzorro.driverappcabscout.controller;

import android.content.Context;
import android.os.AsyncTask;

import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.HttpHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vijay on 6/4/17.
 */

public class LocationSendManager {

    private static final String TAG = LocationSendManager.class.getSimpleName();

    public void locationsend(Context context, String params) {

        new ExecuteApi(context).execute(params);
    }




    public class ExecuteApi extends AsyncTask<String,String,String>{
        Context context;

        ExecuteApi(Context context){

            this.context = context;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler httpHandler = new HttpHandler();
            String response =httpHandler.makeServiceCall(strings[0]);


            return response;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (s!=null){

                try {

                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                    int id = Integer.parseInt(jsonObject1.getString("id"));
                    String message = jsonObject1.getString("message");

                    if (id>0){

                        EventBus.getDefault().post(new Event(Constant.LOCATIONSEND,message));

                    }
                    else {

                        EventBus.getDefault().post(new Event(Constant.LOCATIONNOTSEND,message));

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
