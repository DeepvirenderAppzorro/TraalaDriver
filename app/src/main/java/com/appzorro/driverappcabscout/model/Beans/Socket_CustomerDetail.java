package com.appzorro.driverappcabscout.model.Beans;

/**
 * Created by vijay on 14/11/18.
 */

public class Socket_CustomerDetail {


    /**
     * driver_id : 8
     * cutomerID : 3
     * ride_request_id : 33
     * message : New ride request from Sumit
     * noti_type : customer_request
     * poolType : other
     * distance : 1 m
     */

    private String driver_id;
    private String cutomerID;
    private String ride_request_id;
    private String message;
    private String noti_type;
    private String poolType;
    private String distance;

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getCutomerID() {
        return cutomerID;
    }

    public void setCutomerID(String cutomerID) {
        this.cutomerID = cutomerID;
    }

    public String getRide_request_id() {
        return ride_request_id;
    }

    public void setRide_request_id(String ride_request_id) {
        this.ride_request_id = ride_request_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNoti_type() {
        return noti_type;
    }

    public void setNoti_type(String noti_type) {
        this.noti_type = noti_type;
    }

    public String getPoolType() {
        return poolType;
    }

    public void setPoolType(String poolType) {
        this.poolType = poolType;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
