package com.appzorro.driverappcabscout.model;

import com.appzorro.driverappcabscout.model.Beans.CountryCode;

import java.util.ArrayList;

public class Event {
    private int key;
    private String value, company_id, cab_alias;
    private ArrayList<CountryCode> data = new ArrayList<CountryCode>(233);

    public Event(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public Event(int key, ArrayList<CountryCode> data) {
        this.key = key;
        this.data = data;
    }

    public Event(int key, String company_id, String cab_alias) {
        this.key = key;
        this.company_id = company_id;
        this.cab_alias = cab_alias;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getCompany_id() {
        return company_id;
    }

    public String getCab_alias() {
        return cab_alias;
    }

    public ArrayList<CountryCode> getCountry() {
        return data;
    }
}