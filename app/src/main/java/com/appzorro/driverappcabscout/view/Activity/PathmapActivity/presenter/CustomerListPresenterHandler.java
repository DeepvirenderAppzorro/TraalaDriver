package com.appzorro.driverappcabscout.view.Activity.PathmapActivity.presenter;

import android.content.Context;

/**
 * Created by vijay on 31/10/18.
 */

public interface CustomerListPresenterHandler {
    void getCustomerList(String driverId,String lat,String lng,String Key);
    void getCustomerDetail(Context context, String rideid);
}
