package com.appzorro.driverappcabscout.model;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.view.HomeScreenActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import static android.R.attr.key;

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

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e(TAG, "msg received---" + remoteMessage.getData());
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(4000);


        JSONObject jsonObject1 = new JSONObject(remoteMessage.getData());
        try {
            String key = jsonObject1.getString("noti_type");
            Config.rideRequestid = jsonObject1.getString("ride_request_id");
            message = jsonObject1.getString("message");
            Config.notificationkey = "" + key;

            Log.e("key print", key);
            if (key.equals("customer_request")) {

                intent = new Intent(this, HomeScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                ModelManager.getInstance().getCustomerReQuestManager().CustomerReQuestManager(getApplicationContext(), Operations.
                        getCustomerRequest(getApplicationContext(), CSPreferences.readString(getApplicationContext(),"customer_id")));




              /*  timer = new MyTimer(1500, 1000);
                timer.start();*/
            } else if (key.equals("cancel_ride")) {

                intent = new Intent(this, HomeScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                EventBus.getDefault().post(new Event(Constant.CANCEL_RIDEFCM,""));


            }








        } catch (JSONException e) {
            Log.e("exception", e.toString());
        }

    }

    public void shownotification(){


        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId,
                intent, PendingIntent.FLAG_ONE_SHOT);

        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_icon_profile_pic)
                .setContentTitle("" + key)
                .setContentText(""+message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId, notificationBuilder.build());

    }


   /* public class MyTimer extends CountDownTimer {


        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            long millis = l;
            String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));


        }

        @Override
        public void onFinish() {

            notificationManager.cancel(1410);

        }


    }*/


   /* private void cancel_Notification() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

               // notificationManager.cancel(1410);

            }
        };
        handler.postDelayed(runnable, 15000);
    }*/


}
