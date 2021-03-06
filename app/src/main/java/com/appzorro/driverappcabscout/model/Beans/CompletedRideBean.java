package com.appzorro.driverappcabscout.model.Beans;

import java.io.Serializable;

/**
 * Created by vijay on 20/2/17.
 */

public class CompletedRideBean implements Serializable {
    String requestid, pickuplat, pickuplng, droplat, droplng, startdate, edndtime, edndtym, totalamount, name, profilepic, mobile, start_tym, end_tym, id, curPage, lastPage, pagesize, start_Tym, dropLoc, pickUpLoc, feedback, vehicleName, PaymentType, pathImg;

    public String getPathImg() {
        return pathImg;
    }

    public void setPathImg(String pathImg) {
        this.pathImg = pathImg;
    }

    public CompletedRideBean(String requestid, String pickuplat, String pickuplng, String droplat, String droplng, String startdate, String start_tym,
                             String edndtime, String end_tym, String totalamount, String name, String profilepic, String mobile, String dropLocc, String PickupLoc, String feedback, String vehicleName, String paymentType, String pathImg) {
        this.requestid = requestid;
        this.pickuplat = pickuplat;
        this.pickuplng = pickuplng;
        this.pathImg = pathImg;

        this.droplat = droplat;
        this.feedback = feedback;
        PaymentType = paymentType;
        this.vehicleName = vehicleName;

        this.droplng = droplng;
        this.startdate = startdate;
        this.edndtime = edndtime;
        this.edndtym = end_tym;

        this.totalamount = totalamount;
        this.name = name;
        this.profilepic = profilepic;
        this.mobile = mobile;
        start_Tym = start_tym;
        dropLoc = dropLocc;
        pickUpLoc = PickupLoc;
    }


    public String getEnd_tym() {
        return end_tym;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public void setEnd_tym(String end_tym) {
        this.end_tym = end_tym;
    }

    public String getLastPage() {
        return lastPage;
    }

    public void setLastPage(String lastPage) {
        this.lastPage = lastPage;
    }

    public String getCurPage() {

        return curPage;
    }

    public void setCurPage(String curPage) {
        this.curPage = curPage;
    }

    public String getId() {


        return id;
    }

    public String getPagesize() {
        return pagesize;
    }

    public void setPagesize(String pagesize) {
        this.pagesize = pagesize;
    }

    public String getStart_tym() {
        return start_Tym;
    }

    public void setStart_tym(String start_tym) {
        this.start_tym = start_tym;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDropLoc() {
        return dropLoc;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public String getPickUpLoc() {
        return pickUpLoc;
    }

    public void setPickUpLoc(String pickUpLoc) {
        this.pickUpLoc = pickUpLoc;
    }

    public void setDropLoc(String dropLoc) {
        this.dropLoc = dropLoc;
    }


    public String getEdndtym() {
        return edndtym;
    }

    public void setEdndtym(String edndtym) {
        this.edndtym = edndtym;
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
