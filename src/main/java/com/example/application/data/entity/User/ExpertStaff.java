package com.example.application.data.entity.User;

public class ExpertStaff extends User{

    public ExpertStaff(){
        super("ac894ada-2a4a-4ad8-9683-8ec17b41d676", "Harry", "Williams", "HW", "0490000009");
    }

    public ExpertStaff(String user_id, String userGivenName, String userFamilyName, String userName, String userPhoneNumber, String userAdditionalInfo) {
        super(user_id, userGivenName, userFamilyName, userName, userPhoneNumber, userAdditionalInfo);
    }

    public ExpertStaff(String user_id, String userGivenName, String userFamilyName, String userName, String userPhoneNumber) {
        super(user_id, userGivenName, userFamilyName, userName, userPhoneNumber);
    }
}
