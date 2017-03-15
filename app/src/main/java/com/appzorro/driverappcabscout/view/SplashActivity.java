package com.appzorro.driverappcabscout.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by pankaj on 18/1/17.
 */

public class SplashActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {
    private Handler handler;
    private Runnable runnable;
    Activity activity = this;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOACTION = 199;
    AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        avi = (AVLoadingIndicatorView)findViewById(R.id.progresbaar);
        String token = FirebaseInstanceId.getInstance().getToken();


        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.e("if called", "");
            enableLoc();
        }
        else {
            handleSleep();
            Log.e("if  else called", "");
        }
    }

    private void handleSleep() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                if (CSPreferences.readString(activity, "login_status").equals("true")){

                    Intent i = new Intent(activity, HomeScreenActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent intent = new Intent(activity, CabCompanyActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        int SPLASH_TIME_OUT = 5000;
        handler.postDelayed(runnable, SPLASH_TIME_OUT);
    }
    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.e("Location error ", "" + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        (Activity) SplashActivity.this, REQUEST_LOACTION);
                            }catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }

    }
    @Override
    protected void onResume() {
        super.onResume();

       // ModelManager.getInstance().getCabCompaniesManager().getCabCompanies(activity, Operations.getCabCompaniesTask(activity));

       //  ModelManager.getInstance().getCabCompaniesManager().getCabCompanies(activity, Operations.getCabCompaniesTask(activity));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LOACTION:
                switch (resultCode) {
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                    case Activity.RESULT_OK:
                         handleSleep();
                         break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacks(runnable);
        finish();
    }
}
