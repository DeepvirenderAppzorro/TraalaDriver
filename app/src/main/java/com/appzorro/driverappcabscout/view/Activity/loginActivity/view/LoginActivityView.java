package com.appzorro.driverappcabscout.view.Activity.loginActivity.view;

import com.appzorro.driverappcabscout.view.Activity.loginActivity.model.LoginResponse;
import com.appzorro.driverappcabscout.view.Activity.loginActivity.model.ProfileResponse;

/**
 * Created by vijay on 29/10/18.
 */

public interface LoginActivityView {
    void showProgess();
    void hideProgess();
    void showFeedBackMessage(String message);
    void loginSuccess(LoginResponse response);
    void profileSuccess(ProfileResponse response);
}
