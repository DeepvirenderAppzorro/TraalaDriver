package com.appzorro.driverappcabscout.view.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.appzorro.driverappcabscout.AppController;
import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.CollectAmtManager;
import com.appzorro.driverappcabscout.controller.CustomerReQuestManager;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.Beans.CashCollect_bean;
import com.appzorro.driverappcabscout.model.Beans.CustomerRequest;
import com.appzorro.driverappcabscout.model.Beans.CustomersRequestBean;
import com.appzorro.driverappcabscout.model.Beans.SnapShotMapResponse;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.JsonRequestIR;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;
import com.appzorro.driverappcabscout.view.Activity.PathmapActivity.model.CustomerListResponse;
import com.appzorro.driverappcabscout.view.Activity.PathmapActivity.view.PathMapActivity;
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
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.appzorro.driverappcabscout.view.Activity.PathmapActivity.view.PathMapActivity.selectedCustomerPosition;

public class CashCollect extends AppCompatActivity implements OnMapReadyCallback, RoutingListener, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMapLoadedCallback {
    private static final int[] COLORS = new int[]{R.color.colorRed, R.color.colorRed, R.color.colorRed, R.color.colorRed, R.color.colorRed, R.color.colorRed};
    protected LatLng start, end;
    Toolbar toolbar;
    TextView basefare, timefare, discount, totalfare, disancefare, collectCash, tollfee, stopfee, date_tv;
    Context context;
    String covertedImage;
    double totalamount = 0;
    public static String isCollect = "";
    CustomerRequest customerRequest;
    AlertDialog dialog;
    Bitmap bitmapGooglemap;
    GoogleMap googleMap;
    ArrayList<Polyline> polylines;
    LatLng currentlatlng, customer_source, customer_destination;
    ArrayList<CustomerRequest> list;
    LatLng latLng;
    int chkState;
    double lat, lng;
    String distanc = "";
    double distanceTraveled;
    Dialog messagedialog;
    String cabid, distance, time, profileImg;
    ArrayList<CashCollect_bean> cashList;
    @BindView(R.id.user_pic)
    CircleImageView userPic;
    String chkStatus = "";
    public static String chkCashtatus = "";
    List<LatLng> points;
    String customerToken = "";

    public static boolean isActiveCollect = false;
    CustomersRequestBean.Datum customer_list;
    CustomerListResponse.Datum listData;
    List<CustomerListResponse.Datum> datmList;
    CustomerListResponse customerListResponse;

    //chkstate=1 for not collect chkstate=2 for collect
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_cash_collect);
        ButterKnife.bind(this);
        context = CashCollect.this;
        new MyExceptionHandler(CashCollect.this);
        customerListResponse= CustomerReQuestManager.customerListResponse;
        listData =customerListResponse.getResponse().getData().get(selectedCustomerPosition);
       // customer_list = CustomerReQuestManager.customersRequestBean.getResponse().getData().get(selectedCustomerPosition);
        getIntents();
        initviews();
        isActiveCollect = true;
        points = PathMapActivity.lattLngList;
        setDistanceConvert();
        try{
            setTime();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        getAmtApi();
        ClickListeners();

        if (!listData.getProfilePic().isEmpty())
            Picasso.with(this).load(Config.baserurl_image + listData.getProfilePic()).placeholder(R.drawable.ic_icon_profile_pic).error(R.drawable.ic_icon_profile_pic).into(userPic);


    }
    public void initviews() {
        points = new ArrayList<>();
        cashList = new ArrayList<>();
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
        //here we  get the detail of cutomer whoes make the request using bean class

        if (listData != null) {
            profileImg = listData.getProfilePic();
            customer_source = new LatLng(Double.parseDouble(Utils.getLatitude(listData.getPickupCordinates())), Double.parseDouble(Utils.getLongitude(listData.getPickupCordinates())));
            customer_destination = new LatLng(Double.parseDouble(Utils.getLatitude(listData.getDropCordinates())), Double.parseDouble(Utils.getLongitude(listData.getDropCordinates())));


        } else {
            Utils.makeSnackBar(context, timefare, "No data available");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map12);
        mapFragment.getMapAsync(this);


        Log.e("total ammount", String.valueOf(totalamount));
        date_tv.setText(Utils.getCurrentDate());
        chkStatus = CSPreferences.readString(context, "stat");
    }

    private void ClickListeners() {
        collectCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new SpotsDialog(context);
                dialog.show();
                ModelManager.getInstance().getAmt().AmountPaid(context,
                        Operations.collectAmtDone(CSPreferences.readString(context, "customer_id"),
                                listData.getCustomerId(), listData.getRideRequestId(),
                                CSPreferences.readString(context, Constant.BASEFARE)));
            }
        });
    }

    private void getAmtApi() {
        if (PathMapActivity.isDistanceCalculated) {
            distanc = CSPreferences.readString(context, Constant.Distance);
            String[] splited = distanc.split("\\s+");
            distanc = splited[0];
            distanceTraveled = Double.parseDouble(distanc);
            distanceTraveled = getDis(distanceTraveled);

        } else {
            distanc = "N/A";
        }
        ModelManager.getInstance().getAmt().getAmount(context, Operations.collectAmt(context, CSPreferences.readString(context, Constant.COMPANYID), CSPreferences.readString(context, Constant.VEHICLETYPE), distanceTraveled + "", CSPreferences.readString(context, "tymDiff"), listData.getCustomerId(), CSPreferences.readString(context, "customer_id"), listData.getRideRequestId()));

    }

    private void setDistanceConvert() {
        distance = CSPreferences.readString(context, Constant.Distance);

        String separated[] = distance.split("\\s+");
        String dis = separated[0];
        if (dis.equalsIgnoreCase("")) {
            dis = "0.0";
        }
        double disNew = Double.parseDouble(dis);
        disNew = disNew / 1000;
        Log.d("Dis", dis + "");
        disancefare.setText(disNew + "Km");
    }

    private void setTime() {
        time = CSPreferences.readString(context, "tymDiff");

        discount.setText("N/A");
        String[] separated = time.split(":");
        Log.d("chkT",  separated[1]);
        String tim=separated[1];
        int time=Integer.parseInt(tim);
        if(time>=60)
        {
            timefare.setText(time + "min");
        }
        else {
            timefare.setText(separated[1] + "min");
        }


    }

    public double getDis(double i) {

        return i * 0.000621371192;

    }

    private void getIntents() {
        if (getIntent() != null && getIntent().hasExtra("token"))
            customerToken = getIntent().getStringExtra("token");
        Log.d("ckToken", customerToken);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActiveCollect = false;
    }

    //** Draw Path on Map travelled by Driver
    private void redrawLine() {


        PolylineOptions option = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            option.add(point);
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
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom((end), 11.0f));

        option.color(getResources().getColor(COLORS[5]));
        option.width(12);
        Polyline polyline = googleMap.addPolyline(option);
    }
//** End

    public String convertImageToBase64(Bitmap bit) {
//        bit = BitmapFactory.decodeResource(getResources(),
//                R.drawable.ic_icon_profile_pic);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 50, bao);
        byte[] byteArray = bao.toByteArray();
        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        covertedImage = BitMapToString(compressedBitmap);
        Log.e("convertedImage", "" + covertedImage);
        return covertedImage;
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
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

                break;

            case Constant.COLLECTAMT:
                cashList = CollectAmtManager.cash_list;
                String base = cashList.get(0).getBase();
                if (!base.equals("[]"))
                    base = Utils.currencyConverter(Double.parseDouble(base));
                CSPreferences.putString(context, Constant.BASEFARE, base);
                CSPreferences.putString(context, Constant.DISCOUNT, " Discount N/A");
                basefare.setText(base);
                totalfare.setText(base);
                AppController.chkStatus=true;
                JSONObject jsonObject_ = JsonRequestIR.jsonRequestForCash(context, base, customerToken, CSPreferences.readString(context, Constant.RIDER_ID));
                Utils.sendJsonIO(context,jsonObject_,"private_message");
                break;

            case Constant.CASHNOTCOLLECT:

                dialog.dismiss();
                Utils.makeSnackBar(context, timefare, "please try again");

                //   Toast.makeText(context, "please try again", Toast.LENGTH_SHORT).show();
                break;

            case Constant.SERVER_ERROR:
                dialog.dismiss();
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Server Problem")
                        .show();
                basefare.setText("N/A");
                totalfare.setText("N/A");
                break;

            case Constant.COLLECTAMT_DONE:
                dialog.dismiss();
                Intent it = new Intent(CashCollect.this, Rating.class);
                startActivity(it);
                finish();
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
        builder.include(new LatLng(Double.parseDouble(Utils.getLatitude(listData.getPickupCordinates())), Double.parseDouble(Utils.getLongitude(listData.getPickupCordinates()))));
        builder.include(new LatLng(Double.parseDouble(Utils.getLatitude(listData.getDropCordinates())), Double.parseDouble(Utils.getLongitude(listData.getDropCordinates()))));
        LatLngBounds bounds = builder.build();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int j = 0; j < arrayList.size(); j++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[5]));
            polyOptions.width(5 + i * 3);
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
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom((end), 11.0f));


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
        try {
            start = new LatLng(Double.parseDouble(Utils.getLatitude(listData.getPickupCordinates())),
                    Double.parseDouble(Utils.getLongitude(listData.getPickupCordinates())));

            end = new LatLng(Double.parseDouble(Utils.getLatitude(listData.getDropCordinates())),
                    Double.parseDouble(Utils.getLongitude(listData.getDropCordinates())));

            lat = Double.parseDouble(Utils.getLatitude(listData.getPickupCordinates()));
            lng = Double.parseDouble(Utils.getLongitude(listData.getPickupCordinates()));

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(end, 12.0f);
            googleMap.animateCamera(cameraUpdate);
            latLng = new LatLng(lat, lng);
            //**Comment by deep *** 18Aug
            Routing routing = new Routing.Builder()
                    .travelMode(Routing.TravelMode.DRIVING)
                    .withListener(this)
                    .waypoints(start, end)
                    .build();

            routing.execute();
            redrawLine();

        } catch (Exception ex) {
            Toast.makeText(context, "Data Not Available", Toast.LENGTH_LONG).show();
        }


        //***End


        if (googleMap != null)
            googleMap.setOnMapLoadedCallback(this);
    }

    public void onBackPressed() {
        return;
    }

    private void snapShotMap(String im) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Log.d("imgIm", im);
        Call<SnapShotMapResponse> call = apiService.snapShotGoogleMap(listData.getRideRequestId(), im);
        call.enqueue(new Callback<SnapShotMapResponse>() {

            @Override
            public void onResponse(Call<SnapShotMapResponse> call, Response<SnapShotMapResponse> response) {
                boolean res = response.isSuccessful();

                String msg = response.body().getResponse().getMessage();
            }

            @Override
            public void onFailure(Call<SnapShotMapResponse> call, Throwable t) {
                Log.e("profile_pic", "Error");
                Utils.makeSnackBar(context, userPic, "Error in Sending Snapshot");


            }
        });
    }

    @Override
    public void onMapLoaded() {
        takeSNapshot();
    }

    private void takeSNapshot() {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                bitmapGooglemap = snapshot;
                covertedImage = convertImageToBase64(bitmapGooglemap);
                try {
                    snapShotMap(covertedImage);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }

            }

        };

        googleMap.snapshot(callback);
    }
}
