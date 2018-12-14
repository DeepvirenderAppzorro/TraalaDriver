package com.appzorro.driverappcabscout.view.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.appzorro.driverappcabscout.R;

import butterknife.ButterKnife;

public class Trip_info_Detail extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_info__detail);
        ButterKnife.bind(this);
    }

}
