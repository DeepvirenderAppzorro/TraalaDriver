package com.appzorro.driverappcabscout.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.CustomerReQuestManager;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.Beans.CustomerRequest;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.JsonRequestIR;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class NotificatonDialog extends AppCompatActivity implements LocationListener {
    ArrayList<CustomerRequest> list;
    TextView timer;
    MyTimer timer1;
    Activity activity ;
    AlertDialog dialog;
    LocationManager locationManager;
    double currentlat, currentlang;
    RelativeLayout open_dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_activity);

        activity = this;
        this.setFinishOnTouchOutside(false);
        open_dialog = (RelativeLayout) findViewById(R.id.bottom_sheet);

        getLastBestStaleLocation();


        list = new ArrayList<>();
        list = CustomerReQuestManager.requestlis;
        CustomerRequest customerRequest = null;
        if (list != null) {
            customerRequest = list.get(0);

        }

        ImageView customerimage = (ImageView) findViewById(R.id.imagevieww);
        TextView customername = (TextView) findViewById(R.id.txtdrivername);
        final TextView requestid = (TextView) findViewById(R.id.txtrequestid);
        TextView distance = (TextView) findViewById(R.id.txtdistance);
        final TextView accept = (TextView) findViewById(R.id.btnaccept);
        TextView destination = (TextView) findViewById(R.id.txtsource);
        TextView reject = (TextView) findViewById(R.id.btReject);
        timer = (TextView) findViewById(R.id.txttime);

        timer.setText("00:00");
        timer1 = new MyTimer(15000, 1000);
        timer1.start();

        if (customerRequest != null) {
            String profileimage = customerRequest.getProfilepic();
            Log.e("image ", profileimage);
            TextView pickuloaction = (TextView) findViewById(R.id.txtpickaddress);
            requestid.setText("" + customerRequest.getRequestid());
            customername.setText("" + customerRequest.getName());
            pickuloaction.setText("" + Utils.getCompleteAddressString(activity, Double.parseDouble(customerRequest.getSourcelat()),
                    Double.parseDouble(customerRequest.getSourcelng())));
            destination.setText("" + Utils.getCompleteAddressString(activity, Double.parseDouble(customerRequest.getDroplat()),
                    Double.parseDouble(customerRequest.getDroplng())));
            String pickupaddres = Utils.getCompleteAddressString(activity, Double.parseDouble(customerRequest.getSourcelat()),
                    Double.parseDouble(customerRequest.getSourcelng()));
            String destination_s = Utils.getCompleteAddressString(activity, Double.parseDouble(customerRequest.getDroplat()),
                    Double.parseDouble(customerRequest.getDroplng()));

            Log.e("address", "" + pickupaddres);
            Picasso.with(this).load(profileimage).placeholder(R.drawable.ic_icon_pic).into(customerimage);
            double distance_find = distance(Double.parseDouble(customerRequest.getSourcelat()), Double.parseDouble(customerRequest.getSourcelng()), Double.parseDouble(customerRequest.getDroplat()), Double.parseDouble(customerRequest.getDroplng()));

            String currentString = String.valueOf(distance_find);
            Log.e("eror",currentString);
        /*    String separated []= currentString.split(".");
            String s = separated[0]; // this will contain "Fruit"
            String s2 = separated[1];*/
            distance.setText(currentString + "Km");
        } else {

            Toast.makeText(this, "No data available", Toast.LENGTH_LONG).show();
        }

        final CustomerRequest finalCustomerRequest = customerRequest;
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.clearNotifications(getApplicationContext());
                dialog = new SpotsDialog(activity);
                dialog.show();
                timer1.cancel();
               // {latitude=0.0, id=38, longitude=0.0, ride_request_id=9, message=157 has accepted the request, noti_type=driver_request}
                JSONObject jsonObject = JsonRequestIR.driverRequest(getApplicationContext(),currentlat,currentlang);
                Utils.sendMessage(activity,jsonObject);


                assert finalCustomerRequest != null;
                ModelManager.getInstance().getAcceptCustomerRequest().AcceptCustomerRequest(activity, Operations.acceptByDriver(activity,
                        CSPreferences.readString(activity, "customer_id"), finalCustomerRequest.getRequestid(),
                        String.valueOf(currentlat), String.valueOf(currentlang)));
                open_dialog.setVisibility(View.GONE);

            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.clearNotifications(getApplicationContext());
                startActivity(new Intent(NotificatonDialog.this,HomeScreenActivity.class));
                finish();
            }

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }


    //Find distance between 2 locations

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    //End
    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constant.CUSTOMERREQUEST:

        }
    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            currentlat = location.getLatitude();
            currentlang = location.getLongitude();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public Location getLastBestStaleLocation() {

        Location bestresult = null;
        Log.e("call", "getbesccctlocation");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return null;

        }
        Location gpslocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location networklocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (gpslocation != null && networklocation != null) {
            if (gpslocation.getTime() > networklocation.getTime()) {
                bestresult = gpslocation;
                Log.e("result", "both location are found---- " + bestresult.getLatitude());
                Config.currentLAT = bestresult;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, (LocationListener) this);
            } else {
                bestresult = networklocation;
                Log.e("result", "network location ---- " + bestresult.getLatitude());
                Config.currentLAT = bestresult;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, NotificatonDialog.this);
            }
        } else if (gpslocation != null) {
            bestresult = gpslocation;
            Log.e("result", "gps location only found---- " + bestresult.getLatitude());
            Config.currentLAT = bestresult;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, (LocationListener) this);
        } else if (networklocation != null) {
            bestresult = networklocation;
            Log.e("result", "network location only found--- " + bestresult.getLatitude());
            Config.currentLAT = bestresult;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, (LocationListener) this);
            Log.e("", bestresult.toString());
        }
        return bestresult;
    }

    public class MyTimer extends CountDownTimer {


        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            long millis = l;
            String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            timer.setText("" + hms);


        }

        @Override
        public void onFinish() {

            timer.setText("00:00");
            Utils.clearNotifications(getApplicationContext());
            finish();            //  requestdialog.dismiss();
        }

    }

    // here we are getting the cureent location of the user... this method return you our current locaton after every 10 second

}

