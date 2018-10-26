package com.ignovate.lectrefymob.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegisterReqModel implements Serializable {

    @SerializedName("userId")
    public String userId;

    @SerializedName("firstName")
    public String firstName;

    @SerializedName("lastName")
    public String lastName;

    @SerializedName("email")
    public String email;

    @SerializedName("title")
    public String title;

    @SerializedName("supervisor")
    public String supervisor;

    @SerializedName("region")
    public String region;

    @SerializedName("baseSalary")
    public String baseSalary;

    @SerializedName("contract")
    public String contract;

    @SerializedName("workingDays")
    public String workingDays;

    @SerializedName("workHoursStart")
    public String workHoursStart;

    @SerializedName("workHoursEnd")
    public String workHoursEnd;

    @SerializedName("profileImagePath")
    public String profileImagePath;

    @SerializedName("createdBy")
    public String createdBy;

    @SerializedName("createdOn")
    public String createdOn;

    @SerializedName("modifiedBy")
    public String modifiedBy;

    @SerializedName("modifiedOn")
    public String modifiedOn;

    @SerializedName("password")
    public String password;

    @SerializedName("pin")
    public String pin;

    @SerializedName("salesRepOverallRank")
    public String salesRepOverallRank;

    @SerializedName("salesRepRegionalRank")
    public String salesRepRegionalRank;

    @SerializedName("positionLatitude")
    public String positionLatitude;

    @SerializedName("positionLongitude")
    public String positionLongitude;

    @SerializedName("age")
    public String age;

    @SerializedName("gender")
    public String gender;

    @SerializedName("ethinicity")
    public String ethinicity;

    @SerializedName("middleName")
    public String middleName;

    @SerializedName("phone")
    public String phone;

    @SerializedName("status")
    public String status;

    @SerializedName("fcm")
    public String fcm;

    @SerializedName("dateOfBirth")
    public String dateOfBirth;

    @SerializedName("reasonOfReject")
    public String reasonOfReject;

    @SerializedName("role")
    public String role;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(String baseSalary) {
        this.baseSalary = baseSalary;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    public String getWorkHoursStart() {
        return workHoursStart;
    }

    public void setWorkHoursStart(String workHoursStart) {
        this.workHoursStart = workHoursStart;
    }

    public String getWorkHoursEnd() {
        return workHoursEnd;
    }

    public void setWorkHoursEnd(String workHoursEnd) {
        this.workHoursEnd = workHoursEnd;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getSalesRepOverallRank() {
        return salesRepOverallRank;
    }

    public void setSalesRepOverallRank(String salesRepOverallRank) {
        this.salesRepOverallRank = salesRepOverallRank;
    }

    public String getSalesRepRegionalRank() {
        return salesRepRegionalRank;
    }

    public void setSalesRepRegionalRank(String salesRepRegionalRank) {
        this.salesRepRegionalRank = salesRepRegionalRank;
    }

    public String getPositionLatitude() {
        return positionLatitude;
    }

    public void setPositionLatitude(String positionLatitude) {
        this.positionLatitude = positionLatitude;
    }

    public String getPositionLongitude() {
        return positionLongitude;
    }

    public void setPositionLongitude(String positionLongitude) {
        this.positionLongitude = positionLongitude;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEthinicity() {
        return ethinicity;
    }

    public void setEthinicity(String ethinicity) {
        this.ethinicity = ethinicity;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFcm() {
        return fcm;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getReasonOfReject() {
        return reasonOfReject;
    }

    public void setReasonOfReject(String reasonOfReject) {
        this.reasonOfReject = reasonOfReject;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
