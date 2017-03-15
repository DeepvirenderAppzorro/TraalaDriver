package com.appzorro.driverappcabscout.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.HttpHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vijay on 9/2/17.
 */

public class UserDetailManager {

    private static final String TAG = UserDetailManager.class.getSimpleName();

    public void UserDetailManager(Context context, String params) {

        new UserDetailManager.ExecuteApi(context).execute(params);
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

            Log.e(TAG, "user update detail--" + response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject response = jsonObject.getJSONObject("response");
                    String email = response.getString("email");
                    String name = response.getString("name");
                    String mobile = response.getString("mobile");
                    String profile_pic = response.getString("profile_pic");
                    CSPreferences.putString(mContext, "user_email", email);
                    CSPreferences.putString(mContext, "user_name", name);
                    CSPreferences.putString(mContext, "user_mobile", mobile);
                    CSPreferences.putString(mContext, "profile_pic", Config.baserurl_image+""+profile_pic);

                    EventBus.getDefault().post(new Event(Constant.USERDETAILSTAUS, ""));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                EventBus.getDefault().post(new Event(Constant.SERVER_ERROR, ""));
            }


        }
    }
}
