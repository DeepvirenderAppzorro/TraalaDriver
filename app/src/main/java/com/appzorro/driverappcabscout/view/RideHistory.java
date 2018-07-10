package com.appzorro.driverappcabscout.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.AllAdapter.CompleteAdapter_new;
import com.appzorro.driverappcabscout.model.Beans.CompletedRideBean;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Date_Time;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dmax.dialog.SpotsDialog;

import static com.appzorro.driverappcabscout.model.Date_Time.date;

public class RideHistory extends AppCompatActivity implements RecyclerView.OnItemTouchListener {
    MenuItem shareditem;
    Context context;
    String searchdate;
    ImageView calnder, back_img;
    Toolbar toolbar;
    TextView txtsearchdate, txttotalearning, txttotaltrips, txtnotrips;
    RecyclerView recyclerView;
    Boolean isScrolling = true;
    android.app.AlertDialog dialog;
    ArrayList<CompletedRideBean> tripshistory;
    int totalearning = 0;
    int state = 0;
    SimpleDateFormat outputFormat2;
    CompleteAdapter_new completedtrpsAdapter;
    int currentItem, totalItem, ScrollItem;
    LinearLayoutManager layoutManager;
    ProgressBar progressBar;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int currentPage = 1, lastPage = 1;
    private boolean loading = true;
    public static  ArrayList<CompletedRideBean>trips;
    Calendar calendar;
    SimpleDateFormat mdformat,mdformatM;
    String strDate,stDate2;
    Dialog messagedialog;
    TextView month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_ride_history);
        context = this;
        initviews();

        dialog = new SpotsDialog(context);
        dialog.show();

        ModelManager.getInstance().getTripsmanager().Tripsmanager(context, Operations.tripsCompleted(context,
                CSPreferences.readString(context, "customer_id"),strDate,currentPage+""));


        // back_button
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //edit fields added by deep

        calnder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 1) {
                    //    shareditem.setVisible(false);
                    dialog = new SpotsDialog(context);
                    dialog.show();
                    Log.d("searchdateLog", searchdate + "  l");

                    ModelManager.getInstance().getTripsmanager().Tripsmanager(context, Operations.tripsCompleted(context,
                            CSPreferences.readString(context, "customer_id"),strDate,currentPage+""));


                    calnder.setImageResource(R.drawable.ic_icon_clanders);
                    state = 0;


                } else {
                    calnder.setImageResource(R.mipmap.tick);
                    //  shareditem.setVisible(true);
                    Date_Time date_time = new Date_Time(context);
                    date_time.dateDialog();

                    state = 1;
                }

            }
        });



    }

    public void initviews() {

        trips = new ArrayList<>();
        month=(TextView)findViewById(R.id.lbl_month);
        txttotaltrips = (TextView) findViewById(R.id.txttotaltrips);
        txtsearchdate = (TextView) findViewById(R.id.datetime);
        txttotalearning = (TextView) findViewById(R.id.dayearning);
        recyclerView = (RecyclerView) findViewById(R.id.completedridelist);
        recyclerView.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL));
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        calnder = (ImageView) findViewById(R.id.img_calnder);
        txtnotrips = (TextView) findViewById(R.id.notrips);
        back_img = (ImageView) findViewById(R.id.back);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        calendar = Calendar.getInstance();
        mdformat = new SimpleDateFormat("dd-MM-yyyy");
        strDate = "" + mdformat.format(calendar.getTime());
        messagedialog = Utils.createMessageDialog(context);
        mdformatM = new SimpleDateFormat("LLLL dd");
        stDate2 = "" + mdformatM.format(calendar.getTime());
        messagedialog = Utils.createMessageDialog(context);
        month.setText(stDate2);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) //check for scroll down
                {
                    //progressBar.setVisibility(View.VISIBLE);
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            if (currentPage < lastPage) {
                                currentPage++;

                                ModelManager.getInstance().getTripsmanager().Tripsmanager(context, Operations.tripsCompleted(context,
                                        CSPreferences.readString(context, "customer_id"),strDate,currentPage+""));
                            }
                            Log.v("SelfLastPage", "Last Item Wow !");
                        }
                    }
                } else if (dy < 0) {

                }
            }
        });
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
            case Constant.DATE:
                searchdate = date;
                strDate=searchdate;
                DateFormat outputFormatt = new SimpleDateFormat("LLLL dd");
                DateFormat inputFormatt = new SimpleDateFormat("dd-MM-yyyy");

                Date date1 = null;
                try {
                    date1 = inputFormatt.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String outputText = outputFormatt.format(date1);
                month.setText(outputText);
                trips.clear();
                break;
            case Constant.TRIPSHOSTORY:
                txtnotrips.setVisibility(View.GONE);
                dialog.dismiss();
                if(event.getValue()!=null) {
                    try {
                        JSONObject jsonObject = new JSONObject(event.getValue());
                        JSONObject res = jsonObject.getJSONObject("response");
                        String msg = res.getString("message");
                        String status = res.getString("status");
                        currentPage = Integer.parseInt(res.getString("current_page"));
                        lastPage = Integer.parseInt(res.getString("last_page"));
                        JSONArray jsonArray = res.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String requestid = jsonObject1.getString("ride_request_id");
                                String pickcordinate = jsonObject1.getString("pickup_cordinates");
                                String dropcordinate = jsonObject1.getString("drop_cordinates");
                                String startdate = jsonObject1.getString("start_date");
                                String startTym = jsonObject1.getString("start_time");
                                String enddate = jsonObject1.getString("end_date");
                                String endTym = jsonObject1.getString("end_time");
                                String totalamount = jsonObject1.getString("total_amount");
                                JSONObject jsonObject2 = jsonObject1.getJSONObject("Customer Detail");
                                String customername = jsonObject2.getString("name");
                                String id = jsonObject2.getString("id");
                                String profile = jsonObject2.getString("profile_pic");
                                String mobile = jsonObject2.getString("mobile");
                                String picsplit[] = pickcordinate.split(",");
                                String picklat = picsplit[picsplit.length - 2];
                                String picklng = picsplit[picsplit.length - 1];
                                String dropsplit[] = dropcordinate.split(",");
                                String droplat = dropsplit[dropsplit.length - 2];
                                String droplng = dropsplit[dropsplit.length - 1];
                                SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE LLLL dd");
                                outputFormat2 = new SimpleDateFormat(" LLLL EEEE");
                                SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                                Date date = inputFormat.parse(startdate);
                                System.out.println(outputFormat.format(date));
                                CompletedRideBean completedRideBean = new CompletedRideBean(requestid, picklat, picklng, droplat, droplng,
                                        outputFormat.format(date), startTym, enddate, endTym, totalamount, customername, Config.baserurl_image + profile, mobile);
                                trips.add(completedRideBean);

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (trips.size() <= 6) {
                        completedtrpsAdapter = new CompleteAdapter_new(context, trips);
                        recyclerView.setAdapter(completedtrpsAdapter);
                    } else {
                        completedtrpsAdapter.customNotify(context, trips);
                        //    progressBar.setVisibility(View.GONE);
                    }


                }

                break;
            case Constant.BLANKLIST:
                dialog.dismiss();
                txtnotrips.setVisibility(View.VISIBLE);
                //     recyclerView.setVisibility(View.GONE);
                txttotalearning.setText("00:00");
                txttotaltrips.setText("0");
                break;
            case Constant.CANCEL_RIDEFCM:

                messagedialog.show();

                TextView titletext = (TextView) messagedialog.findViewById(R.id.txttitle);
                TextView messagetext = (TextView) messagedialog.findViewById(R.id.txtmessage);

                TextView ok = (TextView) messagedialog.findViewById(R.id.txtok);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        finish();
                    }
                });
                break;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }
    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    public String currentTime() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        Log.d("datecurrent", date);
        return date;
    }
}
