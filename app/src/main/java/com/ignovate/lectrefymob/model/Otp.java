package com.ignovate.lectrefymob.model;

import com.google.gson.annotations.SerializedName;

public class Otp {

    @SerializedName("otp")
    public String otp;

    @SerializedName("refNo")
    public String refNo;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }


}
