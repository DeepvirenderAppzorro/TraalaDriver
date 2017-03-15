package com.appzorro.driverappcabscout.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.controller.Tripsmanager;
import com.appzorro.driverappcabscout.model.AllAdapter.CompletedtrpsAdapter;
import com.appzorro.driverappcabscout.model.Beans.CompletedRideBean;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Date_Time;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;

import static com.appzorro.driverappcabscout.model.Date_Time.date;

public class RideHistory extends AppCompatActivity implements RecyclerView.OnItemTouchListener{
    MenuItem shareditem;
    Context context;
    String searchdate;
    Toolbar toolbar;
    TextView txtsearchdate,txttotalearning,txttotaltrips,txtnotrips;
    ListView recyclerView;
    android.app.AlertDialog dialog;
    ArrayList<CompletedRideBean>tripshistory;
    int totalearning =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);
        context=this;
        initviews();
        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context,HistoryDeatil.class);
                intent.putExtra("postion",String.valueOf(i));
                startActivity(intent);
            }
        });

    }
    public void initviews(){
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Ride History");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        txttotaltrips =(TextView)findViewById(R.id.txttotaltrips);
        txtsearchdate =(TextView)findViewById(R.id.datetime);
        txttotalearning =(TextView)findViewById(R.id.dayearning);
        recyclerView =(ListView) findViewById(R.id.completedridelist);

        txtnotrips =(TextView)findViewById(R.id.notrips);
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(context);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(context);
    }

    @Override
    protected void onResume() {
        super.onResume();
        totalearning=0;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = ""+mdformat.format(calendar.getTime());
        Log.e("date ",strDate);
      /*  ModelManager.getInstance().getTripsmanager().Tripsmanager(context, Operations.tripsCompleted(context,
                CSPreferences.readString(context,"customer_id"),currentDate));*/
        dialog = new SpotsDialog(context);
        dialog.show();
        txtsearchdate.setText(strDate);
        ModelManager.getInstance().getTripsmanager().Tripsmanager(context, Operations.tripsCompleted(context,
                CSPreferences.readString(context,"customer_id"),strDate));


    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constant.DATE:
               searchdate = date;
                Log.e("searchdate",searchdate);

                break;
            case Constant.TRIPSHOSTORY:
                dialog.dismiss();
                txtnotrips.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                tripshistory = new ArrayList<>();
                tripshistory = Tripsmanager.trips;
                totalearning=0;

                for(int i=0;i<tripshistory.size();i++) {

                    CompletedRideBean completedRideBean = tripshistory.get(i);
                    totalearning = totalearning+Integer.parseInt(completedRideBean.getTotalamount());

                }
                txttotalearning.setText(String.valueOf(totalearning));
                txttotaltrips.setText(String.valueOf(tripshistory.size()));
                CompletedtrpsAdapter completedtrpsAdapter = new CompletedtrpsAdapter(context, Tripsmanager.trips);
                recyclerView.setAdapter(completedtrpsAdapter);
                break;
            case Constant.BLANKLIST:
                dialog.dismiss();
                txtnotrips.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                txttotalearning.setText("00:00");
                txttotaltrips.setText("0");
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calender, menu);
        shareditem = menu.findItem(R.id.tick);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.tick:
                shareditem.setVisible(false);
                dialog = new SpotsDialog(context);
                dialog.show();
                ModelManager.getInstance().getTripsmanager().Tripsmanager(context, Operations.tripsCompleted(context,
                        "4",searchdate.replaceAll(" ","")));


                break;
            case R.id.calender:
                shareditem.setVisible(true);
                Date_Time date_time = new Date_Time(context);
                date_time.dateDialog();
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
