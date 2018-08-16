package com.appzorro.driverappcabscout.model.Beans;

/**
 * Created by vijay on 10/7/18.
 */

public class State_bean {
    public State_bean(String sid,String Cid,String  SName) {
        Sid = sid;
        Countryid=Cid;
        Sid=SName;
    }

    public String getCountryid() {
        return Countryid;

    }

    public void setCountryid(String countryid) {
        Countryid = countryid;
    }

    public String getSName() {
        return SName;

    }

    public void setSName(String SName) {
        this.SName = SName;
    }

    public String getSid() {
        return Sid;

    }

    public void setSid(String sid) {
        Sid = sid;
    }

    String Sid,SName,Countryid;
}
