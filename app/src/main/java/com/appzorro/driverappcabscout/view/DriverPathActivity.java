package com.appzorro.driverappcabscout.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.appzorro.driverappcabscout.R;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class DriverPathActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback
        , GoogleApiClient.OnConnectionFailedListener, RoutingListener {

    GoogleApiClient googleApiClient;
    GoogleMap googleMap;
    ArrayList<Polyline> polylines;
    Context context;
    Location mLastLocation;
    Double currentlat, currentlng;
    LatLng driverpoint, customerpickpoint;
    Toolbar toolbar;
    private static final int[] COLORS = new int[]{R.color.pathcolor, R.color.colorPrimaryDark, R.color.colorAccent, R.color.colorAccent, R.color.primary_dark_material_light};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_path);
        context = this;
        initViews();

    }


    public void initViews() {

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Confirmation");
        setSupportActionBar(toolbar);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    private void initCamera(Location mLocation) {

        Log.e("init camera", "fuction are called");
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraPosition position = CameraPosition
                .builder().target(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()))
                .zoom(15f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();
        currentlat = mLocation.getLatitude();
        currentlng = mLocation.getLongitude();
        googleMap.addMarker(new MarkerOptions().position(new LatLng(currentlat,
                currentlng)));
        driverpoint = new LatLng(currentlat,currentlng);
        customerpickpoint = new LatLng(30.729805,76.769364);

        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener((RoutingListener) context)
                .alternativeRoutes(true)
                .waypoints(driverpoint, customerpickpoint)
                .build();
        routing.execute();

    }


    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onresume","");



    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        Log.e("on connected", "");
        initCamera(mLastLocation);



    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onMapReady(GoogleMap mgoogleMap) {
        googleMap = mgoogleMap;
        Log.e("onmapready","");

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {



            }
        });


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {


        polylines = new ArrayList<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(driverpoint);
        builder.include(customerpickpoint);
        LatLngBounds bounds = builder.build();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
        PolylineOptions polyOptions = null;
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }
        int min = 0;
        int indexvalue = 0;

        for (int j = 0; j < arrayList.size(); j++) {
            int colorIndex = j % COLORS.length;
            polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(7 + j * 3);

            if (j > 0) {
                if (min > arrayList.get(j).getDurationValue()) {

                    indexvalue = j;
                    min = arrayList.get(j).getDistanceValue();

                }
            } else {


                min = arrayList.get(j).getDistanceValue();

            }
            Log.e("route", String.valueOf(j + 1) + "     distance--" + arrayList.get(j).getDistanceValue() + "  duration---" + arrayList.get(j).getDurationValue());
            Toast.makeText(getApplicationContext(), "Route " + (j + 1) + ": distance - " + arrayList.get(j).getDistanceValue() + ": duration - " + arrayList.get(j).getDurationValue(), Toast.LENGTH_SHORT).show();
        }
        polyOptions.addAll(arrayList.get(indexvalue).getPoints());
        Polyline polyline = googleMap.addPolyline(polyOptions);
        Log.e("shortest distance", String.valueOf(arrayList.get(indexvalue).getDistanceValue()));
        Log.e("shortest duration", String.valueOf(arrayList.get(indexvalue).getDurationValue()));
        polylines.add(polyline);

        MarkerOptions options = new MarkerOptions();
        options.position(driverpoint);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_pin));
        googleMap.addMarker(options);

        // End marker
        options = new MarkerOptions();
        options.position(customerpickpoint);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_pin));
        googleMap.addMarker(options);

    }

    @Override
    public void onRoutingCancelled() {

    }
}
