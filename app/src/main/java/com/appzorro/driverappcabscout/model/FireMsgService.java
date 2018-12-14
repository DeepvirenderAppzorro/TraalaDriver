package com.appzorro.driverappcabscout.model;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.appzorro.driverappcabscout.view.Activity.NotificatonDialog;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

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

        JSONObject jsonObject1 = new JSONObject(remoteMessage.getData());
        Log.d("jsonObj", remoteMessage.getData() + "");

        try {
            key = jsonObject1.getString("noti_type");
            message = jsonObject1.getString("message");
            Config.notificationkey = "" + key;

            Log.e("key print", key);
            if (key.equals("verify_driver")) {
                if (Utils.isAppIsInBackground(getApplicationContext())) {
                    //sendNotification(message);
                    sendmessage(message);

                }
            } else if (Utils.isAppIsInBackground(getApplicationContext())&&key.equals("customer_request")) {
             //   sendNotification(message);
                sendmessage(message);
                String ride_request_id = jsonObject1.getString("ride_request_id");
                Intent intent1 = new Intent(this, NotificatonDialog.class);
                intent1.putExtra("coming_from", "FCM");
                intent1.putExtra("ride_request_id", ride_request_id);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);

            }


        } catch (JSONException e) {
            Log.e("exception", e.toString());
        }

    }

    /*
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
    */
//    private void sendNotification(String messageBody) {
//        Intent intent = new Intent(this, SplashActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.ic_icon_logo)
//                .setContentTitle("Trala Notification")
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0, notificationBuilder.build());
//
//
//    }

    public void sendmessage(String message){
        Intent pushNotification = new Intent(NotificationUtils.PUSH_NOTIFICATION);
        pushNotification.putExtra("message", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        // play notification sound
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.playNotificationSound();
    }
}

