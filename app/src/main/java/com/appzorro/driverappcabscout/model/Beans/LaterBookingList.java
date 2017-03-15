package com.appzorro.driverappcabscout.model.Beans;

/**
 * Created by vijay on 11/2/17.
 */

public class LaterBookingList {
    String requestid, sourcelat, sourcelng, destlat, destlng, date, time;

    public LaterBookingList(String requestid, String sourcelat, String sourcelng, String destlat, String destlng,
                            String date, String time) {
        this.requestid=requestid;
        this.sourcelat =sourcelat;
        this.sourcelng = sourcelng;
        this.destlat=destlat;
        this.destlng =destlng;
        this.date=date;
        this.time =time;
    }

    public String getRequestid() {
        return requestid;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getDestlng() {
        return destlng;
    }

    public String getDestlat() {
        return destlat;
    }

    public String getSourcelng() {
        return sourcelng;
    }

    public String getSourcelat() {
        return sourcelat;
    }
}