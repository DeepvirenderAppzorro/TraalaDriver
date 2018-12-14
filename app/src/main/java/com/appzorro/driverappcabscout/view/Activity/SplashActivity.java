package com.appzorro.driverappcabscout.view.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.CustomerReQuestManager;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.Beans.City;
import com.appzorro.driverappcabscout.model.Beans.Country;
import com.appzorro.driverappcabscout.model.Beans.State;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.NetworkChangeReceiver;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;
import com.appzorro.driverappcabscout.view.Activity.PathmapActivity.view.PathMapActivity;
import com.appzorro.driverappcabscout.view.HomeActivity;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by pankaj on 18/1/17.
 */

public class SplashActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, LocationListener {
    private Handler handler;
    private Runnable runnable;
    Activity activity = this;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOACTION = 199;
    AVLoadingIndicatorView avi;
    boolean succeed = false;
    String json_country, json_state, json_city;
    public static ArrayList<Country> countries;
    public static ArrayList<State> states;
    public static ArrayList<City> cities;
    Context context;
    boolean backgroundValue = false;
    LocationManager locationManager;
    double latitude, longitude;
    private Location mLastLocation;
    String socket_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        context = this;
        LocPermission();
        initializeUI();
        getIntents();
        getLocation();
        socketMethod();
        getLocations();
        CSPreferences.putString(activity, Constant.OfflineOrOnline, "false");


        @SuppressLint("HandlerLeak") final Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                } else {

                }
            }
        };

        Thread t = new Thread() {
            @Override
            public void run() {
                json_country = loadJSONFromAsset();
                json_state = loadStateJsonFromAsset();
                json_city = loadCityJsonFromAsset();
                SetSpinnerCountries();
                SetStatesSpinner();
                SetCitySpinner();
                succeed = true;
                if (succeed) {
                    //we can't update the UI from here so we'll signal our handler and it will do it for us.
                    h.sendEmptyMessage(0);
                } else {
                    h.sendEmptyMessage(1);
                }
            }
        };
        t.start();

    }

    private void getLocations()
    {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.e("if called", "");
            enableLoc();
        } else {
            handleSleep();
            Log.e("if  else called", "");
        }
    }

    //***LocationPermissions***

    private void LocPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, 12);
        }
    }

    private void socketMethod()
    {     Utils.connectServer();
        /*socket_id=CSPreferences.readString(context,Constant.SOCKET_ID);
        if(socket_id!=null)
        {
            Utils.sendMessageIo(context,"onlineDriver",socket_id);
        }*/
        Utils.ConnectionChkMethod(context);
        Utils.cancelRideCustomer(context,"finalConfirmation");
        Utils.getMessages(context,"requestDriver");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 12:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                   // Toast.makeText(this, " location!", Toast.LENGTH_SHORT).show();

                } else {
                    //Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void getIntents() {
        backgroundValue = false;
        if (getIntent() != null && getIntent().hasExtra("comes_from")) {
            if (getIntent().getStringExtra("comes_from").equals("dialog")) {
                backgroundValue = true;
            }

        }
    }
    //**** spinner items
    private void initializeUI() {
        avi = (AVLoadingIndicatorView) findViewById(R.id.progresbaar);
        String token = FirebaseInstanceId.getInstance().getToken();
        countries = new ArrayList<>();
        states = new ArrayList<>();
        cities = new ArrayList<>();
        mLastLocation=  Utils.getLastBestStaleLocation(context);
        if(mLastLocation==null)
        {
            latitude=30.7097499;
            longitude=76.6934968;
        }
        else {
            latitude=  mLastLocation.getLatitude();
            longitude= mLastLocation.getLongitude();
        }
    }

    // *** getCountries Spinner ***
    public void SetSpinnerCountries() {
        try {
            JSONObject jsonObject = new JSONObject(json_country);
            JSONArray jsonArray = jsonObject.getJSONArray("countries");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String cid = jsonObject1.getString("id");
                String name = jsonObject1.getString("name");
                countries.add(i, new Country(Integer.parseInt(cid), name));
            }

        } catch (JSONException e) {

        }
    }

    public void SetStatesSpinner() {
        try {
            JSONObject jsonObject = new JSONObject(json_state);
            JSONArray jsonArray = jsonObject.getJSONArray("states");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String chkCountry_id_state = jsonObject1.getString("country_id");
                String name = jsonObject1.getString("name");
                String State_id = jsonObject1.getString("id");
                try {
                    states.add(i, new State(Integer.parseInt(State_id), Integer.parseInt(chkCountry_id_state), name));

                } catch (Exception ex) {

                }


            }

        } catch (JSONException e) {

        }

    }
    //End
    public void SetCitySpinner() {
        try {
            JSONObject jsonObject = new JSONObject(json_city);
            JSONArray jsonArray = jsonObject.getJSONArray("cities");


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String stateId = jsonObject1.getString("state_id");
                String name = jsonObject1.getString("name");
                String cityId = jsonObject1.getString("id");
                try {
                    cities.add(i, new City(Integer.parseInt(cityId), Integer.parseInt(stateId), name));

                } catch (Exception e) {

                }


            }

        } catch (JSONException e) {
        }

    }


    private void handleSleep() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                String driver_status = CSPreferences.readString(context, Constant.DRIVER_STATUS);

                if (CSPreferences.readString(activity, "login_status").equals("true")) {
                    if (backgroundValue) {
                        startActivity(new Intent(activity, NotificatonDialog.class));
                        finish();
                    } else
                        hitApi();
                } else {
                  //  CSPreferences.putString(activity, Constant.OfflineOrOnline, "true");
                    Intent intent = new Intent(activity, CabCompanyActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        int SPLASH_TIME_OUT = 3000;
        handler.postDelayed(runnable, SPLASH_TIME_OUT);
    }

    private void hitApi() {
        ModelManager.getInstance().getCustomerReQuestManager().CustomerReQuestManager(context, Operations.getCustomerRequest(context,
                CSPreferences.readString(getApplicationContext(), "customer_id"), String.valueOf(latitude), String.valueOf(longitude)), "checkin_splash");

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
                            } catch (IntentSender.SendIntentException e) {
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
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            Log.d("ChkEventBus", "registed bus now in OnResumMethod");
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkChangeReceiver(), intentFilter);

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


    @Override
    protected void onPause() {
        super.onPause();// ATTENTION: This was auto-generated to implement the App Indexing API.
        EventBus.getDefault().unregister(this);

    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constant.LAST_RIDE_STATE_SUCCCESS:
                Intent intent = new Intent(activity, PathMapActivity.class);
                startActivity(intent);
              /*  Intent intent11 = new Intent(activity, HomeScreenActivity.class);
                startActivity(intent11);*//**/
                break;
            case Constant.NOTIFICATION_DIALOG:

                Intent intentNoti = new Intent(activity, NotificatonDialog.class);
                startActivity(intentNoti);
                break;

            case Constant.SERVER_ERROR:
                CSPreferences.putString(activity, Constant.OfflineOrOnline, "false");
                Intent intent2 = new Intent(getApplicationContext(), HomeActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                overridePendingTransition (0, 0);
                break;
            case Constant.SPLASH_CHECKIN:
                boolean isRideAccepted = false;
                if (CustomerReQuestManager.customersRequestBean != null) {
                    for (int i = 0; i < CustomerReQuestManager.customersRequestBean.getResponse().getData().size(); i++) {
                        if (!CustomerReQuestManager.customersRequestBean.getResponse().getData().get(i).getCustomerStatus().equals("0"))
                            isRideAccepted = true;
                    }
                    if (isRideAccepted) {
                        startActivity(new Intent(context, PathMapActivity.class));
                       // startActivity(new Intent(context, HomeScreenActivity.class));
                        finish();
                    } else {
                        CSPreferences.putString(activity, Constant.OfflineOrOnline, "false");
                        Intent intent21 = new Intent(getApplicationContext(), HomeActivity.class);
                        intent21.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent21.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent21);
                        overridePendingTransition (0, 0);
                    }
                } else {
                    //Intent intent22 = new Intent(getApplicationContext(), Pa.class);
                    CSPreferences.putString(activity, Constant.OfflineOrOnline, "false");

                    Intent intent22 = new Intent(getApplicationContext(), HomeActivity.class);
                    intent22.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent22.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent22);
                    finish();
                }
                break;

        }
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = SplashActivity.this.getAssets().open("countries.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String loadStateJsonFromAsset() {
        String json = null;
        try {
            InputStream is = SplashActivity.this.getAssets().open("states.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String loadCityJsonFromAsset() {
        String json = null;
        try {
            InputStream is = SplashActivity.this.getAssets().open("cities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
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
}
