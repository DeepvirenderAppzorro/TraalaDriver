package com.appzorro.driverappcabscout.model.Beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 19/7/18.
 */

public class CustomerDetailBean {

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
        @SerializedName("detail")
        @Expose
        private Detail detail;

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

        public Detail getDetail() {
            return detail;
        }

        public void setDetail(Detail detail) {
            this.detail = detail;
        }

    }
    public class Detail {

        @SerializedName("ride_request_id")
        @Expose
        private String rideRequestId;
        @SerializedName("payment_type")
        @Expose
        private String paymentType;
        @SerializedName("pickup_cordinates")
        @Expose
        private String pickupCordinates;
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
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("vehicle_type")
        @Expose
        private String vehicleType;
        @SerializedName("pickup_location")
        @Expose
        private String pickupLocation;
        @SerializedName("device_token")
        @Expose
        private String deviceToken;
        @SerializedName("device_type")
        @Expose
        private String deviceType;
        @SerializedName("customer_status")
        @Expose
        private String customerStatus;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("driver_id")
        @Expose
        private String driverId;
        @SerializedName("distance")
        @Expose
        private String distance;

        public String getRideRequestId() {
            return rideRequestId;
        }

        public void setRideRequestId(String rideRequestId) {
            this.rideRequestId = rideRequestId;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
        }

        public String getPickupLocation() {
            return pickupLocation;
        }

        public void setPickupLocation(String pickupLocation) {
            this.pickupLocation = pickupLocation;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getCustomerStatus() {
            return customerStatus;
        }

        public void setCustomerStatus(String customerStatus) {
            this.customerStatus = customerStatus;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDriverId() {
            return driverId;
        }

        public void setDriverId(String driverId) {
            this.driverId = driverId;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

    }

  /*  @SerializedName("response")
    @Expose
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public class Detail {

        @SerializedName("ride_request_id")
        @Expose
        private String rideRequestId;
        @SerializedName("payment_type")
        @Expose
        private String paymentType;
        @SerializedName("pickup_cordinates")
        @Expose
        private String pickupCordinates;
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
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("vehicle_type")
        @Expose
        private String vehicleType;
        @SerializedName("pickup_location")
        @Expose
        private String pickupLocation;
        @SerializedName("device_token")
        @Expose
        private String deviceToken;
        @SerializedName("device_type")
        @Expose
        private String deviceType;
        @SerializedName("customer_status")
        @Expose
        private String customerDetail;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("driver_id")
        @Expose
        private String driverId;
        @SerializedName("distance")
        @Expose
        private String distance;

        public String getRideRequestId() {
            return rideRequestId;
        }

        public void setRideRequestId(String rideRequestId) {
            this.rideRequestId = rideRequestId;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
        }

        public String getPickupLocation() {
            return pickupLocation;
        }

        public void setPickupLocation(String pickupLocation) {
            this.pickupLocation = pickupLocation;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getCustomerStatus() {
            return customerDetail;
        }

        public void setCustomerStatus(String customerDetail) {
            this.customerDetail = customerDetail;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDriverId() {
            return driverId;
        }

        public void setDriverId(String driverId) {
            this.driverId = driverId;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

    }

    public class Response {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("detail")
        @Expose
        private Detail detail;

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

        public Detail getDetail() {
            return detail;
        }

        public void setDetail(Detail detail) {
            this.detail = detail;
        }

    }*/
}