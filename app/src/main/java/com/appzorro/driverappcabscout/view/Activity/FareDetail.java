package com.appzorro.driverappcabscout.view.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.model.AllAdapter.FaredetailAdpter;

import java.util.ArrayList;

public class FareDetail extends AppCompatActivity {
    Toolbar toolbar;

    Context context;
    ArrayList<String>namelist;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_detail);
        initviews();
    }

    public void initviews(){
        context =this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("FARE DETAILS");
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //farelist.setLayoutManager(new LinearLayoutManager(context));

        namelist = new ArrayList<>();
        namelist.add("UBER");
        namelist.add("AUSTIN EXPRESS");
        namelist.add("CABSCOUT");
        namelist.add("TAPZORRO");
        namelist.add("WAY CAB");
        namelist.add("YELLOW CAB");

        recyclerView =(RecyclerView) findViewById(R.id.listview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));


        FaredetailAdpter faredetailAdpter = new FaredetailAdpter(context,namelist);
        recyclerView.setAdapter(faredetailAdpter);

    }
    @Override
    protected void onResume() {
        super.onResume();

       // ModelManager.getInstance().getFareDetailManager().

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
