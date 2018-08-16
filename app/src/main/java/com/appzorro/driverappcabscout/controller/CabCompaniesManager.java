package com.appzorro.driverappcabscout.controller;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.VolleyError;
import com.appzorro.driverappcabscout.model.Beans.CompanyLocationBean;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.HttpHandler;
import com.appzorro.driverappcabscout.model.network.OnApihit;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/** Created by Pankaj on 22/1/17. **/
public class CabCompaniesManager implements OnApihit{
    private final String TAG = CabCompaniesManager.class.getSimpleName();

    //public static final HashMap<Integer, String> cabCompaniesList = new HashMap<>();
    public  static CompanyLocationBean companyLocationBean ;
    public void getCabCompanies(Context context, String params) {

        //HashMap<String,String> stringStringHashMap =  new HashMap<>();
        //stringStringHashMap.put("action","get_cab");
        //stringStringHashMap.put("cab_alias",cabName);
        //Log.d("Params_getCabCompanies",Config.BaseURL+stringStringHashMap);
        //new VolleyBase(this).main(stringStringHashMap, Config.BaseURL,0);
        new ExecuteApi(context).execute(params);
    }
    public void getLocationId(Context context, String params) {
        new ExecuteLocationApi(context).execute(params);
    }

    @Override
    public void success(String Response, int index) throws JSONException {
        Log.d("responseVolley",Response);
        if (Response != null) {
            try {
                JSONObject jsonObject = new JSONObject(Response);
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

    @Override
    public void error(VolleyError error, int index) {
        Log.d("errorVolley",error.getMessage()+"");
        EventBus.getDefault().post(new Event(Constant.SERVER_ERROR,""));
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
            Log.e(TAG, "companies_list--"+response);
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

    private class ExecuteLocationApi extends AsyncTask<String, String, String> {

        Context mContext;

        ExecuteLocationApi(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(strings[0]);
            Log.e(TAG, "get_location_companies_response-- "+response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject response = jsonObject.getJSONObject("response");
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        Gson gson = new Gson();
                        companyLocationBean =  gson.fromJson(s, CompanyLocationBean.class);
                        EventBus.getDefault().post(new Event(Constant.COMPANYLOCATION_SUCCESS, ""));
                    }
                    else{
                        EventBus.getDefault().post(new Event(Constant.COMPANYLOCATION_FAILURE, ""));
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

            }
        }
    }

}