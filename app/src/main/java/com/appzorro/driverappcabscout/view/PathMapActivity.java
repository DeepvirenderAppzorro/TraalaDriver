package com.appzorro.driverappcabscout.view;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.CustomerReQuestManager;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.Beans.CustomerRequest;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.ConfigVariable;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.JsonRequestIR;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.exoplayer.C;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
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
import com.shitij.goyal.slidebutton.SwipeButton;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Paint.Join.ROUND;
import static com.appzorro.driverappcabscout.R.id.l;
import static com.appzorro.driverappcabscout.R.id.makecall;
import static com.appzorro.driverappcabscout.controller.NearestRoadManager.polyLineList;

public class PathMapActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback,
        RoutingListener, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleApiClient googleApiClient;
    GoogleMap googleMap;
    ArrayList<Polyline> polylines;
    Polyline polyline_Route;
    Context context;
    Location mLastLocation;
    SwipeButton swipe_StartTrip, swipe_CompleteTrip;
    CardView starttriplayout, paymentcardview;
    RelativeLayout addtolllayout;
    AlertDialog dialog;
    ArrayList<CustomerRequest> list;
    CustomerRequest customerRequest;
    LocationManager locationManager;
    Double lat, lng;
    ArrayList<Double> latlist;
    ArrayList<Double> lnglist;
    Dialog canceltripdialog, addtolldialog, stopfeeDailog;
    RadioButton reasonbutton;
    int tollfee = 0;
    int stopfee = 0;
    Toolbar toolbar;
    private String TAG = PathMapActivity.class.getSimpleName();
    boolean isMarkerRotating;
    private LatLng myLocation_Start;
    private LatLng myLocation_End;
    LatLng currentlatlng, customer_source, customer_destination, firstlat, lastlat;
    String startTrips_status = "false", stopTrips_status = "false";
    private boolean isFirstTime = true;
    Dialog messagedialog;

    ImageView callimage, cancelimage, customerimage, navigate_image;
    TextView makecalltext, customername, stoptrips, customer_name, payment_method, complete_trip, complete_ride, addtoll, addstopfee, addresstext, txt_begin_trip;
    TextView text_paymentstaus;
    private PolylineOptions polylineOptions, blackPolylineOptions;
    private Polyline blackPolyline, greyPolyLine;
    private static final int[] COLORS = new int[]{R.color.colorBlack, R.color.colorBlack, R.color.colorBlack, R.color.colorBlack, R.color.colorBlack, R.color.black};
    private LatLng startPosition;
    private LatLng endPosition;
    private boolean isFirstPosition = true;
    private boolean isFirstMarker = true;
    private Marker carMarker;
    private Marker stopMarker;
    // state=1 for arrived // state =2 when start and state=3 fom completing
    int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_trip_new);
        context = this;
        initViews();
     /*   makecalltext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (makecalltext.getText().toString().equals("Arrived")) {
                    makecalltext.setText("Start");
                    dialog = new SpotsDialog(context);
                    dialog.show();
                    ModelManager.getInstance().getArrivedManager().ArrivedManager(context, Operations.arrivedDriver(context,
                            CSPreferences.readString(context, "customer_id"), customerRequest.getRequestid()));

                } else {


                    dialog = new SpotsDialog(context);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss a");
                    Calendar calander = Calendar.getInstance();
                    String currentDate = DateFormat.getDateInstance().format(Calendar.getInstance().getTime()).replaceAll(" ", "");
                    Log.e("date and time ", currentDate);
                    String time = simpleDateFormat.format(calander.getTime()).replaceAll(" ", "");

                    Log.e("start time", String.valueOf(time));

                    Log.e("driver id ", CSPreferences.readString(context, "customer_id"));
                    dialog.show();

                    ModelManager.getInstance().getStartTripsManager().StartTripsManager(context, Operations.startTrips(
                            context, CSPreferences.readString(context, "customer_id"), customerRequest.getRequestid(),
                            time, currentDate, String.valueOf(lat) + "," + String.valueOf(lng)
                    ));

                }

            }
        });
*/
        txt_begin_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_begin_trip.getText().toString().equals("Click When You Arrived")) {
                    txt_begin_trip.setText("Arrived");
                    state = 1;

                    dialog = new SpotsDialog(context);
                    dialog.show();
                    ModelManager.getInstance().getArrivedManager().ArrivedManager(context, Operations.arrivedDriver(context,
                            CSPreferences.readString(context, "customer_id"), customerRequest.getRequestid()));

                    JSONObject jsonObject = JsonRequestIR.ArrivedjsonRequest(getApplicationContext());
                    Utils.sendMessage(PathMapActivity.this, jsonObject);
                    addresstext.setText(Utils.getCompleteAddressString(context, customer_destination.latitude,
                            customer_destination.longitude));


                    googleMap.clear();
                    drawPath(myLocation_Start, customer_destination);
                    isFirstTime = true;
                    isFirstMarker = true;
                } else {


                    dialog = new SpotsDialog(context);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss a");
                    Calendar calander = Calendar.getInstance();
                    String currentDate = DateFormat.getDateInstance().format(Calendar.getInstance().getTime()).replaceAll(" ", "");
                    Log.e("date and time ", currentDate);
                    String time = simpleDateFormat.format(calander.getTime()).replaceAll(" ", "");

                    Log.e("start time", String.valueOf(time));

                    Log.e("driver id ", CSPreferences.readString(context, "customer_id"));
                    dialog.show();

                    ModelManager.getInstance().getStartTripsManager().StartTripsManager(context, Operations.startTrips(
                            context, CSPreferences.readString(context, "customer_id"), customerRequest.getRequestid(),
                            time, currentDate, String.valueOf(lat) + "," + String.valueOf(lng)
                    ));

                }
            }
        });


//here we stop the the trips by using click on this button
        complete_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss a");
                Calendar calander = Calendar.getInstance();

                String time = simpleDateFormat.format(calander.getTime()).replaceAll(" ", "");
                SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy");
                String strDate = "" + mdformat.format(calander.getTime());
                Log.e("stop time", String.valueOf(time));

                dialog = new SpotsDialog(context);
                dialog.show();

                Log.e("nkjkf;vf", CSPreferences.readString(context, "customer_id"));

                ModelManager.getInstance().getStopRideManager().StopRideManager(context, Operations.stopRide(context,
                        CSPreferences.readString(context, "customer_id"), customerRequest.getRequestid(), time, strDate, String.valueOf(lat) + "," + String.valueOf(lng)));

            }
        });
// here we are canceling the trip
        cancelimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                canceltripdialog = Utils.createCancelDialog(context);
                canceltripdialog.show();
                RadioGroup radioGroup = (RadioGroup) canceltripdialog.findViewById(R.id.radiogrup);
                TextView cancel = (TextView) canceltripdialog.findViewById(R.id.txtcancel);
                TextView dont_cancel = (TextView) canceltripdialog.findViewById(R.id.txtdontcancel);

                int selectedId = radioGroup.getCheckedRadioButtonId();

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {

                        int id = radioGroup.getCheckedRadioButtonId();
                        Log.e("radio id ", String.valueOf(id));
                        reasonbutton = (RadioButton) canceltripdialog.findViewById(id);
                        Log.e("text", "a  " + reasonbutton.getText().toString());

                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        dialog = new SpotsDialog(context);

                        dialog.show();

                        ModelManager.getInstance().getCancelTripManager().CancelTripManager(context, Operations.cancelTrips(context,
                                CSPreferences.readString(context, "customer_id"), customerRequest.getRequestid()));


                    }
                });

                dont_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        canceltripdialog.dismiss();
                    }
                });
            }
        });

//.......................... here we are implemnt the call intent ...........................................


        callimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", customerRequest.getMobile(), null));
                startActivity(intent);
            }
        });

// ........................................here we are adding the add toll amount here ........................................

        addtoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addtolldialog = Utils.createaddtollDialog(context);
                addtolldialog.show();
                final EditText amount = (EditText) addtolldialog.findViewById(R.id.edttotalamount);
                EditText description = (EditText) addtolldialog.findViewById(R.id.edtdescription);
                TextView add = (TextView) addtolldialog.findViewById(R.id.txtadd);
                TextView cancel = (TextView) addtolldialog.findViewById(R.id.txtcancel);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (amount.getText().toString().isEmpty()) {

                            amount.setError("Required");
                        } else {

                            tollfee = tollfee + Integer.parseInt(amount.getText().toString());
                            Log.e("tollfee", String.valueOf(tollfee));
                            addtolldialog.dismiss();

                        }

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        addtolldialog.dismiss();
                    }
                });


            }
        });


//........................... Here We are showing the alert to the user for .........................................

        addstopfee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(context)
                        .setTitle("Add Tolls")
                        .setMessage("Are you sure you would like to add a stop fee ?")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog1, int which) {
                                // continue with delete
                                dialog1.dismiss();
                                stopfeeDailog = Utils.createStopfeeDialog(context);
                                stopfeeDailog.show();
                                final EditText editstopamount = (EditText) stopfeeDailog.findViewById(R.id.edtstopamount);
                                TextView addstopfee = (TextView) stopfeeDailog.findViewById(R.id.txtadd);
                                TextView cancel = (TextView) stopfeeDailog.findViewById(R.id.txtcancel);
                                addstopfee.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        if (editstopamount.getText().toString().isEmpty()) {

                                            editstopamount.setError("Required");
                                        } else {

                                            stopfee = stopfee + Integer.parseInt(editstopamount.getText().toString());
                                            Log.e("stofee", String.valueOf(stopfee));
                                            stopfeeDailog.dismiss();
                                        }

                                    }
                                });

                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        stopfeeDailog.dismiss();
                                    }
                                });

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog1, int which) {

                                dialog1.dismiss();
                            }
                        })
                        .show();

            }
        });


    }

    void swipeButtonListeners() {
        swipe_CompleteTrip.addOnSwipeCallback(new SwipeButton.Swipe() {
            @Override
            public void onButtonPress() {

                //    Toast.makeText(PathMapActivity.this, "Pressed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeCancel() {

            }

            @Override
            public void onSwipeConfirm() {
                state = 3;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss a");
                Calendar calander = Calendar.getInstance();
                //  String currentDate = DateFormat.getDateInstance().format(Calendar.getInstance().getTime()).replaceAll(" ", "");
                //Log.e("date and time ", currentDate);
                String time = simpleDateFormat.format(calander.getTime()).replaceAll(" ", "");
                SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy");
                String strDate = "" + mdformat.format(calander.getTime());
                Log.e("stop time", String.valueOf(time));

                dialog = new SpotsDialog(context);
                dialog.show();

                Log.e("nkjkf;vf", CSPreferences.readString(context, "customer_id"));

                //latitude=, Total Amount=, id=31, json3=, longitude=,
                // ride_request_id=, message=Trip Has completed, noti_type=trip_completed}
                JSONObject jsonObject = JsonRequestIR.RideStartjsonRequest(getApplicationContext(), "Trip Has completed", "trip_completed");
                Utils.sendMessage(PathMapActivity.this, jsonObject);

                ModelManager.getInstance().getStopRideManager().StopRideManager(context, Operations.stopRide(context,
                        CSPreferences.readString(context, "customer_id"), customerRequest.getRequestid(), time, strDate, String.valueOf(lat) + "," + String.valueOf(lng)));


            }
        });

        swipe_StartTrip.addOnSwipeCallback(new SwipeButton.Swipe() {
            @Override
            public void onButtonPress() {

                //    Toast.makeText(PathMapActivity.this, "Pressed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeCancel() {

            }

            @Override
            public void onSwipeConfirm() {
                state = 2;
                swipe_StartTrip.setVisibility(View.GONE);
                trip_begin();
                visibleButton();

                // Toast.makeText(PathMapActivity.this, "Trip has started!", Toast.LENGTH_LONG).show();
            }
        });

    }

    void visibleButton() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // yourMethod();
                LatLng coordinate = new LatLng(currentlatlng.latitude, currentlatlng.longitude); //Store these lat lng values somewhere. These should be constant.
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                        coordinate, 16);
                googleMap.animateCamera(location);
                swipe_CompleteTrip.setVisibility(View.VISIBLE);
            }
        }, 200);
    }


    //Addeed by deep

    private void trip_begin() {
      /*  String myConcatedString = cursor.getString(numcol).concat('-').
                concat(cursor.getString(cursor.getColumnIndexOrThrow(db.KEY_DESTINATIE)));
*/
        //{latitude=, Total Amount=, id=31, json3=, longitude=, ride_request_id=, message=Trip Has Started, noti_type=trip_started}
        JSONObject jsonObject = JsonRequestIR.RideStartjsonRequest(getApplicationContext(), "Trip Has Started", "trip_started");
        Utils.sendMessage(this, jsonObject);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = "" + mdformat.format(calendar.getTime());
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        String latLong = lat.toString().concat(",").concat(lng.toString());
        Date currentTime = Calendar.getInstance().getTime();
        Call<ModelTrip> call = apiService.getmodeldemoresult(CSPreferences.readString(this, "customer_id"), customerRequest.getRequestid(), "01:01pm", strDate, latLong);
        call.enqueue(new Callback<ModelTrip>() {

            @Override
            public void onResponse(Call<ModelTrip> call, Response<ModelTrip> response) {
                boolean res = response.isSuccessful();
                String msg = response.body().getResponse().getMessage();
                Log.e("msg_trip", msg);
            }

            @Override
            public void onFailure(Call<ModelTrip> call, Throwable t) {
                Log.e("msg_tripp", "Error");

            }
        });
    }
    //End


    public void initViews() {

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        isMarkerRotating = false;

        starttriplayout = (CardView) findViewById(R.id.card1);
        callimage = (ImageView) findViewById(makecall);
        cancelimage = (ImageView) findViewById(R.id.cancelride);

        customerimage = (ImageView) findViewById(R.id.customerimage);
        customername = (TextView) findViewById(R.id.customername);
        makecalltext = (TextView) findViewById(R.id.txtstart);

        customer_name = (TextView) findViewById(R.id.txtname);
        stoptrips = (TextView) findViewById(R.id.stoptrips);
        payment_method = (TextView) findViewById(R.id.txtpayment);

        txt_begin_trip = (TextView) findViewById(R.id.txt_begin_trip);
        complete_trip = (TextView) findViewById(R.id.txtcollectcash);
        addtolllayout = (RelativeLayout) findViewById(R.id.laoutaddtoll);

        addtoll = (TextView) findViewById(R.id.txtaddtoll);
        addstopfee = (TextView) findViewById(R.id.txtaddstopfee);
        //  addtolllayout.setVisibility(View.GONE);

        paymentcardview = (CardView) findViewById(R.id.paymentcard);
        messagedialog = Utils.createMessageDialog(context);
        navigate_image = (ImageView) findViewById(R.id.navigate);
        addresstext = (TextView) findViewById(R.id.txtdestaddress);
        text_paymentstaus = (TextView) findViewById(R.id.txtpayment);
        //here we  get the detail of cutomer whoes make the request using bean class
        list = new ArrayList<>();
        list = CustomerReQuestManager.requestlis;
        customerRequest = list.get(0);

        customer_source = new LatLng(Double.parseDouble(customerRequest.getSourcelat()), Double.parseDouble(customerRequest.getSourcelng()));
        customer_destination = new LatLng(Double.parseDouble(customerRequest.getDroplat()), Double.parseDouble(customerRequest.getDroplng()));

        Picasso.with(this)
                .load(customerRequest.getProfilepic())
                .into(customerimage);
        customername.setText(customerRequest.getName());
        swipe_StartTrip = (SwipeButton) findViewById(R.id.slide);
        swipe_CompleteTrip = (SwipeButton) findViewById(R.id.slide2);


        customer_name.setText(CSPreferences.readString(this, Constant.RIDER_NAME));
        paymentType(CSPreferences.readString(this, Constant.PAYEMENT_METHOD));
        swipeButtonListeners();

    }

    private void paymentType(String s) {
        switch (s) {
            case "0":
                text_paymentstaus.setText("Cash");
                break;

            case "1":
                text_paymentstaus.setText("Credit Card");
                break;

            case "2":
                text_paymentstaus.setText("Corp. Account");

                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        googleApiClient.connect();
        EventBus.getDefault().register(context);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        googleApiClient.disconnect();
        EventBus.getDefault().unregister(context);
    }


    public void onclickNavigate(View view) {

        if (startTrips_status.equals("false")) {

            String uri = "http://maps.google.com/maps?daddr=" + customer_source.latitude + "," + customer_source.longitude + " (" + "Pickup location" + ")";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        } else {

            String uri = "http://maps.google.com/maps?daddr=" + customer_destination.latitude + "," + customer_destination.longitude + " (" + "Drop location" + ")";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        }

    }


    @SuppressLint("MissingPermission")
    public void getLastBestStaleLocation() {

        Location bestresult = null;

        latlist = new ArrayList<>();
        lnglist = new ArrayList<>();

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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        Log.e("on connected", "");
        initCamera(mLastLocation);
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


    }

    private void initCamera(Location mLocation) {

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

        customer_source = new LatLng(Double.parseDouble(customerRequest.getSourcelat()),
                Double.parseDouble(customerRequest.getSourcelng()));

        customer_destination = new LatLng(Double.parseDouble(customerRequest.getDroplat()),
                Double.parseDouble(customerRequest.getDroplng()));


        currentlatlng = new LatLng(lat, lng);

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));

        MarkerOptions options = new MarkerOptions();
        options.position(new LatLng(lat, lng));


        addresstext.setText(Utils.getCompleteAddressString(context, customer_source.latitude,
                customer_source.longitude));

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
        myLocation_Start = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        //Comment by Sumit
        drawPath(currentlatlng, customer_source);
/*
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener((RoutingListener) context)
                .alternativeRoutes(true)
                .waypoints(currentlatlng, customer_source)
                .build();
        routing.execute();
*/


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getLastBestStaleLocation();

                latlist = new ArrayList<>();
                lnglist = new ArrayList<>();
            }
        }, 1000);


    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRoutingFailure(RouteException e) {
    Toast.makeText(getApplicationContext(),"Invalid Request "+e.getMessage(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {

        String focuspostion = null;

        polylines = new ArrayList<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.35);

      /*  float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int padding = (int) (40 * scale + 0.5f);*/
        Log.e("padding", String.valueOf(padding));

        if (carMarker != null && startTrips_status.equals("false")) {
            currentlatlng = carMarker.getPosition();
            builder.include(currentlatlng);
            builder.include(customer_source);
            LatLngBounds bounds = builder.build();

            CSPreferences.putString(context, "path", "first");
            CSPreferences.putString(context, "end_lat", String.valueOf(customer_source.latitude));
            CSPreferences.putString(context, "end_lng", String.valueOf(customer_source.longitude));

          /*  focuspostion = Utils.midPoint(currentlatlng.latitude, currentlatlng.longitude, customer_source.latitude,
                    customer_source.longitude);
            Log.e("mid point", "d" + focuspostion.toString());*/

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
            //  googleMap.moveCamera(cu);

        } else {

            builder.include(customer_source);
            builder.include(customer_destination);
            LatLngBounds bounds = builder.build();

            CSPreferences.putString(context, "path", "second");
            CSPreferences.putString(context, "end_lat", String.valueOf(customer_destination.latitude));
            CSPreferences.putString(context, "end_lng", String.valueOf(customer_destination.longitude));

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
            //googleMap.moveCamera(cu);
            // googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new LatLng(focuspostion.latitude,focuspostion.longitude)));

        }
        drawPolyLine(arrayList);

    }


    // here we can the  total ammount to server if customer select the credit card payment

    public void Collectcash(View view) {


    }

    @Override
    public void onRoutingCancelled() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event event) {
        switch (event.getKey()) {

            case Constant.ARRIVEDSTATUS:
                dialog.dismiss();
                txt_begin_trip.setVisibility(View.GONE);
                swipe_StartTrip.setVisibility(View.VISIBLE);
                //   Toast.makeText(PathMapActivity.this, "You are arrived customer location", Toast.LENGTH_SHORT).show();
//                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
//                        .setTitleText("Arrived")
//                        .setContentText("You are arrived on the customer location please make the call to customer")
//                        .show();


                break;

            case Constant.RESTART_SERVICE:
                Log.d("serviceRestartted", "Yes");
                Utils.getInstance().serviceMethods(this);
                break;

            case Constant.DRAW_POLYLINE:
                Log.d("serviceRestartted", "Yes");
                staticPolyLine();
                break;

            case Constant.SERVER_ERROR:
                dialog.dismiss();

                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Please check your internet connection")
                        .show();
                break;

            case Constant.STARTTRIPS:

                dialog.dismiss();

                CSPreferences.putString(context, "path", "second");

                startTrips_status = "true";
                // find the distance between driver current location to customer pickup location

                double driverToCustomerDistance = findDistance();

                Log.e("driver distance ", String.valueOf(driverToCustomerDistance));

                starttriplayout.setVisibility(View.GONE);
                //addtolllayout.setVisibility(View.VISIBLE);

                googleMap.clear();

                CustomerRequest customerRequest = list.get(0);

                customer_source = new LatLng(Double.parseDouble(customerRequest.getSourcelat()), Double.parseDouble(customerRequest.getSourcelng()));
                customer_destination = new LatLng(Double.parseDouble(customerRequest.getDroplat()), Double.parseDouble(customerRequest.getDroplng()));

                addresstext.setText(Utils.getCompleteAddressString(context, Double.parseDouble(customerRequest.getDroplat()),
                        Double.parseDouble(customerRequest.getDroplng())));

                // again intilize the array list to store the cureent location for find the distance

                latlist = new ArrayList<>();
                lnglist = new ArrayList<>();

                Routing routing = new Routing.Builder()
                        .travelMode(AbstractRouting.TravelMode.DRIVING)
                        .withListener((RoutingListener) context)
                        .alternativeRoutes(true).waypoints(customer_source, customer_destination).build();
                routing.execute();

                break;

            case Constant.STOPRIDE:

                dialog.dismiss();
                removeUpdate();

                CSPreferences.putString(context, "path", "");
                CSPreferences.putString(context, "TOLLFEE", String.valueOf(tollfee));
                CSPreferences.putString(context, "STOPFEE", String.valueOf(stopfee));

                //  addtolllayout.setVisibility(View.GONE);
                //   starttriplayout.setVisibility(View.GONE);


                //    paymentcardview.setVisibility(View.VISIBLE);
                Intent intent = new Intent(PathMapActivity.this, CashCollect.class);
                startActivity(intent);


                if (CSPreferences.readString(context, "payment_method").equals("1")) {

                    text_paymentstaus.setText("Credit card");
                } else {
                    text_paymentstaus.setText("Cash");
                }
                finish();
                break;
            case Constant.STOPRIDEFAIL:
                dialog.dismiss();
                //   Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
                break;

            case Constant.MY_LOCATION:
                if (!isFirstMarker) {
                    double lat = ModelManager.getInstance().getNearestRoadManager().driverlat;
                    double lng = ModelManager.getInstance().getNearestRoadManager().driverlongititude;
                    myLocation_Start = carMarker.getPosition();
                    myLocation_End = new LatLng(lat, lng);
                    //drawPath(myLocation_Start, myLocation_End);
                    moveCarAnimation(lat, lng);
                }
                break;


            case Constant.CANCELTRIP:

                dialog.dismiss();

                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Cacel Trip")
                        .setContentText(event.getValue())
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
                break;

            case Constant.DRIVERSTATUS:

                String drivermessage = event.getValue();
                //     Toast.makeText(context, "" + drivermessage, Toast.LENGTH_SHORT).show();

                break;

            case Constant.CASHNOTCOLLECT:
                //   Toast.makeText(context, " please try again", Toast.LENGTH_SHORT).show();

                break;

            case Constant.CANCEL_RIDEFCM:

                messagedialog.show();

                TextView titletext = (TextView) messagedialog.findViewById(R.id.txttitle);
                TextView messagetext = (TextView) messagedialog.findViewById(R.id.txtmessage);

                TextView ok = (TextView) messagedialog.findViewById(R.id.txtok);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(PathMapActivity.this, HomeScreenActivity.class));
                        finish();
                    }
                });
                break;
            case Constant.LOCATIONSEND:

                //    Toast.makeText(context, "send", Toast.LENGTH_SHORT).show();

                break;

            case Constant.COLLECTCASH:

                ratingToCustomer();

                break;

        }
    }
    // here we perofrm of collectcash fuctionality

    public void onclickCollect(View view) {

// if the customer select the payment method as acredit card
        double totaldistance = findDistance();

        if (CSPreferences.readString(context, "payment_method").equals("1")) {


            double totalamount = 0.0;

            //collect the total amount of the ride

            totalamount = totalamount + totaldistance * ConfigVariable.dbdistancefare + 10 * ConfigVariable.dbtimefare +
                    ConfigVariable.dbbasefare + Double.parseDouble(CSPreferences.readString(context, "TOLLFEE")) +
                    Double.parseDouble(CSPreferences.readString(context, "STOPFEE"));
            Log.e("total amount ", totalamount + "");


            // send the ride amount to the server and server sen the amount to the customer and amount deduct from customer card

            ModelManager.getInstance().getCollectCashmanager().CollectCashmanager(context, Operations.collectCash(context,
                    CSPreferences.readString(context, "customer_id"), CSPreferences.readString(context, "request_id"),
                    customerRequest.getCutomerid(), String.valueOf(totalamount)));


            return;

        }


        Intent intent = new Intent(context, CashCollect.class);
        CSPreferences.putString(context, "distance", String.valueOf(totaldistance));

        startActivity(intent);

    }

    public void ratingToCustomer() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.activity_rating);
        bottomSheetDialog.show();
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(android.support.design.R.id.design_bottom_sheet);
                assert bottomSheet != null;
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        TextView submit = (TextView) bottomSheetDialog.findViewById(R.id.btSubmit);
        final RatingBar ratingBar = (RatingBar) bottomSheetDialog.findViewById(R.id.rbUserrating);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String rating = String.valueOf(ratingBar.getRating());

                ModelManager.getInstance().getRatingManager().RatingManager(context, Operations.sendRatingtoCustomer(context,
                        CSPreferences.readString(context, "customer_id"), customerRequest.getCutomerid(), rating, "feedback"));


            }
        });
    }

    // find the distance between customer pickup location to destination

    private double findDistance() {

        double distance = 0.0;

        for (int i = 0; i < latlist.size(); i++) {

            if (i <= latlist.size() - 2) {

                Location startPoint = new Location("locationA");

                startPoint.setLatitude(latlist.get(i));
                startPoint.setLongitude(lnglist.get(i));

                Location endPoint = new Location("locationb");

                endPoint.setLatitude(latlist.get(i + 1));
                endPoint.setLongitude(lnglist.get(i + 1));

                distance = distance + startPoint.distanceTo(endPoint);
            }
        }

        return distance;
    }

    @Override
    public void onLocationChanged(Location location) {
        double dest_lat;
        double dest_long;

        lat = location.getLatitude();
        lng = location.getLongitude();

        currentlatlng = new LatLng(lat, lng);
        String points = lat + "," + lng;
        ModelManager.getInstance().getNearestRoadManager().NearestRoadManager(this, Operations.nearestRoadlatlng(this, points), 1);

        Utils.getCompleteAddressString(context, Double.parseDouble(customerRequest.getDroplat()),
                Double.parseDouble(customerRequest.getDroplng()));

        if (state == 2) {
            dest_lat = customer_destination.latitude;
            dest_long = customer_destination.longitude;
        } else {
            dest_lat = customer_source.latitude;
            dest_long = customer_source.longitude;
        }
        ModelManager.getInstance().getNearestRoadManager().NearestRoadManager(this, Operations.nearestDistancelatlng(this,
                lat, lng, dest_lat, dest_long), 2);

        //  moveCarAnimation(lat,lng);
        CSPreferences.putString(context, "start_lat", String.valueOf(lat));
        CSPreferences.putString(context, "start_lng", String.valueOf(lng));

        Log.e("current_location", currentlatlng.toString());
        float bearing = location.getBearing();

        Log.e("location bearing", "" + String.valueOf(bearing));


        //  Toast.makeText(context, "" + String.valueOf(lat), Toast.LENGTH_SHORT).show();

        latlist.add(lat);
        lnglist.add(lng);

        Log.e("curent lat lng", latlist.toString());

        Log.e(" stop trip  ", " Stop trip value is : " + stopTrips_status);

        if (stopTrips_status.equals("false")) {

            Log.e("stop trips11111", stopTrips_status);
            Log.e("driver locatsion ", String.valueOf(lat) + "     " + String.valueOf(lng));

            //{id=30.7097883, ride_request_id=76.6934938, noti_type=driver location}

            JSONObject jsonObject = JsonRequestIR.jsonRequestForLatLong(getApplicationContext(), lat, lng);
            Utils.sendMessage(this, jsonObject);

            ModelManager.getInstance().getLocationSendManager().locationsend(context, Operations.sendLocationurl(context,
                    String.valueOf(lat), String.valueOf(lng), customerRequest.getCutomerid(), CSPreferences.readString(context,
                            "customer_id")));


        } else {

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
            locationManager.removeUpdates(this);
        }


    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);

        //   Toast.makeText(getBaseContext(), "Gps is turned off!! ", Toast.LENGTH_SHORT).show();
    }

    private void rotateMarker(final Marker marker, final float toRotation) {

        if (!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 3000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    float bearing = -rot > 180 ? rot / 2 : rot;

                    marker.setRotation(bearing);

                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }

    private void removeUpdate() {

        //   Toast.makeText(context, "removeupadte", Toast.LENGTH_SHORT).show();

        stopTrips_status = "true";

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
        locationManager.removeUpdates(this);
    }

    private void moveCarAnimation(double lat, double lng) {
        LatLng latLng = new LatLng(lat, lng);
        if (isFirstPosition) {
            startPosition = latLng;
//            carMarker = googleMap.addMarker(new MarkerOptions().position(startPosition).
//                    flat(true).icon(BitmapDescriptorFactory.fromResource(R.mipmap.new_car_small)));
            //carMarker.setAnchor(0.5f, 0.5f);
            isFirstPosition = false;

        } else {
            endPosition = new LatLng(lat, lng);
            Log.d(TAG, startPosition.latitude + "--" + endPosition.latitude + "--Check --" + startPosition.longitude + "--" + endPosition.longitude);
            if ((startPosition.latitude != endPosition.latitude) || (startPosition.longitude != endPosition.longitude)) {
                Log.e(TAG, "NOT SAME");
                Utils.getInstance().startBikeAnimation(googleMap, carMarker, carMarker.getPosition(), endPosition);
            } else {
                Log.e(TAG, "SAMME");
            }
        }
    }

    public void onArrivingState() {

    }

    public void onBeginTripState() {
        txt_begin_trip.setVisibility(View.GONE);
        swipe_StartTrip.setVisibility(View.VISIBLE);
    }

    public void onCompleteTripState() {
        swipe_StartTrip.setVisibility(View.GONE);
        trip_begin();
        visibleButton();
    }


    public void drawPath(LatLng startPosition, LatLng end) {
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(startPosition, end)
                .build();
        routing.execute();
    }


    private void drawPolyLine(ArrayList<Route> arrayList) {
        String focuspostion = null;
        int min = 0;
        int indexvalue = 0;

        polylines = new ArrayList<>();

        if (isFirstTime && state == 0) {
            lastlat = customer_source;
        } else {
            lastlat = customer_destination;

        }

        PolylineOptions polyOptions = null;

        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {

                poly.remove();
            }
        }

        for (int j = 0; j < arrayList.size(); j++) {

            int colorIndex = j % COLORS.length;
            polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[4]));
            polyOptions.width(7 + j * 5);
            if (j > 0) {

                if (min > arrayList.get(j).getDurationValue()) {
                    indexvalue = j;
                    min = arrayList.get(j).getDistanceValue();

                }
            } else {

                min = arrayList.get(j).getDistanceValue();

            }
            Log.e("route", String.valueOf(j + 1) + "     distance--" + arrayList.get(j).getDistanceValue() + "  duration---" + arrayList.get(j).getDurationValue());

        }

        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(midlat,midlng),10));
        if (isFirstTime) {
            isFirstTime = false;
            isFirstMarker = false;
            MarkerOptions options1 = new MarkerOptions();
            options1.position(myLocation_Start);
            options1.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_car_new));
            carMarker = googleMap.addMarker(options1);

            //ß googleMap.addMarker(options1);
            options1 = new MarkerOptions();
            options1.position(lastlat);
            options1.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_pin_white));
            stopMarker = googleMap.addMarker(options1);

            if (state == 0) {
                assert polyOptions != null;
                polyOptions.addAll(arrayList.get(indexvalue).getPoints());
                polyline_Route = googleMap.addPolyline(polyOptions);
                polylines.add(polyline_Route);
            }

        } else {
            firstlat = myLocation_Start;
            lastlat = myLocation_End;
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
        LatLngBounds bounds = builder.build();
        //   CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
        // googleMap.animateCamera(mCameraUpdate);

        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLACK);
        polylineOptions.width(10);
        polylineOptions.startCap(new SquareCap());
        polylineOptions.endCap(new SquareCap());
        polylineOptions.jointType(JointType.ROUND);
        polylineOptions.addAll(polyLineList);
        greyPolyLine = googleMap.addPolyline(polylineOptions);


        //other project <code></code>


        blackPolylineOptions = new PolylineOptions();
        blackPolylineOptions.width(10);
        blackPolylineOptions.color(Color.BLACK);
        blackPolylineOptions.startCap(new SquareCap());
        blackPolylineOptions.endCap(new SquareCap());
        blackPolylineOptions.jointType(JointType.ROUND);
        blackPolyline = googleMap.addPolyline(blackPolylineOptions);

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
        if (polylines != null && polylines.size() > 0) {
            polyline_Route.remove();
        }

    }


}
