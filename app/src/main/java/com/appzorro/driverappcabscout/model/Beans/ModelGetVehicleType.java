package com.appzorro.driverappcabscout.model.Beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vijay on 17/7/18.
 */

public class ModelGetVehicleType {
    @SerializedName("response")
    @Expose
    private List<Response> response = null;

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

    public class Response {

        @SerializedName("company_id")
        @Expose
        private String companyId;
        @SerializedName("vehicles_type")
        @Expose
        private Object vehiclesType;

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public Object getVehiclesType() {
            return vehiclesType;
        }

        public void setVehiclesType(Object vehiclesType) {
            this.vehiclesType = vehiclesType;
        }


    }
}
