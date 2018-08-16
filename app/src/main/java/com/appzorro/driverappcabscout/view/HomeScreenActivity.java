package com.appzorro.driverappcabscout.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.Beans.CustomerRequest;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.SignalRService;
import com.appzorro.driverappcabscout.model.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class HomeScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, LocationListener {
    // private int STORAGE_PERMISSION_CODE = 23;
    private static final int STORAGE_PERMISSION_CODE = 23;
    private static final int[] COLORS = new int[]{R.color.pathcolor, R.color.colorPrimaryDark, R.color.colorAccent, R.color.colorAccent, R.color.primary_dark_material_light};
    Toolbar toolbar;
    Dialog Canceldialog;
    MyTimer timer1;
    String userChoosenTask;
    LocationManager locationManager;
    Activity activity = this;
    double currentlat, currentlang;
    Location mLastLocation;
    TextView timer;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView profileimage, img_location;
    Dialog requestdialog, messagedialog;
    Switch myswitch;
    String covertedImage;
    TextView txtstatus, name, phonenumber;
    FloatingActionButton myLocation;
    ArrayList<CustomerRequest> list;
    AlertDialog dialog;
    Dialog requestdialog1;
    RelativeLayout open_dialog;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    Context mContext;
    CircleImageView circleImageView;
    String message = "";
    private static SignalRService mService;
    private boolean mBound = false;
    String driver_status = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Log.d("init", "Called OnCreate");

        initViews();
        //** Comment by deep *** Multiple tym

       /* ModelManager.getInstance().getUserDetailManager().UserDetailManager(activity,
                Operations.getUserDetail(activity, CSPreferences.readString(activity, "customer_id")));
*/
        //End

        checkOnlineOfflineStatus();
        //serviceMethods();
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(HomeScreenActivity.this, Profile_Activity.class);
                startActivity(it);
                finish();
            }
        });
    }


    public void initViews() {
        mContext = this;
        Log.d("init", "Called INIT");
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle("Traala");
        img_location = (ImageView) findViewById(R.id.fb_Location);
        open_dialog = (RelativeLayout) findViewById(R.id.bottom_sheet);
        setSupportActionBar(toolbar);
        checkPermissions();
        initNavigationDrawer();
        // Log.e("tokenn", FirebaseInstanceId.getInstance().getToken());


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        myswitch = (Switch) findViewById(R.id.checkbox1);
        txtstatus = (TextView) findViewById(R.id.txtstatus);
        messagedialog = Utils.createMessageDialog(activity);
        requestdialog1 = new Dialog(this);

        // Customize  My Location Button
        img_location.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                Log.e("on connected", "");
                LatLng coordinate = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()); //Store these lat lng values somewhere. These should be constant.
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                        coordinate, 15);
                mMap.animateCamera(location);


            }
        });

        //myswitch.setChecked(false);
        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    String points = String.valueOf(currentlat) + "," + String.valueOf(currentlang);
                    Log.e("driver current ", "location" + String.valueOf(currentlang));
                    hitRoadAPI(points);
                    driver_status = "1";
                    CSPreferences.putString(activity, Constant.OfflineOrOnline, "false");
                } else {
                    // here we send the  the offline status
                    driver_status = "0";
                    hitAPI(driver_status);


                }
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void onSelectFromGalleryResult(Intent data) {


        if (data != null) {
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                byte[] byteArray = bytes.toByteArray();
                covertedImage = Base64.encodeToString(byteArray, 0);
                circleImageView.setImageBitmap(bm);
                //Log.e("From gallery",covertedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        circleImageView.setImageBitmap(thumbnail);
        byte[] byteArray = bytes.toByteArray();
        covertedImage = Base64.encodeToString(byteArray, 0);
        Log.e("camera images", covertedImage);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);

            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }

    }


    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, STORAGE_PERMISSION_CODE);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Utils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                }
                break;
        }
        if (requestCode == STORAGE_PERMISSION_CODE) {
            Log.d("hello", "hello");
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
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.e("on connected", "");
            initCamera(mLastLocation);
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
        circleImageView = (CircleImageView) navigationView.findViewById(R.id.nav_image);

        View header = navigationView.getHeaderView(0);
        name = (TextView) header.findViewById(R.id.driverName);
        circleImageView = (CircleImageView) header.findViewById(R.id.nav_image);
        phonenumber = (TextView) header.findViewById(R.id.driverContact);
        Log.e("imagedetail", CSPreferences.readString(activity, "user_name") + "\n" + CSPreferences.readString(activity, "profile_pic"));
        name.setText("" + CSPreferences.readString(activity, "user_name"));
        phonenumber.setText("" + CSPreferences.readString(activity, "user_mobile"));
        Picasso.with(this).load(CSPreferences.readString(activity, "profile_pic")).placeholder(R.drawable.ic_icon_profile_pic).error(R.drawable.ic_icon_profile_pic).into(circleImageView);
        Log.d("pic", CSPreferences.readString(activity, "profile_pic"));


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
        navigationView.setItemIconTintList(null);


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
            case R.id.contact:
                Intent intent1 = new Intent(activity, ContactUsActivity.class);
                startActivity(intent1);
                break;
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
                Canceldialog = Utils.logoutDialog(HomeScreenActivity.this);
                Canceldialog.show();
                TextView ok = Canceldialog.findViewById(R.id.txtok);
                TextView cancel = Canceldialog.findViewById(R.id.txtcancel);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intLogout = new Intent(HomeScreenActivity.this, CabCompanyActivity.class);
                        startActivity(intLogout);
                        finish();
                        CSPreferences.clearPref(activity);
                        CSPreferences.putString(activity, "login_status", "false");
                        CSPreferences.putString(activity, "customer_id", "");
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Canceldialog.dismiss();
                    }
                });
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
    }
// here we are geeting the current location of the user and camera move to cureent location of the user.........

    private void initCamera(Location mLocation) {

        if (mLocation != null) {

            Log.e("init camera", "fuction are called");
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
            mMap.setMyLocationEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            CameraPosition position = CameraPosition
                    .builder().target(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()))
                    .zoom(15f)
                    .bearing(0.0f)
                    .tilt(0.0f)
                    .build();
            currentlat = mLocation.getLatitude();
            currentlang = mLocation.getLongitude();
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15.0f);
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
            mMap.animateCamera(zoom);
            String points = String.valueOf(currentlat) + "," + String.valueOf(currentlang);
            //hitRoadAPI(points);

           /* //Added by deep
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(currentlat,currentlang), 10);
            mMap.animateCamera(cameraUpdate);

            //end*/
            MarkerOptions options = new MarkerOptions();
            options.position(new LatLng(currentlat, currentlang));


        } else {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getLastBestStaleLocation();
                }
            }, 1000);
        }


        // dialog = new SpotsDialog(activity);
        //dialog.show();

        //hitAPI("1");

        mMap.getUiSettings().setZoomControlsEnabled(true);

    }


    //hit API FOR ONLINE/OFFLINE DRIVER

    public void hitAPI(String status) {
        showOrDismissDialog();
        ModelManager.getInstance().getOnlineOfflineManager().OnlineOfflineManager(activity, Config.onlineoffilne, Operations.sendDriverstatus(
                activity, CSPreferences.readString(activity, "customer_id"), status, String.valueOf(currentlat), String.valueOf(currentlang), Utils.getTimeStamp()
        ));
    }

    private void showOrDismissDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        else {
            dialog = new SpotsDialog(activity);
            dialog.show();
        }
    }


    public void hitRoadAPI(String points) {
        //showOrDismissDialog();
        ModelManager.getInstance().getNearestRoadManager().NearestRoadManager(HomeScreenActivity.this,
                Operations.nearestRoadlatlng(HomeScreenActivity.this, points), 1, false);

    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constant.ACCEPTBYDRIVER:

                dialog.dismiss();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df3 = new SimpleDateFormat("HH:mm:ss aa");
                String formattedDate3 = df3.format(c.getTime());
                Log.d("CurrentTime_", formattedDate3);
                CSPreferences.putString(HomeScreenActivity.this, "currenTimeDriver", formattedDate3);
                // String message = event.getValue();
                Intent intent = new Intent(activity, PathMapActivity.class);

                startActivity(intent);


                break;
            case Constant.DRIVERSTATUS:

                dialog.dismiss();
                String drivermessage = event.getValue();

                if (drivermessage.equals("online")) {

                    txtstatus.setText("Online");
                    myswitch.setChecked(true);
                    //  Toast.makeText(activity, "your status is online", Toast.LENGTH_SHORT).show();
                    Utils.makeSnackBar(mContext, txtstatus, "Your status is Online");

                } else {
                    myswitch.setChecked(false);
                    txtstatus.setText("Offline");
                    Utils.makeSnackBar(mContext, txtstatus, "Your status is Offline");

                }
                break;
            case Constant.MY_LOCATION:

                currentlat = ModelManager.getInstance().getNearestRoadManager().driverlat;
                currentlang = ModelManager.getInstance().getNearestRoadManager().driverlongititude;

                hitAPI(driver_status);

                break;
            case Constant.MY_LOCATION_FAILURE:

                hitAPI(driver_status);

                break;
            case Constant.ERROR_DETAIL:
                dialog.dismiss();
                myswitch.setChecked(false);
                txtstatus.setText("Offline");
                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Error")
                        .setContentText("Your Network connection is very slow please try again")
                        .show();
                break;
            case Constant.SERVER_ERROR:
                dialog.dismiss();
                myswitch.setChecked(false);
                txtstatus.setText("Offline");
                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Error")
                        .setContentText("Your Network connection is very slow please try again")
                        .show();
                break;
            case Constant.USERDETAILSTAUS:

                name.setText("" + CSPreferences.readString(activity, "user_name"));
                phonenumber.setText("" + CSPreferences.readString(activity, "user_mobile"));
                String profile = CSPreferences.readString(activity, "profile_pic");
                Picasso.with(this)
                        .load(CSPreferences.readString(activity, "profile_pic")).placeholder(R.drawable.ic_icon_pic)
                        .into(circleImageView);
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
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
            } else {
                bestresult = networklocation;
                Log.e("result", "network location ---- " + bestresult.getLatitude());
                Config.currentLAT = bestresult;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
            }
        } else if (gpslocation != null) {
            bestresult = gpslocation;
            Log.e("result", "gps location only found---- " + bestresult.getLatitude());
            Config.currentLAT = bestresult;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        } else if (networklocation != null) {
            bestresult = networklocation;
            Log.e("result", "network location only found--- " + bestresult.getLatitude());
            Config.currentLAT = bestresult;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
            Log.e("", bestresult.toString());
        }
        return bestresult;
    }

    // here we are getting the cureent location of the user... this method return you our current locaton after every 10 second

    public class MyTimer extends CountDownTimer {


        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            long millis = l;
            String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            timer.setText("" + hms);
            Log.d("currentTime", l + "");

        }

        @Override
        public void onFinish() {

            timer.setText("00:00");
            //  requestdialog.dismiss();
            if (requestdialog != null) {
                requestdialog.dismiss();
                requestdialog = null;
            }

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //   if (!isMyServiceRunning(mService.getClass()))
        //      stopService(serviceIntent);
        if (requestdialog != null) {
            requestdialog.dismiss();
            requestdialog = null;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        minimizeApp();
    }

    public void minimizeApp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finishAffinity();
    }

    private void checkOnlineOfflineStatus() {
        Log.d("init", "Called ONLINEOFFLine");

        String status = CSPreferences.readString(activity, Constant.OfflineOrOnline);
        if (status.equals("true")) {
            hitAPI("0");
        } else {
            myswitch.setChecked(true);
            txtstatus.setText("Online");
        }
    }

}