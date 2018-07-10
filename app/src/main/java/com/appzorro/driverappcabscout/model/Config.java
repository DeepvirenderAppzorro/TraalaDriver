package com.appzorro.driverappcabscout.model;

import android.location.Location;


public class Config {
    public static String BaseURL="http://traala.com/cabscout/driver_api.php?";
    static final  String base_url = "http://traala.com/cabscout/driver_api.php?action=";
    static  final String login_url =base_url+"driver_login&device_type=A";
    static  final String signUp_url = base_url+"driver_register";
    public  static String event_message="";
    public static String notificationkey="";
    public static String rideRequestid="";
    public static Location currentLAT;


    public static final String companyaliasurl ="http://traala.com/cabscout/driver_api.php?action=get_cab";
    public static final  String simplesignupurl ="http://traala.com/cabscout/driver_api.php?action=driver_register";
    static final String verify_email ="";
    public static final String facebook_login_verify_url = base_url+"driverDetailFacebookId&facebook_id=";
    public static final String cab_companies_url = base_url + "company_list";
    public static String nearestroadurl = "https://roads.googleapis.com/v1/nearestRoads?points=";


    public static final String userdetail_url = "http://traala.com/cabscout/driver_api.php?action=user_detail&driver_id=";
    public static final String baserurl_image="http://traala.com/cabscout/profile_pics/";
    public static final String customerrequesturl="http://traala.com/cabscout/driver_api.php?action=get_customer_list&driver_id=";
    //public static final String customerrequesturl="http://traala.com/cabscout/customer_api.php?action=request_driver&customer_id=27&pickup_location=Spencer%27s+Warehouse+%28RPG%29%2C+Phase+8B%2C+Phase+7%2C+Industrial+Area%2C+Sector+73%2C+Sahibzada+Ajit+Singh+Nagar%2C+Punjab+140308%2C+India%2C+Sector+73&drop_location=Ivy+Hospital&vehicle_type=0&pickup_cordinates=30.711692913757425,76.69615276157856&drop_cordinates=30.7064593,76.7081365&request_type=0&date=16%2F010%2F2017&time=07%3A25PM&payment_type=0";public static final String customerrequesturl="http://traala.com/cabscout/driver_api.php?action=get_customer_list&driver_id=";
    public static final String acceptrequestbydriver="http://traala.com/cabscout/driver_api.php?action=requestAcceptedByDriver&driver_id=";
    public static final String laterbookingurl="http://traala.com/cabscout/driver_api.php?action=myBooking&driver_id=";

    public static final String changepasswordurl ="http://traala.com/cabscout/driver_api.php?action=reset_password&driver_id=";
    public static final String updateprofileurl="http://traala.com/cabscout/driver_api.php?action=update_profile";
    public static final String reviewcheckurl="http://traala.com/cabscout/driver_api.php?action=checkReview&driver_id=";

    public static final String driveravilablityurl=" http://traala.com/cabscout/driver_api.php?action=driverStatus&driver_id=";
    public static final String ratingtocustomerurl ="http://traala.com/cabscout/driver_api.php?action=driverFeedback&driver_id=";
    public static final String changecompanyurl ="http://traala.com/cabscout/driver_api.php?action=updateCabCompany&driver_id=4&cab_alias=";

    public static final String arrivedurl="http://traala.com/cabscout/driver_api.php?action=driverArrived&driver_id=";
    public static final String starttripsurl="http://traala.com/cabscout/driver_api.php?action=tripStarted&driver_id=";
    public static final String canceltripsurl="http://traala.com/cabscout/driver_api.php?action=tripCancel&driver_id=";

    public static final String stopurl="http://traala.com/cabscout/driver_api.php?action=tripStopped&driver_id=";
    public static final String collectcashurl="http://traala.com/cabscout/driver_api.php?action=collectCash&driver_id=";
    public static final String triphistoryurl=" http://traala.com/cabscout/driver_api.php?action=tripHistory&driver_id=";

    public static final String locationsendurl= "http://traala.com/cabscout/driver_api.php?action=getdriversLocation";

    public static final String sendAmountCard ="http://traala.com/cabscout/driver_api.php?action=collectCash&driver_id=3&ride_request_id=299&cash=50.0";

}
