package com.appzorro.driverappcabscout.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appzorro.driverappcabscout.model.Beans.ReviewBeans;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.HttpHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vijay on 14/2/17.
 */

public class ReviewManager {

    private static final String TAG = ReviewManager.class.getSimpleName();
    public static ArrayList<ReviewBeans>reviewliast;

    public void ReviewManager(Context context, String params) {

        new ReviewManager.ExecuteApi(context).execute(params);
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

            Log.e(TAG, "Review url--" +response);

            return response;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null) {

                    reviewliast = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    if (jsonArray.length()>0) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String name = jsonObject1.getString("name");
                            String profilepic = Config.baserurl_image + jsonObject1.getString("profile_pic");
                            String feedbacktime = jsonObject1.getString("feedback_time");
                            String rating = jsonObject1.getString("rating");
                            String feedback = jsonObject1.getString("feedback");
                            ReviewBeans reviewBeans = new ReviewBeans(name, feedbacktime, rating, feedback);
                            reviewliast.add(reviewBeans);
                            EventBus.getDefault().post(new Event(Constant.REVIEW, ""));
                        }
                    }else{

                        EventBus.getDefault().post(new Event(Constant.NOREVIEW, ""));
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
