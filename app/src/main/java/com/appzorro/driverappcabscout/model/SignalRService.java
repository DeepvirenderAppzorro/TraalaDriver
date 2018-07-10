package com.appzorro.driverappcabscout.model;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.view.HomeScreenActivity;
import com.appzorro.driverappcabscout.view.NotificatonDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.InvalidStateException;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

public class SignalRService extends Service {
    private static HubConnection mConnection;
    private static HubProxy mChat;
    public static final String BROADCAST_ACTION = "com.android.com.simplechatwithsignalr";
    public static Handler mHandler;
    public static Context context;
    private final IBinder mBinder = new LocalBinder(); // Binder given to clients
    private static boolean isDisconnnected = true;
    Intent intent;

    public SignalRService() {
    }

    public SignalRService(Context applicationContext) {
        super();
        Log.i("HERE", "here I am!");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(Looper.getMainLooper());
        context = this;
        Log.d("Services", "Started");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer();
        return START_STICKY;
    }

    private Timer timer;
    private TimerTask timerTask;

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                if (mConnection != null) {
                    getMessages();
                } else {
                    SignalRFuture<Void> connect = connect(Constant.SOCKET_URL);
                    configConnectFuture(connect);
                    getMessages();
                }
                // Log.i("in timer", "in timer ++++  " + (counter++));
            }
        };
    }

    private void configConnectFuture(SignalRFuture<Void> connect) {
        connect.onError(new ErrorCallback() {
            @Override
            public void onError(final Throwable error) {

                Log.d("messgaes", error.getMessage());
                if (error.getMessage().equals("The operation is not allowed in the 'Disconnected' state")){
                    if (isDisconnnected){
                        Log.d("messgaesInside", error.getMessage());
                        isDisconnnected =false;
                        SignalRFuture<Void> connect = connect(Constant.SOCKET_URL);
                        SignalRService signalRService = new SignalRService();
                        signalRService.configConnectFuture(connect);
                        signalRService.getMessages();
                    }

                }


            }
        });

    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onDestroy() {
        // mChat = null;
        //  mConnection.stop();
        Log.d("Services", "Destroyed");
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= 26) {
            //startServiceWithNotification();
        } else {

            Intent broadcastIntent = new Intent("com.example.appzorro.quickbloxsample.RestartSensor");
            sendBroadcast(broadcastIntent);
            stoptimertask();


        }

    }

    public HubConnection getHubConnection() {
        return mConnection;
    }

    public static HubProxy getChatHub() {
        return mChat;
    }

    public static SignalRFuture<Void> connect(String url) {
        final SignalRFuture<Void> future = new SignalRFuture<Void>();
        createObjects(url, future);

        return future;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        //connect(URL);
        //stopForeground(true); // <- remove notification

        return mBinder;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public SignalRService getService() {
            // Return this instance of SignalRService so clients can call public methods
            return SignalRService.this;
        }
    }

    /**
     * method for clients (activities)
     */
    public static void createObjects(String url, final SignalRFuture<Void> future) {

        mConnection = new HubConnection(url, "", true, new Logger() {

            @Override
            public void log(String message, LogLevel level) {
                if (level == LogLevel.Critical) {


                    Log.d("SignalR195", level.toString() + ":: " + message);
                  //  if (message.equals("The operation is not allowed in the 'Disconnected' state")) {
//                        Intent intent = new Intent(BROADCAST_ACTION);
//                        intent.putExtra("message", "restartService");
//                        context.sendBroadcast(intent);


                }
            }
        });

        try {
            mChat = mConnection.createHubProxy("chatHub");
        } catch (InvalidStateException e) {
            Log.d("SignalR204", "Error getting creating proxy: " + e.toString());
            future.triggerError(e);
        }

        //ClientTransport transport = new LongPollingTransport(mConnection.getLogger()); // works as expected
        ClientTransport transport = new ServerSentEventsTransport(mConnection.getLogger()); // Works on WiFi, never connects on 3G, no error is thrown either
        //ClientTransport transport = new WebsocketTransport(mConnection.getLogger()); // Never connects, not error is thrown
        SignalRFuture<Void> connectionFuture = mConnection.start(transport);

        mConnection.connected(new Runnable() {
            @Override
            public void run() {
                System.out.println("Connected");
                future.setResult(null);
            }
        });


        // Subscribe to the connected event

        // Subscribe to the closed event
        mConnection.closed(new Runnable() {
            @Override
            public void run() {
                System.out.println("DISCONNECTED");
                if (isDisconnnected){
                    isDisconnnected =false;
                    SignalRFuture<Void> connect = connect(Constant.SOCKET_URL);
                    SignalRService signalRService = new SignalRService();
                    signalRService.configConnectFuture(connect);
                    signalRService.getMessages();
                }

//                Toast.makeText(context,"DISCONNECTED",Toast.LENGTH_SHORT).show();

            }
        });

        // Start the connection
        mConnection.start()
                .done(new Action<Void>() {
                    @Override
                    public void run(Void obj) throws Exception {
                        isDisconnnected=true;
                        System.out.println("Done Connecting!");
                      //  Toast.makeText(context,"Done Connecting!",Toast.LENGTH_SHORT).show();

                    }
                });




        mConnection.received(new MessageReceivedHandler() {
            @Override
            public void onMessageReceived(final JsonElement json) {

                mHandler.post(new Runnable() {
                    public void run() {
                        String messsage = "";
                        JsonObject jsonObject = json.getAsJsonObject();
                        String methodName = jsonObject.toString();
                        Log.e("<Debug>", "response = " + jsonObject.toString());
                        try {
                            JSONObject jsonObject1 = new JSONObject(methodName);
                            JSONArray jsonArray = null;
                            if (jsonObject1.has("A")) {
                                jsonArray = jsonObject1.getJSONArray("A");
                            }
//                            Log.d("methodObject",jsonArray.toString()+"  d");
                            if (jsonArray != null) {
                                String s = jsonArray.getString(0);
                                JSONObject jsonObject2 = new JSONObject(s);
                                String id = jsonObject2.getString("id");
                                messsage = jsonObject2.getString("message");
                                Log.d("messagesLog", id + "  " + messsage);
                                Toast.makeText(context, messsage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonArray jsonArray = jsonObject.getAsJsonArray("A");
                        Log.d("jsonarraylog", jsonArray + "  l");
                        if (jsonArray != null) {
                            String data = jsonArray.toString();

                            Log.d("datalog", data + " v");

//                            Intent intent = new Intent(BROADCAST_ACTION);
//                            intent.putExtra("message", messsage);
//                            context.sendBroadcast(intent);
                        }
                    }
                });
            }
        });


        mConnection.error(new ErrorCallback() {
            @Override
            public void onError(Throwable error) {
                Log.d("SignalR269", "Connection error: " + error.toString());

                if (!future.isDone()) {
                    future.triggerError(error);
                }
            }
        });
    }

    public void getMessages() {
        mConnection.received(new MessageReceivedHandler() {
            @Override
            public void onMessageReceived(final JsonElement json) {

                mHandler.post(new Runnable() {
                    public void run() {
                        String messsage = "";
                        String noti_type = "";
                        JsonObject jsonObject = json.getAsJsonObject();
                        String methodName = jsonObject.toString();
                        Log.e("<Debug>", "response = " + jsonObject.toString());
                        try {
                            JSONObject jsonObject1 = new JSONObject(methodName);
                            JSONArray jsonArray = null;
                            if (jsonObject1.has("A")) {
                                jsonArray = jsonObject1.getJSONArray("A");
                            }
//                            Log.d("methodObject",jsonArray.toString()+"  d");
                            if (jsonArray != null) {
                                String s = jsonArray.getString(0);
                                JSONObject jsonObject2 = new JSONObject(s);

                                notificationCases(jsonObject2);
                                //  notificationCases(noti_type,messsage);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonArray jsonArray = jsonObject.getAsJsonArray("A");
                        Log.d("jsonarraylog", jsonArray + "  l");
                        if (jsonArray != null) {
                            String data = jsonArray.toString();

                            Log.d("datalog", data + " v");

//                            Intent intent = new Intent(BROADCAST_ACTION);
//                            intent.putExtra("message", messsage);
//                            context.sendBroadcast(intent);
                        }
                    }
                });
            }
        });
    }

    private void notificationCases(JSONObject jsonObject) {
        String key = "";
        String message = "";
        String ride_request_id = "";
        String driver_status = "";

        try {
            message = jsonObject.getString("message");
            key = jsonObject.getString("noti_type");
            driver_status = CSPreferences.readString(context, Constant.DRIVER_STATUS);
            Log.d("driverStatus", driver_status + " ");

            if (key.equals("customer_request")&&jsonObject.has("ride_request_id")) {
                ride_request_id = jsonObject.getString("ride_request_id");
                CSPreferences.putString(SignalRService.this, Constant.REQUEST_ID, ride_request_id);
            }


            if (!driver_status.equals("2") && key.equals("customer_request")) {
                sendNotification(message);
                //Calling method to generate notification
                intent = new Intent(this, NotificatonDialog.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ModelManager.getInstance().getCustomerReQuestManager().CustomerReQuestManager(getApplicationContext(), Operations.
                        getCustomerRequest(getApplicationContext(), CSPreferences.readString(getApplicationContext(), "customer_id")));

              /*  timer = new MyTimer(1500, 1000);
                timer.start();*/
            } else if (key.equals("cancel_ride")) {
                ride_request_id = jsonObject.getString("ride_request_id");
                if (CSPreferences.readString(this, Constant.REQUEST_ID).equals(ride_request_id)) {
                    CSPreferences.putString(this, Constant.DRIVER_STATUS, "5");
                    intent = new Intent(this, HomeScreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    EventBus.getDefault().post(new Event(Constant.CANCEL_RIDEFCM, ""));
                }
            }

        } catch (Exception e) {
            Log.d("Exceptions",e.getMessage()+"");

        }
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, NotificatonDialog.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_icon_logo)
                .setContentTitle("Trala Notification")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

//        ModelManager.getInstance().getCustomerReQuestManager().CustomerReQuestManager(getApplicationContext(), Operations.
//                getCustomerRequest(getApplicationContext(), CSPreferences.readString(getApplicationContext(), "customer_id")));
//

    }

}