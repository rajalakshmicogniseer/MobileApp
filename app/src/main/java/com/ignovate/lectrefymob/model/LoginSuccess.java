package com.ignovate.lectrefymob.model;

import com.google.gson.annotations.SerializedName;

public class LoginSuccess {

    @SerializedName("authToken")
    public String authToken;

    @SerializedName("code")
    public String code;

    @SerializedName("message")
    public String message;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
