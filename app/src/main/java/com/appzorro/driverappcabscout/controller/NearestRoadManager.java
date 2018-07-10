package com.appzorro.driverappcabscout.controller;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appzorro.driverappcabscout.AppController;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.HttpHandler;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by rishav on 27/4/17.
 */

public class NearestRoadManager {

    private static final String TAG = NearestRoadManager.class.getSimpleName();

    Context mContext;
    public static Double driverlongititude = null;
    public static Double driverlat = null;
    int status;
    public static List<LatLng> polyLineList;

    public void NearestRoadManager(Context context, String params, int status) {
        this.mContext = context;
        this.status = status;
        // new ExecuteApi(context).execute(params);
        if (status == 1)
            hitRoadAPI(params);
        else
            hitDirectionApi(params);
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
            // Log.e(TAG, "add_home response-- "+response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //  Log.e("road response",""+s);

            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("snappedPoints");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("location");

                        driverlat = Double.parseDouble(jsonObject2.getString("latitude"));
                        driverlongititude = Double.parseDouble(jsonObject2.getString("longitude"));


                        CSPreferences.putString(mContext, "current_lat", jsonObject2.getString("latitude"));
                        CSPreferences.putString(mContext, "current_lng", jsonObject2.getString("longitude"));

                        EventBus.getDefault().post(new Event(Constant.MY_LOCATION, ""));


                    }

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

            }
        }
    }

    public void hitRoadAPI(String params) {
        StringRequest request = new StringRequest(params, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("PartnerInfoRes::", response);
                JSONObject jObj;
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("snappedPoints");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("location");

                            driverlat = Double.parseDouble(jsonObject2.getString("latitude"));
                            driverlongititude = Double.parseDouble(jsonObject2.getString("longitude"));


                            CSPreferences.putString(mContext, "current_lat", jsonObject2.getString("latitude"));
                            CSPreferences.putString(mContext, "current_lng", jsonObject2.getString("longitude"));

                            EventBus.getDefault().post(new Event(Constant.MY_LOCATION, ""));

                        }
                    } catch (Exception e) {
                        Log.d("jsonError::", e + "");
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, error.getMessage() + "error");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //params.put("driver_id", driverId);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //Log.d("acc::", ClientAccToken);
                //params.put("authorization", "ClientAccToken");

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request);
    }

    private void hitDirectionApi(String url) {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                polyLineList = new ArrayList<>();
                Log.d("DistanceInfoRes::", response + "");
                JSONObject jObj;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject route = jsonArray.getJSONObject(i);
                        JSONObject poly = route.getJSONObject("overview_polyline");
                        String polyline = poly.getString("points");
                        polyLineList = decodePoly(polyline);
                        Log.d(TAG, polyLineList + "");
                        EventBus.getDefault().post(new Event(Constant.DRAW_POLYLINE, ""));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //params.put("driver_id", driverId);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //Log.d("acc::", ClientAccToken);
                //params.put("authorization", "ClientAccToken");

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(request, TAG);
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

}
