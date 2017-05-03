package com.appzorro.driverappcabscout.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;
import com.facebook.CallbackManager;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

import static com.appzorro.driverappcabscout.R.id.edtfirstname;

/**
 * Created by vijay on 9/2/17.
 */

public class Profile_Activity  extends AppCompatActivity{
    String userChoosenTask;
    Context context;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String covertedImage;
    CallbackManager callbackManager;
    ImageView imageView,edtimage;
    Dialog passworddialog;
    MenuItem shareditem;
    android.app.AlertDialog dialog;
    EditText firstname , emaiid,phonenumber;
    TextView passwordchange,changecompany,update;
    BottomSheetDialog bottomSheetDialog;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context =this;


        initview();

        edtimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        passwordchange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                passworddialog =Utils.createpasswordDialog(Profile_Activity.this);
                passworddialog.show();

                final EditText editcurrentpassword = (EditText)passworddialog.findViewById(R.id.edtcurrentpassword);
                final EditText newpassword = (EditText)passworddialog.findViewById(R.id.edtnewpassword);
                TextView cancel=(TextView)passworddialog.findViewById(R.id.txtcancel1);

                TextView reset=(TextView)passworddialog.findViewById(R.id.txtreset);
                final EditText confirmpassword = (EditText)passworddialog.findViewById(R.id.edtconfirmpassword);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        passworddialog.dismiss();

                    }
                });
                reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                   public void onClick(View view) {

                       if(newpassword.getText().toString().equals(confirmpassword.getText().toString())){

                           dialog= new SpotsDialog(context);
                           dialog.show();
                           ModelManager.getInstance().getChangepasswordManager().
                                   ChangepasswordManager(context, Operations.changePassword(context, CSPreferences.readString(context,"customer_id"),
                                           editcurrentpassword.getText().toString(),newpassword.getText().toString()));

                       }
                        else {

                           Toast.makeText(context, "Please confirm your new Password", Toast.LENGTH_SHORT).show();

                       }
                    }
                });
            }
        });

        changecompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog = new BottomSheetDialog(context);
                bottomSheetDialog.setContentView(R.layout.changecompany);
                bottomSheetDialog.show();
                bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(android.support.design.R.id.design_bottom_sheet);
                        assert bottomSheet != null;
                        BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                });

                final EditText edtchangecompany = (EditText)bottomSheetDialog.findViewById(R.id.changecompany);
                TextView cancel1 =(TextView)bottomSheetDialog.findViewById(R.id.txtcancel1);
                TextView save =(TextView)bottomSheetDialog.findViewById(R.id.txtreset);
                cancel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        bottomSheetDialog.dismiss();

                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (edtchangecompany.getText().toString().isEmpty()){

                            edtchangecompany.setError("required");

                        }
                        else{
                            dialog = new SpotsDialog(context);
                            dialog.show();
                            ModelManager.getInstance().getChangeComapnyManager().ChangeComapnyManager(context,Operations.changeComapnyName(
                                    context,CSPreferences.readString(context,"customer_id"),edtchangecompany.getText().toString()
                            ));

                        }

                    }
                });


            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(context);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(context);
    }
    @Subscribe
    public void onEvent(Event event){
        switch (event.getKey()) {
            case Constant.CHANGEPASSWORD:
                dialog.dismiss();
                String response = event.getValue();

                    new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText(response)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    passworddialog.dismiss();
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();

                break;
            case Constant.PASSWORDERROR:
                Config.event_message =event.getValue();
                dialog.dismiss();
                Toast.makeText(context, ""+Config.event_message, Toast.LENGTH_SHORT).show();
                passworddialog.dismiss();
                break;
            case Constant.UPDATEPROFILE:
                firstname.setClickable(false);
                shareditem.setVisible(false);
                firstname.setEnabled(false);
                emaiid.setClickable(false);
                emaiid.setEnabled(false);
                phonenumber.setClickable(false);
                phonenumber.setEnabled(false);

                ModelManager.getInstance().getUserDetailManager().UserDetailManager(context,
                        Operations.getUserDetail(context,CSPreferences.readString(context,"customer_id")));



                break;
            case Constant.USERDETAILSTAUS:
                dialog.dismiss();
                firstname.setText(""+CSPreferences.readString(context,"user_name"));
                emaiid.setText(""+CSPreferences.readString(context,"user_email"));
                phonenumber.setText(""+CSPreferences.readString(context,"user_mobile"));
                String profile= CSPreferences.readString(context,"profile_pic");
                Picasso.with(this)
                        .load(CSPreferences.readString(context, "profile_pic"))
                        .into(imageView);
                break;
            case Constant.SERVER_ERROR:
                dialog.dismiss();
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("NETWORK ERROR")
                        .setContentText("Your network connection is very slow  please  try again")
                        .show();
                break;

            case Constant.COMPANYCHANGE:
                dialog.dismiss();
                Config.event_message = event.getValue();

                        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Done")
                            .setContentText(Config.event_message)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    bottomSheetDialog.dismiss();
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();


                break;
            case Constant.COMPANYCHANGEERROR:
                dialog.dismiss();
                Config.event_message = event.getValue();
                Toast.makeText(context, ""+Config.event_message, Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
                break;
        }
    }
    public void initview(){

        callbackManager = CallbackManager.Factory.create();
        imageView =(ImageView)findViewById(R.id.driverpic);
        firstname=(EditText)findViewById(edtfirstname);
        emaiid=(EditText)findViewById(R.id.edtemail);
        passwordchange=(TextView)findViewById(R.id.txtpassworddd);

        phonenumber=(EditText)findViewById(R.id.edphonenumber);
        edtimage =(ImageView)findViewById(R.id.imageedit);
        toolbar =(Toolbar)findViewById(R.id.toolbar);
        changecompany =(TextView)findViewById(R.id.txtchangeccompany);

        toolbar.setTitle("Setting");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        firstname.setText(""+CSPreferences.readString(context,"user_name"));
        emaiid.setText(""+CSPreferences.readString(context,"user_email"));
        phonenumber.setText(""+CSPreferences.readString(context,"user_mobile"));


        String profile= CSPreferences.readString(context,"profile_pic");
        Picasso.with(this)
                .load(CSPreferences.readString(context, "profile_pic"))
                .into(imageView);

        imageView.buildDrawingCache();
        Bitmap bm= imageView.getDrawingCache();
       /*Picasso.with(this)
                .load(CSPreferences.readString(context, "profile_pic"))
                .into(imageView);
*/
   //this code convert the image already set to in imgeview to base64
        if (bm==null){

          //  imageView.setImageResource(R.drawable.ic_icon_profile_pic);
            convertImageToBase64();
        }
        else {
            Picasso.with(this)
                    .load(CSPreferences.readString(context, "profile_pic"))
                    .into(imageView);

            ByteArrayOutputStream strams = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 50, strams);
            byte[] image = strams.toByteArray();
            covertedImage = Base64.encodeToString(image, 0);
        }
        firstname.setClickable(false);
        firstname.setEnabled(false);
        emaiid.setClickable(false);
        emaiid.setEnabled(false);
        phonenumber.setClickable(false);
        phonenumber.setEnabled(false);
    }
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
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    public  void convertImageToBase64() {
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
    private void onSelectFromGalleryResult(Intent data) {


        if (data != null) {
            try {
                Bitmap  bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                byte[] byteArray = bytes.toByteArray();
                covertedImage = Base64.encodeToString(byteArray, 0);
                imageView.setImageBitmap(bm);
                //Log.e("From gallery",covertedImage);
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
        imageView.setImageBitmap(thumbnail);
        byte[] byteArray = bytes.toByteArray();
        covertedImage = Base64.encodeToString(byteArray, 0);
        Log.e("camera images",covertedImage);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Utils.hideSoftKeyboard((Activity) context);
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

        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);

            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        shareditem = menu.findItem(R.id.tick);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.tick:
                shareditem.setVisible(false);
                dialog = new SpotsDialog(context);
                dialog.show();
                ModelManager.getInstance().getUpdateProfileManager().UpdateProfileManager(context, Config.updateprofileurl,Operations.updateProfile(
                        context,CSPreferences.readString(context,"customer_id"),covertedImage,firstname.getText().toString(),phonenumber.getText().toString()
                ));
                break;
            case R.id.edit:
                shareditem.setVisible(true);


                firstname.setEnabled(true);
                firstname.setClickable(true);
                emaiid.setEnabled(false);
                emaiid.setClickable(false);
                phonenumber.setClickable(true);
                phonenumber.setEnabled(true);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
