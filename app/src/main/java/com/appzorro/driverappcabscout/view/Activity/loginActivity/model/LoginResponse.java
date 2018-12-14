package com.appzorro.driverappcabscout.view.Activity.loginActivity.model;

/**
 * Created by vijay on 29/10/18.
 */

public class LoginResponse {

    /**
     * response : {"id":"23","message":"true","profile_pic":"img_1542086780.jpg","username":"s","company_id":"1","socket_id":"7"}
     */

    private ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean {
        /**
         * id : 23
         * message : true
         * profile_pic : img_1542086780.jpg
         * username : s
         * company_id : 1
         * socket_id : 7
         */

        private String id;
        private String message;
        private String profile_pic;
        private String username;
        private String company_id;
        private String socket_id;

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

        public String getProfile_pic() {
            return profile_pic;
        }

        public void setProfile_pic(String profile_pic) {
            this.profile_pic = profile_pic;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }

        public String getSocket_id() {
            return socket_id;
        }

        public void setSocket_id(String socket_id) {
            this.socket_id = socket_id;
        }
    }
}
