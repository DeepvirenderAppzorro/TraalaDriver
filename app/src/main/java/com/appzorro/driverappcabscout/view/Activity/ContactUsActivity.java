package com.appzorro.driverappcabscout.view.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.lblemail)
    TextView lblemail;
    @BindView(R.id.lblEmailValue)
    TextView lblEmailValue;
    @BindView(R.id.lbl_phone)
    TextView lblPhone;
    @BindView(R.id.lbl_phoneValue)
    TextView lblPhoneValue;
    @BindView(R.id.lblBottom)
    TextView lblBottom;
    private CardView message_CardVl, mail_CardVl, phone_CardVl, sendBtn_CardVl;
    private Activity activity;
    private ImageView img_Back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        activity = this;
        context = this;
        initViews();
        setFontStyle();
        clickListeners();
    }


    private void initViews() {

        //  message_CardVl = (CardView) findViewById(R.id.cardView_message);
        mail_CardVl = (CardView) findViewById(R.id.cardView_mail);
        phone_CardVl = (CardView) findViewById(R.id.cardView_phone);
        //  sendBtn_CardVl = (CardView) findViewById(R.id.cardView_send);
        img_Back = (ImageView) findViewById(R.id.back);
    }

    private void clickListeners() {
        mail_CardVl.setOnClickListener(this);
//        sendBtn_CardVl.setOnClickListener(this);
        phone_CardVl.setOnClickListener(this);
        //  message_CardVl.setOnClickListener(this);
        img_Back.setOnClickListener(this);
    }

    private void setFontStyle() {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.montserratsemibold);
        Typeface typeface3 = ResourcesCompat.getFont(context, R.font.montserratlight);
        Typeface typeface2Bold = ResourcesCompat.getFont(context, R.font.montserratbold);
        lblBottom.setTypeface(typeface3);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.cardView_mail:
                mailHelpline("traala@mail.com");
                break;

            case R.id.cardView_phone:
                onCallBtnClick();
                break;

           /* case R.id.cardView_send:
                Toast.makeText(activity, "You will notify about your last ride details soon!!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cardView_message:
                startActivity(new Intent(activity, HelpActivity.class));
                break;*/
            case R.id.back:
                finish();
                break;
        }
    }


    private void onCallBtnClick() {
        if (Build.VERSION.SDK_INT < 23) {
            try {
                callHelpline("9501875983");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            if (ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                try {
                    callHelpline("9501875983");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 9);
            }
        }
    }

    private void callHelpline(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private void mailHelpline(String email) {
        Intent intent = new Intent(Intent.ACTION_VIEW);//common intent
        intent.setData(Uri.parse("mailto:" + email));
        startActivity(intent);
    }
}
