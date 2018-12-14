package com.appzorro.driverappcabscout.view.Activity.loginActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Utils;
import com.appzorro.driverappcabscout.view.Activity.BaseActivity;
import com.appzorro.driverappcabscout.view.Activity.CabCompanyActivity;
import com.appzorro.driverappcabscout.view.Activity.ForgotPassword;
import com.appzorro.driverappcabscout.view.Activity.loginActivity.model.LoginResponse;
import com.appzorro.driverappcabscout.view.Activity.loginActivity.model.ProfileResponse;
import com.appzorro.driverappcabscout.view.Activity.loginActivity.presenter.LoginActivityPresenter;
import com.appzorro.driverappcabscout.view.Activity.loginActivity.presenter.LoginActivityPresenterHandler;
import com.appzorro.driverappcabscout.view.Activity.loginActivity.view.LoginActivityView;
import com.appzorro.driverappcabscout.view.HomeActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.nkzawa.emitter.Emitter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.appzorro.driverappcabscout.model.Constant.COMPANYID;

/**
 * Created by pankaj on 20/1/17.
 */

public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, LoginActivityView {
    EditText edit_email, edit_password;
    String emailid;
    String password;
    Activity context;
    CallbackManager callbackManager;
    RelativeLayout relativeLayout;
    String devicetoken;
    @BindView(R.id.image_main)
    ImageView imageMain;
    @BindView(R.id.linear_1)
    LinearLayout linear1;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.etLoginid)
    EditText etLoginid;
    @BindView(R.id.image_password)
    ImageView imagePassword;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btLogin)
    TextView btLogin;
    @BindView(R.id.forgot)
    TextView forgot;
    @BindView(R.id.linear_2)
    LinearLayout linear2;
    @BindView(R.id.linear_11)
    LinearLayout linear11;
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.image3)
    ImageView image3;
    @BindView(R.id.linear_images)
    LinearLayout linearImages;
    @BindView(R.id.lbl_acc)
    TextView lblAcc;
    @BindView(R.id.linear_3)
    LinearLayout linear3;
    GoogleApiClient mGoogleApiClient;
    LoginButton fbLogin;
    ImageView google_signin, fb_login;
    private static final String EMAIL = "email";
    TwitterLoginButton twloginButton;
    @BindView(R.id.text_line)
    TextView textLine;
    @BindView(R.id.tvSignup)
    TextView tvSignup;
    Dialog ErrorDialog;
    LoginActivityPresenterHandler handler;
    public static ProfileResponse.Response profileDetails;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.CONSUMER_KEY), getString(R.string.CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login_new);
        ButterKnife.bind(this);
        printhashkey();
        init();
        setFontStyle();
        TwitterLogin();
        FbLogin();
        setOnClickListener();

    }

    //***Facebook Login
    private void FbLogin() {
        fbLogin = (LoginButton) findViewById(R.id.login_button);
        fbLogin.setReadPermissions(Arrays.asList(EMAIL));
        // Callback registration
        fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
            }

            @Override
            public void onCancel() {

            }
            @Override
            public void onError(FacebookException exception) {

            }
        });
    }
//***End

    //*** Twitter Login
    private void TwitterLogin() {
        twloginButton = (TwitterLoginButton) findViewById(R.id.tw_login_button);
        twloginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession session = TwitterCore.getInstance()
                TwitterSession session = TwitterCore.getInstance()
                        .getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;
                String twitterName = session.getUserName();
                Log.i("Class: ", "Twitter token and secret: " + token + "\t" + secret);
                startActivity(new Intent(context, HomeActivity.class));
            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
            }
        });
    }

    //***Set Font Style
    private void setFontStyle() {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.montserratsemibold);
        edit_email.setTypeface(typeface);
        edit_password.setTypeface(typeface);
        btLogin.setTypeface(typeface);
        textLine.setTypeface(typeface);
        lblAcc.setTypeface(typeface);
        tvSignup.setTypeface(typeface);
        forgot.setTypeface(typeface);

    }
//***End

    //***Get HashKey
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
    //***End

    private void init() {
        context = this;
        handler = new LoginActivityPresenter(this);
        callbackManager = CallbackManager.Factory.create();
        edit_email = (EditText) findViewById(R.id.etLoginid);
        fb_login = (ImageView) findViewById(R.id.image1);
        edit_password = (EditText) findViewById(R.id.etPassword);
        google_signin = (ImageView) findViewById(R.id.image2);
        relativeLayout = (RelativeLayout) findViewById(R.id.activitylogin);
        devicetoken = FirebaseInstanceId.getInstance().getToken();
        Log.e("device tokenn", "j " + devicetoken);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,
                        (GoogleApiClient.OnConnectionFailedListener) this).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        google_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }


    @OnClick(R.id.tvSignup)
    public void goSignup() {

        Intent signupIntent = new Intent(LoginActivity.this, CabCompanyActivity.class);
        startActivity(signupIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
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

            CSPreferences.putString(context, Constant.Password, password);
            handler.getLogin(emailid, password, devicetoken);
        }

    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 123);
    }

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
        twloginButton.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void showProgess() {
      //  showProgressDialog();
    }

    @Override
    public void hideProgess() {
    //   hideProgressDialog();
    }

    @Override
    public void showFeedBackMessage(String message) {
        ErrorDialog = Utils.serverError(context);
        ErrorDialog.show();
        final TextView ok = ErrorDialog.findViewById(R.id.txtok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok.setBackgroundColor(Color.parseColor("#0083db"));
                ErrorDialog.dismiss();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // OffSocketMethods("test",getMessages);
    }

    @Override
    public void loginSuccess(LoginResponse response) {
        try {
            String id = response.getResponse().getId();
            Log.d("idd",id);
            String socket_id= response.getResponse().getSocket_id();
            Log.d("soc", socket_id + " soc id");
            CSPreferences.putString(context,Constant.SOCKET_ID,socket_id);
            int id1 = Integer.parseInt(id);
           // getMessages("test");
            String companyID = response.getResponse().getCompany_id();
            if (id1 > 0) {
                CSPreferences.putString(context, "customer_id", String.valueOf(id1));
                CSPreferences.putString(context, Constant.DRIVER_ID, id);
                Log.d("driverId", id + " driver id");
                CSPreferences.putString(context, COMPANYID, companyID);
                handler.getProfile(context, id);
            } else {
                ErrorDialog = Utils.serverError(context);
                ErrorDialog.show();
                final TextView ok = ErrorDialog.findViewById(R.id.txtok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ok.setBackgroundColor(Color.parseColor("#0083db"));

                        ErrorDialog.dismiss();

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* public  void getMessages( String key) {
       if( Utils.mSocket!=null)
       {
           Utils.mSocket.on(key, getMessages);

       }

    }*/
    public void OffSocketMethods(String Key,Emitter.Listener listener) {
        Utils.mSocket.disconnect();
        Utils.mSocket.off(Key, listener);

    }
   /* public Emitter.Listener getMessages = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
           runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.d("chkData", data.toString());

                }
            });
        }
    };*/

    @Override
    public void profileSuccess(ProfileResponse response) {
        try {
            profileDetails = response.getResponse();
            CSPreferences.putString(context,"profile_pic", response.getResponse().getProfilePic());
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            CSPreferences.putString(LoginActivity.this, "login_status", "true");
            startActivity(i);
            finish();
            overridePendingTransition(0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private  void setOnClickListener()
    {
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbLogin.performClick();
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twloginButton.performClick();
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

}
