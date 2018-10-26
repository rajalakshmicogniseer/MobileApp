package com.ignovate.lectrefymob.model;


import com.google.gson.annotations.SerializedName;

public class Forgot_passwordmodel {
    @SerializedName("email")
    public String email;

    @SerializedName("phone")
    public String phone;



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {

        this.phone = phone;
    }

}

