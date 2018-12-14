package com.appzorro.driverappcabscout.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by arvi on 12/11/17.
 */

public class SensorRestartBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = SensorRestartBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "ServiceStops, let's restart again.");
        Log.d("chkService","Intent start  service");
        Utils.sendMessageIo(context, "socketDisconnect", CSPreferences.readString(context, Constant.SOCKET_ID));



    }
}