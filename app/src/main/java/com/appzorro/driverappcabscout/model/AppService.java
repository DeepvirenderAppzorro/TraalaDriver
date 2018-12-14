package com.appzorro.driverappcabscout.model;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by vijay on 10/12/18.
 */

public class AppService extends Service {
    Context context;
    public AppService() {

    }

    public AppService(Context context) {
        this.context=context;
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Utils.sendMessageIo(getApplicationContext(), "socketDisconnect", CSPreferences.readString(getApplicationContext(), Constant.SOCKET_ID));

        stopSelf();
    }

    @Nullable

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
