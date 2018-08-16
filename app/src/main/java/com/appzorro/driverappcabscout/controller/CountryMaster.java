package com.appzorro.driverappcabscout.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import com.appzorro.driverappcabscout.model.Beans.CountryCode;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by vijay on 1/8/18.
 */

public class CountryMaster {
    public static final String TAG = "CountryMaster";

    public static CountryMaster sInstance = null;
    private Context mContext = null;
    private String[] mCountryList;
    public SparseArray<ArrayList<CountryCode>> mCountriesMap = new SparseArray<ArrayList<CountryCode>>();

    public static  ArrayList<CountryCode> mCountries = new ArrayList<CountryCode>();
    public static  ArrayList<String> mCountries_ISO = new ArrayList<String>();
    public static  ArrayList<String> mCountries_CODE= new ArrayList<String>();
    public static  ArrayList<String> mIso= new ArrayList<String>();

    public CountryMaster(Context context) {
        this.mContext = context;
        startMethod(mContext);

    }
    public void startMethod(Context context){
        new AsyncPhoneInitTask(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    public class AsyncPhoneInitTask extends AsyncTask<Void, Void, ArrayList<CountryCode>> {

        private int mSpinnerPosition = -1;
        private Context mContext;

        public AsyncPhoneInitTask(Context context) {
            mContext = context;
        }

        @Override
        protected ArrayList<CountryCode> doInBackground(Void... params) {
            ArrayList<CountryCode> data = new ArrayList<CountryCode>(233);
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(mContext.getApplicationContext().getAssets().open("countries.dat"), "UTF-8"));

                // do reading, usually loop until end of file reading
                String line;
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    //process line
                    CountryCode c = new CountryCode(mContext, line, i);
                    data.add(c);
                    ArrayList<CountryCode> list = mCountriesMap.get(c.getCountryCode());
                    if (list == null) {
                        list = new ArrayList<CountryCode>();
                        mCountriesMap.put(c.getCountryCode(), list);
                    }
                    list.add(c);
                    i++;
                }
            } catch (IOException e) {
                //log the exception
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        //log the exception
                    }
                }
            }

            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<CountryCode> data) {
            // mAdapter.addAll(data);
            mCountries = data;

            for (int i =0;i<data.size();i++) {
                String name = data.get(i).getName();
                String code = data.get(i).getCountryCodeStr();
                String iso=data.get(i).getCountryISO();
                Log.d("Hellos",name);
                mIso.add(iso);
                mCountries_ISO.add(name);
                mCountries_CODE.add(code);
            }
            if (mSpinnerPosition > 0) {
                //mSpinner.setSelection(mSpinnerPosition);
            }
            EventBus.getDefault().post(new Event(Constant.GetCountryISO, data));
        }
    }

    public ArrayList<CountryCode> getCountryName(){
        return mCountries;
    }

}
