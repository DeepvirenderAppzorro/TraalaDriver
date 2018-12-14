package com.appzorro.driverappcabscout.web.handler;

import com.appzorro.driverappcabscout.view.Activity.loginActivity.model.ProfileResponse;

/**
 * Created by vijay on 30/10/18.
 */

public interface GetProfileHandler {
    void onSuccess(ProfileResponse Response);
    void onFail(String message);
}
