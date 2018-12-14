package com.appzorro.driverappcabscout.view.Activity.loginActivity.presenter;

import android.content.Context;

import com.appzorro.driverappcabscout.view.Activity.loginActivity.model.LoginResponse;
import com.appzorro.driverappcabscout.view.Activity.loginActivity.model.ProfileResponse;
import com.appzorro.driverappcabscout.view.Activity.loginActivity.view.LoginActivityView;
import com.appzorro.driverappcabscout.web.WebService;
import com.appzorro.driverappcabscout.web.handler.GetLoginHandler;
import com.appzorro.driverappcabscout.web.handler.GetProfileHandler;

/**
 * Created by vijay on 29/10/18.
 */

public class LoginActivityPresenter implements LoginActivityPresenterHandler  {
    LoginActivityView view;
    public LoginActivityPresenter(LoginActivityView view) {
        this.view = view;

    }

    @Override
    public void getLogin(String email, String pwd, String token) {
        view.showProgess();
        WebService.getInstance().getLogin(new GetLoginHandler() {
            @Override
            public void onSuccess(LoginResponse s) {
                view.hideProgess();
                view.loginSuccess(s);
            }

            @Override
            public void onFail(String message) {
                view.hideProgess();
                view.showFeedBackMessage(message.toString());
            }
        },email,pwd,token);
    }

    @Override
    public void getProfile(Context context, String driverid) {
        view.showProgess();
        WebService.getInstance().getProfile(new GetProfileHandler() {
            @Override
            public void onSuccess(ProfileResponse s) {
                view.hideProgess();
                view.profileSuccess(s);
            }

            @Override
            public void onFail(String message) {
                view.hideProgess();
                view.showFeedBackMessage(message.toString());
            }
        },driverid);
    }
}
