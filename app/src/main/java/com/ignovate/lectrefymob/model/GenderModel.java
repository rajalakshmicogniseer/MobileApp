package com.ignovate.lectrefymob.model;

import com.google.gson.annotations.SerializedName;


public class GenderModel {

    @SerializedName("codeId")
    public String codeId;

    @SerializedName("codeType")
    public String codeType;

    @SerializedName("codeName")
    public String codeName;


    public GenderModel() {

    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
}
