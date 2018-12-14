package com.appzorro.driverappcabscout.model;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.appzorro.driverappcabscout.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by vijay on 15/10/18.
 */

public class NotificationUtils {

    private static String TAG = NotificationUtils.class.getSimpleName();

    private Context mContext;
    Bitmap bitmap;
    Uri alarmSound;
    private static final String CHANNEL_ID = "channel_01";
    PendingIntent resultPendingIntent;
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    NotificationCompat.Builder mBuilder;
    public static final String PUSH_NOTIFICATION = "pushNotification";


    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void showNotificationMessage(String title, String message, String timeStamp, Intent intent, String img) {
        showNotificationMessages(title, message, timeStamp, intent, img);
    }

    public void showNotificationMessages(final String title, final String message, final String timeStamp, Intent intent, String imageUrl) {
        final int icon = R.drawable.ic_launcher;
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        resultPendingIntent =
                PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext.getPackageName() + "/raw/notification");
        mBuilder = new NotificationCompat.Builder(mContext);
        imageUrl = "http://traala.com/cabscout/profile_pics/" + imageUrl;

        if (!TextUtils.isEmpty(imageUrl)) {
            if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
                //bitmap = getBitmapFromURL(imageUrl);
                new sendNotification(mContext, imageUrl,message).execute();

            }
        } else {
            showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
            playNotificationSound();
        }
    }

    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(message);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = mContext.getString(R.string.app_name);
            // Create the channel for the notification
            @SuppressLint("WrongConstant") NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(bitmap)
                .setContentText(message)
                .setPriority(Notification.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Traala"))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);;

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(CHANNEL_ID); // Channel ID
        }

        notificationManager.notify(NOTIFICATION_ID, notification.build());
    }

    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, PendingIntent resultPendingIntent, Uri alarmSound) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
                .setSmallIcon(R.drawable.ic_launcher)
               // .setLargeIcon(bitmap)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID_BIG_IMAGE, notification);
    }


    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("err", e.getMessage());
            return null;
        }
    }

    // Playing notification sound
    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }


    private class sendNotification extends AsyncTask<String, Void, Bitmap> {

        Context ctx;
        String imageUrl;
        String message;

        public sendNotification(Context context, String imageUrl,String msg) {
            super();
            ctx = context;
            this.imageUrl = imageUrl;
            message=msg;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {

                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);
            try {
                Log.d("hi", "dhbdj");
               // bitmap = compressImage(result);
                bitmap=result;
                final int icon = R.drawable.ic_launcher;
                if (bitmap != null) {
                    showSmallNotification(mBuilder, icon, "Traala", message, "", resultPendingIntent, alarmSound);

                    //   showBigNotification(bitmap, mBuilder, icon, "traala", "hii", resultPendingIntent, alarmSound);
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    Bitmap compressImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);

        byte[] byteArray = stream.toByteArray();
        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return compressedBitmap;
    }

}