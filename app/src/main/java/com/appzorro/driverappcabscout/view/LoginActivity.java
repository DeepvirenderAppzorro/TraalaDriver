package com.appzorro.driverappcabscout.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
    EditText logIn, passWord;
    TextView facebooklogin;
    Button loginbt;
    String mob;
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
        logIn = (EditText) findViewById(R.id.etLoginid);
        passWord = (EditText) findViewById(R.id.etPassword);
        facebooklogin=(TextView)findViewById(R.id.fbLogin);
        progressView = (CircularProgressView) findViewById(R.id.progressView);
        progressView.setVisibility(View.GONE);
        relativeLayout =(RelativeLayout)findViewById(R.id.activitylogin);
        ButterKnife.bind(this);
        devicetoken = FirebaseInstanceId.getInstance().getToken();
        Log.e("login token",devicetoken);

    }

   @OnClick(R.id.btLogin)
    public void loginDriver() {
        mob = logIn.getText().toString();
        password = passWord.getText().toString();
        if (mob.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Plaese enter login no and passwrod", Toast.LENGTH_SHORT).show();
        }
        else if (!Utils.emailValidator(mob)){

            logIn.setError("Required");
        }
        else if (passWord.getText().toString().isEmpty()){

            passWord.setError("Required");
        }

        else {
            Utils.hideSoftKeyboard(context);


            dialog = new SpotsDialog(context);
            dialog.show();

           // progressView.setVisibility(View.VISIBLE);
            ModelManager.getInstance().getLoginManager().doLogin(LoginActivity.this, Operations.loginTask(LoginActivity.this,
                    mob, password,devicetoken));

        }
    }

    @OnClick(R.id.tvSignup)
    public void goSignup() {

        Intent signupIntent = new Intent(LoginActivity.this, SignUp.class);
        startActivity(signupIntent);
    }
      public void faceBookLogin(View view){
         dialog = new SpotsDialog(context);
          dialog.show();

          ModelManager.getInstance().getFacebookLoginManager().doFacebookLogin(context,callbackManager);
          return;

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
        progressView.setVisibility(View.GONE);
        switch (event.getKey()) {
            case Constant.LOGIN_STATUS:
                dialog.dismiss();

                String response = event.getValue();
                String[] messagespli = response.split(",");
                int id = Integer.parseInt(messagespli[messagespli.length - 2]);
                String message = messagespli[messagespli.length - 1];
                if (id > 0) {
                    CSPreferences.putString(context,"customer_id",String.valueOf(id));
                    dialog = new SpotsDialog(context);
                    dialog.show();
                    ModelManager.getInstance().getUserDetailManager().UserDetailManager(context, Operations.getUserDetail(context, String.valueOf(id)));

                } else {

                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText(message)
                            .show();
                }
                break;
            case Constant.USERDETAILSTAUS:
                dialog.dismiss();
                Intent i = new Intent(LoginActivity.this, HomeScreenActivity.class);
                CSPreferences.putString(LoginActivity.this, "login_status", "true");
                startActivity(i);
                finish();
                break;
            case Constant.FACEBOOK_LOGIN_EMPTY:
                dialog.dismiss();
               /* Intent intent = new Intent(context, LoginFacebookActivity.class);
                startActivity(intent);*/
                break;
            case Constant.FACEBOOK_LOGIN_SUCCESS:
                dialog.dismiss();
                Log.e("event bus", "facebooklogin success");
                Intent intent1 = new Intent(context, HomeScreenActivity.class);
                CSPreferences.putString(context, "login_status", "true");
                startActivity(intent1);
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
