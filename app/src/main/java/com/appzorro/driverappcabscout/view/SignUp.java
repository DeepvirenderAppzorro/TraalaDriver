package com.appzorro.driverappcabscout.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;
import com.facebook.CallbackManager;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

/**
 * Created by pankaj on 21/1/17.
 */
public class SignUp extends AppCompatActivity {
    Toolbar toolbar;
    Context context;
    String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView back, driverPic;
    Activity activity = this;
    EditText etDrivername, etDriEmail, etDrivNo, etDrivPasswrd, etReDrivPassword, etDrivLicence, etDrivCity, etDrivZip;
    String drivername, driEmail, drivNo, drivPasswrd, redrivPasswrd, drivLicence, drivCity, drivZip, devicetoken, cabid;
    CheckBox termsCheckBox, policyCheckBox;
    String covertedImage;
    CallbackManager callbackManager;
    Dialog dialog;
    TextView lbl_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_driver);
        initViews();
        setSpanText();
        //Top toolbar
        driverPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        //Added by deep

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void initViews() {
     /*   toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("CREATE ACCOUNT");
        setSupportActionBar(toolbar);*/
        ButterKnife.bind(this);
        context = this;

        callbackManager = CallbackManager.Factory.create();
        convertImageToBase64();

     /*   if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }*/
        devicetoken = FirebaseInstanceId.getInstance().getToken();
        cabid = getIntent().getStringExtra("cab_id");

        etDrivername = (EditText) findViewById(R.id.edtname);
        etDriEmail = (EditText) findViewById(R.id.edtemail);
        etDrivNo = (EditText) findViewById(R.id.edtphone);
        etDrivPasswrd = (EditText) findViewById(R.id.edtpassword);
        etReDrivPassword = (EditText) findViewById(R.id.edtconfirmpassword);
        etDrivLicence = (EditText) findViewById(R.id.edtdriverlicence);
        etDrivCity = (EditText) findViewById(R.id.edtcity);
        etDrivZip = (EditText) findViewById(R.id.edtzipcode);
        back = (ImageView) findViewById(R.id.back);
        termsCheckBox = (CheckBox) findViewById(R.id.termsCheckbox);
        policyCheckBox = (CheckBox) findViewById(R.id.privacyCheckbox);
        lbl_submit = (TextView) findViewById(R.id.txSubmit);
        driverPic = (ImageView) findViewById(R.id.driverimage);
    }

    @OnClick(R.id.tvAlreadyacc)
    public void login() {
        Intent logIntent = new Intent(SignUp.this, LoginActivity.class);
        startActivity(logIntent);
    }


    @OnClick(R.id.txSubmit)
    public void signup() {
        // get String
        Utils.hideSoftKeyboard(activity);
        drivername = etDrivername.getText().toString().trim();
        driEmail = etDriEmail.getText().toString().trim();
        drivNo = etDrivNo.getText().toString().trim();
        drivPasswrd = etDrivPasswrd.getText().toString().trim();
        redrivPasswrd = etReDrivPassword.getText().toString().trim();
        drivLicence = etDrivLicence.getText().toString().trim();
        drivCity = etDrivCity.getText().toString().trim();
        drivZip = etDrivZip.getText().toString().trim();

        if (etDrivername.getText().toString().isEmpty()) {

            etDrivername.setError("Required");

        } else if (!drivPasswrd.equals(redrivPasswrd)) {
            Utils.makeSnackBar(context, etDriEmail, "Password didn't match");

            //  Toast.makeText(this, "Password didn't match", Toast.LENGTH_SHORT).show();

        } else if (!Utils.emailValidator(driEmail)) {

            etDriEmail.setError("Required vaild Email id");

        } else if (!termsCheckBox.isChecked() || !policyCheckBox.isChecked()) {
            Utils.makeSnackBar(context, etDriEmail, "You must be agree to all terms and conditions");

            // Toast.makeText(this, "You must be agree to all terms and conditions", Toast.LENGTH_SHORT).show();
        } else if (drivNo.isEmpty()) {

            etDrivNo.setError("Required");
        } else {
            //progressView.setVisibility(View.VISIBLE);
            Utils.hideSoftKeyboard(activity);
            dialog = new SpotsDialog(context);
            dialog.show();

            Log.e("sending detail", cabid + "\n" + driEmail + "\n" + drivPasswrd + "\n" + drivCity + "\n" + drivZip);
         /*  ModelManager.getInstance().getRegistrationManager().registerUser(activity,
                   Operations.registrationTask(getApplicationContext(), driEmail, cabid, drivername, redrivPasswrd, "dhxddhfd", drivNo, drivLicence, drivCity,drivZip,covertedImage));*/
            ModelManager.getInstance().getRegistrationManager().registerUser(context, Config.simplesignupurl, Operations.simpleuserRegister(context,
                    cabid, driEmail, drivPasswrd, drivername, "A", devicetoken, etDrivNo.getText().toString(), covertedImage, drivCity, drivZip, drivLicence));

        }


    }


    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void convertImageToBase64() {
        Bitmap bit = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_icon_profile_pic);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte[] ba = bao.toByteArray();
        covertedImage = Base64.encodeToString(ba, 0);
        Log.e("converted Image", "" + covertedImage);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }
// select image from the Gallery >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.

    private void selectImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utils.checkPermission(context);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                byte[] byteArray = bytes.toByteArray();
                covertedImage = Base64.encodeToString(byteArray, 0);
                driverPic.setImageBitmap(bm);
                Log.e("From gallery", covertedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        driverPic.setImageBitmap(thumbnail);
        byte[] byteArray = bytes.toByteArray();
        covertedImage = Base64.encodeToString(byteArray, 0);
        Log.e("camera images", covertedImage);


    }

    private void setSpanText() {
        String terms_str = "<font color=#000>&nbsp I have read,understand and agree to the</font> <font color=#009de0> terms of service</font>";
        String privacy_str = "<font color=#000>&nbsp I have read,understand and agree to the</font> <font color=#009de0> Privacy Policy</font>";
        termsCheckBox.setText(Html.fromHtml(terms_str));
        policyCheckBox.setText(Html.fromHtml(privacy_str));

        SpannableString ss = new SpannableString("I have read,understand and agree to the terms of service");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent videoclk= new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=jvO6CqtiRmo"));
                startActivity(videoclk);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                int color = ContextCompat.getColor(activity, R.color.blue);
                ds.setColor(color);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 40, 56, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsCheckBox.setText(ss);
        termsCheckBox.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString ss1 = new SpannableString("I have read,understand and agree to the Privacy Policy");
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent videoclk= new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=jvO6CqtiRmo"));
                startActivity(videoclk);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                int color = ContextCompat.getColor(activity, R.color.blue);
                ds.setColor(color);
                ds.setUnderlineText(false);
            }
        };
        ss1.setSpan(clickableSpan1, 40, 54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        policyCheckBox.setText(ss1);
        policyCheckBox.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constant.SIGNUPRESPONSE:
                dialog.dismiss();

                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success")
                        .setContentText(event.getValue())
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                Intent i = new Intent(context, LoginActivity.class);
                                startActivity(i);
                                finish();

                            }
                        })
                        .show();


                break;
            case Constant.ERROR:
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText(event.getValue())
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent i = new Intent(context, SignUp.class);
                                startActivity(i);
                                sweetAlertDialog.dismiss();
                                finish();


                            }
                        })
                        .show();

                break;


            case Constant.SERVER_ERROR:
                dialog.dismiss();
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Please check your internetconnection and  please try again")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                                Intent i = new Intent(context, SignUp.class);
                                startActivity(i);

                            }
                        })
                        .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);

            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
