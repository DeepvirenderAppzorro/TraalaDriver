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
 * Created by vijay on 13/2/17.
 */

public class UpdateProfileManager {
    private static final String TAG = UpdateProfileManager.class.getSimpleName();

    public void UpdateProfileManager(Context context, String url,String params) {

        new UpdateProfileManager.ExecuteApi(context).execute(url,params);
    }

    private class ExecuteApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApi(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.getResponse(strings[0],strings[1]);

            Log.e(TAG, "updateprofileresponse--" +response);

            return response;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null) {
           //     EventBus.getDefault().post(new Event(Constant.UPDATEPROFILE,"profile updated"));

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                    int id = Integer.parseInt(jsonObject1.getString("id"));

                    String message = jsonObject1.getString("message");
                    if (id>0){

                        EventBus.getDefault().post(new Event(Constant.UPDATEPROFILE,message));

                    }
                    else {

                        EventBus.getDefault().post(new Event(Constant.ERROR,message));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                EventBus.getDefault().post(new Event(Constant.SERVER_ERROR,""));

            }
        }
    }
}
