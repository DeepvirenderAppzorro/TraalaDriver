package com.appzorro.driverappcabscout.view.Activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.appzorro.driverappcabscout.AppController;

/**
 * Created by vijay on 5/10/18.
 */

public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Activity activity;

    public MyExceptionHandler(Activity a) {
        activity = a;
    }
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.d("heyException",e.getMessage()+"");
        Intent intent = new Intent(activity, SplashActivity.class);
        intent.putExtra("crash", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(AppController.getInstance().getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager mgr = (AlarmManager) AppController.getInstance().getBaseContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);

//        activity.finish();
//        System.exit(2);
    }
}
