package com.appzorro.driverappcabscout.model.Beans;

/**
 * Created by vijay on 19/7/18.
 */

public class Country   {
    public int countryID;
    public String countryName;


    public Country(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

    public int getCountryID() {
        return countryID;
    }

    public String getCountryName() {
        return countryName;
    }

    @Override
    public String toString() {
        return countryName;
    }


}
