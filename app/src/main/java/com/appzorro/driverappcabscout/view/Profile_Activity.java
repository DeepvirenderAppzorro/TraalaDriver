package com.appzorro.driverappcabscout.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.Beans.CabCompanyBean;
import com.appzorro.driverappcabscout.model.Beans.ModelUpdatePic;
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
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.appzorro.driverappcabscout.R.id.edtfirstname;
import static com.appzorro.driverappcabscout.model.Constant.COMPANYID;

/**
 * Created by vijay on 9/2/17.
 */

public class Profile_Activity extends AppCompatActivity {
    String userChoosenTask;
    Context context;
    String name;
    boolean isDialogOpen = false;
    int state = 0;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String covertedImage;
    CallbackManager callbackManager;
    ImageView imageView, edtimage, editFields, back_img;
    Dialog passworddialog;
    MenuItem shareditem;
    ArrayList<String> compnyList;
    android.app.AlertDialog dialog;
    EditText firstname;
    TextView emaiid, phonenumber,passwordchange, changecompany;
    BottomSheetDialog bottomSheetDialog;
    Spinner spinner;
    Toolbar toolbar;
    ImageView dropDown;
    List<CabCompanyBean.CompanyList> cmpnyList;
    ArrayList<String> list;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);
        context = this;


        initview();

        getList();
        //   setCabCompanyAdapter();

        edtimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();

            }
        });
        //Added by deep

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_Activity.this, HomeScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //End
        passwordchange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                passworddialog = Utils.createpasswordDialog(Profile_Activity.this);
                passworddialog.show();

                final EditText editcurrentpassword = (EditText) passworddialog.findViewById(R.id.edtcurrentpassword);
                final EditText newpassword = (EditText) passworddialog.findViewById(R.id.edtnewpassword);
                final EditText confirmPwd = (EditText) passworddialog.findViewById(R.id.edtconfirmpassword);
                TextView cancel = (TextView) passworddialog.findViewById(R.id.txtcancel1);

                TextView reset = (TextView) passworddialog.findViewById(R.id.txtreset);
                final EditText confirmpassword = (EditText) passworddialog.findViewById(R.id.edtconfirmpassword);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        passworddialog.dismiss();

                    }
                });
                reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String passwor = CSPreferences.readString(context, Constant.Password);
                        if (editcurrentpassword.getText().toString().equalsIgnoreCase(passwor)) {

                            if (!editcurrentpassword.getText().toString().isEmpty() && !newpassword.getText().toString().isEmpty() && !confirmPwd.getText().toString().isEmpty()) {
                                if (newpassword.getText().toString().equals(confirmpassword.getText().toString())) {

                                    dialog = new SpotsDialog(context);
                                    dialog.show();
                                    ModelManager.getInstance().getChangepasswordManager().
                                            ChangepasswordManager(context, Operations.changePassword(context, CSPreferences.readString(context, "customer_id"),
                                                    editcurrentpassword.getText().toString(), newpassword.getText().toString()));

                                } else {
                                    Toast.makeText(context, "Please confirm your new Password", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Utils.makeSnackBar(context, editcurrentpassword, "Please Fill all Fields");
                            }
                        } else {
                            Utils.makeSnackBar(context, editcurrentpassword, "Please Fill Correct Password");

                        }
                    }
                });
            }
        });

        //edit fields added by deep
        editFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (state == 1) {
                    if (!phonenumber.getText().toString().equalsIgnoreCase("") && !firstname.getText().toString().equalsIgnoreCase("")) {

                        if (phonenumber.getText().toString().length() == 10) {
                            dialog = new SpotsDialog(context);
                            dialog.show();
                            ModelManager.getInstance().getUpdateProfileManager().UpdateProfileManager(context, Config.updateprofileurl, Operations.updateProfile(
                                    context, CSPreferences.readString(context, "customer_id"), firstname.getText().toString(), phonenumber.getText().toString()
                            ));
                            firstname.setEnabled(false);
                            firstname.setClickable(false);
                            emaiid.setEnabled(false);
                            emaiid.setClickable(false);
                            phonenumber.setClickable(false);
                            phonenumber.setEnabled(false);
                            passwordchange.setEnabled(false);
                            changecompany.setEnabled(false);
                            editFields.setImageResource(R.drawable.ic_icon_edit_black);
                            state = 0;
                        } else {
                            Utils.makeSnackBar(context, phonenumber, "Fill Correct number");
                        }
                    } else {
                        Utils.makeSnackBar(context, phonenumber, "Please Fill All Fields");
                    }


                } else {
                    editFields.setImageResource(R.mipmap.tick);
                    firstname.setEnabled(true);
                    firstname.setClickable(true);
                    passwordchange.setEnabled(true);
                    changecompany.setEnabled(true);
                    emaiid.setEnabled(false);
                    emaiid.setClickable(false);
                    phonenumber.setClickable(true);
                    phonenumber.setEnabled(true);
                    state = 1;
                }

            }
        });
        changecompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                spinner = (Spinner) bottomSheetDialog.findViewById(R.id.selectCab);
                dropDown = (ImageView) bottomSheetDialog.findViewById(R.id.dropdown);

                setCabCompanyAdapter();
                dropDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        expandSpinner();
                    }
                });

                Log.e("msg_trip", cmpnyList.size() + "");
                TextView cancel1 = (TextView) bottomSheetDialog.findViewById(R.id.txtcancel1);
                TextView save = (TextView) bottomSheetDialog.findViewById(R.id.txtreset);
                cancel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        bottomSheetDialog.dismiss();

                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (spinner.getSelectedItem().toString().isEmpty()) {

                            Utils.makeSnackBar(context, spinner, "Please Select Company");

                        } else {
                            dialog = new SpotsDialog(context);
                            dialog.show();
                            ModelManager.getInstance().getChangeComapnyManager().ChangeComapnyManager(context, Operations.changeComapnyName(
                                    context, CSPreferences.readString(context, "customer_id"), CSPreferences.readString(context, "company_id")
                            ));

                        }

                    }
                });
            }

        });
    }

    private void getList() {
        dialog = new SpotsDialog(Profile_Activity.this);
        dialog.show();
        dialog.setCancelable(false);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CabCompanyBean> call = apiService.GetCompanyList();
        call.enqueue(new Callback<CabCompanyBean>() {

            @Override
            public void onResponse(Call<CabCompanyBean> call, Response<CabCompanyBean> response) {
                dialog.dismiss();
                boolean res = response.isSuccessful();
                String msg = response.body().getResponse().getMessage();
                cmpnyList = response.body().getResponse().getCompanyList();
                cmpnyList.get(0).getCompanyName();
                for (int i = 0; i < cmpnyList.size(); i++) {

                    if (cmpnyList.get(i).getCompanyName().equalsIgnoreCase("")) {

                    } else {
                        list.add(cmpnyList.get(i).getCompanyName());

                    }

                    int chkId = Integer.parseInt(CSPreferences.readString(Profile_Activity.this, COMPANYID));
                    if (chkId == Integer.parseInt(cmpnyList.get(i).getComapnyId())) {

                        name = cmpnyList.get(i).getCompanyName();
                        CSPreferences.putString(context, "nameComp", name);
                        changecompany.setText(name);
                    }
                }
                String nm = CSPreferences.readString(context, "spinnerItem");
                if (!nm.equalsIgnoreCase("")) {
                    changecompany.setText(nm);
                }

            }
            //End


            @Override
            public void onFailure(Call<CabCompanyBean> call, Throwable t) {
                dialog.dismiss();
                Utils.makeSnackBar(Profile_Activity.this, spinner, "Network Problem");
            }

        });

    }

    public void setCabCompanyAdapter() {

        ArrayAdapter<String> adp = new ArrayAdapter<String>(Profile_Activity.this, android.R.layout.simple_list_item_1, list);
        spinner.setAdapter(adp);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                id = cmpnyList.get(i).getComapnyId();
                CSPreferences.putString(context, "company_id", id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

    public void expandSpinner() {
        MotionEvent motionEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, 0, 0, 0);
        spinner.dispatchTouchEvent(motionEvent);
    }

    @Subscribe
    public void onEvent(Event event) {
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
                startActivity(new Intent(context, LoginActivity.class));
                break;
            case Constant.ERROR:
                dialog.dismiss();
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText(event.getValue())
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
                break;

            case Constant.PASSWORDERROR:
                Config.event_message = event.getValue();
                dialog.dismiss();
                Toast.makeText(context, "" + Config.event_message, Toast.LENGTH_SHORT).show();
                passworddialog.dismiss();
                break;
            case Constant.UPDATEPROFILE:
                dialog.dismiss();
                Utils.makeSnackBar(context, edtimage, "Updated Successfully");
                firstname.setClickable(false);
                //   shareditem.setVisible(false);
                firstname.setEnabled(false);
                emaiid.setClickable(false);
                emaiid.setEnabled(false);
                phonenumber.setClickable(false);
                phonenumber.setEnabled(false);

                ModelManager.getInstance().getUserDetailManager().UserDetailManager(context,
                        Operations.getUserDetail(context, CSPreferences.readString(context, "customer_id")));


                break;
            case Constant.USERDETAILSTAUS:

                firstname.setText("" + CSPreferences.readString(context, "user_name"));
                emaiid.setText("" + CSPreferences.readString(context, "user_email"));
                phonenumber.setText("" + CSPreferences.readString(context, "user_mobile"));
                String profile = CSPreferences.readString(context, "profile_pic");
                Picasso.with(this)
                        .load(CSPreferences.readString(context, "profile_pic"))
                        .into(imageView);
                break;
            case Constant.SERVER_ERROR:
                //   dialog.dismiss();
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("NETWORK ERROR")
                        .setContentText("Your network connection is very slow  please  try again")
                        .show();
                break;

            case Constant.UPDATEPIC:
                dialog.dismiss();
                Utils.makeSnackBar(context, edtimage, "Profile pic updated");
                break;

            case Constant.UPDATEPICERROR:
                dialog.dismiss();
                Utils.makeSnackBar(context, edtimage, "Profile pic not updated");
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
                                changecompany.setText(spinner.getSelectedItem().toString());
                                CSPreferences.putString(context, "spinnerItem", spinner.getSelectedItem().toString());
                            }
                        })
                        .show();


                break;
            case Constant.COMPANYCHANGEERROR:
                dialog.dismiss();
                Config.event_message = event.getValue();
                Toast.makeText(context, "" + Config.event_message, Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
                break;
            case Constant.SPINNER_DIALOG:
                String isvisible = event.getValue();
                if (isvisible.equals("true") && list.get(0).equals("Select Company Name")) {
                    list.remove(0);
                    isDialogOpen = true;
                } else if (isDialogOpen && isvisible.equals("false"))
                    list.add(0, "Select Company Name");
                break;
        }
    }

    public void initview() {

        callbackManager = CallbackManager.Factory.create();
        imageView = (ImageView) findViewById(R.id.driverpic);
        editFields = (ImageView) findViewById(R.id.edit_profile);
        firstname = (EditText) findViewById(edtfirstname);
        emaiid = (TextView) findViewById(R.id.edtemail);
        passwordchange = (TextView) findViewById(R.id.txtpassworddd);
        changecompany = (TextView) findViewById(R.id.txtchangeccompany);
        phonenumber = (TextView) findViewById(R.id.edphonenumber);
        edtimage = (ImageView) findViewById(R.id.imageedit);
        back_img = (ImageView) findViewById(R.id.back);
        changecompany = (TextView) findViewById(R.id.txtchangeccompany);
        firstname.setText("" + CSPreferences.readString(context, "user_name"));
        emaiid.setText("" + CSPreferences.readString(context, "user_email"));
        phonenumber.setText("" + CSPreferences.readString(context, "user_mobile"));
        cmpnyList = new ArrayList<>();
        list = new ArrayList<String>();
        list.add(0, "Select Company Name");
        String profile = CSPreferences.readString(context, "profile_pic");
        Picasso.with(this)
                .load(CSPreferences.readString(context, "profile_pic")).placeholder(R.drawable.ic_icon_pic)
                .into(imageView);

        imageView.buildDrawingCache();
        Bitmap bm = imageView.getDrawingCache();
       /*Picasso.with(this)
                .load(CSPreferences.readString(context, "profile_pic"))
                .into(imageView);
*/
        //this code convert the image already set to in imgeview to base64
        if (bm == null) {

            //  imageView.setImageResource(R.drawable.ic_icon_profile_pic);
            convertImageToBase64();
        } else {
            Picasso.with(this)
                    .load(CSPreferences.readString(context, "profile_pic")).placeholder(R.drawable.ic_icon_pic)
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

    private void onSelectFromGalleryResult(Intent data) {


        if (data != null) {
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                byte[] byteArray = bytes.toByteArray();
                covertedImage = Base64.encodeToString(byteArray, 0);
                imageView.setImageBitmap(bm);
                //Log.e("From gallery",covertedImage);
                updatePic();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Log.d("pic", "comes");
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
        Bitmap bm400 = Utils.getScaledBitmap(thumbnail, 500, 500);
        covertedImage = BitMapToString(bm400);
        imageView.setImageBitmap(thumbnail);
        byte[] byteArray = bytes.toByteArray();
        //    covertedImage = Base64.encodeToString(byteArray, 0);
        Log.e("camera_images_me", covertedImage);

        updatePic();
      /*  ModelManager.getInstance().updateProfilepicManager().updateProfilePic(context,
                Operations.updateProfilePic(context, CSPreferences.readString(context, "customer_id"), covertedImage));
*/

    }

    private void updatePic() {
        dialog = new SpotsDialog(context);
        dialog.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Log.d("img", covertedImage);
        Call<ModelUpdatePic> call = apiService.updateProfilePic(CSPreferences.readString(this, "customer_id"), covertedImage);
        call.enqueue(new Callback<ModelUpdatePic>() {

            @Override
            public void onResponse(Call<ModelUpdatePic> call, Response<ModelUpdatePic> response) {
                boolean res = response.isSuccessful();
                dialog.dismiss();
                String msg = response.body().getResponse().getMessage();
                Log.e("profile_pic", msg);
                Utils.makeSnackBar(context, edtimage, "Profile pic updated");

            }

            @Override
            public void onFailure(Call<ModelUpdatePic> call, Throwable t) {
                dialog.dismiss();
                Log.e("profile_pic", "Error");
                Utils.makeSnackBar(context, edtimage, "Profile pic Not updated");


            }
        });
    }
    //End


    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
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

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);

            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

}
