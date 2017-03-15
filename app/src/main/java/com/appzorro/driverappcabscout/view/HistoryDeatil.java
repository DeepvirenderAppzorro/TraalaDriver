package com.appzorro.driverappcabscout.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.Tripsmanager;
import com.appzorro.driverappcabscout.model.Beans.CompletedRideBean;
import com.appzorro.driverappcabscout.model.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HistoryDeatil extends AppCompatActivity {
    Toolbar toolbar;
    TextView customername,rideprice,datetime,pickuplocation,droplocation;
    ImageView profilepic;
    ArrayList<CompletedRideBean>list;
    Context context;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_deatil);
        context=this;
        initviews();
    }

    public void initviews(){
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Ride Detail");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        customername =(TextView)findViewById(R.id.editname);
        rideprice =(TextView)findViewById(R.id.edtrideprice);
        datetime =(TextView)findViewById(R.id.requestdate);
        pickuplocation =(TextView)findViewById(R.id.pickuplocation);
        droplocation =(TextView)findViewById(R.id.droplocation);
        profilepic =(ImageView) findViewById(R.id.customerPics);
        ratingBar =(RatingBar)findViewById(R.id.rbCustomer);
        ratingBar.setRating((float) 4.0);

        if (intent!=null){

            int postion = Integer.parseInt(intent.getStringExtra("postion"));
            list= Tripsmanager.trips;
            CompletedRideBean completedRideBean= list.get(postion);
            customername.setText(""+completedRideBean.getName());
            rideprice.setText(""+completedRideBean.getTotalamount());
            pickuplocation.setText(""+ Utils.getCompleteAddressString(context,Double.parseDouble(completedRideBean.getPickuplat()),
                    Double.parseDouble(completedRideBean.getPickuplng())));
            droplocation.setText(""+ Utils.getCompleteAddressString(context,Double.parseDouble(completedRideBean.getDroplat()),
                    Double.parseDouble(completedRideBean.getDroplng())));
         /*   pickuplocation.setText("Chandigarh");
            droplocation.setText("Mohali Punjab");*/
            Picasso.with(this)
                    .load(completedRideBean.getProfilepic())
                    .into(profilepic);
        }

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
