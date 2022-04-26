package com.example.application.data.entity.User;

public abstract class User {
    private String id;
    private String givenName;
    private String familyName;
    private String userName;
    private String phoneNumber;
    private Boolean isCustomer;
    private Boolean isReceptionist;
    private Boolean isHealthcareWorker;
    private String additionalInfo;

    // Constructor with all parameters
    public User(String user_id, String userGivenName, String userFamilyName, String userName, String userPhoneNumber, Boolean isCustomer, Boolean isReceptionist, Boolean isHealthcareWorker, String userAdditionalInfo) {
        this.id = user_id;
        this.givenName = userGivenName;
        this.familyName = userFamilyName;
        this.userName = userName;
        this.phoneNumber = userPhoneNumber;
        this.isCustomer = isCustomer;
        this.isReceptionist = isReceptionist;
        this.isHealthcareWorker = isHealthcareWorker;
        this.additionalInfo = userAdditionalInfo;
    }

    // Constructor without additionalInfo
    public User(String user_id, String userGivenName, String userFamilyName, String userName, String userPhoneNumber, Boolean isCustomer, Boolean isReceptionist, Boolean isHealthcareWorker) {
        this.id = user_id;
        this.givenName = userGivenName;
        this.familyName = userFamilyName;
        this.userName = userName;
        this.phoneNumber = userPhoneNumber;
        this.isCustomer = isCustomer;
        this.isReceptionist = isReceptionist;
        this.isHealthcareWorker = isHealthcareWorker;
        this.additionalInfo = null;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getCustomer() {
        return isCustomer;
    }

    public void setCustomer(Boolean customer) {
        isCustomer = customer;
    }

    public Boolean getReceptionist() {
        return isReceptionist;
    }

    public void setReceptionist(Boolean receptionist) {
        isReceptionist = receptionist;
    }

    public Boolean getHealthcareWorker() {
        return isHealthcareWorker;
    }

    public void setHealthcareWorker(Boolean healthcareWorker) {
        isHealthcareWorker = healthcareWorker;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
