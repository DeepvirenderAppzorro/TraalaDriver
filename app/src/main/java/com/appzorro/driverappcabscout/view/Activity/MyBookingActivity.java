package com.appzorro.driverappcabscout.view.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.LaterBookingManager;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.AllAdapter.LaterBookingAdapter;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

public class MyBookingActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView bookinglist;
    Context context;
    android.app.AlertDialog dialog;
    TextView sehedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking);
        context = this;
        initView();
    }
    public void initView(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Upcoming Rides");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        bookinglist = (RecyclerView)findViewById(R.id.bookinglist);
        bookinglist.setLayoutManager(new LinearLayoutManager(context));
        sehedule=(TextView)findViewById(R.id.txtsehedule);


    }
    @Override
    protected void onResume() {
        super.onResume();
        dialog = new SpotsDialog(context);
        dialog.show();
     /*   ModelManager.getInstance().getLaterBookingManager().LaterBookingManager(context, Operations.laterBooking(context,
                CSPreferences.readString(context,"customer_id")));*/
        ModelManager.getInstance().getLaterBookingManager().LaterBookingManager(context, Operations.laterBooking(context, CSPreferences.readString(context,"customer_id")));

    }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);

    }
    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);

    }
    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {

            case Constant.LATERBOOKING:

                dialog.dismiss();
                sehedule.setVisibility(View.GONE);

                LaterBookingAdapter laterBookingAdapter = new LaterBookingAdapter(context,LaterBookingManager.bookinglis);
                bookinglist.setAdapter(laterBookingAdapter);
                break;
            case Constant.SERVER_ERROR:
                dialog.dismiss();
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Your Network connection is very slow please try again")
                        .show();
                break;
            case Constant.NOBOOKINGYET:
                dialog.dismiss();
                sehedule.setVisibility(View.VISIBLE);
                bookinglist.setVisibility(View.GONE);
                break;

        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
