package com.appzorro.driverappcabscout.model.Beans;

/**
 * Created by vijay on 10/7/18.
 */

public class GetCityBean {
    String locationId,locationName;

    public GetCityBean(String locationId,String locationName) {
        this.locationId = locationId;
        this.locationName=locationName;
    }

    public String getLocationName() {

        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationId() {

        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
}
