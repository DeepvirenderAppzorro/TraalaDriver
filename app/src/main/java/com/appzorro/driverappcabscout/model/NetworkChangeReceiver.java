package com.appzorro.driverappcabscout.model;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.appzorro.driverappcabscout.AppController;


public class NetworkChangeReceiver extends BroadcastReceiver {

    public static boolean isConnected= false;
    Dialog dialog;
    @Override
    public void onReceive(final Context context, final Intent intent) {

        try {
            boolean isVisible = AppController.isActivityVisible();// Check if activity is visible or not
            Log.i("Activity is Visible ", "Is activity visible : " + isVisible);

            // If it is visible then trigger the task else do nothing
            if (isVisible == true) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager
                        .getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    isConnected = true;
                    dialog.dismiss();
                  //  Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
                    Utils.connectServer();
                    Utils.sendMessageIo(context,"onlineDriver",CSPreferences.readString(context,Constant.SOCKET_ID));

                    // Utils.getInstance().serviceMethods(context);
                   // Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
                } else {

                    isConnected=false;
                    dialog = Utils.InternetStatus(context);
                    dialog.setCancelable(false);
                    dialog.show();

                    Utils.sendMessageIo(context,"socketDisconnect",CSPreferences.readString(context,Constant.SOCKET_ID));
                   // SignalRService.mConnection = null;
                    //Utils.mService.onDestroy();

                 //   Toast.makeText(context, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}