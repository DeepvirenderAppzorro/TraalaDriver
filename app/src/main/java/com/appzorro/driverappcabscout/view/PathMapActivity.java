package com.appzorro.driverappcabscout.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
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
import com.appzorro.driverappcabscout.model.Config;
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
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
    ArrayList<Double>latlist;
    ArrayList<Double>lnglist;
    Dialog canceltripdialog,addtolldialog,stopfeeDailog;
    RadioButton reasonbutton;
    int tollfee=0;
    int stopfee=0;
    LatLng currentlatlng;
    String mapzoomstatus ="yes";
    ImageView callimage, cancelimage, customerimage;
    TextView makecalltext, customername, stoptrips, customer_name, payment_method, collect_cash, complete_ride,addtoll,addstopfee;
    public RadioButton radioButton;

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
                     /*ModelManager.getInstance().getArrivedManager().ArrivedManager(context, Operations.arrivedDriver(context,
                             CSPreferences.readString(context,"customer_id"),HomeScreenActivity.request_id));
*/
                    ModelManager.getInstance().getArrivedManager().ArrivedManager(context, Operations.arrivedDriver(context,
                            "4", customerRequest.getRequestid()));
                } else {

                    makecalltext.setText("Arrived");
                    dialog = new SpotsDialog(context);


                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss a");
                    Calendar calander = Calendar.getInstance();
                    String currentDate = DateFormat.getDateInstance().format(Calendar.getInstance().getTime()).replaceAll(" ", "");
                    Log.e("date and time ", currentDate);
                    String time = simpleDateFormat.format(calander.getTime()).replaceAll(" ", "");


                    dialog.show();
                   /*  ModelManager.getInstance().getStartTripsManager().StartTripsManager(context,Operations.startTrips(
                             context,CSPreferences.readString(context,"customer_id"),customerRequest.getRequestid(),
                            time,currentDate,String.valueOf(lat)+","+String.valueOf(lng)
                     ));*/
                    ModelManager.getInstance().getStartTripsManager().StartTripsManager(context, Operations.startTrips(
                            context, "2", customerRequest.getRequestid(),
                            time, currentDate, String.valueOf(lat) + "," + String.valueOf(lng)
                    ));
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
                ModelManager.getInstance().getStopRideManager().StopRideManager(context, Operations.stopRide(context,
                        "2", customerRequest.getRequestid(), time, currentDate, String.valueOf(lat) + "," + String.valueOf(lng)));

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
                final EditText amount = (EditText)addtolldialog.findViewById(R.id.edttotalamount);
                EditText description =(EditText)addtolldialog.findViewById(R.id.edtdescription);
                TextView add= (TextView)addtolldialog.findViewById(R.id.txtadd);
                TextView cancel=(TextView)addtolldialog.findViewById(R.id.txtcancel);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (amount.getText().toString().isEmpty()){

                            amount.setError("Required");
                        }
                        else {

                           tollfee =tollfee+Integer.parseInt(amount.getText().toString());
                            Log.e("tollfee",String.valueOf(tollfee));
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
                                final EditText editstopamount = (EditText)stopfeeDailog.findViewById(R.id.edtstopamount);
                                TextView addstopfee = (TextView)stopfeeDailog.findViewById(R.id.txtadd);
                                TextView  cancel =(TextView)stopfeeDailog.findViewById(R.id.txtcancel);
                                addstopfee.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View view) {

                                      if (editstopamount.getText().toString().isEmpty()){

                                          editstopamount.setError("Required");
                                      }
                                      else {

                                          stopfee =stopfee+Integer.parseInt(editstopamount.getText().toString());
                                          Log.e("stofee",String.valueOf(stopfee));
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
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        addtolllayout =(RelativeLayout)findViewById(R.id.laoutaddtoll);
        addtoll =(TextView)findViewById(R.id.txtaddtoll);
        addstopfee=(TextView)findViewById(R.id.txtaddstopfee);
        addtolllayout.setVisibility(View.GONE);
        paymentcardview = (CardView) findViewById(R.id.paymentcard);


        //here we  get the detail of cutomer whoes make the request using bean class
        list = new ArrayList<>();
        list = CustomerReQuestManager.requestlis;
        customerRequest = list.get(0);
        Picasso.with(this)
                .load(customerRequest.getProfilepic())
                .into(customerimage);
        customername.setText(customerRequest.getName());


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getLastBestStaleLocation();
            }
        }, 1000);


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
        Location networklocation= locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (gpslocation != null && networklocation != null)
        {
            if (gpslocation.getTime() > networklocation.getTime())
            {
                bestresult = gpslocation;
                Log.e("result", "both location are found---- "+ bestresult.getLatitude());
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
            }
            else {
                bestresult = networklocation;
                Log.e("result", "network location ---- "+ bestresult.getLatitude());
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
            }
        }
        else if (gpslocation != null)
        {
            bestresult = gpslocation;
            Log.e("result", "gps location only found---- "+ bestresult.getLatitude());
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        } else if (networklocation != null)
        {
            bestresult = networklocation;
            Log.e("result", "network location only found--- "+ bestresult.getLatitude());
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
            Log.e("",bestresult.toString());
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
        styleManager.addStyle(10,R.raw.map_style_retro);


        googleMap.setMyLocationEnabled(true);

        googleMap.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
            @Override
            public void onCameraMoveCanceled() {

            }
        });
        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {


            }
        });
     /*   Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener((RoutingListener) context)
                .alternativeRoutes(true)
                .waypoints(Config.startpoint, Config.endpoint)
                .build();
        routing.execute();*/
    }
    private void initCamera(Location mLocation) {

        Log.e("init camera","fuction are called");
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraPosition position = CameraPosition
                .builder().target(new LatLng(mLocation.getLatitude(),mLocation.getLongitude()))
                .zoom(15f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();
        lat = mLocation.getLatitude();
        lng = mLocation.getLongitude();
        currentlatlng = new LatLng(lat,lng);

        //googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
        MarkerOptions options = new MarkerOptions();
        options.position(currentlatlng);
        options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car));

       /*googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,
                lng)));*/
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

        polylines = new ArrayList<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        if (mapzoomstatus.equals("yes")){
            builder.include(Config.startpoint);
            builder.include(Config.endpoint);
            LatLngBounds bounds = builder.build();


            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 280));
        }
        else {
            builder.include(currentlatlng);
            builder.include(Config.startpoint);
            LatLngBounds bounds = builder.build();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));

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
            polyOptions.width(7 + j * 1);

            if (j > 0) {
                if (min > arrayList.get(j).getDurationValue()) {

                    indexvalue = j;
                    min = arrayList.get(j).getDistanceValue();

                }
            } else {


                min = arrayList.get(j).getDistanceValue();

            }
            Log.e("route", String.valueOf(j + 1) + "     distance--" + arrayList.get(j).getDistanceValue() + "  duration---" + arrayList.get(j).getDurationValue());
          //  Toast.makeText(getApplicationContext(), "Route " + (j + 1) + ": distance - " + arrayList.get(j).getDistanceValue() + ": duration - " + arrayList.get(j).getDurationValue(), Toast.LENGTH_SHORT).show();
        }
        polyOptions.addAll(arrayList.get(indexvalue).getPoints());
        Polyline polyline = googleMap.addPolyline(polyOptions);
        Log.e("shortest distance", String.valueOf(arrayList.get(indexvalue).getDistanceValue()));
        Log.e("shortest duration", String.valueOf(arrayList.get(indexvalue).getDurationValue()));
        polylines.add(polyline);
        MarkerOptions options = new MarkerOptions();

        if (mapzoomstatus.equals("yes")) {

            options.position(Config.startpoint);
            options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.user));
            googleMap.addMarker(options);
        }
        else {


             options = new MarkerOptions();
             options.position(currentlatlng);
             options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car));
             googleMap.addMarker(options);
        }

        // End marker
        options = new MarkerOptions();
        options.position(Config.startpoint);
        options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.user));
        googleMap.addMarker(options);



      //  googleMap.animateCamera(CameraUpdateFactory.zoomTo(googleMap.getCameraPosition().zoom - 12f));

    }

    @Override
    public void onRoutingCancelled() {

    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {

            case Constant.ARRIVEDSTATUS:
                dialog.dismiss();
             //   Toast.makeText(PathMapActivity.this, "You are arrived customer location", Toast.LENGTH_SHORT).show();
                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Arrived")
                        .setContentText("You are arrived on the customer location please make the call to customer")
                        .show();
                break;

            case  Constant.SERVER_ERROR:
                dialog.dismiss();
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Please check your internet connection")
                        .show();
                break;
            case Constant.STARTTRIPS:
                dialog.dismiss();
                starttriplayout.setVisibility(View.GONE);
                addtolllayout.setVisibility(View.VISIBLE);
                String message = event.getValue();

                latlist = new ArrayList<>();
                lnglist = new ArrayList<>();
                googleMap.getUiSettings().setMapToolbarEnabled(true);
                /*new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Started")
                        .setContentText(message)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                googleMap.getUiSettings().setMapToolbarEnabled(true);
                            }
                        })
                        .show();*/
                break;
            case Constant.STOPRIDE:
                dialog.dismiss();
                CSPreferences.putString(context,"TOLLFEE",String.valueOf(tollfee));
                CSPreferences.putString(context,"STOPFEE",String.valueOf(stopfee));
                 addtolllayout.setVisibility(View.GONE);
                 starttriplayout.setVisibility(View.GONE);
                paymentcardview.setVisibility(View.VISIBLE);
                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Stop")
                        .setContentText(event.getValue())
                        .show();
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

        }
    }
 // here we perofrm of collectcash fuctionality

    public void onclickCollect(View view){

        Intent intent = new Intent(context,CashCollect.class);
        startActivity(intent);





    }

    @Override
    public void onLocationChanged(Location location) {

        lat= location.getLatitude();
        lng= location.getLongitude();
        latlist.add(lat);
        lnglist.add(lng);

        mapzoomstatus="no";

        LatLng latlng=new LatLng(location.getLatitude(),location.getLongitude());
        //This methods gets the users current longitude and latitude.
        Routing routing = new Routing.Builder()

                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener((RoutingListener) context)
                .alternativeRoutes(true).waypoints(latlng, Config.startpoint).build();
                 routing.execute();


        Log.e("REULT", " Latitude and Langtitude  : = "+ latlng);

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

}
