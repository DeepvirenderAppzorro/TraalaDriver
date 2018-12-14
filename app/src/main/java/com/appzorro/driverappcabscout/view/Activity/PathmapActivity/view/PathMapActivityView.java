package com.appzorro.driverappcabscout.view.Activity.PathmapActivity.view;

import com.appzorro.driverappcabscout.view.Activity.PathmapActivity.model.CustomerListResponse;

/**
 * Created by vijay on 31/10/18.
 */

public interface PathMapActivityView {
    void showProgess();
    void hideProgess();
    void showErrorMessage(String message);
    void CustomerDetailSuccess(CustomerListResponse response);
    void CustomerListSuccess(CustomerListResponse response,String key);
}
