package com.appzorro.driverappcabscout.view.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.controller.ReviewManager;
import com.appzorro.driverappcabscout.model.AllAdapter.ReviewAdapter;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import dmax.dialog.SpotsDialog;

/**
 * Created by vijay on 14/2/17.
 */

public class ReviewActivity extends AppCompatActivity{
    Toolbar toolbar;
    RecyclerView recyclerView;
    Context context;
    TextView noReview;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        context =this;
        initviews();
    }

    public void initviews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Reviews");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        recyclerView = (RecyclerView)findViewById(R.id.listreviws);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        noReview =(TextView)findViewById(R.id.textview);


    }
    @Override
    protected void onResume() {
        super.onResume();
        dialog = new SpotsDialog(context);
        dialog.show();
        ModelManager.getInstance().getReviewManager().ReviewManager(context, Operations.reviewofDriver(context, CSPreferences.readString(context,"customer_id")));

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
    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constant.REVIEW:
                dialog.dismiss();
                ReviewAdapter reviewAdapter = new ReviewAdapter(context, ReviewManager.reviewliast);
                recyclerView.setAdapter(reviewAdapter);
                break;
            case Constant.NOREVIEW:
                dialog.dismiss();
                recyclerView.setVisibility(View.GONE);
                noReview.setVisibility(View.VISIBLE);
                break;
            case Constant.SERVER_ERROR:
                Utils.makeSnackBar(context,recyclerView,"Network Error");

               // Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
