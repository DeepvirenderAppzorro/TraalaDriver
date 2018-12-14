package com.appzorro.driverappcabscout.view.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.CustomerReQuestManager;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.Beans.CustomersRequestBean;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;
import com.appzorro.driverappcabscout.view.Activity.PathmapActivity.view.PathMapActivity;
import com.appzorro.driverappcabscout.view.HomeActivity;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

import static com.appzorro.driverappcabscout.view.Activity.PathmapActivity.view.PathMapActivity.selectedCustomerPosition;

public class Rating extends AppCompatActivity implements LocationListener {

    EditText edtComment;
    TextView date_tv, tx_Amt, tx_Discount, tx_Source, tx_Destination, txCusName;
    AlertDialog dialog;
    LinearLayout linearLayout;
    ImageView back;
    CircleImageView circleImageView;
    Context context;
    @BindView(R.id.lbl_ride)
    TextView lblRide;
    @BindView(R.id.lbl_rating_ques)
    TextView lblRatingQues;
    @BindView(R.id.btSubmit)
    TextView btSubmit;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    TextView textRide;
    public static String chkCashtatus = "";
    CustomersRequestBean.Datum customer_list;
    @BindView(R.id.toolbar_skip)
    TextView toolbarSkip;
    private Location mLastLocation;
    private double latitude, longitude;
    RatingBar ratingBar;

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_new);
        ButterKnife.bind(this);
        context = this;
        intializeUi();
        getLocation();
        setData();

        setRatingBar();

        setFontStyle();
        clickListners();


    }

    private void clickListners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hitInitialAPI();
            }
        });
        toolbarSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Rating.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String rating = String.valueOf(ratingBar.getRating());
                String msg = edtComment.getText().toString();
                if (!rating.equalsIgnoreCase("0.0")) {
                    dialog = new SpotsDialog(Rating.this);
                    dialog.show();
                    try {

                        ModelManager.getInstance().getRatingManager().RatingManager(Rating.this, Operations.sendRatingtoCustomer(Rating.this,
                                CSPreferences.readString(Rating.this, "customer_id"), customer_list.getCustomerId(), rating, msg, customer_list.getRideRequestId()));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(Rating.this, " Please give Rating", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private void setRatingBar() {
        ratingBar = (RatingBar) findViewById(R.id.rbUserrating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                textRide.setText(Utils.setRatingText(ratingBar));
            }
        });
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        // Filled stars
        setRatingStarColor(stars.getDrawable(2), ContextCompat.getColor(Rating.this, R.color.blue));
        // Half filled stars
        setRatingStarColor(stars.getDrawable(1), ContextCompat.getColor(Rating.this, R.color.white));
        // Empty stars
        setRatingStarColor(stars.getDrawable(0), ContextCompat.getColor(Rating.this, R.color.white));

    }

    private void intializeUi() {
        textRide = findViewById(R.id.txt_Ride);
        tx_Amt = (TextView) findViewById(R.id.lbl_Amt);
        tx_Source = findViewById(R.id.txt_add1st);
        txCusName = findViewById(R.id.Customer_name);
        tx_Destination = findViewById(R.id.txt_add2nd);
        tx_Discount = findViewById(R.id.lbl_discount);
        TextView submit = (TextView) findViewById(R.id.btSubmit);
        circleImageView = findViewById(R.id.customerPic);
        back = (ImageView) findViewById(R.id.back);
        linearLayout = (LinearLayout) findViewById(R.id.ll_sub);
        edtComment = (EditText) findViewById(R.id.help_desp);
        date_tv = (TextView) findViewById(R.id.lbl_rating_tym);
    }

    private void setFontStyle() {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.montserratsemibold);
        Typeface typeface3 = ResourcesCompat.getFont(context, R.font.montserratlight);
        Typeface typeface2 = ResourcesCompat.getFont(context, R.font.montserratregular);
        date_tv.setTypeface(typeface3);
        tx_Amt.setTypeface(typeface2);
        tx_Source.setTypeface(typeface2);
        tx_Destination.setTypeface(typeface2);
        txCusName.setTypeface(typeface);
        edtComment.setTypeface(typeface);
        lblRatingQues.setTypeface(typeface);
        lblRide.setTypeface(typeface);
        btSubmit.setTypeface(typeface);
        Typeface typeface2Bold = ResourcesCompat.getFont(context, R.font.montserratbold);
        toolbarTitle.setTypeface(typeface2Bold);

    }

    private void getLocation() {
        mLastLocation = Utils.getLastBestStaleLocation(context);
        if (mLastLocation == null) {
            latitude = 30.7097499;
            longitude = 76.6934968;
        } else {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
    }

    private void setData() {
        String amt = CSPreferences.readString(Rating.this, Constant.BASEFARE);
        Log.d("am", amt);
        if (CustomerReQuestManager.customersRequestBean != null) {
            customer_list = CustomerReQuestManager.customersRequestBean.getResponse().getData().get(selectedCustomerPosition);
            String time = Utils.getCurrentTYm(context);
            date_tv.setText(Utils.getCurrentMonth() + " " + time);
            tx_Amt.setText(amt);

        }

        if (customer_list != null) {
            tx_Source.setText(customer_list.getPickupLocation());
            tx_Destination.setText(customer_list.getDropLocation());
            txCusName.setText(customer_list.getName());
            if (!customer_list.getProfilePic().isEmpty())
                Picasso.with(this).load(Config.baserurl_image + customer_list.getProfilePic()).placeholder(R.drawable.ic_icon_profile_pic).error(R.drawable.ic_icon_profile_pic).into(circleImageView);

        } else

        {
            Utils.makeSnackBar(Rating.this, tx_Source, "Network Problem");
        }
    }

    private void setRatingStarColor(Drawable drawable, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DrawableCompat.setTint(drawable, color);
        } else {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    private void hitInitialAPI() {
        ModelManager.getInstance().getCustomerReQuestManager().CustomerReQuestManager(context, Operations.getCustomerRequest(context,
                CSPreferences.readString(getApplicationContext(), "customer_id"), String.valueOf(latitude), String.valueOf(longitude)), "rating");
    }


    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constant.RATING:
                dialog.dismiss();
                hitInitialAPI();
                Toast.makeText(Rating.this, "Thanks for Rating", Toast.LENGTH_LONG).show();

                break;

            case Constant.RATING_SCREEN:
                if (CustomerReQuestManager.customersRequestBean != null) {
                    selectedCustomerPosition = 0;
                    Intent intent = new Intent(Rating.this, PathMapActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    Intent intent = new Intent(Rating.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    finishAffinity();
                }
                break;
            case Constant.SERVER_ERROR:
                if (CustomerReQuestManager.customersRequestBean != null) {
                    selectedCustomerPosition = 0;
                    Intent intent = new Intent(Rating.this, PathMapActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    Intent intent = new Intent(Rating.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    finishAffinity();
                }
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(Rating.this);

    }


    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(Rating.this);
    }

    public void onBackPressed() {
        return;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

