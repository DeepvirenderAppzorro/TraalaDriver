package com.appzorro.driverappcabscout.view.Activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.appzorro.driverappcabscout.controller.NearestRoadManager.polyLineList;

public class Main5Activity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleApiClient googleApiClient;
    GoogleMap googleMap;
    Location mLastLocation;
    private Polyline blackPolyline, greyPolyLine;
    Double lat, lng;
    private Marker carMarker;
    private Marker stopMarker;
    Polyline polyline_Route;
    LocationManager locationManager;
    private PolylineOptions polylineOptions, blackPolylineOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main5);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private Location getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);


            if (l == null) {
                continue;
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            return null;
        }
        return bestLocation;
    }

    @Override
    protected void onStart() {
        super.onStart();

        googleApiClient.connect();
        EventBus.getDefault().register(Main5Activity.this);
    }

    private void initCamera(Location mLocation) {
        if (mLocation != null) {
            Log.e("init camera", "fuction are called");
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            CameraPosition position = CameraPosition
                    .builder().target(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()))
                    .zoom(14f)
                    .bearing(0.5f)
                    .tilt(0.0f)
                    .build();
            lat = mLocation.getLatitude();
            lng = mLocation.getLongitude();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));

            MarkerOptions options = new MarkerOptions();
            options.position(new LatLng(lat, lng));

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
            googleMap.setMyLocationEnabled(true);
/*
            ModelManager.getInstance().getNearestRoadManager().NearestRoadManager(this, Operations.directionApi(this,
                    lat, lng, 30.7333, 76.7794), 2, true);
*/

        } else {
            Toast.makeText(Main5Activity.this, "There is problem", Toast.LENGTH_LONG).show();
        }


    }

    @SuppressLint("MissingPermission")
    public void getLastBestStaleLocation() {

        Location bestresult = null;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled == false && isNetworkEnabled == false) {
                // no network provider is enabled
            } else {
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            0,
                            1, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        mLastLocation = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (mLastLocation != null) {
                       lat=mLastLocation.getLatitude();
                       lng=mLastLocation.getLongitude();
                            initCamera(mLastLocation);
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    mLastLocation = null;
                    if (mLastLocation == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                0,
                                1, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            mLastLocation = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (mLastLocation != null) {
                                initCamera(mLastLocation);
                            } else {
                                Log.d("Location", "Null");
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enable your GPS !", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        Log.e("on connected", "");
        try{
            Location location=  getLastKnownLocation();
            initCamera(location);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        getLastBestStaleLocation();

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event event) {
        switch (event.getKey()) {

            case Constant.DRAW_POLYLINE:
                Log.d("serviceRestartted", "Yes");
                try{
                    staticPolyLine();
                }catch (Exception ex)
                {
                    // Utils.makeSnackBar(context,swipe_StartTrip,"There is some problem in network");
                    ex.printStackTrace();
                }
                ModelManager.getInstance().getNearestRoadManager().NearestRoadManager(this, Operations.directionApi(this,
                        lat, lng, 30.3398, 76.3869), 2, true);
                break;

        }
    }


    void staticPolyLine() {

        if (greyPolyLine != null) {
            greyPolyLine.remove();
            blackPolyline.remove();
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : polyLineList) {
            builder.include(latLng);
        }
        //   CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
        // googleMap.animateCamera(mCameraUpdate);
        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLACK);
        polylineOptions.width(10);
        polylineOptions.startCap(new SquareCap());
        polylineOptions.endCap(new SquareCap());
        polylineOptions.jointType(JointType.ROUND);
        polylineOptions.addAll(polyLineList);


            MarkerOptions options1 = new MarkerOptions();
            options1.position(new LatLng(lat,lng));
            options1.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_car_new));
            carMarker = googleMap.addMarker(options1);
            //ß googleMap.addMarker(options1);
            options1 = new MarkerOptions();
            options1.position(new LatLng(30.7333,76.7794));
            options1.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_pin_white));
            stopMarker = googleMap.addMarker(options1);

        greyPolyLine = googleMap.addPolyline(polylineOptions);
        //other project <code></code>
        blackPolylineOptions = new PolylineOptions();
        blackPolylineOptions.width(10);
        blackPolylineOptions.color(Color.BLACK);
        blackPolylineOptions.startCap(new SquareCap());
        blackPolylineOptions.endCap(new SquareCap());
        blackPolylineOptions.jointType(JointType.ROUND);
        blackPolyline = googleMap.addPolyline(blackPolylineOptions);

        if (polyLineList != null && polyLineList.size() > 0) {
            polyline_Route.remove();
        }
        ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
        polylineAnimator.setDuration(2000);
        polylineAnimator.setInterpolator(new LinearInterpolator());
        polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                List<LatLng> points = greyPolyLine.getPoints();
                int percentValue = (int) valueAnimator.getAnimatedValue();
                int size = points.size();
                int newPoints = (int) (size * (percentValue / 100.0f));
                List<LatLng> p = points.subList(0, newPoints);
                blackPolyline.setPoints(p);
            }
        });
        polylineAnimator.start();

    }
    @Override
    public void onLocationChanged(Location location) {
        ModelManager.getInstance().getNearestRoadManager().NearestRoadManager(this, Operations.directionApi(this,
                lat, lng, 30.3398, 76.3869), 2, true);
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
