package com.appzorro.driverappcabscout.model.Beans;

/**
 * Created by vijay on 20/2/17.
 */

public class CompletedRideBean {
    String requestid,pickuplat,pickuplng,droplat,droplng,startdate, edndtime,totalamount,name,profilepic,mobile,start_tym,end_tym,id,curPage,lastPage,pagesize;

    public String getEnd_tym() {
        return end_tym;
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
        return start_tym;
    }

    public void setStart_tym(String start_tym) {
        this.start_tym = start_tym;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CompletedRideBean(String requestid, String pickuplat, String pickuplng, String droplat, String droplng, String startdate,String start_tym,
                             String edndtime,String end_tym, String totalamount, String name, String profilepic, String mobile) {
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
