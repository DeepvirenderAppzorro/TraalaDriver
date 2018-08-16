package com.appzorro.driverappcabscout.model.Beans;

/**
 * Created by vijay on 10/2/17.
 */

public class CustomerRequest {
    String name,requestid,cutomerid,profilepic,sourcelat,sourcelng,droplat,droplng,mobile,
            payment_method,price,vehicleType,picLoc,dropLoc;

    public String getDropLoc() {
        return dropLoc;
    }

    public String getPicLoc() {
        return picLoc;
    }

    public void setPicLoc(String picLoc) {
        this.picLoc = picLoc;
    }

    public void setDropLoc(String dropLoc) {
        this.dropLoc = dropLoc;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public CustomerRequest(String name, String requestid, String cutomerid, String profilepic, String sourcelat, String sourcelng,
                           String droplat, String droplng, String mobile, String payment_method, String price, String vehicleType,String picLoc,String dropLoc) {
        this.name = name;
        this.requestid = requestid;
        this.cutomerid = cutomerid;
        this.profilepic = profilepic;
        this.sourcelat = sourcelat;

        this.picLoc=picLoc;
        this.dropLoc=dropLoc;
        this.sourcelng = sourcelng;
        this.droplat=droplat;
        this.droplng=droplng;
        this.mobile = mobile;
        this.payment_method = payment_method;
        this.price = price;
        this.vehicleType=vehicleType;
    }


    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }


    public String getRequestid() {
        return requestid;
    }

    public String getCutomerid() {
        return cutomerid;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public String getSourcelat() {
        return sourcelat;
    }

    public String getSourcelng() {
        return sourcelng;
    }

    public String getDroplat() {
        return droplat;
    }

    public String getDroplng() {
        return droplng;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public String getMobile() {
        return mobile;
    }
}
