package com.example.application.data.entity.User;

import com.example.application.data.entity.HttpHelper;

import java.net.http.HttpResponse;
import java.util.ArrayList;

public class Receptionist extends User{

    private String testingSiteId;
    private ArrayList<String> notifications;

    // Constructor with additional info
    public Receptionist(String user_id, String userGivenName, String userFamilyName, String userName, String userPhoneNumber, String testingSiteId, ArrayList<String> notifications) {
        super(user_id, userGivenName, userFamilyName, userName, userPhoneNumber);
        this.testingSiteId = testingSiteId;
        this.notifications = notifications;
    }

    public String getTestingSiteId() {
        return testingSiteId;
    }

    public ArrayList<String> getNotifications() {
        return notifications;
    }

    public void addNotifications(String notification) {
        this.notifications.add(notification);
    }
    
    // Prints out Receptionist's attributes in JSON formatted String
    @Override
    public String toString() {
        return "User{\n" +
                "id='" + this.getId() + '\'' +
                ", \ngivenName='" + this.getGivenName() + '\'' +
                ", \nfamilyName='" + this.getFamilyName() + '\'' +
                ", \nuserName='" + this.getUserName() + '\'' +
                ", \nphoneNumber='" + this.getPhoneNumber() + '\'' +
                ", \ntestingSiteId='" + this.getTestingSiteId() + '\'' +
                ", \nnotifications=" + this.getNotifications() +
                "\n}";
    }
}
