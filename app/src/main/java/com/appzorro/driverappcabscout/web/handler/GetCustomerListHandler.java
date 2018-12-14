package com.appzorro.driverappcabscout.web.handler;

import com.appzorro.driverappcabscout.view.Activity.PathmapActivity.model.CustomerListResponse;

/**
 * Created by vijay on 31/10/18.
 */

public interface GetCustomerListHandler {
    void onSuccess(CustomerListResponse Response);
    void onFail(String message);
}
