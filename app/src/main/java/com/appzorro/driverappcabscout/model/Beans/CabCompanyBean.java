package com.appzorro.driverappcabscout.model.Beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CabCompanyBean {

    @SerializedName("response")
    @Expose
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
    public class CompanyList {

        @SerializedName("comapny_id")
        @Expose
        private String comapnyId;
        @SerializedName("company_name")
        @Expose
        private String companyName;

        public String getComapnyId() {
            return comapnyId;
        }

        public void setComapnyId(String comapnyId) {
            this.comapnyId = comapnyId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

    }
    public class Response {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("company_list")
        @Expose
        private List<CompanyList> companyList = null;

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

        public List<CompanyList> getCompanyList() {
            return companyList;
        }

        public void setCompanyList(List<CompanyList> companyList) {
            this.companyList = companyList;
        }

    }
}