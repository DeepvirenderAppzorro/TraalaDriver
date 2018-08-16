package com.appzorro.driverappcabscout.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.Beans.CabCompanyBean;
import com.appzorro.driverappcabscout.model.Beans.CompanyLocationBean;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.CustomSearchableSpinner;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pankaj on 23/1/17.
 */
public class CabCompanyActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {
    private final String TAG = CabCompanyActivity.class.getSimpleName();
    Activity activity = this;
    Toolbar toolbar;
    LinearLayout next_register;
    int count = 0;
    TextView alreadyAccount, mtextView_noLocation, selectComapny, select_location;
    BottomSheetDialog bottomSheetDialog;
    int cab_id;
    android.app.AlertDialog dialog;
    CustomSearchableSpinner spinner, selectLocation;
    public ArrayList<String> list;
    String id = "";
    List<String> locationNameList;
    List<CabCompanyBean.CompanyList> cmpnyList;
    List<CompanyLocationBean.LocationList> locationLists = new ArrayList<>();
    private String select_locationId;
    ImageView dropDown, dropdown_loc;
    boolean isDialogOpen = false;
    boolean open = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_company_new);
        initViews();
        getList();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isDialogOpen = false;
            }
        }, 500);


    }


    public void initViews() {
        cmpnyList = new ArrayList<>();

        next_register = (LinearLayout) findViewById(R.id.linear_next);
        alreadyAccount = (TextView) findViewById(R.id.alreadyAccount);
        spinner = findViewById(R.id.selectCab);
        dropDown = (ImageView) findViewById(R.id.dropdown);
        dropdown_loc = (ImageView) findViewById(R.id.dropdown_loc);
        selectLocation = findViewById(R.id.selectLoc);
        mtextView_noLocation = (TextView) findViewById(R.id.nolocation);
        select_location = (TextView) findViewById(R.id.txt_select_Location);
        dialog = new SpotsDialog(activity);
        selectComapny = (TextView) findViewById(R.id.txt_select_company);

        dropDown.setOnClickListener(this);
        dropdown_loc.setOnClickListener(this);

        selectLocation.setPositiveButton("");
        spinner.setPositiveButton("Close", this);
        alreadyAccount.setOnClickListener(this);
        next_register.setOnClickListener(this);
        intializeList();
    }

    public void intializeList() {
        locationNameList = new ArrayList<>();
        list = new ArrayList<String>();
        list.add(0, "Select Company Name");
        locationNameList.add(0, "Select Location");
        setLocationAdapter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alreadyAccount:
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.linear_next:
                if (!spinner.getSelectedItem().toString().equalsIgnoreCase("Select Company Name")) {
                    if (mtextView_noLocation.getVisibility() == View.VISIBLE) {
                        Utils.makeSnackBar(activity, alreadyAccount, "Please Add Location");

                    } else {
                        dialog.show();
                        CSPreferences.putString(CabCompanyActivity.this, Constant.ComapnyName, spinner.getSelectedItem().toString());
                        // ModelManager.getInstance().getCabCompaniesManager().getCabCompanies(activity, id, Operations.getCabCompaniesTask(activity, id));
                        ModelManager.getInstance().getCabCompaniesManager().getCabCompanies(activity, Operations.getCabCompaniesTask(activity, id));


                    }

                } else {
                    Utils.makeSnackBar(activity, alreadyAccount, "Please Select Company Name");

                }
                break;
            case R.id.selectCab:
                bottomSheetDialog.show();
                break;

            case R.id.dropdown:
                //   if (selectCab.getSelectedItem() == null)  // user selected nothing...
                //  spinner.performClick();
                expandSpinner();

                break;
            case R.id.dropdown_loc:
                //   if (selectCab.getSelectedItem() == null)  // user selected nothing...
                // selectLocation.performClick();
                expandSpinner_Loc();

                break;

        }
    }

    @Override
    protected void onStart() {

        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constant.CAB_COMPANIES_SUCCESS:
                dialog.dismiss();
                String id = event.getValue();
                Intent intCab = new Intent(activity, SignUp.class);
                intCab.putExtra("cab_id", id);
                CSPreferences.putString(CabCompanyActivity.this, "CompanyIdAb", id);
                Log.d("Comapnyid", id + "");
                CSPreferences.putString(CabCompanyActivity.this, "LocationId", select_locationId);
                intCab.putExtra("location_id", select_locationId);
                startActivity(intCab);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
            case Constant.SERVER_ERROR:
                dialog.dismiss();
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Network Connection is too weak")
                        .show();
                break;
            case Constant.COMPANYNOTREGISTERD:
                dialog.dismiss();
                String message = event.getValue();
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Select Company Name")
                        .setContentText("Please Select Company Name")
                        .show();
                break;
            case Constant.COMPANYLOCATION_SUCCESS:


                //  selectComapny.setVisibility(View.VISIBLE);
                locationNameList = new ArrayList<>();

                NolocationGone();
                locationLists = ModelManager.getInstance().getCabCompaniesManager().companyLocationBean.getResponse().getLocationList();
                for (int j = 0; j < locationLists.size(); j++) {
                    if (!locationLists.get(j).getLocationName().isEmpty())
                        locationNameList.add(locationLists.get(j).getLocationName());
                }
                setLocationAdapter();
                dialog.dismiss();
                mtextView_noLocation.setVisibility(View.GONE);
                dropdown_loc.setVisibility(View.VISIBLE);

                break;

            case Constant.COMPANYLOCATION_FAILURE:
                dialog.dismiss();

                NolocationVisible();

                break;

            case Constant.SPINNER_DIALOG:
                String isvisible = event.getValue();
                if (isvisible.equals("true") && list.get(0).equals("Select Company Name")) {
                    list.remove(0);
                    isDialogOpen = true;
                } else if (isDialogOpen && isvisible.equals("false"))
                    list.add(0, "Select Company Name");
                break;
            case Constant.OPENDIALOG:
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(spinner.getWindowToken(), 0);
                break;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getList() {
        dialog = new SpotsDialog(CabCompanyActivity.this);
        dialog.show();
        dialog.setCancelable(false);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CabCompanyBean> call = apiService.GetCompanyList();
        call.enqueue(new Callback<CabCompanyBean>() {

            @Override
            public void onResponse(Call<CabCompanyBean> call, Response<CabCompanyBean> response) {
                dialog.dismiss();
                boolean res = response.isSuccessful();
                String msg = response.body().getResponse().getMessage();
                cmpnyList = response.body().getResponse().getCompanyList();
                cmpnyList.get(0).getCompanyName();
                for (int i = 0; i < cmpnyList.size(); i++) {
                    if (cmpnyList.get(i).getCompanyName().equalsIgnoreCase("")) {

                    } else {
                        list.add(cmpnyList.get(i).getCompanyName());

                    }
                }

                setCabCompanyAdapter();
            }
            //End


            private void setCabCompanyAdapter() {
                ArrayAdapter<String> adp = new ArrayAdapter<String>(CabCompanyActivity.this, android.R.layout.simple_list_item_1, list);
                spinner.setAdapter(adp);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        if (isDialogOpen && !list.contains("Select Company Name"))
//                            list.add(0, "Select Company Name");
                        EventBus.getDefault().post(new Event(Constant.OPENDIALOG, "true"));

                        id = cmpnyList.get(i).getComapnyId();

                        if (!list.get(i).equals("Select Company Name")) {
                            if (locationNameList.get(0).equals("Select Location"))
                                locationNameList.remove(0);

                            dialog.show();
                            ModelManager.getInstance().getCabCompaniesManager().getLocationId(activity, Operations.getLocationId(activity, id));
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                Log.e("msg_trip", cmpnyList.size() + "");
            }

            @Override
            public void onFailure(Call<CabCompanyBean> call, Throwable t) {
                dialog.dismiss();
                Utils.makeSnackBar(CabCompanyActivity.this, spinner, "Network Problem");
            }

        });
        Hide_Key(spinner);
    }

    private void setLocationAdapter() {
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locationNameList);
        selectLocation.setAdapter(stringArrayAdapter);

        selectLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (locationLists.size() > 0)
                    select_locationId = locationLists.get(i).getLocationId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void expandSpinner() {
        MotionEvent motionEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, 0, 0, 0);
        spinner.dispatchTouchEvent(motionEvent);
    }

    public void expandSpinner_Loc() {
        MotionEvent motionEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, 0, 0, 0);
        selectLocation.dispatchTouchEvent(motionEvent);
    }

    private void NolocationVisible() {
        dropdown_loc.setVisibility(View.GONE);
        selectLocation.setVisibility(View.GONE);
        mtextView_noLocation.setVisibility(View.VISIBLE);
    }

    private void NolocationGone() {
        selectLocation.setVisibility(View.VISIBLE);
        mtextView_noLocation.setVisibility(View.GONE);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (isDialogOpen && !list.contains("Select Company Name"))
            list.add(0, "Select Company Name");
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
       /* InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);*/
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(spinner.getWindowToken(), 0);
        return true;
    }

    public void Hide_Key(Spinner spinner) {
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(spinner.getWindowToken(), 0);
        }
    }


}
