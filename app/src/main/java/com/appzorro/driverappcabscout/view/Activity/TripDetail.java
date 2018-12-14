package com.appzorro.driverappcabscout.view.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.model.Beans.CompletedRideBean;
import com.appzorro.driverappcabscout.model.Utils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class TripDetail extends AppCompatActivity {
    ImageView backTo, pathImg;
    TextView tvHelp, tvCash, tvReceipt, tvDte, tvCashType, tvTym, tvTot, tvSource, tvDestination, tvCname, tvTotal, tvVName, tvFare, tvContact;
    RatingBar ratingBar;
    LinearLayout llIssues, llCash;
    CircleImageView CustomerPic;
    Double totl;
    Context context;
    String date, time, rating, vName, customerName, Source, destination, fare, total, profilePic, cashType, pathImgg;
    @BindView(R.id.tvtxtAt)
    TextView tvtxtAt;
    @BindView(R.id.tvISsues)
    TextView tvISsues;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        ButterKnife.bind(this);
        InitViews();
        context = this;
        CliclListener();
        setFontStyle();
        try {
            FetchData();
            setData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void setFontStyle() {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.montserratsemibold);
        Typeface typeface2 = ResourcesCompat.getFont(context, R.font.montserratregular);
        Typeface typeface3 = ResourcesCompat.getFont(context, R.font.montserratlight);
        tvDte.setTypeface(typeface);
        tvtxtAt.setTypeface(typeface);
        tvTym.setTypeface(typeface);
        tvCash.setTypeface(typeface);
        tvVName.setTypeface(typeface3);
        tvCashType.setTypeface(typeface3);
        tvSource.setTypeface(typeface);
        tvDestination.setTypeface(typeface);
        tvCname.setTypeface(typeface);
        tvHelp.setTypeface(typeface);
        tvReceipt.setTypeface(typeface);
        tvISsues.setTypeface(typeface2);
        Typeface typeface2Bold = ResourcesCompat.getFont(context, R.font.montserratbold);
        toolbarTitle.setTypeface(typeface2Bold);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
    private void setData() {
        tvTym.setText(time);
        tvDte.setText(date);
        tvCashType.setText(cashType);
        tvVName.setText(vName);
        tvCash.setText(total);
        tvTotal.setText(total);
        tvTot.setText(total);
        tvSource.setText(Source);
        tvDestination.setText(destination);
        if (rating != null && !rating.equals("0") && !rating.equals("null")) {
            ratingBar.setRating(Float.parseFloat(rating));
        } else {
            ratingBar.setRating(Float.parseFloat("0.0"));

        }


        tvCname.setText(customerName);
        tvFare.setText(total);
        Picasso.with(this).load(profilePic).placeholder(R.color.lightreen).error(R.color.lightreen).into(CustomerPic);
        Picasso.with(this).load(pathImgg).placeholder(R.color.lightreen).error(R.color.lightreen).into(pathImg);


    }

    private void FetchData() {
        Intent intent = getIntent();
        CompletedRideBean completedRideBean = (CompletedRideBean) intent.getSerializableExtra("ListData");
        time = completedRideBean.getStart_tym();
        total = completedRideBean.getTotalamount();
        totl = Double.parseDouble(total);
        //** Convert double to $ format
        total = Utils.currencyConverter(totl);
        //**End
        destination = completedRideBean.getDropLoc();
        Source = completedRideBean.getPickUpLoc();
        date = completedRideBean.getStartdate();
        cashType = completedRideBean.getPaymentType();
        customerName = completedRideBean.getName();
        vName = completedRideBean.getVehicleName();
        profilePic = completedRideBean.getProfilepic();
        pathImgg = completedRideBean.getPathImg();
        rating = completedRideBean.getFeedback();
        if (rating.equalsIgnoreCase("null") || rating.equalsIgnoreCase("")) {
            rating = "0.0";
        }
        if (total.equalsIgnoreCase("null") || total.equalsIgnoreCase("")) {
            total = "N/A";
        }
        if (destination.equalsIgnoreCase("null") || destination.equalsIgnoreCase("")) {
            destination = "N/A";
        }
        if (Source.equalsIgnoreCase("null") || Source.equalsIgnoreCase("")) {
            Source = "N/A";
        }
        if (date.equalsIgnoreCase("null") || date.equalsIgnoreCase("")) {
            date = "N/A";
        }
        if (cashType.equalsIgnoreCase("null") || cashType.equalsIgnoreCase("")) {
            cashType = "N/A";
        }
        if (customerName.equalsIgnoreCase("null") || customerName.equalsIgnoreCase("")) {
            customerName = "N/A";
        }
        if (vName.equalsIgnoreCase("null") || vName.equalsIgnoreCase("")) {
            vName = "N/A";
        }
        if (profilePic.equalsIgnoreCase("null") || profilePic.equalsIgnoreCase("")) {
            profilePic = "N/A";
        }
        if (pathImgg.equalsIgnoreCase("null") || pathImgg.equalsIgnoreCase("")) {
            pathImgg = "N/A";
        }

        Log.d("tyminTri", rating);
    }

    private void CliclListener() {
        backTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TripDetail.this, RideHistory.class));
            }
        });
        tvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llCash.setVisibility(View.GONE);
                llIssues.setVisibility(View.VISIBLE);
                tvReceipt.setBackgroundResource(android.R.color.transparent);
                tvHelp.setBackgroundResource(R.drawable.background_line_blue);


            }
        });
        tvReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvReceipt.setBackgroundResource(R.drawable.background_line_blue);
                tvHelp.setBackgroundResource(android.R.color.transparent);
                llCash.setVisibility(View.VISIBLE);
                llIssues.setVisibility(View.GONE);
            }
        });
        tvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TripDetail.this, HelpActivity.class));
            }
        });

    }

    @SuppressLint("ResourceAsColor")
    private void InitViews() {
        backTo = findViewById(R.id.back);
        pathImg = findViewById(R.id.imgMap);
        tvCash = findViewById(R.id.tvCashVal);
        tvHelp = findViewById(R.id.btHelp);
        tvReceipt = findViewById(R.id.tvRecipt);
        llIssues = findViewById(R.id.llIssues);
        tvContact = findViewById(R.id.tvHelp);
        llCash = findViewById(R.id.llCash);
        tvDte = findViewById(R.id.tvDate);
        tvTot = findViewById(R.id.tvtotValue);
        tvCashType = findViewById(R.id.tvCashType);
        tvTym = findViewById(R.id.tvTym);
        tvFare = findViewById(R.id.tvFareValue);
        tvCname = findViewById(R.id.tvRated);
        ratingBar = (RatingBar) findViewById(R.id.rbUserrating);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();

        setRatingStarColor(stars.getDrawable(2), ContextCompat.getColor(TripDetail.this, R.color.blue));
        // Half filled stars
        setRatingStarColor(stars.getDrawable(1), ContextCompat.getColor(TripDetail.this, R.color.silver));
        // Empty stars
        setRatingStarColor(stars.getDrawable(0), ContextCompat.getColor(TripDetail.this, R.color.silver));
        tvDestination = findViewById(R.id.txt_add2nd);
        tvSource = findViewById(R.id.txt_add1st);
        tvTotal = findViewById(R.id.tvtxCashValue);
        tvVName = findViewById(R.id.tvCarName);
        CustomerPic = findViewById(R.id.imgCustomerPic);
    }

    private void setRatingStarColor(Drawable drawable, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DrawableCompat.setTint(drawable, color);
        } else {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }
}
