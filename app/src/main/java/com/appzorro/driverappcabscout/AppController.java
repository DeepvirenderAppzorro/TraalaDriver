package com.appzorro.driverappcabscout;

/**
 * Created by Akshay on 7/19/2016.
 */


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.AppService;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.network.LocationTrack;
import com.appzorro.driverappcabscout.web.WebService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.twitter.sdk.android.core.Twitter;

import java.util.Timer;
import java.util.TimerTask;


public class AppController extends Application implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    public static boolean activityVisible;
    public static boolean picUpdated = false;
    private static AppController mInstance;
    public static Timer mTimer1;
    public static TimerTask mTt1;
    public static boolean chkStatus = false;
    public static Handler mTimerHandler = new Handler();
    public static LocationTrack locationTrack;
    public static Location getmLastLocation;
    static LocationManager locationManager;
    private static GoogleApiClient mGoogleApiClient;
    public static double lattitude, longitude;
    AppService appService;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Twitter.initialize(this);
        new WebService();
        new AppService(mInstance);
        Intent intent = new Intent(mInstance, AppService.class);
        mInstance.startService(intent);
        mGoogleApiClient = new GoogleApiClient.Builder(mInstance).addConnectionCallbacks(mInstance).addOnConnectionFailedListener(mInstance).addApi(LocationServices.API).build();
        getmLastLocation = getLastBestStaleLocation();

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }


    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);

    }

    // stop Updating Location
    public static void stopTimer() {
        if (!chkStatus) {

            if (mTimer1 != null) {
                mTimer1.cancel();
                mTimer1.purge();
            }
        }
      /*  if(locationManager!=null)
        {
            locationManager.removeUpdates(mInstance);
        }
     */
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d("onterminate", "On terminate called");
    }

    // Update Location Every 30 Sec
    public static void startTimer(final Context context, final String lat, final String lng, final String status) {

        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run() {
                        if (chkStatus) {
                            if (ActivityCompat.checkSelfPermission(mInstance, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mInstance, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }

                            // getmLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                            if (getmLastLocation != null) {
                                //  Log.d("chkStatus", getmLastLocation.getLongitude() + "Lattitude api hit");
                              /* String time= Utils.getTimeStamp();
                                ModelManager.getInstance().getOnlineOfflineManager().OnlineOfflineManager(context,
                                        Operations.sendDrivertStatus(context, CSPreferences.readString(context, "customer_id"), status, String.valueOf(lattitude) , String.valueOf(longitude) + "", URLEncoder.encode(time),CSPreferences.readString(context, Constant.SOCKET_ID)));*/
                                //  Toast.makeText(context, lattitude+" lattitude " + longitude +" longitude", Toast.LENGTH_SHORT).show();
                                ModelManager.getInstance().getLocationSendManager().locationsend(context, Operations.sendLocationurl(context,
                                        String.valueOf(lattitude), String.valueOf(longitude), CSPreferences.readString(context,
                                                "customer_id")));
                            }

                            Log.d("chkStatus", "Online api hit");
                        }

                    }
                });
            }
        };

        mTimer1.schedule(mTt1, 1, 5000);
    }
    //End


    public static void activityResumed() {
        activityVisible = true;// this will set true when activity resumed

    }

    public static void activityPaused() {
        activityVisible = false;// this will set false when activity paused

    }


    public static boolean isActivityVisible() {
        return activityVisible;
    }

    //***Get Last Location
    public Location getLastBestStaleLocation() {
        Location bestresult = null;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(mInstance, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
            } else {
                bestresult = networklocation;
                Log.e("result", "network location ---- " + bestresult.getLatitude());
                Config.currentLAT = bestresult;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
            }
        } else if (gpslocation != null) {
            bestresult = gpslocation;
            Log.e("result", "gps location only found---- " + bestresult.getLatitude());
            Config.currentLAT = bestresult;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        } else if (networklocation != null) {
            bestresult = networklocation;
            Log.e("result", "network location only found--- " + bestresult.getLatitude());
            Config.currentLAT = bestresult;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
            Log.e("", bestresult.toString());
        }
        return bestresult;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            getmLastLocation = location;
            lattitude = location.getLatitude();
            longitude = location.getLongitude();
           // Toast.makeText(mInstance,lattitude+"lat"+longitude+"",Toast.LENGTH_SHORT).show();
            Log.d("chkStatus", location.getLatitude() + "Lat in OnlocationChange" + location.getLongitude() + " on Longitude");

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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

