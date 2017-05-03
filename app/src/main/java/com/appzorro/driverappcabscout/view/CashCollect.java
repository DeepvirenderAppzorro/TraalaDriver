package com.appzorro.driverappcabscout.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.CustomerReQuestManager;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.Beans.CustomerRequest;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class CashCollect extends AppCompatActivity {
    Toolbar toolbar;
    TextView basefare, timefare,discount,totalfare,disancefare,collectCash,tollfee,stopfee;
    double dbtimefare=1,dbdistancefare=0.05,dbbasefare=40,dbtoatl;
    Context context;
    Dialog ratingdialog;
    double totalamount=0;
    CustomerRequest customerRequest;
    android.app.AlertDialog dialog;
    BottomSheetDialog bottomSheetDialog;

    ArrayList<CustomerRequest>list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_collect);
        context =this;
        initviews();

        collectCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new SpotsDialog(context);
                dialog.show();
                ModelManager.getInstance().getCollectCashmanager().CollectCashmanager(context, Operations.collectCash(context,
                        CSPreferences.readString(context,"customer_id"),customerRequest.getRequestid(),totalfare.getText().toString()));

            }
        });
    }
    public void initviews(){
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Collect Cash");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        basefare=(TextView)findViewById(R.id.txtbasefare);
        timefare=(TextView)findViewById(R.id.txttimefare);
        discount=(TextView)findViewById(R.id.txtriderdiscount);
        totalfare=(TextView)findViewById(R.id.txtcash);
        disancefare=(TextView)findViewById(R.id.txtdistancefare);
        collectCash=(TextView)findViewById(R.id.collectcash);
        tollfee =(TextView)findViewById(R.id.txttollamount);
        stopfee=(TextView)findViewById(R.id.txtstopfee);

        Toast.makeText(context, "distance "+CSPreferences.readString(context,"distance"), Toast.LENGTH_SHORT).show();

        tollfee.setText(CSPreferences.readString(context,"TOLLFEE"));
        stopfee.setText(CSPreferences.readString(context,"STOPFEE"));
        totalamount =(totalamount+(10*dbtimefare)+(1000*dbdistancefare)+Double.parseDouble(CSPreferences.readString(context,"TOLLFEE"))
        +Double.parseDouble(CSPreferences.readString(context,"STOPFEE"))+dbbasefare);

        Log.e("total ammount",String.valueOf(totalamount));

        basefare.setText(String.valueOf(dbbasefare));
        timefare.setText(String.valueOf(10*dbtimefare));
        disancefare.setText(String.valueOf(1000*dbdistancefare));
        discount.setText("50");
        totalamount =totalamount-50.00;
        totalfare.setText(String.valueOf(totalamount));


        Log.e("total ammount",String.valueOf(totalamount));





        list = CustomerReQuestManager.requestlis;

        customerRequest = list.get(0);

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
              /*  ratingdialog = Utils.craeteRatingDilaog(context);
                ratingdialog.show();
                final RatingBar ratingBar =(RatingBar)ratingdialog.findViewById(R.id.rbUserrating);
               // final EditText comment =(EditText)ratingdialog.findViewById(R.id.etComment);
                TextView submit =(TextView) ratingdialog.findViewById(R.id.btSubmit);

              *//*  submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String rating = String.valueOf(ratingBar.getRating());
                      //  String feedback  = comment.getText().toString();
                        dialog= new SpotsDialog(context);
                        dialog.show();
                        ModelManager.getInstance().getRatingManager().RatingManager(context,Operations.sendRatingtoCustomer(context,
                                CSPreferences.readString(context,"customer_id"),customerRequest.getCutomerid(),rating,feedback));
                    }
                });
*/
                bottomSheetDialog = new BottomSheetDialog(context);
                bottomSheetDialog.setContentView(R.layout.activity_rating);
                bottomSheetDialog.show();
                bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface){

                        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(android.support.design.R.id.design_bottom_sheet);
                        assert bottomSheet != null;
                        BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                });

                TextView submit = (TextView)bottomSheetDialog.findViewById(R.id.btSubmit);
                final RatingBar ratingBar =(RatingBar)bottomSheetDialog.findViewById(R.id.rbUserrating);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String rating = String.valueOf(ratingBar.getRating());

                        ModelManager.getInstance().getRatingManager().RatingManager(context,Operations.sendRatingtoCustomer(context,
                                CSPreferences.readString(context,"customer_id"),customerRequest.getCutomerid(),rating,"feedback"));


                    }
                });
                break;

            case Constant.RATING:
                dialog.dismiss();
                Intent intent = new Intent(context,HomeScreenActivity.class);
                startActivity(intent);
                finish();
                break;

            case Constant.SERVER_ERROR:

                Toast.makeText(context, "Network connection is very slow please check your connection", Toast.LENGTH_SHORT).show();

                break;

        }
    }

}
