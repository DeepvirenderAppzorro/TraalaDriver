package com.appzorro.driverappcabscout.view.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appzorro.driverappcabscout.R;
import com.appzorro.driverappcabscout.controller.CountryMaster;
import com.appzorro.driverappcabscout.controller.ModelManager;
import com.appzorro.driverappcabscout.model.Beans.City;
import com.appzorro.driverappcabscout.model.Beans.Country;
import com.appzorro.driverappcabscout.model.Beans.CountryCode;
import com.appzorro.driverappcabscout.model.Beans.GetCountryCode;
import com.appzorro.driverappcabscout.model.Beans.State;
import com.appzorro.driverappcabscout.model.CSPreferences;
import com.appzorro.driverappcabscout.model.Constant;
import com.appzorro.driverappcabscout.model.Event;
import com.appzorro.driverappcabscout.model.Operations;
import com.appzorro.driverappcabscout.model.Utils;
import com.appzorro.driverappcabscout.view.Activity.loginActivity.LoginActivity;
import com.appzorro.driverappcabscout.view.HomeActivity;
import com.facebook.CallbackManager;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

/**
 * Created by pankaj on 21/1/17.
 */
public class SignUp extends AppCompatActivity {
    Toolbar toolbar;
    CountryMaster countryMaster;
    Context context;
    String userChoosenTask, Country, State, phoneNo;
    ImageView back, driverPic;
    Activity activity = this;
    EditText etDrivername, etDriEmail, etDrivNo, etDrivPasswrd, etReDrivPassword, etDrivLicence, etDrivCity, etDrivZip;
    String CountriesName, iso = "af", drivername, driEmail, drivNo, drivPasswrd, redrivPasswrd, drivLicence, drivCity, drivZip, devicetoken, cabid, location_id, json, jsonStates, State_name, Country_id, State_id, chkCountry_id;
    CheckBox termsCheckBox, policyCheckBox;
    String covertedImage;
    CallbackManager callbackManager;
    LinearLayout layoutCity;
    Dialog dialog;
    TextView lbl_submit, txt_state, txt_city, txt_country, editCountry_Code;
    ArrayList<City> tempCities;
    Spinner state_Spinner, country_Spinner, city_Spinner, spinner_country;
    @BindView(R.id.lbl_acc)
    TextView lblAcc;
    @BindView(R.id.tvAlreadyacc)
    TextView tvAlreadyacc;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ArrayAdapter<Country> countryArrayAdapter;
    private ArrayAdapter<State> stateArrayAdapter;
    private ArrayAdapter<City> cityArrayAdapter;

    private ArrayList<Country> countries;
    private ArrayList<State> states;
    private ArrayList<City> cities;

    Location mLastLocation;
    LocationManager locationManager;
    double latitude, longitude;
    private ArrayList<CountryCode> data = new ArrayList<CountryCode>(233);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_driver);
        ButterKnife.bind(this);
        initViews();
       // setFontStyle();


        TextView txDis = (TextView) findViewById(R.id.txDis); //txt is object of TextView
        txDis.setMovementMethod(LinkMovementMethod.getInstance());
        txDis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
            }
        });


        TextView txPoliciy = (TextView) findViewById(R.id.txPloiciy); //txt is object of TextView
        txPoliciy.setMovementMethod(LinkMovementMethod.getInstance());
        txPoliciy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
            }
        });
        //Top toolbar
        driverPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, CabCompanyActivity.class));
                finish();
            }
        });

        String key = printKeyHash(this);
        Log.d("key_hash", key);

        countryArrayAdapter = new ArrayAdapter<Country>(SignUp.this, R.layout.tx_spinner, countries);
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country_Spinner.setAdapter(countryArrayAdapter);


        stateArrayAdapter = new ArrayAdapter<State>(SignUp.this, R.layout.tx_spinner, states);
        stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state_Spinner.setAdapter(stateArrayAdapter);


        cityArrayAdapter = new ArrayAdapter<City>(SignUp.this, R.layout.tx_spinner, cities);
        cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city_Spinner.setAdapter(cityArrayAdapter);

        country_Spinner.setOnItemSelectedListener(country_listener);
        state_Spinner.setOnItemSelectedListener(state_listener);
        city_Spinner.setOnItemSelectedListener(city_listener);
        txt_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_country.setVisibility(View.GONE);

                country_Spinner.performClick();
                country_Spinner.setVisibility(View.VISIBLE);
            }
        });

        getLocation();

    }


    private void setFontStyle() {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.montserratsemibold);
        Typeface typeface2Bold = ResourcesCompat.getFont(context, R.font.montserratbold);
        toolbarTitle.setTypeface(typeface2Bold);
        etDrivername.setTypeface(typeface);
        editCountry_Code.setTypeface(typeface);
        etDrivNo.setTypeface(typeface);
        etDrivPasswrd.setTypeface(typeface);
        etDrivLicence.setTypeface(typeface);
        etDriEmail.setTypeface(typeface);
        etDrivZip.setTypeface(typeface);
        termsCheckBox.setTypeface(typeface);
        policyCheckBox.setTypeface(typeface);
        lblAcc.setTypeface(typeface);
        tvAlreadyacc.setTypeface(typeface);
        etReDrivPassword.setTypeface(typeface);

    }


    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //  locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, (LocationListener) this);
            mLastLocation = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            mLastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try {
                latitude = mLastLocation.getLongitude();
                longitude = mLastLocation.getLongitude();
            } catch (Exception e) {
                Utils.makeSnackBar(SignUp.this, etDriEmail, "Poor Internet Connection");
            }


        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    private AdapterView.OnItemSelectedListener country_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (position >= 0) {

                final Country country = (Country) country_Spinner.getItemAtPosition(position);
                Log.d("SpinnerCountry", "onItemSelected: country: " + country.getCountryID());
                ArrayList<State> tempStates = new ArrayList<>();
                //   txt_country.setVisibility(View.GONE);
                for (State singleState : states) {
                    if (singleState.country_id == country.getCountryID()) {
                        tempStates.add(singleState);
                    }
                }

                if (tempStates.size() != 0) {
                    txt_state.setVisibility(View.VISIBLE);
                    state_Spinner.setVisibility(View.GONE);
                    stateArrayAdapter = new ArrayAdapter<State>(SignUp.this, android.R.layout.simple_list_item_1, tempStates);
                    stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    state_Spinner.setAdapter(stateArrayAdapter);

                    txt_state.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            txt_state.setVisibility(View.GONE);
                            state_Spinner.performClick();
                            state_Spinner.setVisibility(View.VISIBLE);

                        }
                    });


                } else if (tempStates.size() == 0) {
                    state_Spinner.setVisibility(View.VISIBLE);

                }

            } else {

            }


            cityArrayAdapter = new ArrayAdapter<City>(SignUp.this, android.R.layout.simple_list_item_1, new ArrayList<City>());
            cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            city_Spinner.setAdapter(cityArrayAdapter);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            country_Spinner.setVisibility(View.GONE);
            //  txt_country.setVisibility(View.VISIBLE);
        }
    };
    private AdapterView.OnItemSelectedListener state_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (position >= 0) {

                final State state = (State) state_Spinner.getItemAtPosition(position);
                Log.d("SpinnerCountry", "onItemSelected: state: " + state.getStateID());
                tempCities = new ArrayList<>();

                int id1 = state.getStateID();
                for (City singleCity : cities) {
                    //   Log.d("stateid", " nclknkjn");
                    if (singleCity.stateId == state.getStateID()) {
                        tempCities.add(singleCity);


                    } else {

                    }
                }
                if (tempCities.size() != 0) {
                    layoutCity.setVisibility(View.VISIBLE);
                    txt_city.setVisibility(View.VISIBLE);
                    city_Spinner.setVisibility(View.GONE);
                    cityArrayAdapter = new ArrayAdapter<City>(SignUp.this, android.R.layout.simple_list_item_1, tempCities);
                    cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    city_Spinner.setAdapter(cityArrayAdapter);

                    txt_city.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            txt_city.setVisibility(View.GONE);
                            city_Spinner.setVisibility(View.VISIBLE);
                            city_Spinner.performClick();
                        }
                    });


                } else {
                    layoutCity.setVisibility(View.GONE);
                    city_Spinner.setVisibility(View.GONE);
                }

            } else {

            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private AdapterView.OnItemSelectedListener city_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    //**** spinner items
    private void initializeUI() {
        country_Spinner = (Spinner) findViewById(R.id.selectCounties);
        state_Spinner = (Spinner) findViewById(R.id.selectStates);
        city_Spinner = (Spinner) findViewById(R.id.selectCities);

        countries = new ArrayList<>();
        states = new ArrayList<>();
        cities = new ArrayList<>();


    }

    public void onSMSLoginFlow() {
        String CountryCode = GetCountryCode.getmInstance().GetCountryZipCode(context);
        //String code = editCountry_Code.getText().toString();
        String code = editCountry_Code.getText().toString();
        String mobileNo = etDrivNo.getText().toString();
        //String iso = spinner_country.getSelectedItem().toString();
        TelephonyManager tm = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
      /*  if(tm==null)
        {

        }
        else
        {
            String countryCodeIso = tm.getNetworkCountryIso();
        }*/
        Log.d("ISO", iso + "");
        PhoneNumber phoneNumber = new PhoneNumber(code, mobileNo, iso);

        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.CODE).setInitialPhoneNumber(phoneNumber); // or .ResponseType.CUSTOMER_TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, 101);
    }

    public void onEmailLoginFlow(View view) {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.EMAIL,
                        AccountKitActivity.ResponseType.CODE); // or .ResponseType.CUSTOMER_TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, 101);
    }


    public void initViews() {

        ButterKnife.bind(this);
        context = this;
        txt_state = (TextView) findViewById(R.id.txt_select_state);
        layoutCity = (LinearLayout) findViewById(R.id.linear_11th);
        txt_city = (TextView) findViewById(R.id.txt_select_city);
        txt_country = (TextView) findViewById(R.id.txt_select_country);
        callbackManager = CallbackManager.Factory.create();
        editCountry_Code = (TextView) findViewById(R.id.editPhone_country);
        // convertImageToBase64();
        devicetoken = FirebaseInstanceId.getInstance().getToken();
        cabid = getIntent().getStringExtra("cab_id");
        CSPreferences.putString(context, "CompanyId", cabid);
        location_id = getIntent().getStringExtra("location_id");
        etDrivername = (EditText) findViewById(R.id.edtname);
        etDriEmail = (EditText) findViewById(R.id.edtemail);
        etDrivNo = (EditText) findViewById(R.id.edtphone);
        etDrivPasswrd = (EditText) findViewById(R.id.edtpassword);
        etReDrivPassword = (EditText) findViewById(R.id.edtconfirmpassword);
        etDrivLicence = (EditText) findViewById(R.id.edtdriverlicence);
        etDrivZip = (EditText) findViewById(R.id.edtzipcode);
        back = (ImageView) findViewById(R.id.back);
        termsCheckBox = (CheckBox) findViewById(R.id.termsCheckbox);
        policyCheckBox = (CheckBox) findViewById(R.id.privacyCheckbox);
        lbl_submit = (TextView) findViewById(R.id.txSubmit);
        driverPic = (ImageView) findViewById(R.id.driverimage);
        spinner_country = (Spinner) findViewById(R.id.country_spinner);
        initializeUI();
        countries = SplashActivity.countries;
        states = SplashActivity.states;
        cities = SplashActivity.cities;
        countryMaster = new CountryMaster(context);

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
        String subString = drivLicence.substring(0, 1);
        if (Pattern.matches("[a-zA-Z]+", subString) == true) {
            Log.d("chkString", subString);
        }
        if (country_Spinner.getSelectedItem() != null) {
            Country = country_Spinner.getSelectedItem().toString().trim();

        }
        if (state_Spinner.getSelectedItem() != null) {
            State = state_Spinner.getSelectedItem().toString().trim();

        }
        if (city_Spinner.getSelectedItem() != null) {
            drivCity = city_Spinner.getSelectedItem().toString().trim();

        }
        if (drivPasswrd.length() < 6) {
            etDrivPasswrd.setError("Fill minimum 6 digits");
        }

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


        }
        phoneNo = etDrivNo.getText().toString();
        Log.d("phone_no", phoneNo);
        //  Log.d("image",covertedImage);

        if (!etDrivername.getText().toString().isEmpty() && !driEmail.isEmpty() && !etDrivNo.getText().toString().isEmpty() && !etDrivLicence.getText().toString().isEmpty() && !etDrivPasswrd.getText().toString().isEmpty() && !etReDrivPassword.getText().toString().isEmpty() &&
                Country != null && State != null) {
            if (covertedImage != null) {
                if (drivNo.length() == 10) {
                    if (drivPasswrd.length() >= 6) {
                        if (drivPasswrd.equalsIgnoreCase(redrivPasswrd)) {

                            String phn_no = etDrivLicence.getText().toString();
                            if(phn_no.length()==15)
                            {
                               String first_2 = phn_no.substring(0, 2);
                                if (Pattern.matches("[a-zA-Z]+", first_2) == true) {
                                    String no= phn_no.substring(2,15);
                                    if (no.matches("[0-9]+")) {
                                        dialog = new SpotsDialog(context);
                                        dialog.show();

                                        onSMSLoginFlow();
                                       /* ModelManager.getInstance().getRegistrationManager().
                                                registerUser(context, Config.simplesignupurl,
                                                        Operations.simpleuserRegister(context, CSPreferences.readString(context, "LocationId"),
                                                                cabid, driEmail, drivername, "A", devicetoken, etDrivNo.getText().toString(), covertedImage, drivCity, drivZip, drivLicence, Country, State, latitude + "", longitude + "", drivPasswrd));
*/
                                    }
                                    else
                                    {
                                        Utils.makeSnackBar(context, city_Spinner, "Fill Valid License number ");
                                    }
                                }
                                else
                                {
                                    Utils.makeSnackBar(context, city_Spinner, "Fill Valid License number ");
                                }

                            }
                            else
                            {
                                Utils.makeSnackBar(context, city_Spinner, "License number length should be 15 letters");
                            }
                            CSPreferences.putString(context, Constant.Password, drivPasswrd);


                        } else {
                            Utils.makeSnackBar(context, city_Spinner, "Password did't matched");

                        }
                    } else {
                        Utils.makeSnackBar(context, city_Spinner, "Fill password minimum 6 digits");

                    }


                } else {
                    Utils.makeSnackBar(context, city_Spinner, "Please Fill 10 digit  Number");

                }
            } else {
                Utils.makeSnackBar(context, city_Spinner, "Please Upload Image");

            }
        } else {
            Utils.makeSnackBar(context, city_Spinner, "Please Fill All Fields");
        }


    }

    private void setSpinnerAdapter() {

        spinner_country.setAdapter(new ArrayAdapter<String>(activity, R.layout.sppiner_item, R.id.txt1, countryMaster.mCountries_ISO));

        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                editCountry_Code.setText("+" + countryMaster.mCountries_CODE.get(i));
                iso = countryMaster.mIso.get(i);
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


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
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

    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                bm = Utils.getResizedBitmap(bm, 400);
                try {
                    byte[] byteArray = bytes.toByteArray();
                    Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    covertedImage = BitMapToString(compressedBitmap);
                    Log.d("image", covertedImage);
                    driverPic.setImageBitmap(bm);
                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.makeSnackBar(context, city_Spinner, "Some problem in image");
                }

                //Log.e("From gallery",covertedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
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
        //  covertedImage = Base64.encodeToString(byteArray, 0);
        Bitmap bm400 = Utils.getScaledBitmap(thumbnail, 500, 500);
        covertedImage = BitMapToString(bm400);
        Log.e("camera images", covertedImage);


    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constant.SIGNUPRESPONSE:
                dialog.dismiss();

                ModelManager.getInstance().getUserDetailManager().UserDetailManager(context, Operations.getUserDetail(context,
                        CSPreferences.readString(context, "customer_id")));

                break;

            case Constant.USERDETAILSTAUS:
                dialog.dismiss();
                CSPreferences.putString(SignUp.this, "login_status", "true");
                Intent i = new Intent(context, HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();

                break;
            case Constant.ERROR:
                Toast.makeText(context, event.getValue(), Toast.LENGTH_SHORT).show();
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
                break;
            case Constant.GetCountryISO:
                data = event.getCountry();

                setSpinnerAdapter();
                // setCountryName(GetCountryCode.getmInstance().CountryID);
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
        if (requestCode == 101) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
                //  showErrorActivity(loginResult.getError());
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0, 10));
                }
                Log.d("img", covertedImage + " sigunup");
                // **************Comment by deep

              /*  ModelManager.getInstance().getRegistrationManager().
                        registerUser(context, Config.simplesignupurl,
                                Operations.simpleuserRegister(context, CSPreferences.readString(context, "LocationId"),
                                        cabid, driEmail, drivername, "A", devicetoken, etDrivNo.getText().toString(), covertedImage, drivCity, drivZip, drivLicence, Country, State, latitude + "", longitude + "", drivPasswrd));
*/
            }

            // Surface the result to your user in an appropriate way.
            //  Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(activity, CabCompanyActivity.class));
        finish();
    }
}
