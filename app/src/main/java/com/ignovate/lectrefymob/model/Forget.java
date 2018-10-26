package com.ignovate.lectrefymob.model;

import com.google.gson.annotations.SerializedName;

public class Forget {

    @SerializedName("refNo")
    public String refNo;

    @SerializedName("email")
    public String email;

    @SerializedName("phone")
    public String phone;


    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

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
