package com.appzorro.driverappcabscout.view.Activity.HomeActivity.presenter;

import android.content.Context;

/**
 * Created by vijay on 30/10/18.
 */

public interface ProfileActivityPresenterHandler {
    void getProfile(Context context, String userid);
    void getDriverStatus(Context context, String id,String status,String Latitude,String Longitude,String time,String soc_id);
}
