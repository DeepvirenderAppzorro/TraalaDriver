package com.appzorro.driverappcabscout.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.twitter.sdk.android.core.Twitter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

/**
 * Created by pankaj on 20/1/17.
 */

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
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
    @Bind(R.id.image_main)
    ImageView imageMain;
    @Bind(R.id.linear_1)
    LinearLayout linear1;
    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.etLoginid)
    EditText etLoginid;
    @Bind(R.id.image_password)
    ImageView imagePassword;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.btLogin)
    TextView btLogin;
    @Bind(R.id.forgot)
    TextView forgot;
    @Bind(R.id.linear_2)
    LinearLayout linear2;
    @Bind(R.id.text_line)
    TextView textLine;
    @Bind(R.id.linear_11)
    LinearLayout linear11;
    @Bind(R.id.image1)
    ImageView image1;
    @Bind(R.id.image2)
    ImageView image2;
    @Bind(R.id.image3)
    ImageView image3;
    @Bind(R.id.linear_images)
    LinearLayout linearImages;

    @Bind(R.id.lbl_acc)
    TextView lblAcc;


    @Bind(R.id.linear_3)
    LinearLayout linear3;
    GoogleApiClient mGoogleApiClient;
    LoginButton loginButton;
    ImageView google_signin,fb_login;
    private static final String EMAIL = "email";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
      /*  FacebookSdk.setApplicationId("410517842613797");
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);*/
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_login_new);
        ButterKnife.bind(this);
        context = this;

        printhashkey();
        init();


        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  ModelManager.getInstance().getFacebookLoginManager().doFacebookLogin(context,callbackManager);

                loginButton.performClick();
            }
        });


        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));


        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }


    public void printhashkey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.appzorro.driverappcabscout",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

    public void init() {

        callbackManager = CallbackManager.Factory.create();
        edit_email = (EditText) findViewById(R.id.etLoginid);
        fb_login=(ImageView)findViewById(R.id.image1);
        edit_password = (EditText) findViewById(R.id.etPassword);
        google_signin = (ImageView) findViewById(R.id.image2);
        relativeLayout = (RelativeLayout) findViewById(R.id.activitylogin);
        ButterKnife.bind(this);
        devicetoken = FirebaseInstanceId.getInstance().getToken();

        Log.e("device tokenn", "j " + devicetoken);

         //Added by me
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,

                        (GoogleApiClient.OnConnectionFailedListener) this

                )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        google_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(LoginActivity.this,ForgotPassword.class);
               startActivity(intent);
            }
        });

    }


    ///Added by me

    @OnClick(R.id.tvSignup)
    public void goSignup() {

        Intent signupIntent = new Intent(LoginActivity.this, CabCompanyActivity.class);
        startActivity(signupIntent);
    }


    // Stoped by deep
   /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ModelManager.getInstance().getFacebookLoginManager().getFacebookData(context);
        }
    }
*/
//E

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
                        CSPreferences.readString(context, "customer_id")));

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
                Log.d("ChkHome","Userdeatil");
                CSPreferences.putString(this, Constant.OfflineOrOnline,"true");
                Intent i = new Intent(LoginActivity.this, HomeScreenActivity.class);
                CSPreferences.putString(LoginActivity.this, "login_status", "true");
                startActivity(i);
                finish();
                break;


            case Constant.SERVER_ERROR:
                dialog.dismiss();
                String message1 = event.getValue();
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText(event.getValue())
                        .show();


                break;
        }
    }


    @OnClick(R.id.btLogin)
    public void loginDriver() {

        emailid = edit_email.getText().toString().trim();
        password = edit_password.getText().toString().trim();

        if (emailid.isEmpty() && password.isEmpty()) {
            Utils.makeSnackBar(context, image, "Please enter  email id and password");

        } else if (!Utils.emailValidator(emailid) && !emailid.isEmpty()) {

            Utils.makeSnackBar(context, image, "Please  enter valid  email id");

        } else if (edit_password.getText().toString().isEmpty()) {

            edit_password.setError("Required");
        } else if (emailid.isEmpty()) {

            edit_email.setError("Required");
        } else {


            Utils.hideSoftKeyboard(context);

            dialog = new SpotsDialog(context);
            dialog.show();
            CSPreferences.putString(context, Constant.Password, password);

            ModelManager.getInstance().getLoginManager().doLogin(LoginActivity.this, Operations.loginTask(LoginActivity.this,
                    emailid, password, devicetoken));


        }
    }

    //Added by deep
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 123);
    }

    //Now stopped by me
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 123) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handlePlusSignInResult(googleSignInResult);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handlePlusSignInResult(GoogleSignInResult googleSignInResult) {

        if (googleSignInResult.isSuccess()) {


            GoogleSignInAccount signInAccount = googleSignInResult.getSignInAccount();
            String name = signInAccount.getDisplayName();
            Log.e("name", name);
        }
    }


    private final GoogleApiClient.OnConnectionFailedListener connectionFailedListener =
            new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                }
            };

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }hdutygkidigfk hhgj fjfhjgjhg uyeriuyeoiufo
*/

    //End


}
