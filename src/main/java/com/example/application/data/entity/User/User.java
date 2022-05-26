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

    // Getters for user id
    public String getId() {
        return id;
    }

    // Getters for user givenName
    public String getGivenName() {
        return givenName;
    }

    // Getters for user familyName
    public String getFamilyName() {
        return familyName;
    }

    // Getters for user userName
    public String getUserName() {
        return userName;
    }

    // Getters for user phoneNumber
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Getters for user additionalInfo
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public abstract void update();

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
