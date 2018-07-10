package com.appzorro.driverappcabscout.view;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 15/6/18.
 */

class ModelTrip {
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

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("message")
        @Expose
        private String message;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }
}
