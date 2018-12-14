package com.appzorro.driverappcabscout.view.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appzorro.driverappcabscout.AppController;
import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.Beans.CabCompanyBean;
import com.appzorro.driverappcabscout.model.Beans.CompanyLocationBean;
import com.appzorro.driverappcabscout.model.Beans.ModelUpdatePic;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Config;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.CustomSearchableSpinner;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.UpdateDocument;
import com.appzorro.driverappcabscout.model.Utils;
import com.appzorro.driverappcabscout.model.network.CustomSpinner;
import com.appzorro.driverappcabscout.view.Activity.loginActivity.LoginActivity;
import com.facebook.CallbackManager;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    List<String> locationNameList;
    Dialog upload;
    CustomSearchableSpinner selectLocation;
    List<CompanyLocationBean.LocationList> locationLists = new ArrayList<>();
    int state = 0;
    String covertedImage, cardNumber,name,frontPic,backPic,id,select_locationId;
    CallbackManager callbackManager;
    ImageView imageView, edtimage, editFields, back_img, dropdown, front, back;
    Dialog passworddialog;
    MenuItem shareditem;
    ArrayList<String> compnyList;
    CustomSearchableSpinner locationSpinner;
    android.app.AlertDialog dialog;
    EditText firstname, phonenumber, address, zip, licenseNo, licenseValidity, locationName;
    TextView emaiid, passwordchange, changecompany, cameraFront, galryFront, cameraFront2, galryBAck;
    BottomSheetDialog bottomSheetDialog;
    Spinner spinner;
    Toolbar toolbar;
    ImageView dropDown;
    boolean isOpenDialog = false;
    List<CabCompanyBean.CompanyList> cmpnyList;
    ArrayList<String> list;
    boolean isDialogOpen = false;
    @BindView(R.id.dropdownIdentity)
    ImageView dropdownIdentity;
    @BindView(R.id.llCard)
    LinearLayout llCard;
    CustomSpinner identitySelect;
    List<String> listCard;
    @BindView(R.id.edCardNo)
    EditText edCardNo;
    @BindView(R.id.imageUpload)
    ImageView imageUpload;
    @BindView(R.id.llUploadDoc)
    LinearLayout llUploadDoc;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.lbl_first_name)
    TextView lblFirstName;
    @BindView(R.id.lbl_email)
    TextView lblEmail;
    @BindView(R.id.edtemail)
    TextView edtemail;
    @BindView(R.id.lbl_mob)
    TextView lblMob;
    @BindView(R.id.lbl_chnge_pwd)
    TextView lblChngePwd;
    @BindView(R.id.txtpassworddd)
    TextView txtpassworddd;
    @BindView(R.id.txtchangeccompany)
    TextView txtchangeccompany;
    @BindView(R.id.lbl_address)
    TextView lblAddress;
    @BindView(R.id.lbl_zip)
    TextView lblZip;
    @BindView(R.id.lbl_licenseNo)
    TextView lblLicenseNo;
    @BindView(R.id.lbl_licValidity)
    TextView lblLicValidity;
    @BindView(R.id.lbl_locationName)
    TextView lblLocationName;
    @BindView(R.id.lblIdentity)
    TextView lblIdentity;
    @BindView(R.id.uploadDoc)
    TextView uploadDoc;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    MultipartBody.Part body,backBody;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);
        ButterKnife.bind(this);
        context = this;

        ModelManager.getInstance().getUserDetailManager().UserDetailManager(context,
                Operations.getUserDetail(context, CSPreferences.readString(context, "customer_id")));
        initview();
        addItemsOnSpinner2();
        getList();
       // setFontStyle();
        identitySelect.setEnabled(false);
        selectLocation.setEnabled(false);
        dropdownIdentity.setEnabled(false);
        CSPreferences.putString(context, Constant.CARDTYPE, "other");
        final String locid = CSPreferences.readString(context, "companyLocid");
        Log.d("companyId", locid);
        ModelManager.getInstance().getCabCompaniesManager().getLocationId(context, Operations.getLocationId(context, CSPreferences.readString(context, COMPANYID)));
        //****get user detail***

        ModelManager.getInstance().getUserDetailManager().UserDetailManager(context,
                Operations.getUserDetail(context, CSPreferences.readString(context, "customer_id")));

        //***End

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
                cardNumber = edCardNo.getText().toString();
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

        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload = Utils.uploaDocuments(Profile_Activity.this);
                upload.show();

                front = upload.findViewById(R.id.uploadFront);
                ImageView backDialog = upload.findViewById(R.id.back);
                back = upload.findViewById(R.id.uploadBack);
                Picasso.with(context)
                        .load(CSPreferences.readString(context, "AdharFImage"))
                        .into(front);
                Picasso.with(context)
                        .load(CSPreferences.readString(context, "AdharBImage"))
                        .into(back);
                TextView cancel = upload.findViewById(R.id.btCansel);
                cameraFront = upload.findViewById(R.id.cameraPic);
                galryFront = upload.findViewById(R.id.galeryPic);
                cameraFront2 = upload.findViewById(R.id.cameraPic2);
                TextView txtUpload = upload.findViewById(R.id.upLoadData);
                cardNumber = edCardNo.getText().toString();
                txtUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!cardNumber.isEmpty()) {
                            if (frontPic != null && backPic != null) {
                                {
                                    if (!frontPic.equals("") && !backPic.equals("")) {
                                       String type= CSPreferences.readString(context, Constant.CARDTYPE);
                                        if(type.equalsIgnoreCase("pan")||type.equalsIgnoreCase("aadhar")||type.equalsIgnoreCase("other"))
                                        {
                                            updateCard();
                                        }
                                        else {
                                            Utils.makeSnackBar(context,identitySelect,"Please Select Identity");
                                        }
                                    } else {
                                        Utils.makeSnackBar(context, galryFront, "Please upload Front and back Side of document");
                                    }
                                }

                            }
                        } else {
                            Utils.makeSnackBar(context, galryFront, "Please enter your card number");
                        }

                    }
                });
                galryBAck = upload.findViewById(R.id.galeryPic2);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        upload.dismiss();
                    }
                });
                backDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        upload.dismiss();
                    }
                });
                cameraFront.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1003);
                    }
                });
                galryFront.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);//
                        startActivityForResult(Intent.createChooser(intent, "Select File"), 1005);
                    }
                });
                galryBAck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);//
                        startActivityForResult(Intent.createChooser(intent, "Select File"), 1006);
                    }
                });
                cameraFront2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1004);
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
                          /*  ModelManager.getInstance().getUpdateProfileManager().UpdateProfileManager(context, Config.updateprofileurl, Operations.updateProfile(
                                    context, CSPreferences.readString(context, "customer_id"), firstname.getText().toString(), phonenumber.getText().toString()
                            ));*/
                            ModelManager.getInstance().getUpdateProfileManager().UpdateProfileManager(context, Config.updateprofileurl, Operations.updteProfile(
                                    context, CSPreferences.readString(context, "customer_id"), firstname.getText().toString(), phonenumber.getText().toString()
                                    , licenseValidity.getText().toString(), licenseNo.getText().toString(), address.getText().toString(), zip.getText().toString()
                            ));
                            firstname.setEnabled(false);
                            firstname.setClickable(false);
                            emaiid.setEnabled(false);
                            emaiid.setClickable(false);
                            phonenumber.setClickable(false);
                            phonenumber.setEnabled(false);
                            passwordchange.setEnabled(false);
                            identitySelect.setEnabled(false);
                            edCardNo.setEnabled(false);
                            changecompany.setEnabled(false);
                            zip.setEnabled(false);
                            address.setEnabled(false);
                            selectLocation.setEnabled(false);
                            locationName.setEnabled(false);
                            licenseNo.setEnabled(false);
                            dropdownIdentity.setEnabled(false);
                            licenseValidity.setEnabled(false);
                            editFields.setImageResource(R.drawable.ic_icon_edit_black);
                            state = 0;
                        } else {
                            Utils.makeSnackBar(context, phonenumber, "Fill Correct number");
                        }
                    } else {
                        Utils.makeSnackBar(context, phonenumber, "Please Fill All Fields");
                    }


                } else {
                    locationName.setVisibility(View.GONE);
                    selectLocation.setVisibility(View.VISIBLE);
                    dropdown.setVisibility(View.VISIBLE);
                    editFields.setImageResource(R.mipmap.tick);
                    edCardNo.setEnabled(true);
                    selectLocation.setEnabled(true);
                    firstname.setEnabled(true);
                    firstname.setClickable(true);
                    passwordchange.setEnabled(true);
                    changecompany.setEnabled(true);
                    emaiid.setEnabled(false);
                    identitySelect.setEnabled(true);
                    emaiid.setClickable(false);
                    phonenumber.setClickable(true);
                    phonenumber.setEnabled(true);
                    dropdownIdentity.setEnabled(true);
                    zip.setEnabled(true);
                    address.setEnabled(true);
                    locationName.setEnabled(true);
                    licenseNo.setEnabled(true);
                    licenseValidity.setEnabled(true);
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

        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandSpinner_Loc();
            }
        });

        identitySelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ck = edCardNo.getText().toString();
                if (identitySelect.getSelectedItem().toString().equalsIgnoreCase("Select Identity") && ck.equalsIgnoreCase("")) {
                    llCard.setVisibility(View.GONE);
                    llUploadDoc.setVisibility(View.GONE);

                    isOpenDialog = true;
                } else {
                    llUploadDoc.setVisibility(View.VISIBLE);

                    llCard.setVisibility(View.VISIBLE);

                }

                if (identitySelect.getSelectedItem().toString().equalsIgnoreCase("Pan Card")) {
                    CSPreferences.putString(context, Constant.CARDTYPE, "pan");
                } else if (identitySelect.getSelectedItem().toString().equalsIgnoreCase("Adhar Card")) {
                    CSPreferences.putString(context, Constant.CARDTYPE, "aadhar");
                } else if (identitySelect.getSelectedItem().toString().equalsIgnoreCase("Other")) {
                    CSPreferences.putString(context, Constant.CARDTYPE, "other");
                }
                else
                {
                    CSPreferences.putString(context, Constant.CARDTYPE, "other");

                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setFontStyle() {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.montserratsemibold);
        Typeface typeface2 = ResourcesCompat.getFont(context, R.font.montserratlight);
        firstname.setTypeface(typeface2);
        phonenumber.setTypeface(typeface2);
        address.setTypeface(typeface2);
        zip.setTypeface(typeface2);
        emaiid.setTypeface(typeface2);
        edCardNo.setTypeface(typeface2);
        licenseValidity.setTypeface(typeface2);
        passwordchange.setTypeface(typeface2);
        edCardNo.setTypeface(typeface2);
        licenseNo.setTypeface(typeface2);
        edCardNo.setTypeface(typeface2);
        locationName.setTypeface(typeface2);
        changecompany.setTypeface(typeface2);
        lblAddress.setTypeface(typeface);
        lblChngePwd.setTypeface(typeface);
        lblEmail.setTypeface(typeface);
        lblFirstName.setTypeface(typeface);
        lblIdentity.setTypeface(typeface);
        lblLicenseNo.setTypeface(typeface);
        lblLicValidity.setTypeface(typeface);
        lblLocationName.setTypeface(typeface);
        lblMob.setTypeface(typeface);


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
                Utils.makeSnackBar(Profile_Activity.this, edtimage, "Network Problem");
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

    public void expandSpinner_Loc() {
        MotionEvent motionEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, 0, 0, 0);
        selectLocation.dispatchTouchEvent(motionEvent);
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

            case Constant.COMPANYLOCATION_SUCCESS:

                //  selectComapny.setVisibility(View.VISIBLE);
                locationNameList = new ArrayList<>();
                locationLists = ModelManager.getInstance().getCabCompaniesManager().companyLocationBean.getResponse().getLocationList();
                for (int j = 0; j < locationLists.size(); j++) {
                    if (!locationLists.get(j).getLocationName().isEmpty())
                        locationNameList.add(locationLists.get(j).getLocationName());
                }
                setLocationAdapter();

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
                if (!CSPreferences.readString(context, "AdharNo").equals("")) {
                    llCard.setVisibility(View.VISIBLE);
                    llUploadDoc.setVisibility(View.VISIBLE);
                }
                String v = CSPreferences.readString(context, "AdharNo");
                Log.d("chkVal", v + "");
                edCardNo.setText("" + CSPreferences.readString(context, "AdharNo"));
                emaiid.setText("" + CSPreferences.readString(context, "user_email"));
                phonenumber.setText("" + CSPreferences.readString(context, "user_mobile"));
                address.setText("" + CSPreferences.readString(context, "driverAddress"));
                zip.setText("" + CSPreferences.readString(context, "driverZip"));
                locationName.setText("" + CSPreferences.readString(context, "driverLocation"));
                licenseValidity.setText("" + CSPreferences.readString(context, "LicenseValidity"));
                licenseNo.setText("" + CSPreferences.readString(context, "driverLicense"));
                String profile = CSPreferences.readString(context, "profile_pic");
                Picasso.with(this)
                        .load(CSPreferences.readString(context, "profile_pic")).error(R.drawable.ic_icon_profile_pic).placeholder(R.drawable.ic_icon_profile_pic)
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
        listCard = new ArrayList<String>();
        locationNameList = new ArrayList<>();
        identitySelect = findViewById(R.id.selectIdentity);
        dropdown = findViewById(R.id.dropdownLoc);
        locationSpinner = findViewById(R.id.selectLocation);
        locationNameList.add(0, "Select Location");
        callbackManager = CallbackManager.Factory.create();
        imageView = (ImageView) findViewById(R.id.driverpic);
        editFields = (ImageView) findViewById(R.id.edit_profile);
        firstname = (EditText) findViewById(edtfirstname);
        address = findViewById(R.id.edAddress);
        zip = findViewById(R.id.edZip);
        licenseNo = findViewById(R.id.edLicense);
        licenseValidity = findViewById(R.id.edLicValidity);
        locationName = findViewById(R.id.locationname);
        emaiid = (TextView) findViewById(R.id.edtemail);
        passwordchange = (TextView) findViewById(R.id.txtpassworddd);
        changecompany = (TextView) findViewById(R.id.txtchangeccompany);
        phonenumber = (EditText) findViewById(R.id.edphonenumber);
        edtimage = (ImageView) findViewById(R.id.imageedit);
        selectLocation = findViewById(R.id.selectLocation);
        back_img = (ImageView) findViewById(R.id.back);
        changecompany = (TextView) findViewById(R.id.txtchangeccompany);
        firstname.setText("" + CSPreferences.readString(context, "user_name"));
        emaiid.setText("" + CSPreferences.readString(context, "user_email"));
        phonenumber.setText("" + CSPreferences.readString(context, "user_mobile"));
        address.setText("" + CSPreferences.readString(context, "driverAddress"));
        zip.setText("" + CSPreferences.readString(context, "driverZip"));
        locationName.setText("" + CSPreferences.readString(context, "driverLocation"));
        licenseValidity.setText("" + CSPreferences.readString(context, "LicenseValidity"));
        licenseNo.setText("" + CSPreferences.readString(context, "driverLicense"));
        cmpnyList = new ArrayList<>();
        list = new ArrayList<String>();
        list.add(0, "Select Company Name");

        String profile = CSPreferences.readString(context, "profile_pic");
        if (profile != null) {
            Picasso.with(this)
                    .load(CSPreferences.readString(context, "profile_pic")).placeholder(R.drawable.ic_icon_profile_pic).error(R.drawable.ic_icon_profile_pic)
                    .into(imageView);
        }
        imageView.buildDrawingCache();
        Bitmap bm = imageView.getDrawingCache();
        if (bm == null) {
            convertImageToBase64();
        } else {
            Picasso.with(this)
                    .load(CSPreferences.readString(context, "profile_pic")).placeholder(R.drawable.ic_icon_profile_pic).error(R.drawable.ic_icon_profile_pic)
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

    // add items into spinner dynamically
    public void addItemsOnSpinner2() {


        listCard.add("Select Identity");
        listCard.add("Pan Card");
        listCard.add("Adhar Card");
        listCard.add("Other");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.tx_spinner, listCard);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        identitySelect.setAdapter(dataAdapter);
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

    private void setLocationAdapter() {
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this, R.layout.tx_spinner, locationNameList);
        selectLocation.setAdapter(stringArrayAdapter);

        selectLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (locationLists.size() > 0)
                    select_locationId = locationLists.get(i).getLocationId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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

    public String convertImageToBase64(Bitmap bit) {
//        bit = BitmapFactory.decodeResource(getResources(),
//                R.drawable.-ic_icon_profile_pic);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 50, bao);
        byte[] byteArray = bao.toByteArray();
        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        covertedImage = BitMapToString(compressedBitmap);
        Log.e("convertedImage", "" + covertedImage);
        return covertedImage;
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
                bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                bm=  Utils.getResizedBitmap(bm,400);
                try {
                    byte[] byteArray = bytes.toByteArray();
                    Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    covertedImage = BitMapToString(compressedBitmap);
                    imageView.setImageBitmap(bm);
                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.makeSnackBar(context, edtimage, "Some problem in image");
                }

                //Log.e("From gallery",covertedImage);
                updatePic();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void onFrontGalry(Intent data) {
        if (data != null) {
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                bm=  Utils.getResizedBitmap(bm,400);
                try {
                    byte[] byteArray = bytes.toByteArray();
                    Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                   frontPic= convertImageToBase64(compressedBitmap);
                   // frontPic = BitMapToString(compressedBitmap);
                    front.setImageBitmap(bm);

                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.makeSnackBar(context, edtimage, "Some problem in image");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void onBackgalry(Intent data) {
        if (data != null) {
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                bm=  Utils.getResizedBitmap(bm,400);
                try {
                    byte[] byteArray = bytes.toByteArray();
                    Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    backPic= convertImageToBase64(compressedBitmap);
                    back.setImageBitmap(bm);
                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.makeSnackBar(context, edtimage, "Some problem in image");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
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
        Log.e("camera_images_me", covertedImage);

        // ***Update Pic****
        updatePic();

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
                String pic = response.body().getResponse().getProfilePic();
                Log.d("picUpdated", pic + " Updated");
                CSPreferences.putString(context, "profile_pic", Config.baserurl_image + "" + pic);
                Log.e("profile_pic", msg);
                Utils.makeSnackBar(context, edtimage, "Profile pic updated");
                AppController.picUpdated=true;

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

    private void updateCard() {
        dialog = new SpotsDialog(context);
        dialog.setCancelable(false);
        dialog.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
      /*  String parms = Config.updatedocument + CSPreferences.readString(this, "customer_id") + "&type=" + CSPreferences.readString(context, Constant.CARDTYPE) + "&image=" + frontPic + "&image1=" + backPic+"&number=" +"123456";
        Log.d("paramsChange", parms);
        Log.d("paramsChangeF", frontPic);
        Log.d("paramsChangeB", backPic);*/


        Call<UpdateDocument> call = apiService.updateDocument(CSPreferences.readString(this, "customer_id"), CSPreferences.readString(context, Constant.CARDTYPE), body, backBody,cardNumber);
        call.enqueue(new Callback<UpdateDocument>() {

            @Override
            public void onResponse(Call<UpdateDocument> call, Response<UpdateDocument> response) {
                boolean res = response.isSuccessful();
                if (res) {

                    try {
                        String msg = response.body().getResponse().getMessage();
                        dialog.dismiss();
                        upload.dismiss();

                        Log.e("profile_pic", msg + "My ");
                        Utils.makeSnackBar(context, edtimage, "Documents uploaded");

                    } catch (Exception e) {
                        dialog.dismiss();
                        Utils.makeSnackBar(context, edtimage, "Documents not uploaded");
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<UpdateDocument> call, Throwable t) {
                dialog.dismiss();
                upload.dismiss();
                Log.e("profile_pic", "Error");
                Utils.makeSnackBar(context, edtimage, "Documents not uploaded");


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

            else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);

            } else if (requestCode == 1003) {

                   body=onFrontCapture(data);
            } else if (requestCode == 1004) {
                backBody=onBackCapture(data);

            } else if (requestCode == 1005) {

                 onFrontGalry(data);
            } else if (requestCode == 1006) {
                 onBackgalry(data);
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
// Make body of capture image
    private MultipartBody.Part onFrontCapture(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        front.setImageBitmap(thumbnail);
        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(data.getData());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
         byte[] imageBytes=  getBytes(is);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
             body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }
    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }


    private MultipartBody.Part onBackCapture(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        back.setImageBitmap(thumbnail);
        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(data.getData());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            byte[] imageBytes=  getBytes(is);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
            backBody = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return backBody;

    }

    @OnClick(R.id.dropdownIdentity)
    public void onViewClicked() {
        identitySelect.performClick();
    }

}
