package com.example.application.data.entity.User;

public class Resident extends User{
    public Resident(){
        super("ac894ada-2a4a-4ad8-9683-8ec17b41d676", "Harry", "Williams", "HW", "0490000009");
    }

    public Resident(String user_id, String userGivenName, String userFamilyName, String userName, String userPhoneNumber, String userAdditionalInfo) {
        super(user_id, userGivenName, userFamilyName, userName, userPhoneNumber, userAdditionalInfo);
    }

    public Resident(String user_id, String userGivenName, String userFamilyName, String userName, String userPhoneNumber) {
        super(user_id, userGivenName, userFamilyName, userName, userPhoneNumber);
    }
}
