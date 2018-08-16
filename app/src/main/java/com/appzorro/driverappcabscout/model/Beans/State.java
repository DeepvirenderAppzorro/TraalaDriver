package com.appzorro.driverappcabscout.model.Beans;

/**
 * Created by vijay on 19/7/18.
 */

public class State {
    private int stateID;
    public int country_id;
    private String stateName;

    public State() {

    }

    public State(int stateID, int country_id, String stateName) {
        this.stateID = stateID;
        this.country_id = country_id;
        this.stateName = stateName;
    }

    public int getStateID() {
        return stateID;
    }

    public int getCountryId() {
        return country_id;
    }

    public String getStateName() {
        return stateName;
    }

    @Override
    public String toString() {
        return stateName;
    }

}
