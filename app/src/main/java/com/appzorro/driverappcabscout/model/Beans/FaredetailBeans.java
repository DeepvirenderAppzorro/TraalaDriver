package com.appzorro.driverappcabscout.model.Beans;

import android.content.Context;

/**
 * Created by vijay on 27/2/17.
 */

public class FaredetailBeans {
    private Context context;
    private String companylogo,basefare,timefare,distancefare;

    public  FaredetailBeans(Context context,String companylogo,String basefare,String timefare,String distancefare){

        this.context =context;
        this.companylogo= companylogo;
        this.basefare = basefare;
        this.timefare=timefare;
        this.distancefare = distancefare;

    }

    public Context getContext() {
        return context;
    }

    public String getCompanylogo() {
        return companylogo;
    }

    public String getBasefare() {
        return basefare;
    }

    public String getTimefare() {
        return timefare;
    }

    public String getDistancefare() {
        return distancefare;
    }
}
