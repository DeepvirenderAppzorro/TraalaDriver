package com.appzorro.driverappcabscout.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

/**
 * Created by pankaj on 20/1/17.
 */

public class LoginActivity extends AppCompatActivity  {
    EditText edit_email, edit_password;

    Button loginbt;
    String emailid;
    String password;
    Activity context;
    CallbackManager callbackManager;
    RelativeLayout relativeLayout;
    AlertDialog dialog;
    String devicetoken;

    CircularProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId("410517842613797");
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        context =this;
        init();

    }
    public void init() {

        callbackManager = CallbackManager.Factory.create();
        edit_email = (EditText) findViewById(R.id.etLoginid);
        edit_password = (EditText) findViewById(R.id.etPassword);
        relativeLayout =(RelativeLayout)findViewById(R.id.activitylogin);
        ButterKnife.bind(this);
        devicetoken = FirebaseInstanceId.getInstance().getToken();

        Log.e("device tokenn","j "+ devicetoken);


    }

   @OnClick(R.id.btLogin)
    public void loginDriver() {

        emailid = edit_email.getText().toString().trim();
        password = edit_password.getText().toString().trim();

        if (emailid.isEmpty() && password.isEmpty()) {

            Toast.makeText(LoginActivity.this, "Plaese enter email and passwrod", Toast.LENGTH_SHORT).show();
        }
        else if (!Utils.emailValidator(emailid)&& !emailid.isEmpty()){

            Toast.makeText(context, "please fill valid email id", Toast.LENGTH_SHORT).show();
        }
        else if (edit_password.getText().toString().isEmpty()){

            edit_password.setError("Required");
        }
        else if (emailid.isEmpty()){

            edit_email.setError("Required");
        }

        else {


            Utils.hideSoftKeyboard(context);

            dialog = new SpotsDialog(context);
            dialog.show();

            ModelManager.getInstance().getLoginManager().doLogin(LoginActivity.this, Operations.loginTask(LoginActivity.this,
                    emailid, password,devicetoken));

        }
    }

    @OnClick(R.id.tvSignup)
    public void goSignup() {

        Intent signupIntent = new Intent(LoginActivity.this, SignUp.class);
        startActivity(signupIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            ModelManager.getInstance().getFacebookLoginManager().getFacebookData(context);
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
            case Constant.LOGIN_STATUS:
                dialog.dismiss();


                dialog = new SpotsDialog(context);
                    dialog.show();
                    ModelManager.getInstance().getUserDetailManager().UserDetailManager(context, Operations.getUserDetail(context,
                            CSPreferences.readString(context,"customer_id")));



                break;

              case Constant.LOGINERROR:
                  dialog.dismiss();
                  new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                          .setTitleText("Error")
                          .setContentText(event.getValue())
                          .show();

                  break;


            case Constant.USERDETAILSTAUS:
                dialog.dismiss();
                Intent i = new Intent(LoginActivity.this, HomeScreenActivity.class);
                CSPreferences.putString(LoginActivity.this, "login_status", "true");
                startActivity(i);
                finish();
                break;


            case Constant.SERVER_ERROR:
                dialog.dismiss();
                String message1 = event.getValue();
                Utils.makeSnackBar(context, relativeLayout, message1);
                break;
        }
    }


}
