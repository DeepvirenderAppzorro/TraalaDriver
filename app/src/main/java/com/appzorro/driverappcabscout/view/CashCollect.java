package com.appzorro.driverappcabscout.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.CollectCashmanager;
import com.appzorro.driverappcabscout.controller.CustomerReQuestManager;
import com.appzorro.driverappcabscout.model.Beans.CashCollect_bean;
import com.appzorro.driverappcabscout.model.Beans.CustomerRequest;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Utils;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import okhttp3.internal.Util;

public class CashCollect extends AppCompatActivity implements  OnMapReadyCallback, RoutingListener, GoogleApiClient.OnConnectionFailedListener {
    private static final int[] COLORS = new int[]{R.color.pathcolor, R.color.colorPrimaryDark, R.color.colorAccent, R.color.colorAccent, R.color.primary_dark_material_light,R.color.black};
    Toolbar toolbar;
    TextView basefare, timefare, discount, totalfare, disancefare, collectCash, tollfee, stopfee,date_tv;
    double dbtimefare = 1, dbdistancefare = 0.05, dbbasefare = 40, dbtoatl;
    Context context;
    double totalamount = 0;
    CustomerRequest customerRequest;
    android.app.AlertDialog dialog;
    BottomSheetDialog bottomSheetDialog;
    GoogleMap googleMap;
    ArrayList<Polyline> polylines;
    LatLng currentlatlng, customer_source, customer_destination, firstlat, lastlat;
    ArrayList<CustomerRequest> list;
    protected LatLng start, end;
    LatLng latLng;
    double lat, lng;
    Dialog messagedialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_cash_collect);
        context = CashCollect.this;

        initviews();
       /* dialog = new SpotsDialog(context);
        dialog.show();*/
      /*  ModelManager.getInstance().getCollectCashmanager().CollectCashmanager(context, Operations.collectCash(context,
                CSPreferences.readString(context, "customer_id"), customerRequest.getRequestid(), customerRequest.getCutomerid(), totalfare.getText().toString()));
*/

        collectCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(CashCollect.this, Rating.class);
                startActivity(it);
                finish();


            }
        });

    }

    public void initviews() {

        basefare = (TextView) findViewById(R.id.txtbasefare);
        timefare = (TextView) findViewById(R.id.txttimefare);
        discount = (TextView) findViewById(R.id.txtriderdiscount);
        totalfare = (TextView) findViewById(R.id.txtcash);
        disancefare = (TextView) findViewById(R.id.txtdistancefare);
        collectCash = (TextView) findViewById(R.id.collectcash);
        tollfee = (TextView) findViewById(R.id.txttollamount);
        stopfee = (TextView) findViewById(R.id.txtstopfee);
        stopfee = (TextView) findViewById(R.id.txtstopfee);
        date_tv = (TextView) findViewById(R.id.txt_date);
        messagedialog = Utils.createMessageDialog(context);

        // Toast.makeText(context, "distance " + CSPreferences.readString(context, "distance"), Toast.LENGTH_SHORT).show();

        tollfee.setText(CSPreferences.readString(context, "TOLLFEE"));
        stopfee.setText(CSPreferences.readString(context, "STOPFEE"));
        date_tv.setText(Utils.getCurrentDate());

        totalamount = (totalamount + (10 * dbtimefare) + (1000 * dbdistancefare) + 10.0
                + 5.0 + dbbasefare);

        Log.e("total ammount", String.valueOf(totalamount));

        basefare.setText("$"+String.valueOf(dbbasefare));
        timefare.setText("$"+String.valueOf(10 * dbtimefare));
        disancefare.setText("$"+String.valueOf(1000 * dbdistancefare));
        discount.setText("$"+"50");
        totalamount = totalamount - 50.00;
        totalfare.setText("$"+String.valueOf(totalamount));
        //here we  get the detail of cutomer whoes make the request using bean class
        list = new ArrayList<>();
        list = CustomerReQuestManager.requestlis;
        if(list!=null)
        {
            customerRequest = list.get(0);

        }
        if (customerRequest != null) {
            String profileimage = customerRequest.getProfilepic();
            Log.e("image ", profileimage);
            customer_source = new LatLng(Double.parseDouble(customerRequest.getSourcelat()), Double.parseDouble(customerRequest.getSourcelng()));
            customer_destination = new LatLng(Double.parseDouble(customerRequest.getDroplat()), Double.parseDouble(customerRequest.getDroplng()));


        } else {
            Utils.makeSnackBar(context,timefare,"No data available");

          //  Toast.makeText(this, "No data available", Toast.LENGTH_LONG).show();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map12);
        mapFragment.getMapAsync(this);


        Log.e("total ammount", String.valueOf(totalamount));

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(context);

    }


    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(context);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {

            case Constant.COLLECTCASH:
                dialog.dismiss();

                ArrayList<CashCollect_bean> cashCollect_beans = CollectCashmanager.cash_list;
                for (int i = 0; i < cashCollect_beans.size(); i++) {
                    String Base_fare = cashCollect_beans.get(i).getBase();
                    basefare.setText(Base_fare);
                    String tym = cashCollect_beans.get(i).getTym();
                    timefare.setText(tym);
                    String dis_fare = cashCollect_beans.get(i).getDis();
                    disancefare.setText(dis_fare);

                }

                break;

            case Constant.CASHNOTCOLLECT:

                dialog.dismiss();
                Utils.makeSnackBar(context,timefare,"please try again");

             //   Toast.makeText(context, "please try again", Toast.LENGTH_SHORT).show();
                break;

            case Constant.SERVER_ERROR:

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Utils.makeSnackBar(context,timefare,"Network connection is very slow please check your connection");

              //  Toast.makeText(context, "Network connection is very slow please check your connection", Toast.LENGTH_SHORT).show();

                break;
            case Constant.CANCEL_RIDEFCM:

                messagedialog.show();

                TextView titletext = (TextView) messagedialog.findViewById(R.id.txttitle);
                TextView messagetext = (TextView) messagedialog.findViewById(R.id.txtmessage);

                TextView ok = (TextView) messagedialog.findViewById(R.id.txtok);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(CashCollect.this,HomeScreenActivity.class));

                        finish();
                    }
                });
                break;
        }

    }


    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
        int time = 0;
        double distnace1 = 0.0;

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.20);

        // dialog.dismiss();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(Double.parseDouble(customerRequest.getSourcelat()), Double.parseDouble(customerRequest.getSourcelng())));
        builder.include(new LatLng(Double.parseDouble(customerRequest.getDroplat()), Double.parseDouble(customerRequest.getDroplng())));
        LatLngBounds bounds = builder.build();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int j = 0; j < arrayList.size(); j++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[5]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(arrayList.get(i).getPoints());
            Polyline polyline = googleMap.addPolyline(polyOptions);
            polylines.add(polyline);

            time = arrayList.get(i).getDurationValue();
            distnace1 = arrayList.get(i).getDistanceValue() / 1000;
            double d = (arrayList.get(i).getDistanceValue()) / 1000;

        }

        // Start marker
        MarkerOptions options = new MarkerOptions();
        options.position(start);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_pin_white));

        googleMap.addMarker(options);

        // End marker
        options = new MarkerOptions();
        options.position(end);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_black_pin));
        googleMap.addMarker(options);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom((end), 12.0f));

    }

    @Override
    public void onRoutingCancelled() {

    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap1) {
        googleMap = googleMap1;

        try {

            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style_retro));
            if (!success) {
                Log.e("sorry try again", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        googleMap.clear();

        start = new LatLng(Double.parseDouble(customerRequest.getSourcelat()), Double.parseDouble(customerRequest.getSourcelng()));
        end = new LatLng(Double.parseDouble(customerRequest.getDroplat()), Double.parseDouble(customerRequest.getDroplng()));
        lat = Double.parseDouble(customerRequest.getSourcelat());
        lng = Double.parseDouble(customerRequest.getSourcelng());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(end, 5);
        googleMap.animateCamera(cameraUpdate);
        latLng = new LatLng(lat, lng);
        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(start, end)
                .build();

        routing.execute();
    }


}
