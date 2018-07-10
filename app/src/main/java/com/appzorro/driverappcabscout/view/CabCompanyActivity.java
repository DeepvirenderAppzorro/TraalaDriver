package com.appzorro.driverappcabscout.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.*;
import com.appzorro.driverappcabscout.model.Beans.CabCompanyBean;


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
public class CabCompanyActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = CabCompanyActivity.class.getSimpleName();
    Activity activity = this;
    Toolbar toolbar;
    TextView next_register, alreadyAccount;
    BottomSheetDialog bottomSheetDialog;
    int cab_id;
    android.app.AlertDialog dialog;
    Spinner spinner;
    ArrayList<String> list;
    String id="";

    List<CabCompanyBean.CompanyList> cmpnyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_company_new);
        initViews();
        getList();

        spinner = (Spinner) findViewById(R.id.selectCab);


    }


    public void initViews() {
        cmpnyList = new ArrayList<>();
        list = new ArrayList<String>();
        next_register = (TextView) findViewById(R.id.btRegister);
        alreadyAccount = (TextView) findViewById(R.id.alreadyAccount);

        alreadyAccount.setOnClickListener(this);
        next_register.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alreadyAccount:
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btRegister:
                if (spinner.getSelectedItem().equals("")) {
                    Utils.makeSnackBar(activity, alreadyAccount, "Please Select your cab company");

                } else {
                    dialog = new SpotsDialog(activity);
                    dialog.show();
                    String selecttem=spinner.getSelectedItem().toString();
                   // ModelManager.getInstance().getCabCompaniesManager().getCabCompanies(activity, id, Operations.getCabCompaniesTask(activity, id));
                     ModelManager.getInstance().getCabCompaniesManager().getCabCompanies(activity,Operations.getCabCompaniesTask(activity, id));

                }
                break;
            case R.id.selectCab:
                bottomSheetDialog.show();
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
                startActivity(intCab);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
                        .setTitleText(message)
                        .setContentText("please enter correct company name")
                        .show();
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
      /*  String myConcatedString = cursor.getString(numcol).concat('-').
                concat(cursor.getString(cursor.getColumnIndexOrThrow(db.KEY_DESTINATIE)));
*/
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CabCompanyBean> call = apiService.GetCompanyList();
        call.enqueue(new Callback<CabCompanyBean>() {

            @Override
            public void onResponse(Call<CabCompanyBean> call, Response<CabCompanyBean> response) {
                boolean res = response.isSuccessful();
                String msg = response.body().getResponse().getMessage();
                cmpnyList = response.body().getResponse().getCompanyList();
                cmpnyList.get(0).getCompanyName();
                for (int i = 0; i < cmpnyList.size(); i++) {
                    if(cmpnyList.get(i).getCompanyName().equalsIgnoreCase(""))
                    {

                    }
                    else {
                        list.add(i, cmpnyList.get(i).getCompanyName());

                    }
                }


                ArrayAdapter<String> adp = new ArrayAdapter<String>(CabCompanyActivity.this, R.layout.spinner_item, list);
                adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adp);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        id =cmpnyList.get(i).getComapnyId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                Log.e("msg_trip", cmpnyList.size() + "");
            }

            @Override
            public void onFailure(Call<CabCompanyBean> call, Throwable t) {

            }

        });
    }
    //End
}
