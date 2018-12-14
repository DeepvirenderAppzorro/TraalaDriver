package com.appzorro.driverappcabscout.view.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzorro.driverappcabscout.AppController;
import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.NetworkChangeReceiver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;



public class BaseActivity extends AppCompatActivity {

    AlertDialog.Builder builder;
    View view;
    Dialog dialog;
    private ProgressDialog progressDialog;

    protected void showProgressDialog(int messageId) {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
        }

        progressDialog.setMessage(getString(messageId));
        progressDialog.show();
    }

    protected void hideProgressDialo() {
        if (progressDialog == null) {
            return;
        }

        progressDialog.dismiss();
    }

    protected void showAlertDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setCancelable(false)
                .show();
    }

    protected void showAlertDialog(int messageId) {
        showAlertDialog(getString(messageId));
    }

    public void showProgressDialog() {
        if (dialog == null) {
            builder = new AlertDialog.Builder(this, getResources().getColor(R.color.float_transparent));
            view = LayoutInflater.from(this).inflate(R.layout.custom_progress_bar, null);
            builder.setView(view);
            dialog = builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.grey)));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.width = lp.MATCH_PARENT;
            lp.height = lp.MATCH_PARENT;
            dialog.getWindow().setAttributes(lp);
            dialog.setCancelable(false);
        }
        dialog.show();
    }

    public void hideProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showSnackbar(boolean b, final RelativeLayout relativeLayout) {
        if (b) {
            Snackbar snackbar = Snackbar
                    .make(relativeLayout, "Internet Connected !", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.grey));
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setPadding(0, 0, 0, 0);
            snackbar.show();
        } else {
            Snackbar snackbar = Snackbar
                    .make(relativeLayout, "No internet connection !", Snackbar.LENGTH_LONG)
                    .setAction("Setting", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                        }
                    });
            snackbar.setActionTextColor(Color.BLACK);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(getResources().getColor(R.color.white));
            snackbar.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppController.activityPaused();
        try {
            unregisterReceiver(new NetworkChangeReceiver());
        } catch (Exception e) {
            Log.d("RECEIVEREXCEPTION", e.getMessage().toString());
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        registerReceiver();
        AppController.activityResumed();
    }


    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
        }
    }


    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkChangeReceiver(), intentFilter);
    }


    @Override
    public void finish() {
        super.finish();

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

}
