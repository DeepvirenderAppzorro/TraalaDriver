package com.appzorro.driverappcabscout.view.Activity.PathmapActivity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vijay on 31/10/18.
 */

public class CustomerListResponse {
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

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("data")
        @Expose
        private List<Datum> data = null;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Datum> getData() {
            return data;
        }

        public void setData(List<Datum> data) {
            this.data = data;
        }

    }
    public class Datum {

        @SerializedName("ride_request_id")
        @Expose
        private String rideRequestId;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("payment_type")
        @Expose
        private String paymentType;
        @SerializedName("pickup_cordinates")
        @Expose
        private String pickupCordinates;
        @SerializedName("pickup_location")
        @Expose
        private String pickupLocation;
        @SerializedName("drop_location")
        @Expose
        private String dropLocation;
        @SerializedName("drop_cordinates")
        @Expose
        private String dropCordinates;
        @SerializedName("customer_id")
        @Expose
        private String customerId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("profile_pic")
        @Expose
        private String profilePic;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("device_token")
        @Expose
        private String deviceToken;
        @SerializedName("vehicle_type")
        @Expose
        private String vehicleType;
        @SerializedName("customer_status")
        @Expose
        private String customerStatus;

        public String getRideRequestId() {
            return rideRequestId;
        }

        public void setRideRequestId(String rideRequestId) {
            this.rideRequestId = rideRequestId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getPickupCordinates() {
            return pickupCordinates;
        }

        public void setPickupCordinates(String pickupCordinates) {
            this.pickupCordinates = pickupCordinates;
        }

        public String getPickupLocation() {
            return pickupLocation;
        }

        public void setPickupLocation(String pickupLocation) {
            this.pickupLocation = pickupLocation;
        }

        public String getDropLocation() {
            return dropLocation;
        }

        public void setDropLocation(String dropLocation) {
            this.dropLocation = dropLocation;
        }

        public String getDropCordinates() {
            return dropCordinates;
        }

        public void setDropCordinates(String dropCordinates) {
            this.dropCordinates = dropCordinates;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
        }

        public String getCustomerStatus() {
            return customerStatus;
        }

        public void setCustomerStatus(String customerStatus) {
            this.customerStatus = customerStatus;
        }

    }
}
