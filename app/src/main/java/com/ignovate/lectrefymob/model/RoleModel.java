package com.ignovate.lectrefymob.model;

import com.google.gson.annotations.SerializedName;

public class RoleModel {

    @SerializedName("roleId")
    public String roleId;

    @SerializedName("roleName")
    public String roleName;

    @SerializedName("createdOn")
    public String createdOn;

    @SerializedName("createdBy")
    public String createdBy;

    @SerializedName("deletedOn")
    public String deletedOn;


    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(String deletedOn) {
        this.deletedOn = deletedOn;
    }
}
