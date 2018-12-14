package com.appzorro.driverappcabscout.model;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.Beans.Socket_CustomerDetail;
import com.appzorro.driverappcabscout.view.Activity.NotificatonDialog;
import com.appzorro.driverappcabscout.view.HomeActivity;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();
    private static String API_KEY = "AIzaSyDW4hwt4oKL-B64uDuwZ3LwEsoBLEcHwgw";
    private static Utils modelManager;
    Intent serviceIntent;
    private LatLng startPosition;
    private double lat, lng;
    public static String[] arry_split;
    public static ArrayList<String> arraylist_split = new ArrayList<>();
    private float v;
    static Intent intent;
    public static Socket mSocket;
    public static Context context;
    public static Socket_CustomerDetail socket_customerDetail;


    public static float bearing;
    private static final long ANIMATION_TIME_PER_ROUTE = 2000;

    public static Utils getInstance() {
        if (modelManager == null)
            return modelManager = new Utils();
        else
            return modelManager;
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public static String addMinutesinCurrentTime(String time) {
        String newTime = "";
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        try {
            Date d = dateFormat.parse(dateFormat.format(new Date()));
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.MINUTE, Integer.parseInt(time));
            newTime = dateFormat.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newTime;
    }

    public static String getLatitude(String wholeString) {
        String[] picksplit = wholeString.split(",");
        String sourcelat = picksplit[picksplit.length - 2];
        sourcelat = sourcelat.replaceAll("[\\p{Ps}\\p{Pe}]", "");
        Log.d("longitudechk", sourcelat);
        return sourcelat;
    }

    public static String setRatingText(RatingBar ratingBar) {
        String rating = "";
        if (ratingBar.getRating() == 0.0 || ratingBar.getRating() == 0.5 || ratingBar.getRating() == 1.0)
            rating = "Terrible";
        else if (ratingBar.getRating() == 2.0 || ratingBar.getRating() == 1.5)
            rating = "Bad";
        else if (ratingBar.getRating() == 3.0 || ratingBar.getRating() == 2.5)
            rating = "Okay";
        else if (ratingBar.getRating() == 4.0 || ratingBar.getRating() == 3.5)
            rating = "Good";
        else if (ratingBar.getRating() == 5.0 || ratingBar.getRating() == 4.5)
            rating = "Great";
        return rating;
    }


    public static String getLongitude(String wholeString) {
        String[] picksplit = wholeString.split(",");
        String sourcelng = picksplit[picksplit.length - 1];
        sourcelng = sourcelng.replaceAll("[\\p{Ps}\\p{Pe}]", "");
        Log.d("longitudechk", sourcelng);
        return sourcelng;
    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String currencyConverter(double amount) {
        DecimalFormat format = new DecimalFormat("$#");
        format.setMinimumFractionDigits(2);
        String currency = format.format(amount);
        return currency;
    }


    public static double milesTokm(double distanceInMiles) {
        return distanceInMiles * 1.60934;
    }

    public static String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

              /*  for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i));
                }*/
                strReturnedAddress.append(returnedAddress.getAddressLine(0)).append(", ");

                if (returnedAddress.getSubLocality() != null)
                    strReturnedAddress.append(returnedAddress.getSubLocality());

                strAdd = strReturnedAddress.toString();
                Log.e(TAG, "Current address-- " + strReturnedAddress.toString());
            } else {
                Log.e(TAG, "No address returned");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    public static String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String key = "key=" + API_KEY;

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }


    public static String getTimeStamp() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date
            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static void makeSnackBar(Context context, View view, String message) {
        Snackbar snackbar;
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));

        snackbar.show();
    }

    public static void makeSnackBarGreen(Context context, View view, String message) {
        String white = "#48b02c";
        int whiteInt = Color.parseColor(white);
        Snackbar snackbar;
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.green));

        snackbar.show();
    }


    public static String getCurrentTYm(Context context) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm:ss aa");
        String datetime = dateformat.format(c.getTime());
        Log.d("timein", datetime);
        return datetime;
    }

    public static String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();
        while (itr.hasNext()) {
            String key = itr.next();
            Object value = params.get(key);
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static Dialog createDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.driveracceptlayout);
        dialog.setCancelable(false);
        //   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    public static Dialog createpasswordDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.changepasswordlayout);
        dialog.setCancelable(false);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }


    public static Dialog createaddtollDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addtolllayout);
        dialog.setCancelable(false);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    public static Dialog createMessageDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custome_dialogmessage);
        dialog.setCancelable(false);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }


    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null && inputManager != null) {
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                inputManager.hideSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static String getCurTimeForDiff(Activity activity) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df3 = new SimpleDateFormat("hh:mm a");
        String formattedDate = df3.format(c.getTime());
        Log.d("CurrentTime_", formattedDate);

        return formattedDate;
    }

    public static String getCurrentCalenderTime(Activity activity) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        Calendar calander = Calendar.getInstance();
        String time = simpleDateFormat.format(calander.getTime()).replaceAll(" ", "");
        return time;
    }

    public static String getCurrentDate(Activity activity) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = "" + mdformat.format(calendar.getTime());
        return strDate;
    }

    public static Dialog createStopfeeDialog(Context context) {


        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.stopfee);
        dialog.setCancelable(false);
        return dialog;
    }

    public static String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public static String getCurrentMonth() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public static Location getLastBestStaleLocation(Context context) {
        Location bestresult = null;

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, (LocationListener) context);
            } else {
                bestresult = networklocation;
                Log.e("result", "network location ---- " + bestresult.getLatitude());
                Config.currentLAT = bestresult;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, (LocationListener) context);
            }
        } else if (gpslocation != null) {
            bestresult = gpslocation;
            Log.e("result", "gps location only found---- " + bestresult.getLatitude());
            Config.currentLAT = bestresult;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, (LocationListener) context);
        } else if (networklocation != null) {
            bestresult = networklocation;
            Log.e("result", "network location only found--- " + bestresult.getLatitude());
            Config.currentLAT = bestresult;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, (LocationListener) context);
        }
        return bestresult;
    }

    public static double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;
        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    public static String midPoint(double lat1, double lon1, double lat2, double lon2) {
        LatLng latLng;
        double dLon = Math.toRadians(lon2 - lon1);

        //convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        double lat = Math.toDegrees(lat3);

        double lng = Math.toDegrees(lon3);
        double latlng = Math.toDegrees(lat3);
        String latt = String.valueOf(lng) + "," + String.valueOf(latlng);
        return latt;

    }


    public static void clearNotifications(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

    }

    public static void ConnectionChkMethod(Context context1) {
        context = context1;
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
    }

    public static void sendMessageIo(final Context context, String Key, String data) {
        if (mSocket != null) {
            mSocket.emit(Key, data);
        }

    }

    public static void sendJsonIO(final Context context, JSONObject jsonObject, String Key) {
        if (mSocket != null) {
            mSocket.emit(Key, jsonObject);
        }

    }


    private boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    public static Dialog logoutDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        return dialog;
    }

    public static Dialog serverError(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.server_error);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        return dialog;
    }

    public static Dialog uploaDocuments(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.upload_documents_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        return dialog;
    }

    public static Dialog ErrorDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.errordialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        return dialog;
    }

    public static Dialog CancelDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cancel_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        return dialog;
    }
    public static Dialog InternetStatus(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.internet_status);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        return dialog;
    }

    public static Dialog RatingDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ratoing_thanks);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        return dialog;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    //How compress Pic

    public static Bitmap getScaledBitmap(Bitmap b, int reqWidth, int reqHeight) {
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, b.getWidth(), b.getHeight()), new RectF(0, 0, reqWidth, reqHeight), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
    }

    //End
    public static void connectServer() {
        if (mSocket != null) {
            if (mSocket.connected() == true) {
                Log.d("chkData", "Already connected");
            } else {
                try {
                    mSocket = IO.socket("http://162.144.71.183:8080/");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                mSocket.connect();
            }

        } else {
            try {
                mSocket = IO.socket("http://162.144.71.183:8080/");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            mSocket.connect();
        }

    }

    public static void getMessages(Context context1, String key) {
        mSocket.on(key, getMessages);
        context = context1;
    }

    public static void cancelRideCustomer(Context context1, String key) {
        mSocket.on(key, cancelRide);
        context = context1;
    }

    public static Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Log.d("chkData", "OnConnect");

                }
            });
        }
    };
    public static Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Log.d("chkData", "OnDisConnect");
                    //   Toast.makeText(context,"Socket Disconnect",Toast.LENGTH_SHORT).show();

                }
            });
        }
    };
    public static Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //  Toast.makeText(context,"Socket Error",Toast.LENGTH_SHORT).show();

                    Log.d("chkData", "OnConnectError");


                }
            });
        }
    };
    public static Emitter.Listener cancelRide = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String ride_request_id = "";
                    JSONObject data = (JSONObject) args[0];
                    Log.d("chkData", data + "cancelRide request data");
                    try {
                        ride_request_id = data.getString("ride_request_id");
                        if (CSPreferences.readString(context, Constant.REQUEST_ID).equals(ride_request_id)) {
                            CSPreferences.putString(context, Constant.DRIVER_STATUS, "5");
                            intent = new Intent(context, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            EventBus.getDefault().post(new Event(Constant.CANCEL_RIDEFCM, ""));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    };


    public static Emitter.Listener getMessages = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.d("chkData", data + "getMessages request data");
                    Gson gson = new Gson();
                    socket_customerDetail = gson.fromJson(data + "", Socket_CustomerDetail.class);
                    try {
                        notificationCases(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

    private static void notificationCases(JSONObject jsonObject) {
        String key = "";
        String message = "";
        String socket_id = "";
        String ride_request_id = "";
        String distance = "1 km";
        String driver_status = "";
        String img = "";
        String index="";
        String poolorNot = "";
        boolean isDriverID = false;
        boolean showDialog = false;
        try {
            key = jsonObject.getString("noti_type");
            socket_id = jsonObject.getString("driverSocketId");
            message = jsonObject.getString("message");
            index= jsonObject.getString("driver_index");
            CSPreferences.putString(context,"driver_index",index);
            if (jsonObject.has("poolType"))
                poolorNot = jsonObject.getString("poolType");
            CSPreferences.putString(context, "poolType", poolorNot);
            if (socket_id.equalsIgnoreCase(CSPreferences.readString(context, Constant.SOCKET_ID))) {
                isDriverID = true;
            }

            if (key.equals("customer_request") && jsonObject.has("ride_request_id") && isDriverID && !NotificatonDialog.isActive) {
                ride_request_id = jsonObject.getString("ride_request_id");
                img = jsonObject.getString("CustomerImg");
                if (jsonObject.has("distance")) {
                    distance = jsonObject.getString("distance");
                    CSPreferences.putString(context, "distanceCustomer", distance);
                }
                CSPreferences.putString(context, Constant.DISTANCE, distance);
                CSPreferences.putString(context, Constant.REQUEST_ID, ride_request_id);
                showDialog = true;

            } else if (key.equals("customer_request") && jsonObject.has("ride_request_id") && isDriverID && NotificatonDialog.isActive) {
                ride_request_id = jsonObject.getString("ride_request_id");
                img = jsonObject.getString("CustomerImg");
                CSPreferences.putString(context, Constant.REQUEST_ID, ride_request_id);
                ModelManager.getInstance().getAcceptCustomerRequest().RejectCustomer(context,
                        Operations.rejectByDriver(
                                CSPreferences.readString(context, "customer_id"), ride_request_id));
            }

            if (!driver_status.equals("2") && key.equals("customer_request") && isDriverID && showDialog || poolorNot.equals("pool")) {
                String customer_id = jsonObject.getString("cutomerID");
                img = jsonObject.getString("CustomerImg");
                CSPreferences.putString(context, Constant.RIDER_ID, customer_id);

                if (!driver_status.equals("2") && !poolorNot.equals("pool")) {
                    CSPreferences.putString(context, Constant.IS_POOLTYPE, "false");
                    CSPreferences.putString(context, Constant.IS_OTHERTYPE, "true");
                    intent = new Intent(context, NotificatonDialog.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    sendmessage(message, intent, img);
                    ModelManager.getInstance().getCustomerRideStatus().customerDetail(context, Operations.
                            customer_status(context, customer_id, ride_request_id));

                } else if (poolorNot.equals("pool")) {
                    CSPreferences.putString(context, Constant.IS_POOLTYPE, "true");
                    CSPreferences.putString(context, Constant.IS_OTHERTYPE, "false");
                    if (jsonObject.getString("driver_ID").equals(CSPreferences.readString(context, "customer_id"))) {
                        ModelManager.getInstance().getCustomerRideStatus().customerDetail(context, Operations.
                                customer_status(context, customer_id, ride_request_id));
                    }
                }
              /*  timer = new MyTimer(1500, 1000);
                timer.start();*/
            }
        } catch (Exception e) {

        }
    }

    public static void sendmessage(String message, Intent pushNotification, String img) {
       /* Intent pushNotification = new Intent(this,NotificatonDialog.class);*/
        pushNotification.putExtra("message", message);
        Log.d("img", img);
        LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotification);
        // play notification sound
        NotificationUtils notificationUtils = new NotificationUtils(context);
        notificationUtils.playNotificationSound();
        notificationUtils.showNotificationMessage("Traala", message, "", pushNotification, img);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String get_message = intent.getStringExtra("message");
            if (get_message != null && !get_message.isEmpty() && get_message.equals("restartService")) {
                //serviceMethods();
                Log.d("SignalIR", "Service Restarted");
                //   context.stopService(serviceIntent);

                //SignalRFuture<Void> connect = connect(Constant.SOCKET_URL);
                // configConnectFuture(connect);
                //  getMessages();
                // EventBus.getDefault().post(new Event(Constant.RESTART_SERVICE, ""));
            }

            //          message += "\n" + get_message;

            //incoming_msg.setText(message);
        }
    };

    public void startBikeAnimation(final GoogleMap googleMap, final Marker carMarker, final LatLng start, final LatLng end) {

        Log.i(TAG, "startBikeAnimation called...");

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(ANIMATION_TIME_PER_ROUTE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                //LogMe.i(TAG, "Car Animation Started...");
                v = valueAnimator.getAnimatedFraction();
                lng = v * end.longitude + (1 - v)
                        * start.longitude;
                lat = v * end.latitude + (1 - v)
                        * start.latitude;

                LatLng newPos = new LatLng(lat, lng);
                carMarker.setPosition(newPos);
                carMarker.setAnchor(0.5f, 0.45f);
                carMarker.setRotation(getHeadingForDirection(start, end));
                bearing = getHeadingForDirection(start, end);
                // todo : Shihab > i can delay here
//                googleMap.moveCamera(CameraUpdateFactory
//                        .newCameraPosition
//                                (new CameraPosition.Builder()
//                                        .target(newPos)
//                                        .zoom(15.5f)
//                                        .build()));

                startPosition = carMarker.getPosition();

            }

        });
        valueAnimator.start();
    }

    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }


    public static float getHeadingForDirection(LatLng begin, LatLng end) {

        float beginLat = (float) Math.toRadians(begin.latitude);
        float beginLong = (float) Math.toRadians(begin.longitude);
        float endLat = (float) Math.toRadians(end.latitude);
        float endLong = (float) Math.toRadians(end.longitude);
        return (float) Math.toDegrees(Math.atan2(Math.sin(endLong - beginLong) * Math.cos(endLat),
                Math.cos(beginLat) * Math.sin(endLat) - Math.sin(beginLat) * Math.cos(endLat) *
                        Math.cos(endLong - beginLong)));


    }

    public static void switchColor(boolean checked, Switch myswitch) {
        String white = "#48b02c";
        int whiteInt = Color.parseColor(white);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            myswitch.getThumbDrawable().setColorFilter(checked ? whiteInt : Color.WHITE, PorterDuff.Mode.MULTIPLY);
            myswitch.getTrackDrawable().setColorFilter(!checked ? Color.GREEN : Color.WHITE, PorterDuff.Mode.MULTIPLY);
        }
    }
    public static void switchChange(boolean checked,Switch myswitch) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            myswitch.getThumbDrawable().setColorFilter(checked ? Color.WHITE : Color.WHITE, PorterDuff.Mode.MULTIPLY);
            myswitch.getTrackDrawable().setColorFilter(!checked ? Color.WHITE : Color.WHITE, PorterDuff.Mode.MULTIPLY);
        }
    }

}