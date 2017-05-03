package com.appzorro.driverappcabscout.model.Beans;

/**
 * Created by vijay on 10/2/17.
 */

public class CustomerRequest {
    String name,requestid,cutomerid,profilepic,sourcelat,sourcelng,droplat,droplng,mobile,payment_method;

    public CustomerRequest(String name, String requestid, String cutomerid, String profilepic,String sourcelat,String sourcelng,
                          String droplat,String droplng, String mobile,String payment_method) {
        this.name = name;
        this.requestid = requestid;
        this.cutomerid = cutomerid;
        this.profilepic = profilepic;
        this.sourcelat = sourcelat;
        this.sourcelng = sourcelng;
        this.droplat=droplat;
        this.droplng=droplng;
        this.mobile = mobile;
        this.payment_method = payment_method;
    }

    public String getName() {
        return name;
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
