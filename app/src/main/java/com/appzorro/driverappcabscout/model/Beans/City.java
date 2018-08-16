package com.appzorro.driverappcabscout.model.Beans;

/**
 * Created by vijay on 19/7/18.
 */

public class City {
    private int cityID;
    private String country_id;
    public int stateId;
    private String cityName;

    public City(int cityID, int stateId, String cityName) {
        this.cityID = cityID;
        this.country_id = country_id;
        this.stateId = stateId;
        this.cityName = cityName;
    }

    public int getCityID() {
        return cityID;
    }

    public String getCountryId() {
        return country_id;
    }

    public int getStateId() {
        return stateId;
    }

    public String getCityName() {
        return cityName;
    }

    @Override
    public String toString() {
        return cityName;
    }

}
