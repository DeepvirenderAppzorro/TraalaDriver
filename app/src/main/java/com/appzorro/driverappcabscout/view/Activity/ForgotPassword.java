package com.appzorro.driverappcabscout.view.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.model.Beans.ModelForgot;
import com.appzorro.driverappcabscout.model.Utils;
import com.appzorro.driverappcabscout.view.Activity.loginActivity.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    String email_id;
    AlertDialog dialog;
    TextView resetPassword, createAccount;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.linear1_)
    LinearLayout linear1;
    @BindView(R.id.image_email)
    ImageView imageEmail;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.txReset)
    TextView txReset;
    @BindView(R.id.linear_next)
    LinearLayout linearNext;
    @BindView(R.id.lbl_acc)
    TextView lblAcc;
    @BindView(R.id.tvSignup)
    TextView tvSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        initViews();

    }

    private void initViews() {

        email = (EditText) findViewById(R.id.email);
        resetPassword = (TextView) findViewById(R.id.txReset);
        createAccount = (TextView) findViewById(R.id.tvSignup);
        back = (ImageView) findViewById(R.id.back);


    }


    private void ForgotResponse() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ModelForgot> call = apiService.getForgotResponse(email_id);
        call.enqueue(new Callback<ModelForgot>() {

            @Override
            public void onResponse(Call<ModelForgot> call, Response<ModelForgot> response) {
                dialog.dismiss();
                boolean res = response.isSuccessful();
                String msg = response.body().getResponse().getMessage();
                Log.e("msg_forgot", msg);
            //    Utils.makeSnackBar(ForgotPassword.this, email, msg);
                startActivity(new Intent(ForgotPassword.this,LoginActivity.class));

            }

            @Override
            public void onFailure(Call<ModelForgot> call, Throwable t) {
                dialog.dismiss();
                Utils.makeSnackBar(ForgotPassword.this, email, "Server Error");

                Log.e("msg_forgot", "Error");

            }
        });
    }
    //End

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick({R.id.back, R.id.txReset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
                break;
            case R.id.txReset:
                if (!email.getText().toString().equalsIgnoreCase("")) {
                    email_id = email.getText().toString();
                    dialog = new SpotsDialog(ForgotPassword.this);
                    dialog.show();
                    ForgotResponse();
                } else {
                    Utils.makeSnackBar(ForgotPassword.this, email, "please fill Email id");
                }
                break;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
}
