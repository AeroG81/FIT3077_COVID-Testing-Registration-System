package com.example.application.data.entity.User;

import java.util.ArrayList;

public class Receptionist extends User{

    private String testingSiteId;
    private ArrayList<String> notifications;

    // Constructor with additional info
    public Receptionist(String user_id, String userGivenName, String userFamilyName, String userName, String userPhoneNumber, String testingSiteId, ArrayList<String> notifications) {
        super(user_id, userGivenName, userFamilyName, userName, userPhoneNumber);
        System.out.println(notifications);
        this.setTestingSiteId(testingSiteId);
        this.setNotifications(notifications);
    }

    public String getTestingSiteId() {
        return testingSiteId;
    }

    public void setTestingSiteId(String testingSiteId) {
        this.testingSiteId = testingSiteId;
    }

    public ArrayList<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<String> notifications) {
        this.notifications = notifications;
    }

    public void addNotifications(String notification) {
        this.notifications.add(notification);
    }

    public void clearNotifications(){
        this.notifications.clear();
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
