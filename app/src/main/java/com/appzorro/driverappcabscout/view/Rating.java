package com.appzorro.driverappcabscout.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
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
import com.appzorro.driverappcabscout.model.Beans.CustomerRequest;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class Rating extends AppCompatActivity {
    CustomerRequest customerRequest;
    ArrayList<CustomerRequest> list;
    EditText edtComment;
    TextView date_tv, tx_Amt, tx_Discount, tx_Source, tx_Destination, txCusName;
    AlertDialog dialog;
    LinearLayout linearLayout;
    String picupLoc, dropLoc;
    ImageView back;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_new);
        tx_Amt = (TextView) findViewById(R.id.lbl_Amt);
        tx_Source = findViewById(R.id.txt_add1st);
        txCusName = findViewById(R.id.Customer_name);
        tx_Destination = findViewById(R.id.txt_add2nd);
        tx_Discount = findViewById(R.id.lbl_discount);
        circleImageView = findViewById(R.id.customerPic);
        back = (ImageView) findViewById(R.id.back);

        linearLayout = (LinearLayout) findViewById(R.id.ll_sub);
        edtComment = (EditText) findViewById(R.id.help_desp);
        date_tv = (TextView) findViewById(R.id.lbl_rating_tym);
        String amt = CSPreferences.readString(Rating.this, Constant.BASEFARE);
        Log.d("am", amt);
        tx_Amt.setText("$" + amt);
        //   tx_Discount.setText(CSPreferences.readString(Rating.this, Constant.DISCOUNT));
        //here we  get the detail of cutomer whoes make the request using bean class
        list = new ArrayList<>();
        list = CustomerReQuestManager.requestlis;
        customerRequest = list.get(0);
        picupLoc = customerRequest.getPicLoc();
        dropLoc = customerRequest.getDropLoc();
        tx_Source.setText(picupLoc);
        tx_Destination.setText(dropLoc);
        txCusName.setText(customerRequest.getName());
        Picasso.with(this).load(CSPreferences.readString(Rating.this, "profile_pic")).placeholder(R.drawable.ic_icon_profile_pic).error(R.drawable.ic_icon_profile_pic).into(circleImageView);

        date_tv.setText(Utils.getCurrentDate());
        TextView submit = (TextView) findViewById(R.id.btSubmit);
        final RatingBar ratingBar = (RatingBar) findViewById(R.id.rbUserrating);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        // Filled stars
        setRatingStarColor(stars.getDrawable(2), ContextCompat.getColor(Rating.this, R.color.blue));
        // Half filled stars
        setRatingStarColor(stars.getDrawable(1), ContextCompat.getColor(Rating.this, R.color.white));
        // Empty stars
        setRatingStarColor(stars.getDrawable(0), ContextCompat.getColor(Rating.this, R.color.white));


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Rating.this, HomeScreenActivity.class);
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

                    ModelManager.getInstance().getRatingManager().RatingManager(Rating.this, Operations.sendRatingtoCustomer(Rating.this,
                            CSPreferences.readString(Rating.this, "customer_id"), customerRequest.getCutomerid(), rating, msg));

                } else {
                    Toast.makeText(Rating.this, " Please give Rating", Toast.LENGTH_LONG).show();
                }


            }
        });

    }
    private void setRatingStarColor(Drawable drawable, @ColorInt int color)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            DrawableCompat.setTint(drawable, color);
        }
        else
        {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }


    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constant.RATING:
                dialog.dismiss();
                Toast.makeText(Rating.this, "Thanks for Rating", Toast.LENGTH_LONG).show();
             /*   Dialog dialog = Utils.RatingDialog(Rating.this);
                dialog.show();*/
                //  TextView ok = dialog.findViewById(R.id.txtok);
                Intent intent = new Intent(Rating.this, HomeScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finishAffinity();

               /* ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Rating.this,HomeScreenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finishAffinity();
                    }
                });*/

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
}

