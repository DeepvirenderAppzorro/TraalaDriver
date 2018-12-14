package com.appzorro.driverappcabscout.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.appzorro.driverappcabscout.view.Fragments.Completed_Trip;
import com.appzorro.driverappcabscout.view.Fragments.Pending_Trip;

/**
 * Created by vijay on 2/11/18.
 */

public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                Pending_Trip tab1 = new Pending_Trip();
                return tab1;
            case 1:
                Completed_Trip tab2 = new Completed_Trip();
                return tab2;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}