package com.appzorro.driverappcabscout.view.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyEarning extends AppCompatActivity {
    @BindView(R.id.img_daily)
    ImageView imgDaily;
    @BindView(R.id.ll_daily_earning)
    LinearLayout llDailyEarning;
    @BindView(R.id.img_monthly)
    ImageView imgMonthly;
    @BindView(R.id.ll_weekly)
    LinearLayout llWeekly;
    @BindView(R.id.img_weekly)
    ImageView imgWeekly;
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.card_view_get)
    CardView cardViewGet;
    @BindView(R.id.ll_monthly)
    LinearLayout llMonthly;
    boolean clicked = false;
    boolean clickedM = false;
    boolean clickedW = false;
    @BindView(R.id.sc_view)
    ScrollView scView;
    @BindView(R.id.tx_dldate)
    TextView txDldate;
    @BindView(R.id.tx_dltotfee)
    TextView txDltotfee;
    @BindView(R.id.tx_dlfare)
    TextView txDlfare;
    @BindView(R.id.flash)
    ImageView flash;
    @BindView(R.id.tx_dlsurge)
    TextView txDlsurge;
    @BindView(R.id.tx_dlboots)
    TextView txDlboots;
    @BindView(R.id.tx_dlfee)
    TextView txDlfee;
    @BindView(R.id.tx_dltymonline)
    TextView txDltymonline;
    @BindView(R.id.tx_dlcompleted)
    TextView txDlcompleted;
    Context context;
    SimpleDateFormat mdformat;
    String dltotlAmt, dlDate, dlTotRides, dlTime, date;
    Calendar calendar;
    @BindView(R.id.dltxEstiPayout)
    TextView dltxEstiPayout;
    LinearLayout ll_main_dailyE,ll_main_monthlyE,ll_main_weeklyE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_earning);
        context = this;
        ll_main_dailyE=findViewById(R.id.ll_main_daily);
        ll_main_monthlyE=findViewById(R.id.ll_main_monthly);
        ll_main_weeklyE=findViewById(R.id.ll_main_weekly);
        ButterKnife.bind(this);
        getCurrentDate();

        try {
            ButterKnife.bind(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ModelManager.getInstance().getDailyEarning().getDailyEarn(context, Operations.getDailyEarn(context,
                CSPreferences.readString(context, "customer_id")));


    }
    private void getCurrentDate()
    {
        calendar = Calendar.getInstance();
        mdformat = new SimpleDateFormat("EEEE dd LLLL");
        date = "" + mdformat.format(calendar.getTime());
        txDldate.setText(date);
    }

    @OnClick({R.id.ll_earnings, R.id.ll_monthlys, R.id.ll_weeklys, R.id.image_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
               // startActivity(new Intent(MyEarning.this, HomeActivity.class));
                break;
            case R.id.ll_earnings:
                if (!clicked) {
                    clicked = true;
                    imgDaily.setImageResource(R.drawable.ic_icon_downarrow_icon);
                    ll_main_dailyE.setVisibility(View.VISIBLE);
                    ll_main_weeklyE.setVisibility(View.GONE);
                    ll_main_monthlyE.setVisibility(View.GONE);
                } else {
                    clicked = false;
                    ll_main_dailyE.setVisibility(View.GONE);
                    ll_main_weeklyE.setVisibility(View.GONE);
                    ll_main_monthlyE.setVisibility(View.GONE);
                    imgDaily.setImageResource(R.drawable.ic_icon_next_arrow);
                }
                imgWeekly.setImageResource(R.drawable.ic_icon_next_arrow);
                imgMonthly.setImageResource(R.drawable.ic_icon_next_arrow);

                break;
            case R.id.ll_monthlys:
                if (!clickedM) {
                    clickedM = true;
                    imgMonthly.setImageResource(R.drawable.ic_icon_downarrow_icon);
                    ll_main_dailyE.setVisibility(View.GONE);
                    ll_main_monthlyE.setVisibility(View.VISIBLE);
                    ll_main_weeklyE.setVisibility(View.GONE);
                } else {
                    clickedM = false;
                    imgMonthly.setImageResource(R.drawable.ic_icon_next_arrow);
                    ll_main_dailyE.setVisibility(View.GONE);
                    ll_main_monthlyE.setVisibility(View.GONE);
                    ll_main_weeklyE.setVisibility(View.GONE);
                 //   imgMonthly.setImageResource(R.drawable.ic_icon_next_arrow);
                }
                imgDaily.setImageResource(R.drawable.ic_icon_next_arrow);
                imgWeekly.setImageResource(R.drawable.ic_icon_next_arrow);
                break;
            case R.id.ll_weeklys:
                if (!clickedW) {
                    clickedW = true;
                 //   imgWeekly.setRotation(imgWeekly.getRotation() + 90);
                    imgWeekly.setImageResource(R.drawable.ic_icon_downarrow_icon);
                    ll_main_dailyE.setVisibility(View.GONE);
                    ll_main_monthlyE.setVisibility(View.GONE);
                    ll_main_weeklyE.setVisibility(View.VISIBLE);
                } else {
                    clickedW = false;
                    //imgWeekly.setRotation(imgWeekly.getRotation() - 90);
                    ll_main_dailyE.setVisibility(View.GONE);
                    ll_main_monthlyE.setVisibility(View.GONE);
                    ll_main_weeklyE.setVisibility(View.GONE);
                    imgWeekly.setImageResource(R.drawable.ic_icon_next_arrow);
                }
                imgMonthly.setImageResource(R.drawable.ic_icon_next_arrow);
                imgDaily.setImageResource(R.drawable.ic_icon_next_arrow);
                break;
        }
    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constant.DAILYEARN:
                dltotlAmt = CSPreferences.readString(context, Constant.DLTOTALAMT);
                dlTime = CSPreferences.readString(context, Constant.DLTIME);
                dlDate = CSPreferences.readString(context, Constant.DLDATE);
                dlTotRides = CSPreferences.readString(context, Constant.DLRIDES);
                txDlfare.setText(dltotlAmt);
                txDlfee.setText(dltotlAmt);
                txDlcompleted.setText(dlTotRides);
                txDltymonline.setText(dlTime);
                txDltotfee.setText(dltotlAmt);
                dltxEstiPayout.setText(dltotlAmt);
                break;
        }
    }
   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }*/
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }
}
