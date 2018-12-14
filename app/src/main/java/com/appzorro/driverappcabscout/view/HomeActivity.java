package com.appzorro.driverappcabscout.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.appzorro.driverappcabscout.AppController;
import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;
import com.appzorro.driverappcabscout.view.Activity.BaseActivity;
import com.appzorro.driverappcabscout.view.Activity.CabCompanyActivity;
import com.appzorro.driverappcabscout.view.Activity.ContactUsActivity;
import com.appzorro.driverappcabscout.view.Activity.FareDetail;
import com.appzorro.driverappcabscout.view.Activity.HelpActivity;
import com.appzorro.driverappcabscout.view.Activity.HomeActivity.model.DriverStatusResponse;
import com.appzorro.driverappcabscout.view.Activity.HomeActivity.presenter.ProfileActivityPresenter;
import com.appzorro.driverappcabscout.view.Activity.HomeActivity.presenter.ProfileActivityPresenterHandler;
import com.appzorro.driverappcabscout.view.Activity.HomeActivity.view.ProfileActivityView;
import com.appzorro.driverappcabscout.view.Activity.MapStyleManager;
import com.appzorro.driverappcabscout.view.Activity.MyEarning;
import com.appzorro.driverappcabscout.view.Activity.NotificatonDialog;
import com.appzorro.driverappcabscout.view.Activity.Profile_Activity;
import com.appzorro.driverappcabscout.view.Activity.ReviewActivity;
import com.appzorro.driverappcabscout.view.Activity.RideHistory;
import com.appzorro.driverappcabscout.view.Activity.loginActivity.model.ProfileResponse;
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

import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ProfileActivityView {
    private static final int STORAGE_PERMISSION_CODE = 23;
    Toolbar toolbar;
    Dialog Canceldialog;
    String userChoosenTask;
    Dialog ErrorDialog;
    Location getmLastLocation;
    LocationManager locationManager;
    Activity activity = this;
    double currentlat, currentlang;
    Location mLastLocation;
    @BindView(R.id.fb_Location)
    ImageView fbLocation;
    @BindView(R.id.checkbox1)
    Switch myswitch;
    String covertedImage;
    TextView txtstatus, name, phonenumber;
    Context mContext;
    CircleImageView circleImageView;
    String driver_status = "0";
    ProfileActivityPresenterHandler handler;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        awakeScreen();
        setContentView(R.layout.activity_homepage);
        ButterKnife.bind(this);
        initViews();
        String id = CSPreferences.readString(mContext, "customer_id");
        handler.getProfile(mContext, id);
        //***SwitchChange
        setSwicthChange();
        //***End
        checkOnlineOfflineStatus();

    }


    public void initViews() {
        mContext = this;
        currentlat = 30.7097499;
        currentlang = 76.6934968;
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle("Traala");
        setSupportActionBar(toolbar);
        checkPermissions();
        initNavigationDrawer();
        handler = new ProfileActivityPresenter(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        txtstatus = (TextView) findViewById(R.id.txtstatus);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void switchColor(boolean checked) {
        String white = "#48b02c";
        int whiteInt = Color.parseColor(white);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            myswitch.getThumbDrawable().setColorFilter(checked ? whiteInt : Color.WHITE, PorterDuff.Mode.MULTIPLY);
            myswitch.getTrackDrawable().setColorFilter(!checked ? Color.GREEN : Color.WHITE, PorterDuff.Mode.MULTIPLY);
        }
    }

    private void switchChange(boolean checked) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            myswitch.getThumbDrawable().setColorFilter(checked ? Color.WHITE : Color.WHITE, PorterDuff.Mode.MULTIPLY);
            myswitch.getTrackDrawable().setColorFilter(!checked ? Color.WHITE : Color.WHITE, PorterDuff.Mode.MULTIPLY);
        }
    }

    private void setSwicthChange() {
        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myswitch.setChecked(false);
                    driver_status = "1";
                    AppController.chkStatus = true;
                    AppController.startTimer(activity, String.valueOf(currentlat), String.valueOf(currentlang), driver_status);
                    String points = String.valueOf(currentlat) + "," + String.valueOf(currentlang);
                    Log.e("driver current ", "location" + String.valueOf(currentlang));
                    hitRoadAPI(points);
                    handler.getDriverStatus(mContext, CSPreferences.readString(activity, "customer_id"), driver_status, String.valueOf(currentlat), String.valueOf(currentlang), Utils.getTimeStamp(), CSPreferences.readString(mContext, Constant.SOCKET_ID));
                } else {
                    // here we send the  the offline status
                    driver_status = "0";
                    AppController.chkStatus = false;
                    AppController.stopTimer();
                    Utils.switchChange(true,myswitch);
                    handler.getDriverStatus(mContext, CSPreferences.readString(activity, "customer_id"), driver_status, String.valueOf(currentlat), String.valueOf(currentlang), Utils.getTimeStamp(), CSPreferences.readString(mContext, Constant.SOCKET_ID));

                }
            }
        });
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

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
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

    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
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
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(HomeActivity.this, Profile_Activity.class);
                startActivity(it);
                finish();
            }
        });
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
        // drawer.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {

            case R.id.settings:
                Intent intent = new Intent(activity, Profile_Activity.class);
                startActivity(intent);
                break;
            case R.id.contact:
                Intent intent1 = new Intent(activity, ContactUsActivity.class);
                startActivity(intent1);
                break;
            case R.id.rideHistory:
                Intent intHistory = new Intent(activity, RideHistory.class);
                startActivity(intHistory);
                break;
            case R.id.review:
                Intent reviewintent = new Intent(activity, ReviewActivity.class);
                startActivity(reviewintent);
                break;
            case R.id.fareDetail:
                Intent faredetial = new Intent(this, FareDetail.class);
                startActivity(faredetial);
                break;
            case R.id.help:
                Intent helpactivity = new Intent(activity, HelpActivity.class);
                startActivity(helpactivity);
                break;

            case R.id.earning:
                Intent earning = new Intent(activity, MyEarning.class);
                startActivity(earning);
                break;
            case R.id.logout:
                Canceldialog = Utils.logoutDialog(HomeActivity.this);
                Canceldialog.show();
                final TextView ok = Canceldialog.findViewById(R.id.txtok);
                final TextView cancel = Canceldialog.findViewById(R.id.txtcancel);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ok.setBackgroundColor(Color.parseColor("#0083db"));
                        Utils.sendMessageIo(mContext, "socketDisconnect", CSPreferences.readString(mContext, Constant.SOCKET_ID));
                        Intent intLogout = new Intent(HomeActivity.this, CabCompanyActivity.class);
                        intLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intLogout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intLogout);
                        finish();
                        CSPreferences.clearPref(activity);
                        CSPreferences.putString(activity, "login_status", "false");
                        CSPreferences.putString(activity, "customer_id", "");
                        CSPreferences.putString(activity, Constant.OfflineOrOnline, "false");

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancel.setBackgroundColor(Color.parseColor("#0083db"));
                        Canceldialog.dismiss();
                    }
                });
                break;

        }
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        getmLastLocation = getLastBestStaleLocation();
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
        if (mLastLocation != null) {
            initCamera(mLastLocation);
        } else {
            if (getmLastLocation != null) {
                initCamera(getmLastLocation);
            }
        }

        Log.d("loccc", getmLastLocation + "LAstLoc");

    }

    @Override
    protected void onResume() {
        super.onResume();
        //startTimer();
        if (AppController.picUpdated) {
            String id = CSPreferences.readString(mContext, "customer_id");
            handler.getProfile(mContext, id);
            AppController.picUpdated = false;
        }
        Utils.sendMessageIo(mContext, "onlineDriver", CSPreferences.readString(mContext, Constant.SOCKET_ID));

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
            CameraPosition position = CameraPosition.builder().target(new LatLng(mLocation.getLatitude(), mLocation.getLongitude())).zoom(15f).bearing(0.0f).tilt(0.0f).build();
            currentlat = mLocation.getLatitude();
            currentlang = mLocation.getLongitude();
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15.0f);
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
            mMap.animateCamera(zoom);
            MarkerOptions options = new MarkerOptions();
            options.position(new LatLng(currentlat, currentlang));

        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getmLastLocation = getLastBestStaleLocation();

                }
            }, 1000);
            if (getmLastLocation != null) {
                mMap.setMyLocationEnabled(true);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                CameraPosition position = CameraPosition.builder().target(new LatLng(mLocation.getLatitude(), mLocation.getLongitude())).zoom(15f).bearing(0.0f).tilt(0.0f).build();
                currentlat = mLocation.getLatitude();
                currentlang = mLocation.getLongitude();
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15.0f);
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
                mMap.animateCamera(zoom);
            }
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    public void hitRoadAPI(String points) {

        ModelManager.getInstance().getNearestRoadManager().NearestRoadManager(HomeActivity.this,
                Operations.nearestRoadlatlng(HomeActivity.this, points), 1, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constant.NOTIFICATION_DIALOG:
                Intent intent1 = new Intent(activity, NotificatonDialog.class);
                startActivity(intent1);
                break;
            case Constant.MY_LOCATION:

                currentlat = ModelManager.getInstance().getNearestRoadManager().driverlat;
                currentlang = ModelManager.getInstance().getNearestRoadManager().driverlongititude;

                break;
            case Constant.MY_LOCATION_FAILURE:
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        initCamera(location);
        //  location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.sendMessageIo(mContext, "socketDisconnect", CSPreferences.readString(mContext, Constant.SOCKET_ID));

    }

    @Override
    public void onProviderDisabled(String s) {

    }



    //***Get Last Location
    public Location getLastBestStaleLocation() {
        Location bestresult = null;
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
//***End

    @Override
    public void showProgess() {
        //  showProgressDialog();
    }

    @Override
    public void hideProgess() {
        //  hideProgressDialog();
    }


    //***When Server Error
    @Override
    public void showFeedBackMessage(String message) {
        ErrorDialog = Utils.serverError(mContext);
        ErrorDialog.show();
        final TextView ok = ErrorDialog.findViewById(R.id.txtok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok.setBackgroundColor(Color.parseColor("#0083db"));
                ErrorDialog.dismiss();
               // finish();

            }
        });
    }
    //***End

    //***Get User Detail Success
    @Override
    public void profileSuccess(ProfileResponse response) {
        try {
            name.setText(response.getResponse().getName());
            CSPreferences.putString(mContext, Constant.DRIVER_NAME, response.getResponse().getName());
            phonenumber.setText(response.getResponse().getMobile());
            Picasso.with(this).load(Config.baserurl_image + response.getResponse().getProfilePic()).placeholder(R.drawable.ic_icon_profile_pic).error(R.drawable.ic_icon_profile_pic).into(circleImageView);
            Log.d("pic", CSPreferences.readString(activity, "profile_pic"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //***End

    //***Get driver Status Response
    @Override
    public void driverStatus(DriverStatusResponse response) {
        String drivermessage = response.getResponse().getMessage();
        if (drivermessage.equals("online")) {
            Utils.switchColor(true,myswitch);
            myswitch.setChecked(true);
            CSPreferences.putString(activity, Constant.OfflineOrOnline, "true");
            String iddd= CSPreferences.readString(mContext, Constant.SOCKET_ID);
            Utils.sendMessageIo(mContext, "onlineDriver", CSPreferences.readString(mContext, Constant.SOCKET_ID));
            CSPreferences.putString(mContext, Constant.DRIVER_STATUS, "2");
            txtstatus.setText("Online");
            Log.d("chkOnline", "online");

            Utils.makeSnackBarGreen(mContext, txtstatus, "Your status is Online");
        } else {
            CSPreferences.putString(mContext, Constant.DRIVER_STATUS, "5");
            CSPreferences.putString(activity, Constant.OfflineOrOnline, "false");
            Utils.sendMessageIo(mContext, "socketDisconnect", CSPreferences.readString(mContext, Constant.SOCKET_ID));
            myswitch.setChecked(false);
            Utils.switchChange(true,myswitch);
            txtstatus.setText("Offline");
            Utils.makeSnackBar(mContext, txtstatus, "Your status is Offline");

        }
    }
    //***End

    //*** To animate camera on current Location

    @OnClick({R.id.fb_Location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fb_Location:
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
                if (mLastLocation != null) {
                    Log.e("on connected", "");
                    LatLng coordinate = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()); //Store these lat lng values somewhere. These should be constant.
                    CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                            coordinate, 15);
                    mMap.animateCamera(location);

                } else {

                    Utils.makeSnackBar(HomeActivity.this, txtstatus, "Network problem");
                }
                break;
        }
    }
//***End

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
        Log.d("ondest", "destroy called");

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

    //*** To check Driver Status
    private void checkOnlineOfflineStatus() {

        String status = CSPreferences.readString(activity, Constant.OfflineOrOnline);
        if (status.equals("false")) {
            myswitch.setChecked(false);
            //  handler.getDriverStatus(mContext, CSPreferences.readString(activity, "customer_id"), "0", String.valueOf(currentlat), String.valueOf(currentlang), Utils.getTimeStamp(),CSPreferences.readString(mContext,Constant.SOCKET_ID));
        } else if (status.equals("true")) {
            myswitch.setChecked(true);
            Utils.sendMessageIo(mContext, "onlineDriver", CSPreferences.readString(mContext, Constant.SOCKET_ID));
            handler.getDriverStatus(mContext, CSPreferences.readString(activity, "customer_id"), "1", String.valueOf(currentlat), String.valueOf(currentlang), Utils.getTimeStamp(), CSPreferences.readString(mContext, Constant.SOCKET_ID));
        } else if (NotificatonDialog.isReject) {
            handler.getDriverStatus(mContext, CSPreferences.readString(activity, "customer_id"), "1", String.valueOf(currentlat), String.valueOf(currentlang), Utils.getTimeStamp(), CSPreferences.readString(mContext, Constant.SOCKET_ID));
        } else {
            handler.getDriverStatus(mContext, CSPreferences.readString(activity, "customer_id"), "1", String.valueOf(currentlat), String.valueOf(currentlang), Utils.getTimeStamp(), CSPreferences.readString(mContext, Constant.SOCKET_ID));

        }
    }
    private void  awakeScreen()
    {
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        this.getWindow().setAttributes(params);

    }

}
//***End
