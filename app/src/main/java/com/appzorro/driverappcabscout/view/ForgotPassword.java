package com.appzorro.driverappcabscout.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.model.Beans.ModelForgot;
import com.appzorro.driverappcabscout.model.Utils;

import butterknife.Bind;
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
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.linear1_)
    LinearLayout linear1;
    @Bind(R.id.image_email)
    ImageView imageEmail;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.txReset)
    TextView txReset;
    @Bind(R.id.linear_next)
    LinearLayout linearNext;
    @Bind(R.id.lbl_acc)
    TextView lblAcc;
    @Bind(R.id.tvSignup)
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
}
