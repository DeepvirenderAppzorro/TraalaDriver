package com.appzorro.driverappcabscout.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzorro.driverappcabscout.R;

public class HelpActivity extends AppCompatActivity {


    Toolbar toolbar;
    EditText message;
    String strMessage;
    TextView email;
    LinearLayout submit;
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_xml);
        initViews();
    }

    public void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Help");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        email = (TextView) findViewById(R.id.email);
        iv_back = (ImageView) findViewById(R.id.back);
        message = (EditText) findViewById(R.id.message);
        submit = (LinearLayout) findViewById(R.id.submit_ll);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strMessage = message.getText().toString();
                if (strMessage.isEmpty())
                    Toast.makeText(HelpActivity.this, "Please write your query for help!", Toast.LENGTH_SHORT).show();
                else
                    finish();

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
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
