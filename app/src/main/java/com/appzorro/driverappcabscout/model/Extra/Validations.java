package com.appzorro.driverappcabscout.model.Extra;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vijay on 19/7/18.
 */

public class Validations {
    public static Validations mInstanse;

    public Validations() {
        mInstanse = this;
    }

    public static Validations getInstance() {
        return mInstanse;
    }

    CoordinatorLayout cdl;

    public Validations(CoordinatorLayout coordinatorLayout) {

        cdl = coordinatorLayout;
    }

    public static boolean match(String str) {
        Pattern ps = Pattern.compile("^[a-zA-Z -?.,]+$");
        Matcher ms = ps.matcher(str);
        boolean bs = ms.matches();

        return bs;
    }

    public static boolean match1(String str) {
        Pattern ps = Pattern.compile("^[a-zA-Z0-9._]+$");
        Matcher ms = ps.matcher(str);
        boolean bs = ms.matches();
        return bs;
    }

    String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    private static boolean IsMatch(String s, String pattern) {
        try {
            Pattern patt = Pattern.compile(pattern);
            Matcher matcher = patt.matcher(s);
            return matcher.matches();
        } catch (RuntimeException e) {
            return false;
        }
    }

    private boolean isValidEmaillId(String email) {

//	     return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
//	               + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
//	               + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
//	               + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
//	               + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
//	               + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();

        return Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").matcher(email).matches();
    }

    public static String integerpattern = "[0-9]+";
    //public static String phonepattern = "[0-9]{10}+";
    public static String phonepattern = "[0-9]+";
    //public static String phonepattern="((\\+*)(0*|(0 )*|(0-)*|(91 )*)(\\d{12}+|\\d{10}+))|\\d{5}([- ]*)\\d{6}";

    //**********************************Login with email************************
    public boolean ChangePasswordValidate(Context context, String old_password, String new_password, String cnf_password) {

        if (old_password.length() == 0 || new_password.length() == 0 || cnf_password.length() == 0) {
            String message = "PLease fill all Password Field";
            snackbarMessage(context, message);
            return false;
        } else if (new_password.length() < 6 || new_password.length() >= 15) {
            String message = "New Password and Confirm Password length should be 8 to 15 digits";
            snackbarMessage(context, message);

            return false;
        } else if (cnf_password.length() < 6 || cnf_password.length() >= 15) {
            String message = "New Password and Confirm Password length should be 8 to 15 digits";
            snackbarMessage(context, message);

            return false;
        } else if (new_password.equals(cnf_password)) {

            return true;
        } else {
            String message = "New Password and Confirm Password are not match";
            snackbarMessage(context, message);
            return false;
        }

    }

    //**********************************Login with email************************
    public boolean Loginvalidate(Context context, String username, String userpassword) {

        if (username.length() == 0 && userpassword.length() == 0) {
            String message = "Please fill all fields";
            snackbarMessage(context, message);

            return false;
        }
        if (username.length() == 0) {
            String message = "Please enter email address /phone no/username ";

            snackbarMessage(context, message);

            return false;
        }
        if (!isValidEmaillId(username.trim())) {

            String message = "Please enter a valid username/or phone no or email adddress ";

            snackbarMessage(context, message);

            return false;
        }
        if (userpassword.length() == 0) {
            String message = "Please enter password";
            snackbarMessage(context, message);

            return false;
        } else {
            return true;
        }
    }

    //**********************************Login with phone************************
    public boolean Loginvalidate_phone(Context context, String username, String userpassword) {

        if (username.length() == 0 || userpassword.length() == 0) {
            String message = "Please fill all fields";
            snackbarMessage(context, message);

            return false;
        }
        if (username.length() == 0) {
            String message = "Please enter email address /phone no/username";

            snackbarMessage(context, message);

            return false;
        }
        if (!(username.matches("\\d{10}"))) {
            String message = "Please enter a valid username/or phone no or email adddress";

            snackbarMessage(context, message);

            return false;
        }
        if ((username.length() < 10) || (username.length() > 15)) {
            String message = "Phone no length should be min 10 and max 15 digits.";

            snackbarMessage(context, message);

            return false;
        }
        if (userpassword.length() == 0) {
            String message = "Please enter password";
            snackbarMessage(context, message);

            return false;
        } else {
            return true;
        }
    }

    //**********************************Login with username************************
    public boolean Loginvalidate_username(Context context, String username, String userpassword) {

        if (username.length() == 0 && userpassword.length() == 0) {
            String message = "Please fill all fields";
            snackbarMessage(context, message);
            return false;
        }
        if (username.length() == 0) {
            String message = "Please enter email address /phone no/username ";
            snackbarMessage(context, message);
            return false;
        }
        if (!match1(username.trim())) {
            String message = "Please enter a valid username/or phone no or email adddress ";
            snackbarMessage(context, message);
            return false;
        }
        if (userpassword.length() == 0) {
            String message = "Please enter password";
            snackbarMessage(context, message);
            return false;
        } else {
            return true;
        }
    }

    public boolean editProfileValidate(Context context, String name, String email, String address, String phone) {

        if (name.length() == 0 && email.length() == 0 && address.length() == 0 && phone.length() == 0) {
            String message = "Please fill all fields";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            return false;
        }
        if (name.length() == 0) {
            String message = "Please enter name field";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!match(name)) {
            String message = "Enter characters only in  name field ";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            //showMessageOKCancel(context, message);
            return false;
        }
        if (email.length() == 0) {
            String message = "Please enter email address";
            //showMessageOKCancel(context, message);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidEmaillId(email.trim())) {

            String message = "Please enter a valid email address";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            //showMessageOKCancel(context, message);
            return false;
        }
        if (address.length() == 0) {
            String message = "Please enter address";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            //showMessageOKCancel(context, message);
            return false;
        }
        if ((phone.length() < 10) || (phone.length() > 15)) {
            String message = "Phone length should be 10 to 15 digits ";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            //showMessageOKCancel(context, message);
            return false;
        }
        if (!(phone.trim().matches(phonepattern))) {
            String message = "Please enter a valid phone number";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            //showMessageOKCancel(context, message);
            return false;
        } else
            return true;
    }

    //**********************************Registration with email************************
    public boolean registervalidate(Context context, String name, String email, String password, String unique_username) {
        if (name.length() == 0 && email.length() == 0 && password.length() == 0) {
            String message = "Please fill all fields";
            snackbarMessage(context, message);

            return false;
        }
        if (name.length() == 0) {
            String message = "Please enter name field";
            snackbarMessage(context, message);

            return false;

        } else if (unique_username.length() == 0) {
            String message = "Please enter unique username ";
            snackbarMessage(context, message);
            return false;
        } else if (!match1(unique_username)) {

            String message = "Unique username should contain only characters, number, underscore or dot. ";
            snackbarMessage(context, message);

            return false;
        } else if (!match(name)) {
            String message = "Enter characters only in  name field ";
            snackbarMessage(context, message);

            return false;
        }
        if (email.length() == 0) {
            String message = "Please enter email address";

            snackbarMessage(context, message);

            return false;
        } else if (!isValidEmaillId(email.trim())) {
            String message = "Please enter a valid email address";
            snackbarMessage(context, message);

            return false;
        }

        if (password.length() == 0) {
            String message = "Please enter password";
            snackbarMessage(context, message);

            return false;

        } else if (password.length() < 6 || password.length() >= 15) {
            String message = "Password between 8 To 15 digits";
            snackbarMessage(context, message);

            return false;
        } else
            return true;
    }
    //**********************************Registration with email************************

    public boolean EditProfile(Context context, String name, String unique_username, String email, String phone, String bio, String date_of_birth, String gender, String location, String from, String relationship_status, String relationship_with, String anviversary_date, String interested_in, String fravourite_quote) {

        if (name.length() == 0) {
            String message = "Please enter name field";
            snackbarMessage(context, message);

            return false;

        } else if (!match(name)) {

            String message = "Enter characters only in  name field ";
            snackbarMessage(context, message);

            return false;
        } else if (unique_username.length() == 0) {
            String message = "Please enter unique username ";
            snackbarMessage(context, message);
        } else if (!match1(unique_username)) {

            String message = "Unique username should contain only characters, number, underscore or dot. ";
            snackbarMessage(context, message);

            return false;
        }
        if (email.length() == 0) {
            String message = "Please enter email address";

            snackbarMessage(context, message);

            return false;
        } else if (!isValidEmaillId(email.trim())) {

            String message = "Please enter a valid email address";
            snackbarMessage(context, message);

            return false;

        }
        if (date_of_birth.length() == 0) {
            String message = "Please select Date";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            return false;
        }

        if ((phone.length() < 10) || (phone.length() > 15)) {
            String message = "Phone length should be 10 to 15 digits";
            snackbarMessage(context, message);

            return false;
        }
        if (!(phone.trim().matches(phonepattern))) {
            String message = "Please enter a valid phone number";
            snackbarMessage(context, message);

            return false;
        }
        if ((gender.length() == 0)) {
            String message = "Please Select a Gender";
            snackbarMessage(context, message);

            return false;
        } else if (!match(bio)) {

            String message = "Enter characters only in name field ";
            snackbarMessage(context, message);

            return false;
        } else if (!match(location)) {

            String message = "Enter characters only in name field ";
            snackbarMessage(context, message);

            return false;
        } else if (!match(name)) {

            String message = "Enter characters only in  name field ";
            snackbarMessage(context, message);

            return false;
        } else if (!match(name)) {

            String message = "Enter characters only in  name field ";
            snackbarMessage(context, message);

            return false;
        } else if (!match(name)) {

            String message = "Enter characters only in  name field ";
            snackbarMessage(context, message);

            return false;
        } else if (!match(name)) {

            String message = "Enter characters only in  name field ";
            snackbarMessage(context, message);

            return false;
        } else
            return true;
    }

	/*
	* validation for about_fields
	* */

    public boolean EditProfile1(Context context, String name, String age,String phone, String skype) {


        if (name.length() == 0) {
            String message = "Please enter name field";
            snackbarMessage(context, message);

            return false;

        }
        else if (!match(name)) {

            String message = "Enter characters only in  name field ";
            snackbarMessage(context, message);

            return false;
        }

        if (age.length() == 0) {
            String message = "Please enter age field";

            snackbarMessage(context, message);

            return false;
        }
        else if (!match(name)) {

            String message = "Enter characters only in  name field ";
            snackbarMessage(context, message);

            return false;
        }

        if (skype.length() == 0) {
            String message = "Please enter skype id field";
            snackbarMessage(context, message);

            return false;

        }
        else if (!match(skype)) {

            String message = "Enter characters only in  skype field ";
            snackbarMessage(context, message);

            return false;
        }
        if ((phone.length() < 10) || (phone.length() > 15)) {
            String message = "Phone length should be 10 to 15 digits";
            snackbarMessage(context, message);

            return false;
        }
        if (!(phone.trim().matches(phonepattern))) {
            String message = "Please enter a valid phone number";
            snackbarMessage(context, message);

            return false;
        }

        else
            return true;


    }
	/*
	* End
	*
	* */


    public boolean EditProfile_account(Context context, String phone, String skype) {

        if (skype.length() == 0) {
            String message = "Please enter skype id field";
            snackbarMessage(context, message);

            return false;

        }
        else if (!match(skype)) {

            String message = "Enter characters only in  skype field ";
            snackbarMessage(context, message);

            return false;
        }
        if ((phone.length() < 10) || (phone.length() > 15)) {
            String message = "Phone length should be 10 to 15 digits";
            snackbarMessage(context, message);

            return false;
        }
        if (!(phone.trim().matches(phonepattern))) {
            String message = "Please enter a valid phone number";
            snackbarMessage(context, message);

            return false;
        }

        else
            return true;
    }


    public boolean Company_validation(Context context, String name, String email, String about, String address, String phone, String website) {

        if (name.length() == 0 && email.length() == 0 && about.length() == 0 && address.length() == 0 && phone.length() == 0 && website.length() == 0) {
            String message = "Please fill all fields";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (name.length() == 0) {
            String message = "Please enter name field";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;

        } else if (!match(name)) {

            String message = "Enter characters only in  name field ";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.length() == 0) {
            String message = "Please enter email address";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidEmaillId(email.trim())) {
            String message = "Please enter a valid email address";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (about.length() == 0) {
            String message = "Please enter about your company";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (address.length() == 0) {
            String message = "Please enter address";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            return false;

        } else if (address.length() < 15) {
            String message = "Minimum 15 characters for address";

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;

        }/*else if(!match1(address))
	 {

	 String message  = "Address field contains only characters and alphanumeric special character such as , # - in address field ";
	 Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

	 return false;
	 }*/
        if ((phone.length() < 10) || (phone.length() > 15)) {
            String message = "Phone length should be 10 to 15 digits ";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            //showMessageOKCancel(context, message);
            return false;
        }
        if (!(phone.trim().matches(phonepattern))) {
            String message = "Please enter a valid phone number";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            return false;
        }
        if (website.length() == 0) {
            String message = "Please enter Website Link";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            return false;

        } else
            return true;
    }

    public boolean Offer_card_validation(Context context, String price, String discount, String start_date_string, String Ex_date_string) {

        if (price.length() == 0 && discount.length() == 0 && start_date_string.length() == 0 && Ex_date_string.length() == 0) {
            String message = "Please fill all fields";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            return false;

        }
        if (price.length() == 0) {
            String message = "Please enter offer price";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            return false;

        }/*else if(!match(name))
		{

			String message  = "Enter characters only in  name field ";
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			//showMessageOKCancel(context, message);
			return false;
		}*/


        if (discount.length() == 0) {
            String message = "Please enter discount";

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;
        }

		/*else if((Float.parseFloat(discount)<=1)||(Float.parseFloat(discount)>100))
		{

			String message  = " Discount in between 1 to 100" ;
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			return false;


		}*/
		/*else if((Float.parseFloat(discount)>100))
		{

			String message  = " Discount inbetween 1 to 100" ;
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			return false;
		}*/
        if (start_date_string.length() == 0) {
            String message = "Please enter about your company";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Ex_date_string.length() == 0) {
            String message = "Please enter address";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            return false;

        } else
            return true;
    }


    public boolean Offer_validation(Context context, String description, String price, String discount, String start_date_string, String Ex_date_string) {

        if (description.length() == 0 && price.length() == 0 && discount.length() == 0 && start_date_string.length() == 0 && Ex_date_string.length() == 0) {
            String message = "Please fill all fields";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            return false;

        }

        if (description.length() == 0) {
            String message = "Please enter offer description";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            return false;

        } else if (description.length() < 10) {
            String message = "Minimum 30 characters for description";

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;

        }

        if (price.length() == 0) {
            String message = "Please enter offer price";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            return false;
        }

        if (discount.length() == 0) {
            String message = "Please enter discount";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (discount.equals("0")) {
            String message = "Discount can't be zero";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;
        }
/*
		else if((Float.parseFloat(discount)<=1)||(Float.parseFloat(discount)>100))
		{

			String message  = " Discount in between 1 to 100" ;
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			return false;
		}
		else if((Float.parseFloat(discount)>100))
		{

			String message  = " Discount in between 1 to 100" ;
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			return false;
		}*/
        if (start_date_string.length() == 0) {
            String message = "Please enter start date of the offer";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;
        }


        if (Ex_date_string.length() == 0) {
            String message = "Please enter expiry date of the offer";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            return false;

        }

        else
            return true;
    }


//**********************************Place_card_vlaidate************************

    public boolean Place_card_vlaidate(Context context, String fname, String lname, String email, String phone, String address, String tel) {

        String fname1 = fname.trim();
        String lname1 = lname.trim();
        String email1 = email.trim();
        String address1 = address.trim();
        String tel1 = tel.trim();
        String phone1 = phone.trim();


		/*if (fname1.length() == 0 && lname1.length() == 0 && email1.length() == 0 && address1.length() == 0 && tel.length() == 0) {
			String message = "Please fill all fields";
			//showMessageOKCancel(context, message);
			return false;
		}*/
        //*************first Name****************
        if (fname1.length() == 0) {
            String message = "Please enter first name";
            snackbarMessage(context, message);

            return false;

        } else if (!match(fname1)) {

            String message = "Enter characters only in first name field ";
//			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            snackbarMessage(context, message);
            return false;
        }

        //*************last Name****************
        if (lname1.length() == 0) {
            String message = "Please enter last name";

            snackbarMessage(context, message);
            return false;

        } else if (!match(lname1)) {
            String message = "Enter characters only in last name field";


            snackbarMessage(context, message);
            return false;
        }

        //*************Phone Name****************

        if (!(phone1.trim().matches(phonepattern))) {
            String message = "Please enter a valid phone number";
//			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            snackbarMessage(context, message);
            return false;
        }

        //*************tel Name****************

        if (!(tel1.trim().matches(phonepattern))) {
            String message = "Please enter the telephone number";

            snackbarMessage(context, message);
            return false;
        }

        //*************email Name****************

        if (email.length() == 0) {
            String message = "Please enter email address";

            snackbarMessage(context, message);
            return false;

        }
        if (address1.length() == 0) {
            String message = "Please enter address";

            snackbarMessage(context, message);
            return false;
        } else
            return true;
    }
    public boolean forgotvalidate(Context context, String email1) {

        if (email1.length() == 0) {
            String message = "Please fill email field";
            snackbarMessage(context, message);
            return false;

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
            String message = "Enter valid email address";
            snackbarMessage(context, message);
            return false;
        } else {
            return true;
        }
    }

    private void showMessageOKCancel(Context context, String message) {
        new AlertDialog.Builder(context).
                setTitle("Error!")
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                }).
                create().show();
    }

    private void snackbarMessage(Context c, String message) {

        new AlertDialog.Builder(c).
                setTitle("Error!")
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                }).
                create().show();

    }

    public boolean EnquireValidate(Context context, String pack_title, String from_location, String start_date_str, String end_date_str, String guest, String budget, String description, String transport_str, String name, String email, String mobile_no) {

        if ((name.length() == 0) || (email.length() == 0) || (pack_title.length() == 0) || (from_location.length() == 0) || (start_date_str.length() == 0) || (end_date_str.length() == 0) || (guest.length() == 0) || (budget.length() == 0) || (description.length() == 0) || (transport_str.length() == 0) || (mobile_no.length() == 0)) {
            String message = "Please fill all fields";
            snackbarMessage(context, message);

            return false;
        }
        if (name.length() == 0) {
            String message = "Please enter name field";
            snackbarMessage(context, message);

            return false;

        } else if (!match(name)) {

            String message = "Enter characters only in  name field ";
            snackbarMessage(context, message);

            showMessageOKCancel(context, message);
            return false;
        }
        if (email.length() == 0) {
            String message = "Please enter email address";

            snackbarMessage(context, message);
            return false;
        } else if (!isValidEmaillId(email.trim())) {

            String message = "Please enter a valid email address";
            snackbarMessage(context, message);

            return false;
        }
        if (pack_title.length() == 0) {
            String message = "Please enter Package Title";
            snackbarMessage(context, message);

            return false;

        }
        if (from_location.length() == 0) {
            String message = "Please enter Location Name ";
            snackbarMessage(context, message);

            return false;
        }
        if ((mobile_no.length() < 10) || (mobile_no.length() > 15)) {
            String message = "Phone length should be 10 to 15 digits ";
            snackbarMessage(context, message);

            return false;
        }
        if (!(mobile_no.trim().matches(phonepattern))) {
            String message = "Please enter a valid phone number";
            snackbarMessage(context, message);

            return false;
        } else
            return true;
    }

    public boolean Plan_a_trip_Vaildate(Context context, String wher_location, String from_location, String start_date_str, String end_date_str, String guest, String budget, String description, String transport_str) {

 if(from_location.equals(""))
        {
            String message  = "Please Enter From Location ";
//			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            snackbarMessage(context, message);
            return false;

        }
        else if(wher_location.equals(""))
        {
            String message  = "Please Enter Where To ";
//			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            snackbarMessage(context, message);
            return false;
        }
        else if(start_date_str.equals(""))
        {
            String message  = "Please Select start date ";
//			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            snackbarMessage(context, message);
            return false;
        }
        else if(end_date_str.equals(""))
        {
            String message  = "Please select end date ";
//			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            snackbarMessage(context, message);
            return false;
        }

        else if(guest.equals("0"))
        {
            String message  = "Please fill the Minimum 1 person not accepted";
//			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            //showMessageOKCancel(context, message);
            return false;

        }

        else if(transport_str.equals(""))
        {
            String message  = "Please fill the transport type.";
//			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            snackbarMessage(context, message);
//			showMessageOKCancel(context, message);
            return false;

        }

        else
            return true;


    }

    public boolean editprofilevalidate(Context context, String name, String address, String phoneNo,  String date_of_birth) {

        if (name.length() == 0  && address.length() == 0 && phoneNo.length() == 0 && date_of_birth.length()==0) {
            String message = "Please fill all fields";
            snackbarMessage(context, message);
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (name.length() == 0) {
            String message = "Please enter name field";
            snackbarMessage(context, message);
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            return false;

        } else if (!match(name)) {

            String message = "Enter characters only in  name field ";
            snackbarMessage(context, message);
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            //showMessageOKCancel(context, message);
            return false;
        }

        if (address.length() == 0) {
            String message = "Please enter name field";
            snackbarMessage(context, message);
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            return false;

        }

        if(date_of_birth.length()==0)
        {
            String message  = "Please select Date";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            return false;
        }
        if ((phoneNo.length() < 10) || (phoneNo.length() > 15)) {
            String message = "Phone length should be 10 to 15 digits ";
            snackbarMessage(context, message);

            return false;
        }
        if (!(phoneNo.trim().matches(phonepattern))) {
            String message = "Please enter a valid phone number";
            snackbarMessage(context, message);

            return false;
        } else
            return true;
    }

    private boolean isValidMobile(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() < 6 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;
              //  txtPhone.setError("Not Valid Number");
            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;
    }
}
