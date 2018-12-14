package com.appzorro.driverappcabscout.view.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.JsonRequestIR;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;
import com.appzorro.driverappcabscout.view.Activity.PathmapActivity.view.PathMapActivity;
import com.appzorro.driverappcabscout.view.HomeActivity;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.iwgang.countdownview.CountdownView;
import dmax.dialog.SpotsDialog;

import static com.appzorro.driverappcabscout.controller.CustomerDetail.customer_detailBean;
import static com.appzorro.driverappcabscout.model.Utils.socket_customerDetail;

public class NotificatonDialog extends AppCompatActivity implements LocationListener, RoutingListener, CountdownView.OnCountdownEndListener {
    public static boolean isActive = false;
    public boolean comingFromPath = false;
    public static boolean isReject = false;
    // CustomersRequestBean.Datum list;
    // List<CustomersRequestBean.Datum> DataList;
    CountdownView timer;
    RelativeLayout rLrideType;
    Activity activity;
    AlertDialog dialog;
    LocationManager locationManager;
    double currentlat, currentlang;
    Dialog Canceldialog;
    RelativeLayout open_dialog;
    String VehicleType;
    String RideType;
    private TextView tvPrice;
    private TextView distance;
    private String rideType = "0";
    TextView accept,destination,reject,customername,requestid;
    private LatLng startLatlng, endLatlng;
    Context context;
    ImageView customerimage;
    ArrayList<JSONObject> jsonObjects;
    MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_activity);
        activity = this;
        this.setFinishOnTouchOutside(false);
        context = this;
        open_dialog = findViewById(R.id.bottom_sheet);
        rLrideType = findViewById(R.id.llAddPooltype);
        createMediaPlayer();
        chkRequestType();
        checkForRideType();
        initViews();

        if (checkIsItFCM()) {

        } else {
            isActive = true;
            getIntents();
            getLastBestStaleLocation();

            try {
                if (customer_detailBean != null) {
                    //DataList = list.getCustomerId(listSize);

                } else {

                    Canceldialog = Utils.ErrorDialog(NotificatonDialog.this);
                    Canceldialog.show();
                    TextView ok = Canceldialog.findViewById(R.id.txtok);
                    TextView cancel = Canceldialog.findViewById(R.id.txtcancel);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utils.clearNotifications(getApplicationContext());
                            Intent it = new Intent(NotificatonDialog.this, HomeActivity.class);
                            startActivity(it);

                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Canceldialog.dismiss();
                            Utils.clearNotifications(getApplicationContext());
                        }
                    });

                }

                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isActive = false;
                        Utils.clearNotifications(getApplicationContext());
                        dialog = new SpotsDialog(activity);
                        dialog.show();
                        //.cancel();
                       stopMediaPlayer();
                        timer.stop();

                        ModelManager.getInstance().getAcceptCustomerRequest().AcceptCustomerRequest(activity,
                                Operations.acceptByDriver(activity, CSPreferences.readString(activity, "customer_id"), socket_customerDetail.getRide_request_id(), String.valueOf(currentlat), String.valueOf(currentlang), socket_customerDetail.getCutomerID(), rideType));

                        // open_dialog.setVisibility(View.GONE);

                    }
                });

                reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Added by deep on oct7
                        isActive = false;
                        //End
                        stopMediaPlayer();
                        ModelManager.getInstance().getAcceptCustomerRequest().RejectCustomer(activity,
                                Operations.rejectByDriver(
                                        CSPreferences.readString(activity, "customer_id"), customer_detailBean.getResponse().getDetail().getRideRequestId()));

                        isReject = true;
                        JSONObject jsonObject = JsonRequestIR.rejectRequest(getApplicationContext(), socket_customerDetail.getCutomerID(), socket_customerDetail.getDriver_id(), socket_customerDetail.getRide_request_id(), socket_customerDetail.getNoti_type(), socket_customerDetail.getPoolType());
                        Utils.sendJsonIO(activity, jsonObject, "confirmDriver");
                        Utils.clearNotifications(getApplicationContext());
                        startActivity(new Intent(NotificatonDialog.this, HomeActivity.class));

                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void initViews()
    {
        currentlat = 30.7097499;
        currentlang = 76.6934968;
        customerimage = (ImageView) findViewById(R.id.imagevieww);
        customername = (TextView) findViewById(R.id.txtdrivername);
        requestid = (TextView) findViewById(R.id.txtrequestid);
        distance = (TextView) findViewById(R.id.txtdistance);
        accept = (TextView) findViewById(R.id.btnaccept);
        destination = (TextView) findViewById(R.id.txtsource);
        reject = (TextView) findViewById(R.id.btReject);
        tvPrice = (TextView) findViewById(R.id.txtprice);
        timer = (CountdownView) findViewById(R.id.count_down);
        timer.setOnCountdownEndListener(this);

        if (customer_detailBean != null) {
            String profileimage = Config.baserurl_image + customer_detailBean.getResponse().getDetail().getProfilePic();
            Log.e("image ", profileimage);
            Picasso.with(this).load(profileimage).placeholder(R.drawable.ic_icon_pic).into(customerimage);
            VehicleType = customer_detailBean.getResponse().getDetail().getVehicleType();
            CSPreferences.putString(NotificatonDialog.this, Constant.VEHICLETYPE, VehicleType);
            TextView pickuloaction = (TextView) findViewById(R.id.txtpickaddress);
            requestid.setText("" + customer_detailBean.getResponse().getDetail().getRideRequestId());

            float aDouble = Float.parseFloat(customer_detailBean.getResponse().getDetail().getPrice());
            tvPrice.setText(String.format("%.2f", aDouble));

            customername.setText("" + customer_detailBean.getResponse().getDetail().getName());
            pickuloaction.setText(customer_detailBean.getResponse().getDetail().getPickupLocation());
            destination.setText(customer_detailBean.getResponse().getDetail().getDropLocation());
            String pickupaddres = Utils.getCompleteAddressString(activity, Double.parseDouble(Utils.getLatitude(customer_detailBean.getResponse().getDetail().getPickupCordinates())),
                    Double.parseDouble(Utils.getLongitude(customer_detailBean.getResponse().getDetail().getPickupCordinates())));

            startLatlng = new LatLng(Double.parseDouble(Utils.getLatitude(customer_detailBean.getResponse().getDetail().getPickupCordinates())),
                    Double.parseDouble(Utils.getLongitude(customer_detailBean.getResponse().getDetail().getPickupCordinates())));

            endLatlng = new LatLng(Double.parseDouble(Utils.getLatitude(customer_detailBean.getResponse().getDetail().getDropCordinates())), Double.parseDouble(Utils.getLongitude(customer_detailBean.getResponse().getDetail().getDropCordinates())));
            //   Log.e("address", "" + pickupaddres);

            open_dialog.setVisibility(View.VISIBLE);
            String dis = CSPreferences.readString(NotificatonDialog.this, Constant.DISTANCE);
            distance.setText(dis);
            CSPreferences.putString(NotificatonDialog.this, "FullDisatance", dis);
            startTimer();


        } else {

            Toast.makeText(this, "No data available", Toast.LENGTH_LONG).show();
        }


    }

    private boolean checkIsItFCM() {
        boolean returnValue = false;
        if (getIntent() != null && getIntent().hasExtra("coming_from")) {
            if (getIntent().getStringExtra("coming_from").equals("FCM")) {
                returnValue = true;
                String rideRequestId = getIntent().getStringExtra("ride_request_id");
                ModelManager.getInstance().getCustomerReQuestManager().CustomerRequestDetails(activity, Operations.getLastStateOfRide(activity,
                        rideRequestId), "");

            }

        }
        return returnValue;
    }

    private void createMediaPlayer()
    {
        if(mp==null)
        {

            mp = MediaPlayer.create(this, R.raw.ticktick);
            mp.start();
            mp.setLooping(true);
        }
    }
    private void chkRequestType()
    {
        RideType = CSPreferences.readString(this, Constant.IS_POOLTYPE);
        if (RideType.equals("true")) {
            rLrideType.setVisibility(View.VISIBLE);

        } else {
            rLrideType.setVisibility(View.GONE);
        }
    }

    private void checkForRideType() {
        if (CSPreferences.readString(this, Constant.IS_POOLTYPE).equals("true")) {
            rideType = "1";
        }
    }
    private void stopMediaPlayer() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    private void getIntents() {
        if (getIntent() != null && getIntent().hasExtra("coming_from")) {
            if (getIntent().getStringExtra("coming_from").equals("PathMapActivity"))
                comingFromPath = true;
        }
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
                if (mp != null) {
                    mp.stop();

                }
                JSONObject jsonObject1 = JsonRequestIR.cancelCustomerDialog(activity);
                Utils.sendJsonIO(context, jsonObject1, "private_message");
                // startActivity(new Intent( activity,HomeScreenActivity.class));

                break;
            case Constant.DRIVERREJECTED:
                if (mp != null) {
                    mp.stop();
                }
                // startActivity(new Intent( activity,HomeScreenActivity.class));

                break;

            case Constant.RIDE_DETAILS:

                startActivity(new Intent(activity, SplashActivity.class).putExtra("comes_from", "dialog"));
                finish();

                break;

            case Constant.SERVER_ERROR:
                dialog.dismiss();
                Canceldialog = Utils.serverError(NotificatonDialog.this);
                Canceldialog.show();
                final TextView ok = Canceldialog.findViewById(R.id.txtok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ok.setBackgroundColor(Color.parseColor("#0083db"));

                        Intent it = new Intent(NotificatonDialog.this, HomeActivity.class);
                        startActivity(it);
                        finish();

                    }
                });


                break;

            case Constant.ACCEPTBYDRIVER:
                CSPreferences.putString(context, Constant.REQUEST_TYPE, "accept");
                if (mp != null) {
                    mp.stop();
                }
                JSONObject jsonObject = JsonRequestIR.accept(getApplicationContext(), socket_customerDetail.getCutomerID(), socket_customerDetail.getDriver_id(), socket_customerDetail.getRide_request_id(), socket_customerDetail.getNoti_type(), socket_customerDetail.getPoolType(), CSPreferences.readString(context, "customer_id"), String.valueOf(currentlat), String.valueOf(currentlang));
                Utils.sendJsonIO(activity, jsonObject, "confirmDriver");
                // AppController.chkStatus=false;
                Intent intent = new Intent(activity, PathMapActivity.class);
                startActivity(intent);
                finish();
                Log.d("rideAccepted", "yesyesDialog");
//                if (comingFromPath)
//                    finish();
                break;

            case Constant.DISTANCE_MATRIX_FAILURE:

                break;

            /*case Constant.NOTIFICATION_DIALOG:

                Log.d("rideAccepted", "yesyesDialog");
                break;*/

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
        if (isActive) {
            isActive = false;
            //End
            stopMediaPlayer();
            ModelManager.getInstance().getAcceptCustomerRequest().RejectCustomer(activity,
                    Operations.rejectByDriver(
                            CSPreferences.readString(activity, "customer_id"), customer_detailBean.getResponse().getDetail().getRideRequestId()));

            isReject = true;
            JSONObject jsonObject = JsonRequestIR.rejectRequest(getApplicationContext(), socket_customerDetail.getCutomerID(), socket_customerDetail.getDriver_id(), socket_customerDetail.getRide_request_id(), socket_customerDetail.getNoti_type(), socket_customerDetail.getPoolType());
            Utils.sendJsonIO(activity, jsonObject, "confirmDriver");
            Utils.clearNotifications(getApplicationContext());
        }
        finish();
    }


    // here we are getting the cureent location of the user... this method return you our current locaton after every 10 second
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActive = false;
    }


}

