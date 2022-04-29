package com.example.application.data.entity.User;

public abstract class User {
    // User ID
    private String id;

    // User first name
    private String givenName;

    // User family name
    private String familyName;

    // User username
    private String userName;

    // User phone no
    private String phoneNumber;

    // User additional information
    private String additionalInfo;

    // Constructor with all parameters
    public User(String user_id, String userGivenName, String userFamilyName, String userName, String userPhoneNumber, String userAdditionalInfo) {
        this.id = user_id;
        this.givenName = userGivenName;
        this.familyName = userFamilyName;
        this.userName = userName;
        this.phoneNumber = userPhoneNumber;
        this.additionalInfo = userAdditionalInfo;
    }

    // Constructor without additionalInfo
    public User(String user_id, String userGivenName, String userFamilyName, String userName, String userPhoneNumber) {
        this.id = user_id;
        this.givenName = userGivenName;
        this.familyName = userFamilyName;
        this.userName = userName;
        this.phoneNumber = userPhoneNumber;
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

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    // Prints out User's attributes in JSON formatted String
    @Override
    public String toString() {
        return "User{\n" +
                "id='" + id + '\'' +
                ", \ngivenName='" + givenName + '\'' +
                ", \nfamilyName='" + familyName + '\'' +
                ", \nuserName='" + userName + '\'' +
                ", \nphoneNumber='" + phoneNumber + '\'' +
                ", \nadditionalInfo='" + additionalInfo + '\'' +
                "\n}";
    }
}
