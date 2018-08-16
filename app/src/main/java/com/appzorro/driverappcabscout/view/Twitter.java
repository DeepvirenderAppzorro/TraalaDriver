/*
package com.appzorro.driverappcabscout.view;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.model.Beans.ResponseListener;

public class Twitter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);
        socialAuthAdapter = new SocialAuthAdapter(new ResponseListener(((BitmapDrawable)ivUploadImage.getDrawable()).getBitmap(),etMessage.getText().toString()));
        socialAuthAdapter.addProvider(Provider.TWITTER, R.drawable.twitter);    socialAuthAdapter.authorize(MainActivity.this, Provider.TWITTER);
    }
}
*/
