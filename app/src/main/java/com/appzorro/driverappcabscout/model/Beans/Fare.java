package com.appzorro.driverappcabscout.model.Beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 18/7/18.
 */

public class Fare {
    @SerializedName("fare")
    @Expose
    private Double fare;
    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }
    public class ModelCash23 {

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

            @SerializedName("message")
            @Expose
            private String message;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("fare")
            @Expose
            private Fare fare;

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public Fare getFare() {
                return fare;
            }

            public void setFare(Fare fare) {
                this.fare = fare;
            }
        }
    }
}
