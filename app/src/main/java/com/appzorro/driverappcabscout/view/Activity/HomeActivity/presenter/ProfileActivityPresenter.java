package com.appzorro.driverappcabscout.view.Activity.HomeActivity.presenter;

import android.content.Context;

import com.appzorro.driverappcabscout.view.Activity.HomeActivity.model.DriverStatusResponse;
import com.appzorro.driverappcabscout.view.Activity.HomeActivity.view.ProfileActivityView;
import com.appzorro.driverappcabscout.view.Activity.loginActivity.model.ProfileResponse;
import com.appzorro.driverappcabscout.web.WebService;
import com.appzorro.driverappcabscout.web.handler.GetProfileHandler;
import com.appzorro.driverappcabscout.web.handler.GetStatusHandler;

/**
 * Created by vijay on 30/10/18.
 */

public class ProfileActivityPresenter implements ProfileActivityPresenterHandler {
    ProfileActivityView view;

    public ProfileActivityPresenter(ProfileActivityView view) {
        this.view = view;

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
                //view.showFeedBackMessage(message.toString());
            }
        },driverid);
    }

    @Override
    public void getDriverStatus(Context context,String id,String status, String Latitude, String Longitude, String time,String socket_id) {
        WebService.getInstance().getDriverStatus(new GetStatusHandler() {
            @Override
            public void onSuccess(DriverStatusResponse s) {
                view.hideProgess();
                view.driverStatus(s);
            }
            @Override
            public void onFail(String message) {
                view.hideProgess();
                view.showFeedBackMessage(message.toString());
            }
        },id,status,Latitude,Longitude,time,socket_id);
    }
}

