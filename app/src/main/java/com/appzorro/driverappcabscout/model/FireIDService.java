package com.appzorro.driverappcabscout.model;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by rishav on 11/1/17.
 */

public class FireIDService extends FirebaseInstanceIdService {
    private static final String TAG = FireIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String tkn = FirebaseInstanceId.getInstance().getToken();
        Log.e("Not","Token ["+tkn+"]");
        Log.e(TAG, "Refreshed token:" + tkn);

    }
}