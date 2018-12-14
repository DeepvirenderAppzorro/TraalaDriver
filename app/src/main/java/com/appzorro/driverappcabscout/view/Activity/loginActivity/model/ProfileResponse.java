package com.appzorro.driverappcabscout.view.Activity.loginActivity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 30/10/18.
 */

public class ProfileResponse {
    @SerializedName("response")
    @Expose
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
    public class Response {

        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("profile_pic")
        @Expose
        private String profilePic;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("country")
        @Expose
        private Object country;
        @SerializedName("state")
        @Expose
        private Object state;
        @SerializedName("city")
        @Expose
        private Object city;
        @SerializedName("driver_license")
        @Expose
        private String driverLicense;
        @SerializedName("image")
        @Expose
        private Object image;
        @SerializedName("license_validity")
        @Expose
        private Object licenseValidity;
        @SerializedName("location_name")
        @Expose
        private String locationName;
        @SerializedName("zip_code")
        @Expose
        private String zipCode;
        @SerializedName("location_id")
        @Expose
        private String locationId;
        @SerializedName("adhar_no")
        @Expose
        private String adharNo;
        @SerializedName("adhaar_front")
        @Expose
        private String adhaarFront;
        @SerializedName("adhaar_back")
        @Expose
        private String adhaarBack;
        @SerializedName("device_type")
        @Expose
        private String deviceType;
        @SerializedName("device_token")
        @Expose
        private String deviceToken;
        @SerializedName("driver_id")
        @Expose
        private String driverId;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getCountry() {
            return country;
        }

        public void setCountry(Object country) {
            this.country = country;
        }

        public Object getState() {
            return state;
        }

        public void setState(Object state) {
            this.state = state;
        }

        public Object getCity() {
            return city;
        }

        public void setCity(Object city) {
            this.city = city;
        }

        public String getDriverLicense() {
            return driverLicense;
        }

        public void setDriverLicense(String driverLicense) {
            this.driverLicense = driverLicense;
        }

        public Object getImage() {
            return image;
        }

        public void setImage(Object image) {
            this.image = image;
        }

        public Object getLicenseValidity() {
            return licenseValidity;
        }

        public void setLicenseValidity(Object licenseValidity) {
            this.licenseValidity = licenseValidity;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        public String getAdharNo() {
            return adharNo;
        }

        public void setAdharNo(String adharNo) {
            this.adharNo = adharNo;
        }

        public String getAdhaarFront() {
            return adhaarFront;
        }

        public void setAdhaarFront(String adhaarFront) {
            this.adhaarFront = adhaarFront;
        }

        public String getAdhaarBack() {
            return adhaarBack;
        }

        public void setAdhaarBack(String adhaarBack) {
            this.adhaarBack = adhaarBack;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public String getDriverId() {
            return driverId;
        }

        public void setDriverId(String driverId) {
            this.driverId = driverId;
        }

    }
    }
