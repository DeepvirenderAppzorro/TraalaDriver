package com.appzorro.driverappcabscout.controller;

public class ModelManager {
    private CabCompaniesManager cabCompaniesManager;
    private RegistrationManager registrationManager;
    private LoginManager loginManager;
    private FacebookLoginManager facebookLoginManager;
    private UserDetailManager userDetailManager;
    private static ModelManager modelManager;
    private CustomerReQuestManager customerReQuestManager;
    private AcceptCustomerRequest acceptCustomerRequest;
    private DailyEarning dailyEarning;
    private LaterBookingManager laterBookingManager;
    private ChangepasswordManager changepasswordManager;
    private UpdateProfileManager updateProfileManager;
    private ChangeComapnyManager changeComapnyManager;
    private ReviewManager reviewManager;
    private OnlineOfflineManager onlineOfflineManager;
    private ArrivedManager arrivedManager;
    private StartTripsManager startTripsManager;
    private StopRideManager stopRideManager;
    private CancelTripManager cancelTripManager;
    private CollectCashmanager collectCashmanager;
    private RatingManager ratingManager;
    private Tripsmanager tripsmanager;
    private FareDetailManager fareManager;
    private GetCityManager getCityManager;
    private LocationSendManager locationSendManager;
    private NearestRoadManager nearestRoadManager;
    private UpdateProfilePicManager updateProfilePicManager;
    private CollectAmtManager collectAmtManager;
    private CustomerDetail customerRideStatus;


    private ModelManager() {
        getCityManager = new GetCityManager();
        collectAmtManager = new CollectAmtManager();
        updateProfilePicManager = new UpdateProfilePicManager();
        cabCompaniesManager = new CabCompaniesManager();
        registrationManager = new RegistrationManager();
        loginManager = new LoginManager();
        facebookLoginManager = new FacebookLoginManager();
        userDetailManager = new UserDetailManager();
        customerReQuestManager = new CustomerReQuestManager();
        acceptCustomerRequest = new AcceptCustomerRequest();
        laterBookingManager = new LaterBookingManager();
        changepasswordManager = new ChangepasswordManager();
        updateProfileManager = new UpdateProfileManager();
        changeComapnyManager = new ChangeComapnyManager();
        reviewManager = new ReviewManager();
        onlineOfflineManager = new OnlineOfflineManager();
        arrivedManager = new ArrivedManager();
        startTripsManager = new StartTripsManager();
        stopRideManager = new StopRideManager();
        cancelTripManager = new CancelTripManager();
        collectCashmanager = new CollectCashmanager();
        ratingManager = new RatingManager();
        tripsmanager = new Tripsmanager();
        fareManager = new FareDetailManager();
        locationSendManager = new LocationSendManager();
        nearestRoadManager = new NearestRoadManager();
        customerRideStatus = new CustomerDetail();
        dailyEarning=new DailyEarning();


    }

    public static ModelManager getInstance() {
        if (modelManager == null)
            return modelManager = new ModelManager();
        else
            return modelManager;
    }

    public StopRideManager getStopRideManager() {

        return stopRideManager;
    }

    public NearestRoadManager getNearestRoadManager() {

        return nearestRoadManager;
    }
    public DailyEarning getDailyEarning() {

        return dailyEarning;
    }


    public LocationSendManager getLocationSendManager() {
        return locationSendManager;
    }
    public CustomerDetail getCustomerRideStatus() {
        return customerRideStatus;
    }

    public GetCityManager getCityManagerr() {
        return getCityManager;
    }

    public UpdateProfilePicManager updateProfilepicManager() {
        return updateProfilePicManager;
    }

    public CollectAmtManager getAmt() {
        return collectAmtManager;
    }


    public Tripsmanager getTripsmanager() {
        return tripsmanager;
    }

    public OnlineOfflineManager getOnlineOfflineManager() {
        return onlineOfflineManager;
    }

    public ArrivedManager getArrivedManager() {

        return arrivedManager;
    }

    public StartTripsManager getStartTripsManager() {
        return startTripsManager;
    }

    public CancelTripManager getCancelTripManager() {
        return cancelTripManager;
    }

    public CollectCashmanager getCollectCashmanager() {
        return collectCashmanager;
    }

    public RatingManager getRatingManager() {
        return ratingManager;
    }

    public ReviewManager getReviewManager() {
        return reviewManager;
    }

    public LaterBookingManager getLaterBookingManager() {
        return laterBookingManager;
    }

    public ChangepasswordManager getChangepasswordManager() {
        return changepasswordManager;
    }

    public UpdateProfileManager getUpdateProfileManager() {
        return updateProfileManager;
    }

    public ChangeComapnyManager getChangeComapnyManager() {
        return changeComapnyManager;
    }

    public AcceptCustomerRequest getAcceptCustomerRequest() {
        return acceptCustomerRequest;
    }

    public UserDetailManager getUserDetailManager() {
        return userDetailManager;
    }

    public CustomerReQuestManager getCustomerReQuestManager() {
        return customerReQuestManager;
    }

    public FacebookLoginManager getFacebookLoginManager() {
        return facebookLoginManager;
    }

    public CabCompaniesManager getCabCompaniesManager() {
        return cabCompaniesManager;
    }

    public RegistrationManager getRegistrationManager() {
        return registrationManager;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public FareDetailManager getFareDetailManager() {
        return fareManager;
    }


}