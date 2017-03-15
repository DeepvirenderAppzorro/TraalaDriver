package com.appzorro.driverappcabscout.model.Beans;

/**
 * Created by vijay on 20/2/17.
 */

public class CompletedRideBean {
    String requestid,pickuplat,pickuplng,droplat,droplng,startdate, edndtime,totalamount,name,profilepic,mobile;


    public CompletedRideBean(String requestid, String pickuplat, String pickuplng, String droplat,String droplng,String startdate,
                           String edndtime,String totalamount,String name,String profilepic,String mobile) {
        this.requestid = requestid;
        this.pickuplat = pickuplat;
        this.pickuplng = pickuplng;
        this.droplat=droplat;
        this.droplng=droplng;
        this.startdate=startdate;
        this.edndtime = edndtime;

        this.totalamount=totalamount;
        this.name = name;
        this.profilepic=profilepic;
        this.mobile=mobile;
    }

    public String getRequestid() {
        return requestid;
    }

    public String getPickuplat() {
        return pickuplat;
    }

    public String getPickuplng() {
        return pickuplng;
    }

    public String getDroplat() {
        return droplat;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getDroplng() {
        return droplng;
    }

    public String getEdndtime() {
        return edndtime;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public String getName() {
        return name;
    }


    public String getProfilepic() {
        return profilepic;
    }

    public String getMobile() {
        return mobile;
    }
}
