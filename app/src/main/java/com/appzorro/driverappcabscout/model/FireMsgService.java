package com.appzorro.driverappcabscout.model;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.view.HomeScreenActivity;
import com.appzorro.driverappcabscout.view.NotificatonDialog;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/*
 * Created by rishav on 11/1/17.
 */

public class FireMsgService extends FirebaseMessagingService {

    private final String TAG = FireMsgService.class.getSimpleName();
    Intent intent;
    int notificationId = new Random().nextInt();
    MyTimer timer;
    String message;
    NotificationManager notificationManager;
    Runnable runnable;
    Handler handler;
    String key;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e(TAG, "msg_received---" + remoteMessage.getData());
       /* Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(4000);
*/

        JSONObject jsonObject1 = new JSONObject(remoteMessage.getData());
       // Log.d("")
/*
        try {
            key = jsonObject1.getString("noti_type");
            Config.rideRequestid = jsonObject1.getString("ride_request_id");
            message = jsonObject1.getString("message");
            Config.notificationkey = "" + key;

            Log.e("key print", key);
            if (key.equals("customer_request")) {
                sendNotification(message);
                //Calling method to generate notification

                intent = new Intent(this, NotificatonDialog.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ModelManager.getInstance().getCustomerReQuestManager().CustomerReQuestManager(getApplicationContext(), Operations.
                        getCustomerRequest(getApplicationContext(), CSPreferences.readString(getApplicationContext(), "customer_id")));
              */

/*  timer = new MyTimer(1500, 1000);
                timer.start();*//*

            } else if (key.equals("cancel_ride")) {

                intent = new Intent(this, HomeScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                EventBus.getDefault().post(new Event(Constant.CANCEL_RIDEFCM, ""));

            }


        } catch (JSONException e) {
            Log.e("exception", e.toString());
        }
*/

    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, NotificatonDialog.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_icon_logo)
                .setContentTitle("Trala Notification")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
        ModelManager.getInstance().getCustomerReQuestManager().CustomerReQuestManager(getApplicationContext(), Operations.
                getCustomerRequest(getApplicationContext(), CSPreferences.readString(getApplicationContext(), "customer_id")));


    }

}

