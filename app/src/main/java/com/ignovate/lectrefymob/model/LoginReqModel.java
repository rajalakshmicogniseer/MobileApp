package com.ignovate.lectrefymob.model;

import com.google.gson.annotations.SerializedName;

public class LoginReqModel {
    @SerializedName("email")
    public String login_id;

    @SerializedName("password")
    public String password;





    public String getlogin_id() {
        return login_id;
    }

    public void setlogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getpassword() {
        return password;
    }

    public void setpassword(String password) {
        this.password = password;
    }


}
