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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.CustomerReQuestManager;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.Beans.CustomerRequest;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.ConfigVariable;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class StopTripsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,OnMapReadyCallback,RoutingListener,LocationListener {
    GoogleApiClient googleApiClient;

    ArrayList<Polyline> polylines;
    Context context;
    Location mLastLocation;

    CardView complete_tripcard;
    RelativeLayout addtollPayment;
    ArrayList<CustomerRequest> list;
    CustomerRequest customerRequest;

    ArrayList<Double> latlist;
    ArrayList<Double> lnglist;
    GoogleMap googleMap;

    int tollfee = 0;
    int stopfee = 0;
    Dialog canceltripdialog, addtolldialog, stopfeeDailog;

    LocationManager locationManager;
    Double lat, lng;
    AlertDialog dialog;


    LatLng currentlatlng,customer_source,customer_destination;
    Toolbar toolbar;
    ImageView navigate_image;
    TextView stoptrips, customer_name, payment_method, complete_ride, addtoll, addstopfee, addresstext;


    private static final int[] COLORS = new int[]{R.color.pathcolor, R.color.colorPrimaryDark, R.color.colorAccent, R.color.colorAccent, R.color.primary_dark_material_light};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_trip_new);
        initViews();
    }


    public void initViews() {

      /*  toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("On Trips");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
*/
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


       // complete_tripcard = (CardView) findViewById(R.id.paymentcard);
      //  addtollPayment = (RelativeLayout) findViewById(R.id.laoutaddtoll);


        customer_name = (TextView) findViewById(R.id.txtname);
        stoptrips = (TextView) findViewById(R.id.stoptrips);
        payment_method = (TextView) findViewById(R.id.txtpayment);

     //   addtoll = (TextView) findViewById(R.id.txtaddtoll);
   //     addstopfee = (TextView) findViewById(R.id.txtaddstopfee);


        navigate_image = (ImageView) findViewById(R.id.navigate);
        addresstext = (TextView) findViewById(R.id.txtdestaddress);


        //here we  get the detail of cutomer whoes make the request using bean class
        list = new ArrayList<>();
        list = CustomerReQuestManager.requestlis;
        customerRequest = list.get(0);

        customer_source = new LatLng(Double.parseDouble(customerRequest.getSourcelat()), Double.parseDouble(customerRequest.getSourcelng()));
        customer_destination = new LatLng(Double.parseDouble(customerRequest.getDroplat()), Double.parseDouble(customerRequest.getDroplng()));


    }

    // on click of navigation image  this fuction navigate to you

    public void onclickNavigate(View view) {

        String uri = "http://maps.google.com/maps?daddr=" + customer_destination.latitude + "," + customer_destination.longitude + " (" + "Drop location" + ")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);

    }

    // clicklistner of stop button this fuction used to stop the ride of customer

    public void onStop_Click(View view) {

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

    // clicklistner of   addtoll payment  when customer
    public void addToll(View view) {

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


    public void stopFee(View view) {


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
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap mgoogleMap) {
        this.googleMap = mgoogleMap;


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

    @Override
    public void onLocationChanged(Location location) {


        Log.e("onlocation ","change");

        lat = location.getLatitude();
        lng =location.getLongitude();

        CameraPosition position = CameraPosition
                .builder().target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(12f)
                .bearing(0.5f)
                .tilt(0.0f)
                .build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));

        currentlatlng = new LatLng(lat,lng);
        Log.e("current loaction",String.valueOf(currentlatlng));
          Log.e("destion value","dd"+String.valueOf(ConfigVariable.customer_destination));



        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener((RoutingListener) context)
                .alternativeRoutes(true)
                .waypoints(currentlatlng, customer_destination)
                .build();
        routing.execute();

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    @Override
    protected void onStart() {
        super.onStart();

        googleApiClient.connect();

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

    }


    @Override
    public void onRoutingFailure(RouteException e) {

        Log.e("exception",e.toString());

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {

        Log.e("msmns","kskskks");


        polylines = new ArrayList<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.35);

      /*  float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int padding = (int) (40 * scale + 0.5f);*/
        Log.e("padding", String.valueOf(padding));



        builder.include(currentlatlng);
        builder.include(customer_destination);
        LatLngBounds bounds = builder.build();


        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        googleMap.moveCamera(cu);




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






        // Start marker
           MarkerOptions options = new MarkerOptions();
        options.position(customer_destination);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_user));
         googleMap.addMarker(options);
    }


    @Override
    public void onRoutingCancelled() {

    }


}
