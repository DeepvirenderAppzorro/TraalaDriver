package com.appzorro.driverappcabscout.view.Activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzorro.driverappcabscout.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText message;
    String strMessage;
    TextView email;
    LinearLayout submit;
    Context context;
    ImageView iv_back;
    @BindView(R.id.image_email)
    ImageView imageEmail;
    @BindView(R.id.login_btn)
    TextView loginBtn;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_xml);
        ButterKnife.bind(this);
        context = this;
        initViews();
        setFontStyle();
      /*  final Handler handler=new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                handler.postDelayed(this, 1000);
                count++;
                Log.d("ChkHandler","Fade run " + count);
                fade();

            }
        };
        r.run();
        fade();*/
    }

    private void setFontStyle() {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.montserratsemibold);
        Typeface typeface2 = ResourcesCompat.getFont(context, R.font.montserratregular);
        email.setTypeface(typeface2);
        message.setTypeface(typeface2);
        loginBtn.setTypeface(typeface);

    }

    public void fade() {
        ImageView image = (ImageView) findViewById(R.id.image_email);
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.fade);
        image.startAnimation(animation1);
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

    @OnClick(R.id.image_email)
    public void onViewClicked() {
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
}
