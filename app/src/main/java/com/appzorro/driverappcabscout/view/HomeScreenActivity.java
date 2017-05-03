package com.appzorro.driverappcabscout.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
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
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

public class HomeScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, LocationListener {
    private GoogleMap mMap;

    Toolbar toolbar;
    private GoogleApiClient mGoogleApiClient;
   MyTimer timer1;
    LocationManager locationManager;
    Activity activity = this;
    double currentlat, currentlang;
    Location mLastLocation;
    TextView timer;
    Dialog requestdialog,messagedialog;
    private int STORAGE_PERMISSION_CODE = 23;
    private static final int[] COLORS = new int[]{R.color.pathcolor, R.color.colorPrimaryDark, R.color.colorAccent, R.color.colorAccent, R.color.primary_dark_material_light};
    Switch myswitch;
    TextView txtstatus;
    FloatingActionButton myLocation;
    ArrayList<CustomerRequest> list;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        initViews();
    }

    public void initViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle("CABSCOUT ");
        setSupportActionBar(toolbar);
        requestdialog = Utils.createDialog(this);


        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, STORAGE_PERMISSION_CODE);

        initNavigationDrawer();
       // Log.e("tokenn", FirebaseInstanceId.getInstance().getToken());


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        myswitch = (Switch) findViewById(R.id.checkbox);
        txtstatus = (TextView) findViewById(R.id.txtstatus);
        messagedialog = Utils.createMessageDialog(activity);

        // Customize  My Location Button
        myLocation = (FloatingActionButton) findViewById(R.id.fbMyloc);
        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(HomeScreenActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeScreenActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                Log.e("on connected", "");
                initCamera(mLastLocation);


            }
        });

        myswitch.setChecked(true);
        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    txtstatus.setText("Online");
                    dialog = new SpotsDialog(activity);
                    dialog.show();

                    Log.e("driver current ", "location" + String.valueOf(currentlang));
                    ModelManager.getInstance().getOnlineOfflineManager().OnlineOfflineManager(activity, Operations.sendDriverstatus(
                            activity, CSPreferences.readString(activity, "customer_id"), "1", String.valueOf(currentlat), String.valueOf(currentlang)));

                } else {
                    // here we send the  the offline status
                    txtstatus.setText("Offline");
                    dialog = new SpotsDialog(activity);
                    dialog.show();
                    ModelManager.getInstance().getOnlineOfflineManager().OnlineOfflineManager(activity, Operations.sendDriverstatus(
                            activity, CSPreferences.readString(activity, "customer_id"), "0", "0.0", "0.0"
                    ));
                }
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){


        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        MapStyleManager styleManager = MapStyleManager.attachToMap(this, googleMap);
        styleManager.addStyle(10, R.raw.map_style_retro);

        mMap.setMyLocationEnabled(true);
        // My Current Location icon change location
        // mMap.setPadding(100,800,20,100);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false); // Disable Compass icon

        mMap.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
            @Override
            public void onCameraMoveCanceled() {
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                return false;
            }
        });
    }

    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.driverName);
        ImageView profileimage = (ImageView) header.findViewById(R.id.nav_image);
        TextView phonenumber = (TextView) header.findViewById(R.id.driverContact);
        Log.e("imagedetail", CSPreferences.readString(activity, "user_name") + "\n" + CSPreferences.readString(activity, "profile_pic"));
        name.setText("" + CSPreferences.readString(activity, "user_name"));
        phonenumber.setText("" + CSPreferences.readString(activity, "user_mobile"));
        Picasso.with(this)
                .load(CSPreferences.readString(activity, "profile_pic"))
                .into(profileimage);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };


        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.map:
                Intent intentMap = new Intent(this, HomeScreenActivity.class);
                startActivity(intentMap);
                break;
            case R.id.settings:
                Intent intent = new Intent(activity, Profile_Activity.class);
                startActivity(intent);
                break;
         /*   case R.id.myBooking:
                Intent intent1 = new Intent(activity, MyBookingActivity.class);
                startActivity(intent1);
                break;*/
            case R.id.rideHistory:
                Intent intHistory = new Intent(this, RideHistory.class);
                startActivity(intHistory);
                break;
            case R.id.review:
                Intent reviewintent = new Intent(this, ReviewActivity.class);
                startActivity(reviewintent);
                break;
            case R.id.fareDetail:
                Intent faredetial = new Intent(this, FareDetail.class);
                startActivity(faredetial);
                break;
            case R.id.help:
                Intent helpactivity = new Intent(this, HelpActivity.class);
                startActivity(helpactivity);
                break;

            case R.id.logout:
                dialog = new SpotsDialog(activity);
                dialog.show();
                Intent intLogout = new Intent(this, CabCompanyActivity.class);
                startActivity(intLogout);
                finish();
                CSPreferences.clearPref(activity);
                CSPreferences.putString(activity, "login_status", "false");
                break;

        }
        return true;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.e("on connected", "");
        initCamera(mLastLocation);
    }

    @Override
    protected void onResume() {

        super.onResume();

       /* if (dialog.isShowing()) {
            dialog.dismiss();
        }*/


    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    protected void onStop() {

        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.

        EventBus.getDefault().unregister(this);
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.disconnect();
    }
// here we are geeting the current location of the user and camera move to cureent location of the user.........

    private void initCamera(Location mLocation) {

        if (mLocation != null) {

            Log.e("init camera", "fuction are called");

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            CameraPosition position = CameraPosition
                    .builder().target(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()))
                    .zoom(15f)
                    .bearing(0.0f)
                    .tilt(0.0f)
                    .build();
            currentlat = mLocation.getLatitude();
            currentlang = mLocation.getLongitude();

            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));

            MarkerOptions options = new MarkerOptions();
            options.position(new LatLng(currentlat, currentlang));



        }
        else {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    getLastBestStaleLocation();
                }
            },1000);
        }
        if (myswitch.isChecked()) {

            txtstatus.setText("Online");
            dialog = new SpotsDialog(activity);
            dialog.show();

            ModelManager.getInstance().getOnlineOfflineManager().OnlineOfflineManager(activity, Operations.sendDriverstatus(
                    activity, CSPreferences.readString(activity, "customer_id"), "1", String.valueOf(currentlat), String.valueOf(currentlang)
            ));
        }
        else {

            txtstatus.setText("Offline");
        }



        }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        EventBus.getDefault().register(this);
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event event) {
        switch (event.getKey()) {
            case Constant.CUSTOMERREQUEST:

                list = new ArrayList<>();
                list = CustomerReQuestManager.requestlis;

                for (int i = 0; list.size()>i;i++) {

                    final CustomerRequest customerRequest = list.get(i);
                //    if (customerRequest.getRequestid().equals(Config.rideRequestid)) {
                        requestdialog.show();
                        ImageView customerimage = (ImageView) requestdialog.findViewById(R.id.imagevieww);
                        TextView customername = (TextView) requestdialog.findViewById(R.id.txtdrivername);
                        final TextView requestid = (TextView) requestdialog.findViewById(R.id.txtrequestid);
                        TextView distance = (TextView) requestdialog.findViewById(R.id.txtdistance);
                        final TextView accept = (TextView) requestdialog.findViewById(R.id.btnaccept);
                        TextView reject = (TextView) requestdialog.findViewById(R.id.btReject);
                        timer = (TextView) requestdialog.findViewById(R.id.txttime);

                        timer.setText("00:00");
                        timer1 = new MyTimer(15000, 1000);
                        timer1.start();

                        String profileimage = customerRequest.getProfilepic();
                        Log.e("image ", profileimage);
                        TextView pickuloaction = (TextView) requestdialog.findViewById(R.id.txtpickaddress);
                        requestid.setText("" + customerRequest.getRequestid());
                        customername.setText("" + customerRequest.getName());
                        pickuloaction.setText("" + Utils.getCompleteAddressString(activity, Double.parseDouble(customerRequest.getSourcelat()),
                                Double.parseDouble(customerRequest.getSourcelng())));
                        String pickupaddres = Utils.getCompleteAddressString(activity, Double.parseDouble(customerRequest.getSourcelat()),
                                Double.parseDouble(customerRequest.getSourcelng()));

                        Log.e("address", "" + pickupaddres);

                        Picasso.with(this)
                                .load(profileimage)
                                .into(customerimage);

                        dialog.dismiss();



                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog = new SpotsDialog(activity);
                                dialog.show();
                                timer1.cancel();

                                ModelManager.getInstance().getAcceptCustomerRequest().AcceptCustomerRequest(activity, Operations.acceptByDriver(activity,
                                        CSPreferences.readString(activity, "customer_id"), customerRequest.getRequestid(),
                                        String.valueOf(currentlat), String.valueOf(currentlang)));
                                requestdialog.dismiss();

                            }
                        });
                        reject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                requestdialog.dismiss();
                            }

                        });
                    //}

                }



                break;


            case Constant.ACCEPTBYDRIVER:


                dialog.dismiss();

                // String message = event.getValue();
                Intent intent = new Intent(activity, PathMapActivity.class);

                startActivity(intent);



                break;
            case Constant.DRIVERSTATUS:

                dialog.dismiss();
                String drivermessage = event.getValue();

                if (drivermessage.equals("online")) {

                    txtstatus.setText(drivermessage);

                    Toast.makeText(activity, "your status is online", Toast.LENGTH_SHORT).show();

                } else {

                    txtstatus.setText(drivermessage);
                    Toast.makeText(activity, "your status is offline", Toast.LENGTH_SHORT).show();

                }
                break;
            case Constant.SERVER_ERROR:
                dialog.dismiss();
                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Error")
                        .setContentText("Your Network connection is very slow please try again")
                        .show();
                break;

        }
    }

    @Override
    public void onLocationChanged(Location location) {

        initCamera(location);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
/* here we start the timer when customer make a request and driver get
    the reposne of cusomer request if timer is finish then customer
    request send to other driver */

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
            requestdialog.dismiss();


        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (requestdialog!=null){

            requestdialog.dismiss();
            requestdialog=null;
        }
    }

    // here we are getting the cureent location of the user... this method return you our current locaton after every 10 second

    public Location getLastBestStaleLocation() {

        Location bestresult = null;
        Log.e("call","getbesccctlocation");

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
        Location networklocation= locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (gpslocation != null && networklocation != null)
        {
            if (gpslocation.getTime() > networklocation.getTime())
            {
                bestresult = gpslocation;
                Log.e("result", "both location are found---- "+ bestresult.getLatitude());
                Config.currentLAT = bestresult;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
            }
            else {
                bestresult = networklocation;
                Log.e("result", "network location ---- "+ bestresult.getLatitude());
                Config.currentLAT = bestresult;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
            }
        }
        else if (gpslocation != null)
        {
            bestresult = gpslocation;
            Log.e("result", "gps location only found---- "+ bestresult.getLatitude());
            Config.currentLAT = bestresult;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        } else if (networklocation != null)
        {
            bestresult = networklocation;
            Log.e("result", "network location only found--- "+ bestresult.getLatitude());
            Config.currentLAT = bestresult;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
            Log.e("",bestresult.toString());
        }
        return bestresult;
    }

}
