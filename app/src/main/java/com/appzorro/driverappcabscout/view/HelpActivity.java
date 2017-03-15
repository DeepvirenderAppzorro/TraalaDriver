package com.appzorro.driverappcabscout.view;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;

public class HelpActivity extends AppCompatActivity {

    Toolbar toolbar;
    CardView callcvard,emailcard;
    TextView call , email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        initViews();

        emailcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent variableName = new Intent(Intent.ACTION_SENDTO);
                variableName.setType("text/html");
                variableName.setData(Uri.parse("mailto:"));
                variableName.putExtra(Intent.EXTRA_SUBJECT, "");
                variableName.putExtra(Intent.EXTRA_TEXT, "");

                startActivity(variableName);
            }
        });

        callcvard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", call.getText().toString(), null));
                startActivity(intent);
            }
        });
    }

    public void initViews() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Help");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        emailcard =(CardView)findViewById(R.id.emailcard);
        callcvard =(CardView)findViewById(R.id.callcard);
        call =(TextView)findViewById(R.id.txtcall);
        email =(TextView)findViewById(R.id.txtemail);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
