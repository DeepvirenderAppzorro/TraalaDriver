package com.appzorro.driverappcabscout.web.handler;

import com.appzorro.driverappcabscout.view.Activity.loginActivity.model.LoginResponse;

/**
 * Created by vijay on 29/10/18.
 */

public interface GetLoginHandler {
    void onSuccess(LoginResponse Response);
    void onFail(String message);
}
