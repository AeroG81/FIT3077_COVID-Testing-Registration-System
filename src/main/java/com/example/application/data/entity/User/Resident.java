package com.example.application.data.entity.User;

public class Resident extends User{
    public Resident(String user_id, String userGivenName, String userFamilyName, String userName, String userPhoneNumber, String userAdditionalInfo) {
        super(user_id, userGivenName, userFamilyName, userName, userPhoneNumber, userAdditionalInfo);
    }

    public Resident(String user_id, String userGivenName, String userFamilyName, String userName, String userPhoneNumber) {
        super(user_id, userGivenName, userFamilyName, userName, userPhoneNumber);
    }
}
