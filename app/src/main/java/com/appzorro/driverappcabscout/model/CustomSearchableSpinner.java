package com.appzorro.driverappcabscout.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.greenrobot.eventbus.EventBus;

@SuppressLint("ParcelCreator")
public class CustomSearchableSpinner extends SearchableSpinner implements Parcelable {

     public static boolean isSpinnerDialogOpen = false;

     public CustomSearchableSpinner(Context context) {
         super(context);
     }

     public CustomSearchableSpinner(Context context, AttributeSet attrs) {
         super(context, attrs);
     }

     public CustomSearchableSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
         super(context, attrs, defStyleAttr);
     }

    @Override
    public void setPositiveButton(String strPositiveButtonText) {
        super.setPositiveButton(strPositiveButtonText);
    }

    @Override
    public void setPositiveButton(String strPositiveButtonText, DialogInterface.OnClickListener onClickListener) {
        super.setPositiveButton(strPositiveButtonText, onClickListener);

    }

    @Override
     public boolean onTouch(View v, MotionEvent event) {
         if (event.getAction() == MotionEvent.ACTION_UP) {
             if (!isSpinnerDialogOpen) {
                 isSpinnerDialogOpen = true;
                 EventBus.getDefault().post(new Event(Constant.SPINNER_DIALOG, "true"));
                 return super.onTouch(v, event);
             }
             isSpinnerDialogOpen = false;
             EventBus.getDefault().post(new Event(Constant.SPINNER_DIALOG, "false"));
         }
         new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {
                 isSpinnerDialogOpen = false;
             }
         }, 500);
         return true;
     }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}