package com.appzorro.driverappcabscout.web.handler;

import com.appzorro.driverappcabscout.view.Activity.HomeActivity.model.DriverStatusResponse;

/**
 * Created by vijay on 30/10/18.
 */

public interface GetStatusHandler {
    void onSuccess(DriverStatusResponse Response);
    void onFail(String message);
}
