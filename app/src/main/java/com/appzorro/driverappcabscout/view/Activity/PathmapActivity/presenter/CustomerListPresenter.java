package com.appzorro.driverappcabscout.view.Activity.PathmapActivity.presenter;

import android.content.Context;

import com.appzorro.driverappcabscout.view.Activity.PathmapActivity.model.CustomerListResponse;
import com.appzorro.driverappcabscout.view.Activity.PathmapActivity.view.PathMapActivityView;
import com.appzorro.driverappcabscout.web.WebService;
import com.appzorro.driverappcabscout.web.handler.GetCustomerListHandler;

/**
 * Created by vijay on 31/10/18.
 */

public class CustomerListPresenter implements CustomerListPresenterHandler {
    PathMapActivityView view;

    public CustomerListPresenter(PathMapActivityView view) {
        this.view = view;
    }
    @Override
    public void getCustomerList(String driverId, String lat, String lng, final String Key) {
        view.showProgess();
        WebService.getInstance().getCustomerList(new GetCustomerListHandler() {
            @Override
            public void onSuccess(CustomerListResponse s) {
                view.hideProgess();
                view.CustomerListSuccess(s,Key);
            }
            @Override
            public void onFail(String message) {
                view.hideProgess();
                view.showErrorMessage(message.toString());
            }
        },driverId,lat,lng,Key);
    }

    @Override
    public void getCustomerDetail(Context context, String rideid) {

    }
}
