package com.appzorro.driverappcabscout.model.Beans;

/**
 * Created by vijay on 10/7/18.
 */

public class Countries_bean {
    String CName, Cid;

    public Countries_bean(String CName, String Cid) {
        this.CName = CName;
        this.Cid = Cid;
    }

    public String getCid() {
        return Cid;
    }

    public void setCid(String cid) {
        Cid = cid;
    }

    public String getCName() {

        return CName;
    }

    public void setCName(String CName) {
        this.CName = CName;
    }
}
