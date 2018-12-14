package com.appzorro.driverappcabscout.view.Activity.HomeActivity.view;

import com.appzorro.driverappcabscout.view.Activity.HomeActivity.model.DriverStatusResponse;
import com.appzorro.driverappcabscout.view.Activity.loginActivity.model.ProfileResponse;

/**
 * Created by vijay on 30/10/18.
 */

public interface ProfileActivityView {
    void showProgess();
    void hideProgess();
    void showFeedBackMessage(String message);
    void profileSuccess(ProfileResponse response);
    void driverStatus(DriverStatusResponse response);
}
