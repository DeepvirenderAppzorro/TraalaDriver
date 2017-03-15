package com.appzorro.driverappcabscout.model;

import android.os.CountDownTimer;

import java.util.concurrent.TimeUnit;

/**
 * Created by vijay on 10/2/17.
 */

public class MyTimer  extends CountDownTimer{


    public MyTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long l) {
        long millis = l;
        String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));



    }

    @Override
    public void onFinish() {

    }
}
