package com.appzorro.driverappcabscout.view.Activity.loginActivity.presenter;

import android.content.Context;

/**
 * Created by vijay on 29/10/18.
 */

public interface LoginActivityPresenterHandler {
    void getLogin(String email,String Pwd,String token);
    void getProfile(Context context, String userid);
}
