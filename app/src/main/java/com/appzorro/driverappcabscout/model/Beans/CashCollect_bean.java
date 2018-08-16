package com.appzorro.driverappcabscout.model.Beans;

/**
 * Created by vijay on 18/6/18.
 */

public class CashCollect_bean {
     String Base,tym,dis,tot;

    public CashCollect_bean(String base) {
        Base = base;
        this.tym=tym;
        this.dis=dis;
        this.tot=tot;

    }

    public String getBase() {
        return Base;
    }

    public String getDis() {
        return dis;
    }

    public String getTot() {
        return tot;
    }

    public void setTot(String tot) {
        this.tot = tot;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public String getTym() {
        return tym;
    }

    public void setTym(String tym) {
        this.tym = tym;
    }

    public void setBase(String base) {
        Base = base;
    }




}
