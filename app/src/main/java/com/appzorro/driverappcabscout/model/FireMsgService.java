package com.appzorro.driverappcabscout.model;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.appzorro.driverappcabscout.view.HomeScreenActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.appzorro.driverappcabscout.R;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * Created by rishav on 11/1/17.
 */

public class FireMsgService extends FirebaseMessagingService {

    private final String TAG = FireMsgService.class.getSimpleName();
    Intent intent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e(TAG, "msg received---"+remoteMessage.getData());


        JSONObject jsonObject1 = new JSONObject(remoteMessage.getData());
        try {
            String key = jsonObject1.getString("noti_type");
            Config.notificationkey = ""+key;
            Log.e("key print",key);
            if (key.equals("customer_request")){



                intent = new Intent(this, HomeScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }



            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410,
                    intent, PendingIntent.FLAG_ONE_SHOT);

            android.support.v4.app.NotificationCompat.Builder notificationBuilder = new
                    NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_icon_profile_pic)
                    .setContentTitle("Message")
                 //   .setContentText(""+remoteMessage.getNotification().getBody())
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager)
                            getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1410, notificationBuilder.build());

        } catch (JSONException e) {
          Log.e( "exception",e.toString());
        }




    }
}