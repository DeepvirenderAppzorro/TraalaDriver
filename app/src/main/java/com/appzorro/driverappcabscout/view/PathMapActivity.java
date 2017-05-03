package com.appzorro.driverappcabscout.view;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

import static com.appzorro.driverappcabscout.R.id.makecall;

public class PathMapActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, RoutingListener, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleApiClient googleApiClient;
    GoogleMap googleMap;
    ArrayList<Polyline> polylines;
    Context context;
    Location mLastLocation;

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

    float bearing;
    Toolbar toolbar;
    Marker marker = null;
    boolean isMarkerRotating;

    LatLng currentlatlng, customer_source, customer_destination, firstlat, lastlat;
    String startTrips_status = "false", stopTrips_status = "false";

    Dialog messagedialog;

    ImageView callimage, cancelimage, customerimage, navigate_image;
    TextView makecalltext, customername, stoptrips, customer_name, payment_method, collect_cash, complete_ride, addtoll, addstopfee, addresstext;


    private static final int[] COLORS = new int[]{R.color.pathcolor, R.color.colorPrimaryDark, R.color.colorAccent, R.color.colorAccent, R.color.primary_dark_material_light};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_map);
        context = this;
        initViews();
        makecalltext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (makecalltext.getText().toString().equals("Arrived")) {
                    makecalltext.setText("Start");
                    dialog = new SpotsDialog(context);
                    dialog.show();
                    ModelManager.getInstance().getArrivedManager().ArrivedManager(context, Operations.arrivedDriver(context,
                            CSPreferences.readString(context, "customer_id"), customerRequest.getRequestid()));

                } else {

                    makecalltext.setText("Arrived");
                    dialog = new SpotsDialog(context);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss a");
                    Calendar calander = Calendar.getInstance();
                    String currentDate = DateFormat.getDateInstance().format(Calendar.getInstance().getTime()).replaceAll(" ", "");
                    Log.e("date and time ", currentDate);
                    String time = simpleDateFormat.format(calander.getTime()).replaceAll(" ", "");

                    Log.e("driver id ", CSPreferences.readString(context, "customer_id"));
                    dialog.show();

                    ModelManager.getInstance().getStartTripsManager().StartTripsManager(context, Operations.startTrips(
                            context, CSPreferences.readString(context, "customer_id"), customerRequest.getRequestid(),
                            time, currentDate, String.valueOf(lat) + "," + String.valueOf(lng)
                    ));
                 /*   ModelManager.getInstance().getStartTripsManager().StartTripsManager(context, Operations.startTrips(
                            context, "2", customerRequest.getRequestid(),
                            time, currentDate, String.valueOf(lat) + "," + String.valueOf(lng)
                    ));*/
                }

            }
        });

//here we stop the the trips by using click on this button
        stoptrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss a");
                Calendar calander = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance().format(Calendar.getInstance().getTime()).replaceAll(" ", "");
                Log.e("date and time ", currentDate);
                String time = simpleDateFormat.format(calander.getTime()).replaceAll(" ", "");

                dialog = new SpotsDialog(context);
                dialog.show();
                Log.e("nkjkf;vf", CSPreferences.readString(context, "customer_id"));
                ModelManager.getInstance().getStopRideManager().StopRideManager(context, Operations.stopRide(context,
                        CSPreferences.readString(context, "customer_id"), customerRequest.getRequestid(), time, currentDate, String.valueOf(lat) + "," + String.valueOf(lng)));

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

    public void initViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("On Trips");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

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

        complete_ride = (TextView) findViewById(R.id.txtcomplete);
        collect_cash = (TextView) findViewById(R.id.txtcollectcash);
        addtolllayout = (RelativeLayout) findViewById(R.id.laoutaddtoll);

        addtoll = (TextView) findViewById(R.id.txtaddtoll);
        addstopfee = (TextView) findViewById(R.id.txtaddstopfee);
        addtolllayout.setVisibility(View.GONE);

        paymentcardview = (CardView) findViewById(R.id.paymentcard);
        messagedialog = Utils.createMessageDialog(context);
        navigate_image = (ImageView) findViewById(R.id.navigate);
        addresstext = (TextView) findViewById(R.id.txtdestaddress);


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


    public Location getLastBestStaleLocation() {
        Location bestresult = null;

        latlist = new ArrayList<>();
        lnglist = new ArrayList<>();

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

            bestresult = gpslocation;
            Log.e("result", "both location are found---- " + bestresult.getLatitude());
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);

        } else if (gpslocation != null) {
            bestresult = gpslocation;
            Log.e("result", "gps location only found---- " + bestresult.getLatitude());
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
        } else if (networklocation != null && gpslocation == null) {
            bestresult = networklocation;
            Log.e("result", "network location only found--- " + bestresult.getLatitude());
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this);
            Log.e("", bestresult.toString());
        }
        return bestresult;
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


        addresstext.setText(Utils.getCompleteAddressString(context, Double.parseDouble(customerRequest.getSourcelat()),
                Double.parseDouble(customerRequest.getSourcelng())));

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


        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener((RoutingListener) context)
                .alternativeRoutes(true)
                .waypoints(currentlatlng, customer_source)
                .build();
        routing.execute();


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

        if (startTrips_status.equals("false")) {

            builder.include(currentlatlng);
            builder.include(customer_source);
            LatLngBounds bounds = builder.build();
            focuspostion = Utils.midPoint(currentlatlng.latitude, currentlatlng.longitude, customer_source.latitude,
                    customer_source.longitude);
            Log.e("mid point", "d" + focuspostion.toString());

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding - 50);
            googleMap.moveCamera(cu);

        } else {
            builder.include(customer_source);
            builder.include(customer_destination);
            LatLngBounds bounds = builder.build();
            focuspostion = Utils.midPoint(currentlatlng.latitude, currentlatlng.longitude, customer_source.latitude,
                    customer_source.longitude);
            Log.e("mid point", "d" + focuspostion.toString());

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding - 50);
            googleMap.moveCamera(cu);
            // googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new LatLng(focuspostion.latitude,focuspostion.longitude)));

        }
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
        polyOptions.addAll(arrayList.get(indexvalue).getPoints());
        Polyline polyline = googleMap.addPolyline(polyOptions);
        polylines.add(polyline);
        List<LatLng> list = polyline.getPoints();


        firstlat = list.get(0);
        lastlat = list.get(list.size() - 1);
        Toast.makeText(context, "aa" + firstlat.toString(), Toast.LENGTH_SHORT).show();

        String split[] = focuspostion.split(",");
        double midlat = Double.parseDouble(split[split.length - 2]);
        double midlng = Double.parseDouble(split[split.length - 1]);


        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(midlat,midlng),10));


        MarkerOptions options1 = new MarkerOptions();
        options1.position(lastlat);
        options1.icon(BitmapDescriptorFactory.fromResource(R.mipmap.user));
        googleMap.addMarker(options1);





       /* if (startTrips_status.equals("false")) {

            options.position(customer_source);
            options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.user));
            googleMap.addMarker(options);
            bearing = (float)Utils.bearingBetweenLocations(currentlatlng, customer_source);
            rotateMarker(marker, bearing);

        }
        else {

            options = new MarkerOptions();
             options.position(customer_source);
             options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car));
             marker=  googleMap.addMarker(options);
            float bearing = (float)Utils.bearingBetweenLocations(customer_source, customer_destination);
            rotateMarker(marker, bearing);
        }*/


    }

    @Override
    public void onRoutingCancelled() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event event) {
        switch (event.getKey()) {

            case Constant.ARRIVEDSTATUS:
                dialog.dismiss();
                //   Toast.makeText(PathMapActivity.this, "You are arrived customer location", Toast.LENGTH_SHORT).show();
                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Arrived")
                        .setContentText("You are arrived on the customer location please make the call to customer")
                        .show();
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
                startTrips_status = "true";

                starttriplayout.setVisibility(View.GONE);
                addtolllayout.setVisibility(View.VISIBLE);

                googleMap.clear();
                CustomerRequest customerRequest = list.get(0);

                customer_source = new LatLng(Double.parseDouble(customerRequest.getSourcelat()), Double.parseDouble(customerRequest.getSourcelng()));
                customer_destination = new LatLng(Double.parseDouble(customerRequest.getDroplat()), Double.parseDouble(customerRequest.getDroplng()));

                addresstext.setText(Utils.getCompleteAddressString(context, Double.parseDouble(customerRequest.getDroplat()),
                        Double.parseDouble(customerRequest.getDroplng())));

                Routing routing = new Routing.Builder()
                        .travelMode(AbstractRouting.TravelMode.DRIVING)
                        .withListener((RoutingListener) context)
                        .alternativeRoutes(true).waypoints(customer_source, customer_destination).build();
                routing.execute();

                break;
            case Constant.STOPRIDE:

                dialog.dismiss();

                removeUpdate();

                CSPreferences.putString(context, "TOLLFEE", String.valueOf(tollfee));
                CSPreferences.putString(context, "STOPFEE", String.valueOf(stopfee));

                addtolllayout.setVisibility(View.GONE);
                starttriplayout.setVisibility(View.GONE);
                if (CSPreferences.readString(context,"payment_method").equals("1")){


                    double totaldistance = findDistance();
                    double totalamount = 0.0;

                    totalamount = totalamount+totaldistance* ConfigVariable.dbdistancefare+10* ConfigVariable.dbtimefare+
                            ConfigVariable.dbbasefare+Double.parseDouble(CSPreferences.readString(context,"TOLLFEE"))+
                            Double.parseDouble(CSPreferences.readString(context,"STOPFEE"));

                }
                paymentcardview.setVisibility(View.VISIBLE);

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
                Toast.makeText(context, "" + drivermessage, Toast.LENGTH_SHORT).show();
                break;
            case Constant.CANCEL_RIDEFCM:

                messagedialog.show();
                TextView titletext = (TextView) messagedialog.findViewById(R.id.txttitle);
                TextView messagetext = (TextView) messagedialog.findViewById(R.id.txtmessage);
                TextView ok = (TextView) messagedialog.findViewById(R.id.txtok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        finish();
                    }
                });

                break;
            case Constant.LOCATIONSEND:

                Toast.makeText(context, "send", Toast.LENGTH_SHORT).show();
               /* Routing routing1 = new Routing.Builder()
                        .travelMode(AbstractRouting.TravelMode.DRIVING)
                        .withListener((RoutingListener) context)
                        .alternativeRoutes(true)
                        .waypoints(currentlatlng,customer_destination)
                        .build();
                routing1.execute();*/
                break;

            case Constant.LOCATIONNOTSEND:
                Toast.makeText(context, " not send", Toast.LENGTH_SHORT).show();
                break;

        }
    }
    // here we perofrm of collectcash fuctionality

    public void onclickCollect(View view) {

      /*  double distance = 0.0;

        for (int i = 0; i < latlist.size(); i++) {

            if (i <= latlist.size() - 2) {

                Location startPoint = new Location("locationA");

                startPoint.setLatitude(latlist.get(i));
                startPoint.setLongitude(latlist.get(i));

                Location endPoint = new Location("locationb");
                endPoint.setLatitude(latlist.get(i + 1));
                endPoint.setLongitude(latlist.get(i + 1));

                distance = distance + startPoint.distanceTo(endPoint);
            }


        }*/

        double distance = findDistance();

        Intent intent = new Intent(context, CashCollect.class);
        CSPreferences.putString(context, "distance", String.valueOf(distance));
        startActivity(intent);

    }

    // find the distance between customer pickup location to destination

    private double findDistance(){
        double distance =0.0;
        for (int i = 0; i < latlist.size(); i++) {

            if (i <= latlist.size() - 2) {

                Location startPoint = new Location("locationA");

                startPoint.setLatitude(latlist.get(i));
                startPoint.setLongitude(latlist.get(i));

                Location endPoint = new Location("locationb");

                endPoint.setLatitude(latlist.get(i + 1));
                endPoint.setLongitude(latlist.get(i + 1));

                distance = distance + startPoint.distanceTo(endPoint);
            }
        }

        return distance;
    }

    @Override
    public void onLocationChanged(Location location) {


        lat = location.getLatitude();
        lng = location.getLongitude();
        float bearing = location.getBearing();

        Log.e("location bearing", "" + String.valueOf(bearing));


        Toast.makeText(context, "" + String.valueOf(lat), Toast.LENGTH_SHORT).show();

        latlist.add(lat);
        lnglist.add(lng);

        Log.e(" stop trip  ", " Stop trip value is : " + stopTrips_status);

        if (stopTrips_status.equals("false")) {

            Log.e("driver locatsion ", String.valueOf(lat) + "     " + String.valueOf(lng));

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
        Toast.makeText(getBaseContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();
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

        Toast.makeText(context, "removeupadte", Toast.LENGTH_SHORT).show();

        stopTrips_status="true";

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
