package com.appzorro.driverappcabscout.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appzorro.driverappcabscout.model.Beans.LaterBookingList;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.HttpHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vijay on 11/2/17.
 */

public class LaterBookingManager {

    private static final String TAG = LoginManager.class.getSimpleName();
    public static ArrayList<LaterBookingList>bookinglis;

    public void LaterBookingManager(Context context, String params) {

        new LaterBookingManager.ExecuteApi(context).execute(params);
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

            Log.e(TAG, "later booking response--" +response);

            return response;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null) {
                    bookinglis = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String requestid = jsonObject1.getString("ride_request_id");
                        int id = Integer.parseInt(requestid);
                        if (id>0) {
                            String datetime = jsonObject1.getString("datetime");
                            String pickupcordinate = jsonObject1.getString("pickup_cordinates");
                            String destdropcorndinate = jsonObject1.getString("drop_cordinates");
                            String[] datesplit = datetime.split(" ");
                            String date = datesplit[datesplit.length - 3];
                            String time = datesplit[datesplit.length - 2];
                            String AMPM = datesplit[datesplit.length - 1];
                            String fulltime = time + " " + AMPM;
                            String pickupsplit[] = pickupcordinate.split(",");
                            String sourcelat = pickupsplit[pickupsplit.length - 2];
                            String sourcelng = pickupsplit[pickupsplit.length - 1];
                            String destsplit[] = destdropcorndinate.split(",");
                            String destlat = destsplit[destsplit.length - 2];
                            String destlng = destsplit[destsplit.length - 1];
                            LaterBookingList laterBookingList = new LaterBookingList(requestid, sourcelat, sourcelng, destlat,
                                    destlng, date, fulltime);
                            bookinglis.add(laterBookingList);
                            EventBus.getDefault().post(new Event(Constant.LATERBOOKING,""));

                        }
                        else {


                            EventBus.getDefault().post(new Event(Constant.NOBOOKINGYET,""));
                        }



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{

                EventBus.getDefault().post(new Event(Constant.SERVER_ERROR,""));
            }
        }
    }
}
