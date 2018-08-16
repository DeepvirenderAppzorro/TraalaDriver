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
import com.appzorro.driverappcabscout.model.SignalRService;
import com.appzorro.driverappcabscout.model.Utils;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.iwgang.countdownview.CountdownView;
import dmax.dialog.SpotsDialog;

public class NotificatonDialog extends AppCompatActivity implements LocationListener, RoutingListener, CountdownView.OnCountdownEndListener {
    ArrayList<CustomerRequest> list;
    CountdownView timer;
    int secondsLeft = 0;
    private TextView tvPrice;
    Activity activity;
    AlertDialog dialog;
    LocationManager locationManager;
    double currentlat, currentlang;
    RelativeLayout open_dialog;
    public static boolean isActive = false;
    public boolean isReject = false;
    String VehicleType;
    private TextView distance;
    private LatLng startLatlng, endLatlng;
    static long timeInput = 10000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_activity);
        isActive = true;
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
        distance = (TextView) findViewById(R.id.txtdistance);
        final TextView accept = (TextView) findViewById(R.id.btnaccept);
        TextView destination = (TextView) findViewById(R.id.txtsource);
        TextView reject = (TextView) findViewById(R.id.btReject);
        tvPrice = (TextView) findViewById(R.id.txtprice);
        timer = (CountdownView) findViewById(R.id.count_down);
        timer.setOnCountdownEndListener(this);

        if (customerRequest != null) {
            String profileimage = customerRequest.getProfilepic();
            Log.e("image ", profileimage);
            VehicleType=customerRequest.getVehicleType();
            CSPreferences.putString(NotificatonDialog.this,Constant.VEHICLETYPE,VehicleType);
            TextView pickuloaction = (TextView) findViewById(R.id.txtpickaddress);
            requestid.setText("" + customerRequest.getRequestid());

            float aDouble = Float.parseFloat(customerRequest.getPrice());
            tvPrice.setText(String.format("%.2f", aDouble));

            customername.setText("" + customerRequest.getName());
            pickuloaction.setText("" + Utils.getCompleteAddressString(activity, Double.parseDouble(customerRequest.getSourcelat()),
                    Double.parseDouble(customerRequest.getSourcelng())));
            destination.setText("" + Utils.getCompleteAddressString(activity, Double.parseDouble(customerRequest.getDroplat()),
                    Double.parseDouble(customerRequest.getDroplng())));
            String pickupaddres = Utils.getCompleteAddressString(activity, Double.parseDouble(customerRequest.getSourcelat()),
                    Double.parseDouble(customerRequest.getSourcelng()));

            startLatlng = new LatLng(Double.parseDouble(customerRequest.getSourcelat()),
                    Double.parseDouble(customerRequest.getSourcelng()));
            endLatlng = new LatLng(Double.parseDouble(customerRequest.getDroplat()), Double.parseDouble(customerRequest.getDroplng()));
            Log.e("address", "" + pickupaddres);
            Picasso.with(this).load(profileimage).placeholder(R.drawable.ic_icon_pic).into(customerimage);
            // double distance_find = distance(Double.parseDouble(customerRequest.getSourcelat()), Double.parseDouble(customerRequest.getSourcelng()), Double.parseDouble(customerRequest.getDroplat()), Double.parseDouble(customerRequest.getDroplng()));

            //  String currentString = String.valueOf(distance_find);

            //  Log.e("eror", currentString);

            //  String dis = currentString.substring(0, 1);
            //  Log.e("eror", currentString);
            // CSPreferences.putString(NotificatonDialog.this, "FullDisatance", dis);
            // distance.setText(dis + "km");

            Routing routing = new Routing.Builder()
                    .travelMode(Routing.TravelMode.DRIVING)
                    .withListener(this)
                    .waypoints(startLatlng, endLatlng)
                    .build();
            routing.execute();

        } else {

            Toast.makeText(this, "No data available", Toast.LENGTH_LONG).show();
        }


        final CustomerRequest finalCustomerRequest = customerRequest;
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isActive = false;
                Utils.clearNotifications(getApplicationContext());
                dialog = new SpotsDialog(activity);
                dialog.show();
                //.cancel();
                timer.stop();
                // {latitude=0.0, id=38, longitude=0.0, ride_request_id=9, message=157 has accepted the request, noti_type=driver_request}
                JSONObject jsonObject = JsonRequestIR.driverRequest(getApplicationContext(), currentlat, currentlang);
                Utils.sendMessage(activity, jsonObject);


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
                isReject = true;
                sendToNextDriver();
                Utils.clearNotifications(getApplicationContext());
                startActivity(new Intent(NotificatonDialog.this, HomeScreenActivity.class));
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


    //End
    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constant.CANCEL_RIDEFCM:
                timer.stop();
                finish();
                // startActivity(new Intent( activity,HomeScreenActivity.class));

                break;
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

    @Override
    public void onRoutingFailure(RouteException e) {
        open_dialog.setVisibility(View.VISIBLE);
        startTimer();
    }

    private void startTimer() {
        //  timer.setText("00:00");
        //timer1 = new MyTimer(30000, 1000);
        //timer1.start();

        timer.start(10000);

    }

    @Override
    public void onRoutingStart() {


    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
        open_dialog.setVisibility(View.VISIBLE);
        double distnace1 = arrayList.get(i).getDistanceValue() / 1000;
        double distanceKm = Utils.milesTokm(distnace1);
        distance.setText(String.format("%.2f km", distanceKm));
        CSPreferences.putString(NotificatonDialog.this, "FullDisatance", String.valueOf(distanceKm));
        startTimer();
    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public void onEnd(CountdownView cv) {
        if (!isReject && isActive)
            sendToNextDriver();
        Utils.clearNotifications(getApplicationContext());
        finish();
    }


    public class MyTimer extends CountDownTimer {


        private MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            long millis = l;
            if (Math.round((float) l / 1000.0f) != secondsLeft) {
                secondsLeft = Math.round((float) l / 1000.0f);
            }
            Log.i("test", "ms=" + l + " till finished=" + secondsLeft);

            //  String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            // timer.setText("" + hms);


        }

        @Override
        public void onFinish() {

            //timer.setText("00:00");
            if (!isReject && isActive)
                sendToNextDriver();
            Utils.clearNotifications(getApplicationContext());
            finish();            //  requestdialog.dismiss();
        }
    }

    // here we are getting the cureent location of the user... this method return you our current locaton after every 10 second
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActive = false;
    }


    public void sendToNextDriver() {
        if (SignalRService.arraylist_split.size() > 0) {
            SignalRService.arraylist_split.remove(0);
        }

        if (SignalRService.arraylist_split.size() > 0) {
            if (SignalRService.arraylist_split.size() == 2) {
                String driver_Array = SignalRService.arraylist_split.get(0) + "," + SignalRService.arraylist_split.get(1);
                JSONObject jsonObject = JsonRequestIR.jsonRequestForSingleDriver(activity, driver_Array);
                Utils.sendMessage(NotificatonDialog.this, jsonObject);
            }
            if (SignalRService.arraylist_split.size() == 1) {
                String driver_Array = SignalRService.arraylist_split.get(0);
                JSONObject jsonObject = JsonRequestIR.jsonRequestForSingleDriver(activity, driver_Array);
                Utils.sendMessage(NotificatonDialog.this, jsonObject);
            }

        } else if (SignalRService.arraylist_split.size() == 0) {
            JSONObject jsonObject = JsonRequestIR.cancelCustomerDialog(activity);
            Utils.sendMessage(NotificatonDialog.this, jsonObject);

        }
    }
    // here we are getting the cureent location of the user... this method return you our current locaton after every 10 second


}

