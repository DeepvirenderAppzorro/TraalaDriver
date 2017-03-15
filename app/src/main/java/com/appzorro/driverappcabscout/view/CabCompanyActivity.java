package com.appzorro.driverappcabscout.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

/**
 * Created by pankaj on 23/1/17.
 */
public class CabCompanyActivity extends AppCompatActivity implements View.OnClickListener  {
    private final String TAG = CabCompanyActivity.class.getSimpleName();
    Activity activity = this;
    Toolbar toolbar;
    TextView next_register, alreadyAccount;
    EditText selectCab;
    BottomSheetDialog bottomSheetDialog;
    ArrayList<String> cabCompaniesList;
    ArrayList<Integer> cabIdList;
    ArrayAdapter<String> arrayAdapter;
    ListView listView;
    String selected_cab = "";
    int cab_id;
    android.app.AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_company);
        initViews();

    }
    public void initViews() {

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("CREATE ACCOUNT");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);


//        PackageInfo info;
//        try {
//            info = getPackageManager().getPackageInfo("app.cabscout.driver", PackageManager.GET_SIGNATURES);
//            for (android.content.pm.Signature signature : info.signatures) {
//                MessageDigest md;
//                md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String something = new String(Base64.encode(md.digest(), 0));
//                //String something = new String(Base64.encodeBytes(md.digest()));
//                Log.e("hashxxxx key", something);
//            }
//        } catch (PackageManager.NameNotFoundException e1) {
//            Log.e("name not found", e1.toString());
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("no such an algorithm", e.toString());
//        } catch (Exception e) {
//            Log.e("exception", e.toString());
//        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }
        next_register = (TextView)findViewById(R.id.btRegister);
        selectCab = (EditText) findViewById(R.id.selectCab);
        alreadyAccount = (TextView)findViewById(R.id.alreadyAccount);

        alreadyAccount.setOnClickListener(this);
        next_register.setOnClickListener(this);
       // selectCab.setOnClickListener(this);

     /*   bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(android.support.design.R.id.design_bottom_sheet);
                assert bottomSheet != null;
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);


            }
        });*/
      /*  listView = (ListView)bottomSheetDialog.findViewById(R.id.listView);
        cabCompaniesList = new ArrayList<>();
        cabIdList = new ArrayList<>();
        for (Map.Entry<Integer,String> entry : CabCompaniesManager.cabCompaniesList.entrySet()) {
            Log.e(TAG, "cab id--"+ entry.getKey());
            cabCompaniesList.add(entry.getValue());
            cabIdList.add(entry.getKey());
        }
*/
  // set the campany list in array adapter
        /*arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, cabCompaniesList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectCab.setText(cabCompaniesList.get(position));
                selected_cab = cabCompaniesList.get(position);
                cab_id = cabIdList.get(position);
                bottomSheetDialog.dismiss();
            }
        });*/
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alreadyAccount:
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btRegister:

                if (selectCab.getText().toString().isEmpty())
                    Toast.makeText(activity, "Please enter your cab company", Toast.LENGTH_SHORT).show();
                else {
                    dialog = new SpotsDialog(activity);
                    dialog.show();
                    ModelManager.getInstance().getCabCompaniesManager().getCabCompanies(activity, Operations.getCabCompaniesTask(activity,selectCab.getText().toString()));
                   // ModelManager.getInstance().getCabCompaniesManager().getCabCompanies(activity,selectCab.getText().toString());

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
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
