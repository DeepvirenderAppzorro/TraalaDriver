package com.appzorro.driverappcabscout.view.Activity.PathmapActivity.view;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.CustomerReQuestManager;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.controller.NearestRoadManager;
import com.appzorro.driverappcabscout.model.AllAdapter.BottomSheetAdapter;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.JsonRequestIR;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;
import com.appzorro.driverappcabscout.view.Activity.ApiClient;
import com.appzorro.driverappcabscout.view.Activity.ApiInterface;
import com.appzorro.driverappcabscout.view.Activity.CashCollect;
import com.appzorro.driverappcabscout.view.Activity.Logger;
import com.appzorro.driverappcabscout.view.Activity.MapStyleManager;
import com.appzorro.driverappcabscout.view.Activity.ModelTrip;
import com.appzorro.driverappcabscout.view.Activity.PathmapActivity.model.CustomerListResponse;
import com.appzorro.driverappcabscout.view.HomeActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
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

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.appzorro.driverappcabscout.R.id.makecall;
import static com.appzorro.driverappcabscout.controller.NearestRoadManager.polyLineList;

public class PathMapActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback
        , GoogleApiClient.OnConnectionFailedListener, BottomSheetAdapter.ClickListener {
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 4000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    public static List<LatLng> lattLngList;
    public static boolean isDistanceCalculated = false;
    public static int selectedCustomerPosition = 0;
    public static boolean isFirstRide = false;
    public static List<CustomerListResponse.Datum> datmList;
    public BottomSheetAdapter.ClickListener clickListener;
    GoogleApiClient googleApiClient;
    GoogleMap googleMap;
    Polyline polyline_Route, greyPolyLine;
    Context context;
    SwipeButton swipe_StartTrip, swipe_CompleteTrip;
    CardView paymentcardview;
    AlertDialog dialog;
    private boolean isFirstTimeLocation=false;

    boolean isCustomerListUpdate = false;
    CustomerListResponse.Datum listData;
    boolean isMarkerRotating;
    LatLng currentlatlng, customer_source, customer_destination, lastlat, startPosition, endPosition, roadLatlng;
    String startTrips_status = "false", stopTrips_status = "false", firsttimeLocation = "false";
    Dialog messagedialog;
    ImageView callimage, navigate_image, callButton, expand_icon, locationIcon;
    TextView stoptrips, customer_name, addresstext, txt_begin_trip, text_paymentstaus;
    CircleImageView customer_image;
    // state=1 for arrived // state =2 when start and state=3 for completing // state 4 if its POOL TYPE only used state 4 for saving instance
    int state = 0;
    String navigateUri = "", ride_request_id = "", customerCurrentStatus = "1", rider_Id = "", customerToken = "";
    CoordinatorLayout poolType;
    RelativeLayout goType, addtolllayout;
    LinearLayout sheetLinear;
    BottomSheetBehavior sheetBehavior;
    MarkerOptions stopOptions = new MarkerOptions();
    MarkerOptions carMarkerOptions = new MarkerOptions();
    boolean isPoolType = false;
    CustomerListResponse customerListResponse;
    boolean isFirstTime = false;
    private String TAG = PathMapActivity.class.getSimpleName();
    private PolylineOptions polylineOptions;
    private boolean isFirstPosition = true;
    private boolean isFirstMarker = true;
    private BottomSheetAdapter bottomSheetAdapter;
    private RecyclerView customers_RecyclerView;
    private Marker stopMarker, startMarker;
    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private float f29v;
    SupportMapFragment mapFragment;
    private double lat;
    private double lng;
    private double latitude, longitude;
    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;

    String response="{\"response\":{\"status\":\"1\",\"message\":\"successfull\",\"data\":[{\"ride_request_id\":\"2\",\"price\":\"6.19\",\"payment_type\":\"0\",\"pickup_cordinates\":\"30.7097212,76.69356\",\"pickup_location\":\"F-372, Industrial Area, Sector 74, Sahibzada Ajit Singh Nagar, Punjab 160055, India, Sector 74\",\"drop_location\":\"Phase 6\",\"drop_cordinates\":\"30.7355,76.7121\",\"customer_id\":\"1\",\"name\":\"appzorro\",\"profile_pic\":\"img_1542779372.jpg\",\"mobile\":\"9888546434\",\"device_token\":\"eSRApYsGZUI:APA91bFev8J0Yhz_3Ep99jWeSHnTB4Nt4ziNNrYpL8zDzS1J0ojg6hwbZ8EcKyX2aopyAz0bY1FzGMhjfAVbX1Y6dq8V-ISowa0A9gjG9j1nfJz9pn4w4XmZriEsyO030IaKpwexnP-e\",\"vehicle_type\":\"1\",\"customer_status\":\"0\"}]}}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_trip_new);
        context = this;
        turnOnScreen();
        clickListener = this;
        Gson gson=new Gson();
        customerListResponse=gson.fromJson(response,CustomerListResponse.class);
        initViews();
        init();
        ClickListeners();

    }

//***Get CurrentLocation

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        carMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_car_new));
        carMarkerOptions.flat(true);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (!isFirstTime) {
                    isFirstTime = true;
                    mapFragment.getMapAsync(PathMapActivity.this);
                }
                mCurrentLocation = locationResult.getLastLocation();

                if (!isFirstTimeLocation) {
                    isFirstTimeLocation = true;
                    hitCustomerListApi(String.valueOf(mCurrentLocation.getLatitude()), String.valueOf(mCurrentLocation.getLongitude()), "no_event");
                    initCamera(mCurrentLocation);
                }

                if (!isFirstRide && isCustomerListUpdate) {
                    isFirstRide = true;
                    ModelManager.getInstance().getNearestRoadManager().NearestRoadManager(PathMapActivity.this, Operations.directionApi(PathMapActivity.this,
                            mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), customer_source.latitude, customer_source.longitude), 101, true);
                    distanceMatrix(String.valueOf(customer_source.latitude), String.valueOf(customer_source.longitude));
                }
                lat = mCurrentLocation.getLatitude();
                lng = mCurrentLocation.getLongitude();
              //  customerListResponse = CustomerReQuestManager.customerListResponse;
                if (customerListResponse != null)
                    listData = customerListResponse.getResponse().getData().get(selectedCustomerPosition);
                if (listData != null) {
                    customer_source = new LatLng(Double.parseDouble(Utils.getLatitude(listData.getPickupCordinates())), Double.parseDouble(Utils.getLongitude(listData.getPickupCordinates())));
                    customer_destination = new LatLng(Double.parseDouble(Utils.getLatitude(listData.getDropCordinates())), Double.parseDouble(Utils.getLongitude(listData.getDropCordinates())));
                    currentlatlng = new LatLng(lat, lng);
                    String points = lat + "," + lng;
                    Logger.addRecordToLog(points);
                    hitRoadApi(points, 1, true);
                    try {
                        Utils.getCompleteAddressString(context, Double.parseDouble(Utils.getLatitude(listData.getDropCordinates())),
                                Double.parseDouble(Utils.getLongitude(listData.getDropCordinates())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (state == 0) {
                        addresstext.setText(Utils.getCompleteAddressString(context, customer_source.latitude,
                                customer_source.longitude));
                    }
                    if (startTrips_status.equalsIgnoreCase("true")) {
                        hitDirectionApi(currentlatlng.latitude, currentlatlng.longitude, customer_destination.latitude, customer_destination.longitude, 2, true);

                    } else {
                        hitDirectionApi(lat, lng, customer_source.latitude, customer_source.longitude, 2, true);
                    }

                    CSPreferences.putString(context, "start_lat", String.valueOf(lat));
                    CSPreferences.putString(context, "start_lng", String.valueOf(lng));
                }
            }
        };

        mRequestingLocationUpdates = false;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }

    private void turnOnScreen() {
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

        this.getWindow().setAttributes(params);

    }

    private void ClickListeners() {
        txt_begin_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rider_Id.isEmpty())
                    rider_Id = CSPreferences.readString(context, Constant.RIDER_ID);
                if (customerToken.isEmpty())
                    customerToken = CSPreferences.readString(context, Constant.CUSTOMER_TOKEN);

                if (txt_begin_trip.getText().toString().equals("Click When You Arrived")) {
                    txt_begin_trip.setText("Arrived");
                    state = 1;
                    dialog = new SpotsDialog(context);
                    dialog.show();
                    if (listData != null) {
                        try {
                            ModelManager.getInstance().getArrivedManager().ArrivedManager(context, Operations.arrivedDriver(context,
                                    CSPreferences.readString(context, "customer_id"), listData.getRideRequestId(), rider_Id));

                        } catch (Exception ex) {
                            Toast.makeText(context, "No Data Available", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(context, "Network problem", Toast.LENGTH_LONG).show();
                    }

                } else {
                    dialog = new SpotsDialog(context);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm a");
                    Calendar calander = Calendar.getInstance();
                    String currentDate = DateFormat.getDateInstance().format(Calendar.getInstance().getTime()).replaceAll(" ", "");
                    String time = simpleDateFormat.format(calander.getTime()).replaceAll(" ", "");
                    dialog.show();

                    ModelManager.getInstance().getStartTripsManager().StartTripsManager(context, Operations.startTrips(
                            context, CSPreferences.readString(context, "customer_id"), listData.getRideRequestId(),
                            time, currentDate, String.valueOf(lat) + "," + String.valueOf(lng), rider_Id
                    ));

                }
            }
        });
        locationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentLocation!=null) {
                    LatLng coordinate = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()); //Store these lat lng values somewhere. These should be constant.
                    CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordinate, 15);
                    googleMap.animateCamera(location);
                } else {
                    Toast.makeText(context, "Network problem", Toast.LENGTH_SHORT).show();
                }
            }
        });
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + listData.getMobile()));
                startActivity(intent);
            }
        });

    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    public void initViews() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        stopOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_pin_white));
        isMarkerRotating = false;
        locationIcon = findViewById(R.id.img_Loc);
        callimage = (ImageView) findViewById(makecall);
        lattLngList = new ArrayList<>();
        customer_name = (TextView) findViewById(R.id.txtname);
        stoptrips = (TextView) findViewById(R.id.stoptrips);
        txt_begin_trip = (TextView) findViewById(R.id.txt_begin_trip);
        customer_image = (CircleImageView) findViewById(R.id.customer_image);
        expand_icon = (ImageView) findViewById(R.id.expand_arrow);
        addtolllayout = (RelativeLayout) findViewById(R.id.laoutaddtoll);
        paymentcardview = (CardView) findViewById(R.id.paymentcard);
        messagedialog = Utils.createMessageDialog(context);
        navigate_image = (ImageView) findViewById(R.id.navigate);
        addresstext = (TextView) findViewById(R.id.txtdestaddress);
        text_paymentstaus = (TextView) findViewById(R.id.txtpayment);
        customers_RecyclerView = findViewById(R.id.recycler_customers);
        sheetLinear = findViewById(R.id.cardView);
        poolType = findViewById(R.id.poolTypeView);
        sheetBehavior = BottomSheetBehavior.from(sheetLinear);
        goType = findViewById(R.id.rl_name_pay);
        callButton = findViewById(R.id.callButton);
        swipe_StartTrip = findViewById(R.id.slide);
        swipe_CompleteTrip = findViewById(R.id.slide2);
        customers_RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        customers_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        isFirstTime = true;
        // lat = 30.7097499;
        //  lng = 76.6934968;
        //currentlatlng = new LatLng(30.7097499, 76.6934968);
        try {
            firsttimeLocation = CSPreferences.readString(context, Constant.FIRSTTIMELOCATION);
        } catch (Exception e) {

        }
        swipeButtonListeners();
    }

    //*** Get CustomerList
    private void hitCustomerListApi(String lat, String lng, String key) {
        ModelManager.getInstance().getCustomerReQuestManager().CustomerReQuestManager(context, Operations.getCustomerRequest(context,
                CSPreferences.readString(getApplicationContext(), "customer_id"), lat, lng), key);
    }


    /**
     * BottomSheet Listener Callback
     */
    private void bottomSheetListener() {
        if (datmList != null && datmList.size() > 1) {
            expand_icon.setVisibility(View.VISIBLE);
        }

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        expand_icon.setVisibility(View.INVISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        if (datmList.size() > 1)
                            expand_icon.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

    }

    private void addStopMarker(LatLng latLng) {
        stopOptions.position(latLng);
        if (stopMarker != null)
            stopMarker.remove();
        stopMarker = googleMap.addMarker(stopOptions);
    }

    private void addStartMarker(LatLng latLng) {
        carMarkerOptions.position(latLng);
        if (startMarker != null)
            startMarker.remove();
        startMarker = googleMap.addMarker(carMarkerOptions);
    }

    private void setBottomSheetAdapter() {
        customerListResponse = CustomerReQuestManager.customerListResponse;
        datmList = customerListResponse.getResponse().getData();
        bottomSheetAdapter = new BottomSheetAdapter(getApplicationContext(), datmList, this);
        customers_RecyclerView.setAdapter(bottomSheetAdapter);

    }

    @Override
    public void onClick(int position) {
        listData = datmList.get(position);
        selectedCustomerPosition = position;
        customerCurrentStatus = listData.getCustomerStatus();
        rider_Id = listData.getCustomerId();
        customerToken = listData.getDeviceToken();
        ride_request_id = listData.getRideRequestId();
        bottomSheetAdapter.customNotify(datmList, position);
        CSPreferences.putString(context, Constant.CUSTOMER_TOKEN, customerToken);

        customer_source = new LatLng(Double.parseDouble(Utils.getLatitude(listData.getPickupCordinates())), Double.parseDouble(Utils.getLongitude(listData.getPickupCordinates())));
        customer_destination = new LatLng(Double.parseDouble(Utils.getLatitude(listData.getDropCordinates())), Double.parseDouble(Utils.getLongitude(listData.getDropCordinates())));
        checkForDestination(customer_source, customer_destination);

        isFirstTime = true;
        checkForCurrentView(listData);
        if (customerCurrentStatus.equals("1")) {
            navigateUri = "http://maps.google.com/maps?daddr=" + customer_source.latitude + "," + customer_source.longitude + " (" + "Pickup location" + ")";
            addStopMarker(customer_source);
            hitDirectionApi(lat, lng, customer_source.latitude, customer_source.longitude, 2, true);
            addresstext.setText(listData.getPickupLocation());

        } else {
            navigateUri = "http://maps.google.com/maps?daddr=" + customer_source.latitude + "," + customer_source.longitude + " (" + "Pickup location" + ")";
            hitDirectionApi(lat, lng, customer_destination.latitude, customer_destination.longitude, 2, true);
            addStopMarker(customer_destination);
            addresstext.setText(listData.getDropLocation());
        }

    }

    private void checkForCurrentView(CustomerListResponse.Datum list) {

        switch (list.getCustomerStatus()) {
            case "1":
                whenArrivedViewActive();
                addStopMarker(customer_source);
                break;
            case "2":
                whenSwipeStartRideActive();
                addStopMarker(customer_source);
                break;
            case "3":
                whenSwipeCompleteRideActive();

                addStopMarker(customer_destination);
                break;
            case "4":
                if (customerToken.isEmpty())
                    customerToken = CSPreferences.readString(context, Constant.CUSTOMER_TOKEN);
                Intent intent = new Intent(PathMapActivity.this, CashCollect.class);
                intent.putExtra("token", customerToken);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finishAffinity();

                break;
        }
    }

    public void checkForDestination(LatLng customer_source, LatLng customer_destination) {
        if (isPoolType) {
            if (customerCurrentStatus.equals("1") || customerCurrentStatus.equals("2"))
                lastlat = customer_source;
            else
                lastlat = customer_destination;
        }
    }

    private void checkForDriverStatus() {
        if (CSPreferences.readString(context, "DriverFlag").equalsIgnoreCase("true")) {
            txt_begin_trip.setVisibility(View.VISIBLE);
            swipe_StartTrip.setVisibility(View.GONE);
            swipe_CompleteTrip.setVisibility(View.GONE);
            CSPreferences.putString(context, Constant.DRIVER_STATUS, "5");
            hitDirectionApi(currentlatlng.latitude, currentlatlng.longitude, customer_source.latitude, customer_source.longitude, 2, true);
        }
        if (CSPreferences.readString(context, Constant.IS_POOLTYPE).equals("true")) {
            state = 4;
            isPoolType = true;
            poolType.setVisibility(View.VISIBLE);
            setBottomSheetAdapter();
        } else
            goType.setVisibility(View.VISIBLE);

    }

    void swipeButtonListeners() {
        //***Start trip
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
                startTrips_status = "true";
                isFirstRide = false;
                //   hitCustomerListApi(String.valueOf(currentlatlng.latitude), String.valueOf(currentlatlng.longitude), "no_event");
                addresstext.setText(Utils.getCompleteAddressString(context, customer_destination.latitude,
                        customer_destination.longitude));
                swipe_StartTrip.setVisibility(View.GONE);
                visibleButton();
                Log.d("chkLat", currentlatlng.latitude + "lat" + currentlatlng.longitude + "long");
                hitDirectionApi(currentlatlng.latitude, currentlatlng.longitude, customer_destination.latitude, customer_destination.longitude, 2, true);
                String currentTime = Utils.getCurTimeForDiff((Activity) context);
                CSPreferences.putString(PathMapActivity.this, "currenTimeDriver", currentTime);
                CSPreferences.putString(context, "Lat", lat + "");
                CSPreferences.putString(context, "Longi", lng + "");
                trip_begin(CSPreferences.readString(context, "customer_id"));
            }
        });
        //End

        //*** Complete Trip
        swipe_CompleteTrip.addOnSwipeCallback(new SwipeButton.Swipe() {
            @Override
            public void onButtonPress() {
            }

            @Override
            public void onSwipeCancel() {
            }

            @Override
            public void onSwipeConfirm() {
                if (rider_Id.isEmpty())
                    rider_Id = CSPreferences.readString(context, Constant.RIDER_ID);
                if (customerToken.isEmpty())
                    customerToken = CSPreferences.readString(context, Constant.CUSTOMER_TOKEN);
                if (ride_request_id.isEmpty())
                    ride_request_id = CSPreferences.readString(context, Constant.REQUEST_ID);
                state = 3;
                CSPreferences.putString(context, Constant.RIDE_STATE, String.valueOf(state));
                String latt = CSPreferences.readString(context, "Lat");
                String Lngg = CSPreferences.readString(context, "Longi");
                if (latt.equalsIgnoreCase("")) {
                    latt = "0.0";
                }
                if (Lngg.equalsIgnoreCase("")) {
                    Lngg = "0.0";

                }
                distanceMatrix(latt, Lngg);
                hitStopTripApi();
            }
        });
        //***End

    }

    private void hitStopTripApi() {
        String time = Utils.getCurrentCalenderTime((Activity) context);
        String strDate = Utils.getCurrentDate((Activity) context);
        dialog = new SpotsDialog(context);
        dialog.show();
        ModelManager.getInstance().getStopRideManager().StopRideManager(context, Operations.stopRide(context,
                CSPreferences.readString(context, "customer_id"), listData.getRideRequestId(),
                time, strDate, String.valueOf(lat) + "," + String.valueOf(lng), rider_Id));

    }

    private void distanceMatrix(String latt, String Lngg) {
        ModelManager.getInstance().getNearestRoadManager().NearestRoadManager(context, Operations.distanceMatrix(context,
                Double.parseDouble(latt), Double.parseDouble(Lngg), lat, lng), 4, true);
    }


    void visibleButton() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (currentlatlng.latitude == 0.0 || currentlatlng.longitude == 0.0) {

                } else {

                    LatLng coordinate = new LatLng(currentlatlng.latitude, currentlatlng.longitude); //Store these lat lng values somewhere. These should be constant.
                    CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                            coordinate, 16);
                    googleMap.animateCamera(location);
                    swipe_CompleteTrip.setVisibility(View.VISIBLE);
                }

            }
        }, 200);
    }

    //***Trip starting
    void trip_begin(String driver_id) {
        if (customerToken.isEmpty())
            customerToken = CSPreferences.readString(context, Constant.CUSTOMER_TOKEN);
        if (ride_request_id.isEmpty())
            ride_request_id = CSPreferences.readString(context, Constant.REQUEST_ID);

        String currentDate = Utils.getCurrentDate((Activity) context);
        String time = Utils.getCurrentCalenderTime((Activity) context);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        String latLong = lat+","+lng;
        Log.d("LatLong",latLong);
        Call<ModelTrip> call = apiService.getmodeldemoresult(driver_id, listData.getRideRequestId(), time, currentDate, latLong);
        call.enqueue(new Callback<ModelTrip>() {
            @Override
            public void onResponse(Call<ModelTrip> call, Response<ModelTrip> response) {
                boolean res = response.isSuccessful();
                String msg = response.body().getResponse().getMessage();
                startTrips_status = "true";
                firsttimeLocation = "false";
                CSPreferences.putString(context, Constant.FIRSTTIMELOCATION, firsttimeLocation);
                JSONObject jsonObject = JsonRequestIR.RideStartjsonRequest(getApplicationContext(), "Trip Has Started",
                        "trip_started", customerToken, ride_request_id, rider_Id, "ride_not_completed", String.valueOf(lat), String.valueOf(lng));
                Utils.sendJsonIO(context, jsonObject, "private_message");

            }

            @Override
            public void onFailure(Call<ModelTrip> call, Throwable t) {
                Log.e("msg_tripp", "Error");

            }
        });
    }
//*** End

    private void paymentType(String s) {
        switch (s) {
            case "0":
                text_paymentstaus.setText("Payment By Cash");
                break;

            case "1":
                text_paymentstaus.setText("Payment By Credit Card");
                break;

            case "2":
                text_paymentstaus.setText("Payment By Corp. Account");

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

        googleApiClient.disconnect();
        EventBus.getDefault().unregister(context);
    }


    public void onclickNavigate(View view) {

        if (state != 1 && state != 2 && state != 3) {
            if (navigateUri.isEmpty())
                navigateUri = "http://maps.google.com/maps?daddr=" + customer_destination.latitude + "," + customer_destination.longitude + " (" + "Drop location" + ")";

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(navigateUri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        } else if (state == 1 || state == 2) {
            if (navigateUri.isEmpty())
                navigateUri = "http://maps.google.com/maps?daddr=" + customer_destination.latitude + "," + customer_destination.longitude + " (" + "Drop location" + ")";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(navigateUri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
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
        //  mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        Log.e("on connected", "");
        try {

        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    private void initCamera(final Location mLocation) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (mLocation != null) {
            lat = mLocation.getLatitude();
            lng = mLocation.getLongitude();
            currentlatlng = new LatLng(lat, lng);

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
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            CameraPosition position = CameraPosition.builder().target(new LatLng(mLocation.getLatitude(), mLocation.getLongitude())).zoom(15f).bearing(0.0f).tilt(0.0f).build();
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15.0f);
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
            googleMap.animateCamera(zoom);

        }
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //  Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (CSPreferences.readString(context, Constant.IS_POOLTYPE).equals("true"))
            state = 4;
        CSPreferences.putString(context, Constant.RIDE_STATE, String.valueOf(state));
        Log.d("destroy", "OnDestroy of pathMapActivity");

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event event) {
        switch (event.getKey()) {

            case Constant.ARRIVEDSTATUS:
                dialog.dismiss();
                ArrivedTripResponse();
                break;

            case Constant.MY_LOCATION_FAILURE:
                if (!firsttimeLocation.equalsIgnoreCase("true")) {
                    ModelManager.getInstance().getNearestRoadManager().NearestRoadManager(this, Operations.directionApi(this,
                            lat, lng, customer_source.latitude, customer_source.longitude), 2, true);
                    firsttimeLocation = "true";
                }
                break;

            case Constant.RESTART_SERVICE:
                Log.d("serviceRestartted", "Yes");
                //      Utils.getInstance().serviceMethods(this);
                break;

            case Constant.CUSTOMERREQUEST:
                customerListResponse = CustomerReQuestManager.customerListResponse;
                if (CSPreferences.readString(this, Constant.IS_POOLTYPE).equals("true")) {
                    bottomSheetAdapter.customNotify(customerListResponse.getResponse().getData(), 0);
                    Toast.makeText(context, "New Rider Added", Toast.LENGTH_SHORT).show();

                }
                break;

            case Constant.DRAW_POLYLINE:
                staticPolyLine();
                break;

            case Constant.DRAW_MARKER:
                addMarkers();
                break;
            case Constant.NOTIFICATION_DIALOG:

                ModelManager.getInstance().getAcceptCustomerRequest().AcceptCustomerRequest(context,
                        Operations.acceptByDriver(context,
                                CSPreferences.readString(context, "customer_id"), CSPreferences.readString
                                        (context, Constant.REQUEST_ID), String.valueOf(currentlatlng.latitude),
                                String.valueOf(currentlatlng.longitude), CSPreferences.readString(context, Constant.RIDER_ID), "1"));

                break;

            case Constant.ACCEPTBYDRIVER:
                JSONObject jsonObject1 = JsonRequestIR.driverRequest(getApplicationContext(), currentlatlng.latitude, currentlatlng.longitude,
                        CSPreferences.readString(context, Constant.CUSTOMER_TOKEN));
                Utils.sendJsonIO(context, jsonObject1, "private_message");

                // handler.getCustomerList(CSPreferences.readString(context,"customer_id"),String.valueOf(currentlatlng.latitude), String.valueOf(currentlatlng.longitude),"");
                //**comment on 17nov
                hitCustomerListApi(String.valueOf(currentlatlng.latitude), String.valueOf(currentlatlng.longitude), "");
                break;
            //Comment by deep at 7Dec
            case Constant.SERVER_ERROR:

              /*  Canceldialog = Utils.serverError(context);
                Canceldialog.show();
                final TextView ok = Canceldialog.findViewById(R.id.txtok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ok.setBackgroundColor(Color.parseColor("#0083db"));

                        Canceldialog.dismiss();

                    }
                });
*/
                break;

            case Constant.STARTTRIPS:
                dialog.dismiss();
                CSPreferences.putString(context, "path", "second");
                StartTripSuccessResponse();
                break;

            case Constant.STOPRIDE:
                dialog.dismiss();
                StopRideResponse();
                break;

            case Constant.DISTANCE_MATRIX:
                String dis = NearestRoadManager.calculateDistance;
                isDistanceCalculated = true;
                CSPreferences.putString(context, Constant.Distance, dis);
                break;

            case Constant.STOPRIDEFAIL:
                dialog.dismiss();
                break;

            case Constant.MY_LOCATION:
                final double latt = ModelManager.getInstance().getNearestRoadManager().driverlat;
                final double lngg = ModelManager.getInstance().getNearestRoadManager().driverlongititude;
                roadLatlng = new LatLng(latt, lngg);
                Toast.makeText(context, latt + " Lattitude" + lngg + " longitude", Toast.LENGTH_SHORT);

                if (!firsttimeLocation.equalsIgnoreCase("true")) {
                    ModelManager.getInstance().getNearestRoadManager().NearestRoadManager(this, Operations.directionApi(this,
                            lat, lng, customer_source.latitude, customer_source.longitude), 2, true);
                    firsttimeLocation = "true";
                    CSPreferences.putString(context, Constant.FIRSTTIMELOCATION, firsttimeLocation);
                }

                if (!isFirstMarker) {
                    double lat = ModelManager.getInstance().getNearestRoadManager().driverlat;
                    double lng = ModelManager.getInstance().getNearestRoadManager().driverlongititude;
                    moveCarAnimation(lat, lng);
                    Handler mHandler = new Handler();

                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            //******* Add Latlong to arraylist******
                            LatLng ltlng = new LatLng(latt, lngg);
                            lattLngList.add(ltlng);
                            //End
                        }
                    }, 20000);

                }
                break;


            case Constant.CANCELTRIP:

                dialog.dismiss();
                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Cancel Trip")
                        .setContentText("Trip Canceled By Customer")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
                break;

            case Constant.CANCEL_RIDEFCM:

                Dialog dialog = Utils.CancelDialog(context);
                dialog.show();
                TextView okk = dialog.findViewById(R.id.txtok);

                okk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jsonObject1 = JsonRequestIR.cancelCustomerDialog(context);
                        Utils.sendJsonIO(context, jsonObject1, "private_message");
                        if (isPoolType) {
                            //* ************chnage by deep at nov 15_____----customerDeatilBean
                            if (listData != null) {
                                selectedCustomerPosition = 0;
                                Intent intent = new Intent(context, PathMapActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                Intent intent = new Intent(context, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                                finishAffinity();
                            }

                        } else {
                            Intent it = new Intent(PathMapActivity.this, HomeActivity.class);
                            startActivity(it);
                            finishAffinity();
                        }
                    }
                });
                break;
            case Constant.LOCATIONSEND:
                break;
            case Constant.UPDATE_CUSTOMER_LIST:
                //customerListResponse = CustomerReQuestManager.customerListResponse;
                listData = customerListResponse.getResponse().getData().get(selectedCustomerPosition);
                if (listData != null) {
                    customerCurrentStatus = listData.getCustomerStatus();
                    customer_source = new LatLng(Double.parseDouble(Utils.getLatitude(listData.getPickupCordinates())), Double.parseDouble(Utils.getLongitude(listData.getPickupCordinates())));
                    customer_destination = new LatLng(Double.parseDouble(Utils.getLatitude(listData.getDropCordinates())), Double.parseDouble(Utils.getLongitude(listData.getDropCordinates())));
                    checkForDestination(customer_source, customer_destination);
                    customer_name.setText(listData.getName());
                    customerToken = listData.getDeviceToken();

                    if (state == 0) {
                        addresstext.setText(Utils.getCompleteAddressString(context, customer_source.latitude,
                                customer_source.longitude));
                    }
                    rider_Id = listData.getCustomerId();
                    Picasso.with(context).load(Config.baserurl_image + listData.getProfilePic()).placeholder(R.drawable.ic_icon_pic).into(customer_image);
                    //customer_name.setText(CSPreferences.readString(this, Constant.RIDER_NAME));
                    paymentType(CSPreferences.readString(this, Constant.PAYEMENT_METHOD));
                    checkForCurrentView(listData);
                    checkForDriverStatus();
                    bottomSheetListener();
                    isCustomerListUpdate = true;

                    //  Toast.makeText(context, "No data is Available", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    //*** BeginTripSuccess
    private void StartTripSuccessResponse() {
        startTrips_status = "true";
        customer_source = new LatLng(Double.parseDouble(Utils.getLatitude(listData.getPickupCordinates())),
                Double.parseDouble(Utils.getLongitude(listData.getPickupCordinates())));
        customer_destination = new LatLng(Double.parseDouble(Utils.getLatitude(listData.getDropCordinates())),
                Double.parseDouble(Utils.getLongitude(listData.getDropCordinates())));

        checkForDestination(customer_source, customer_destination);
        addresstext.setText(Utils.getCompleteAddressString(context, Double.parseDouble(Utils.getLatitude(listData.getDropCordinates())),
                Double.parseDouble(Utils.getLongitude(listData.getDropCordinates()))));
        // again intilize the array list to store the cureent location for find the distance
        //  hitDirectionApi(customer_source.latitude,customer_source.longitude,customer_destination.latitude,customer_destination.longitude,2,true);
        //handler.getCustomerList(CSPreferences.readString(context,"customer_id"),String.valueOf(currentlatlng.latitude), String.valueOf(currentlatlng.longitude),"no_event");

        hitCustomerListApi(String.valueOf(currentlatlng.latitude), String.valueOf(currentlatlng.longitude), "no_event");

    }
    //***End

    //***DriverArrivedResponse
    private void ArrivedTripResponse() {
        state = 1;
        txt_begin_trip.setVisibility(View.GONE);
        swipe_StartTrip.setVisibility(View.VISIBLE);
        if (CSPreferences.readString(context, "DriverFlag").equalsIgnoreCase("true")) {
            CSPreferences.putString(context, Constant.DRIVER_STATUS, "5");
        }
        CSPreferences.putString(context, Constant.RIDE_STATE, String.valueOf(state));

        // handler.getCustomerList(CSPreferences.readString(context,"customer_id"),String.valueOf(currentlatlng.latitude), String.valueOf(currentlatlng.longitude),"no_event");

        hitCustomerListApi(String.valueOf(currentlatlng.latitude), String.valueOf(currentlatlng.longitude), "no_event");
        JSONObject jsonObject = JsonRequestIR.ArrivedjsonRequest(getApplicationContext(), customerToken, rider_Id);
        Utils.sendJsonIO(context, jsonObject, "private_message");
        customer_source = new LatLng(Double.parseDouble(Utils.getLatitude(listData.getPickupCordinates())), Double.parseDouble(Utils.getLongitude(listData.getPickupCordinates())));
        customer_destination = new LatLng(Double.parseDouble(Utils.getLatitude(listData.getDropCordinates())), Double.parseDouble(Utils.getLongitude(listData.getDropCordinates())));

        checkForDestination(customer_source, customer_destination);

        addresstext.setText(Utils.getCompleteAddressString(context, customer_source.latitude,
                customer_source.longitude));
        lat = currentlatlng.latitude;
        lng = currentlatlng.longitude;
        lat = customer_source.longitude;
        lng = customer_source.longitude;
        hitDirectionApi(currentlatlng.latitude, currentlatlng.longitude, customer_source.latitude, customer_source.longitude, 2, true);
        isFirstTime = true;
        isFirstMarker = true;
    }
    //***End

    private void StopRideResponse() {
        // handler.getCustomerList(CSPreferences.readString(context,"customer_id"),String.valueOf(currentlatlng.latitude), String.valueOf(currentlatlng.longitude),"no_msg");

        hitCustomerListApi(String.valueOf(currentlatlng.latitude), String.valueOf(currentlatlng.longitude), "no_msg");

        JSONObject jsonObject12 = JsonRequestIR.RideStopjsonRequest(getApplicationContext(), "Trip Has completed", "trip_completed", customerToken, ride_request_id, rider_Id, "ride_completed");
        Utils.sendJsonIO(context, jsonObject12, "private_message");
        //removeUpdate();
        CSPreferences.putString(context, "path", "");
        getTimeDiff();
        if (CSPreferences.readString(context, "payment_method").equals("1")) {
            text_paymentstaus.setText("Credit card");
        } else {
            text_paymentstaus.setText("Cash");
        }
        StartCashCollectActivity();

    }


    private void hitDirectionApi(Double sourceLat, Double sourceLng, Double destLat, Double destLng, int status, boolean v) {
        ModelManager.getInstance().getNearestRoadManager().NearestRoadManager(context, Operations.directionApi(context,
                sourceLat, sourceLng, destLat, destLng), 2, v);

    }

    private void StartCashCollectActivity() {
        state = 5;
        CSPreferences.putString(context, Constant.RIDE_STATE, "5");
        Intent it = new Intent(PathMapActivity.this, CashCollect.class);
        it.putExtra("token", customerToken);
        startActivity(it);
        finish();
    }

    //***Get TimeDiff
    private void getTimeDiff() {
        String Currenttime = Utils.getCurrentCalenderTime((Activity) context);
        String timeChk = CSPreferences.readString(PathMapActivity.this, "currenTimeDriver");
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        try {
            Date date1 = format.parse(Currenttime);
            Date date2 = format.parse(timeChk);
            long mills = date1.getTime() - date2.getTime();
            int hours = (int) (mills / (1000 * 60 * 60));
            int mins = (int) (mills / (1000 * 60)) % 60;

            String diff = hours + ":" + mins; // updated value every1 second
            CSPreferences.putString(context, "tymDiff", diff);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //***EndCustomer

    @Override
    public void onBackPressed() {
        return;
    }


    private void removeUpdate() {
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
        //  if (locationManager != null) {
        //     locationManager.removeUpdates(this);
        // } else {
        //     Toast.makeText(context, "Location null on remove updates", Toast.LENGTH_SHORT).show();
        // }

    }

    private void moveCarAnimation(double lat, double lng) {
        LatLng latLng = new LatLng(lat, lng);
        if (isFirstPosition) {
            startPosition = latLng;
            isFirstPosition = false;

        } else {
            endPosition = new LatLng(lat, lng);
            Log.d(TAG, startPosition.latitude + "--" + endPosition.latitude + "--Check --" + startPosition.longitude + "--" + endPosition.longitude);
            if ((startPosition.latitude != endPosition.latitude) || (startPosition.longitude != endPosition.longitude)) {
                Log.e(TAG, "NOT SAME");
                Utils.getInstance().startBikeAnimation(googleMap, startMarker, startMarker.getPosition(), endPosition);
            } else {
                Log.e(TAG, "SAMME");
            }
        }
    }

    private void hitRoadApi(String points, int status, boolean rideStarted) {
        ModelManager.getInstance().getNearestRoadManager().NearestRoadManager(this, Operations.nearestRoadlatlng(this, points), status, rideStarted);
    }

    public void addMarkers() {

        if (customerCurrentStatus.equals("1") || customerCurrentStatus.equals("2"))
            lastlat = customer_source;
        else
            lastlat = customer_destination;

        stopOptions.position(lastlat);
        addStartMarker(polyLineList.get(0));
        addStopMarker(polyLineList.get(polyLineList.size() - 1));
    }

    void staticPolyLine() {

        if (greyPolyLine != null) {
            greyPolyLine.remove();
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : polyLineList) {
            builder.include(latLng);
        }
        int routePadding = 100;
        LatLngBounds latLngBounds = builder.build();
        //     googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));

        if (customerCurrentStatus.equals("1") || customerCurrentStatus.equals("2")) {
            lastlat = customer_source;
            addStopMarker(customer_source);
        } else {
            addStopMarker(customer_destination);
            lastlat = customer_destination;
        }
        if (startTrips_status.equalsIgnoreCase("true")) {
            addStopMarker(customer_destination);
        }

        if (isFirstTime) {
            isFirstTime = false;
            isFirstMarker = false;
            stopOptions.position(lastlat);
            // addStartMarker(polyLineList.get(0));
            //addStopMarker(polyLineList.get(polyLineList.size() - 1));
        }

        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLACK);
        polylineOptions.width(10);
        polylineOptions.startCap(new SquareCap());
        polylineOptions.endCap(new SquareCap());
        polylineOptions.jointType(JointType.ROUND);
        polylineOptions.addAll(polyLineList);

        greyPolyLine = googleMap.addPolyline(polylineOptions);

        if (polyline_Route != null && polyLineList.size() > 0) {
            polyline_Route.remove();
        }
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
                //sblackPolyline.setPoints(p);
            }
        });
        polylineAnimator.start();

    }


    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
        EventBus.getDefault().post(new Event(Constant.UPDATE_CUSTOMER_LIST, ""));

    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");


                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(PathMapActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                //    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



    public void whenArrivedViewActive() {
        txt_begin_trip.setText("Click When You Arrived");
        txt_begin_trip.setVisibility(View.VISIBLE);
        swipe_StartTrip.setVisibility(View.GONE);
        swipe_CompleteTrip.setVisibility(View.GONE);
        isFirstTime = true;
        state = 0;
    }

    public void whenSwipeStartRideActive() {
        txt_begin_trip.setVisibility(View.GONE);
        swipe_StartTrip.setVisibility(View.VISIBLE);
        swipe_CompleteTrip.setVisibility(View.GONE);
        state = 1;
    }

    public void whenSwipeCompleteRideActive() {
        txt_begin_trip.setVisibility(View.GONE);
        swipe_StartTrip.setVisibility(View.GONE);
        swipe_CompleteTrip.setVisibility(View.VISIBLE);
        state = 2;
    }
}