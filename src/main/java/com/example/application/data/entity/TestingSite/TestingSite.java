package com.example.application.data.entity.TestingSite;

public class TestingSite {
    private String id;
    private String name;
    private String description;
    private String websiteUrl;
    private String phoneNumber;
    private Address address;
    private String facilityType;
    private String openTime;
    private String closeTime;
    private String operationTime;
    private String waitingTime;

    public TestingSite(String id, String name, String description, String websiteUrl, String phoneNumber, double latitude, double longitude, int unitNumber, String street, String street2, String suburb, String state, String postcode, String facilityType, String openTime, String closeTime, String waitingTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.websiteUrl = websiteUrl;
        this.phoneNumber = phoneNumber;
        this.address = new Address(latitude, longitude, unitNumber, street, street2, suburb, state, postcode);
        this.facilityType = facilityType;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.operationTime = openTime + " - " + closeTime;
        this.waitingTime = waitingTime;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public String getOperationTime() {
        return openTime + " - " + closeTime;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public String getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(String waitingTime){
        this.waitingTime = waitingTime;
    }

    @Override
    public String toString() {
        return "TestingSite{" +
                "\nid= '" + id + '\'' +
                ", \nname= '" + name + '\'' +
                ", \ndescription= '" + description + '\'' +
                ", \nwebsiteUrl= '" + websiteUrl + '\'' +
                ", \nphoneNumber= '" + phoneNumber + '\'' +
                ", \naddress=" + address.toString() +
                ", \nfacilityType= '" + facilityType + '\'' +
                ", \noperationTime= '" + operationTime + '\'' +
                ", \nwaitingTime= '" + waitingTime + '\'' +
                '}';
    }

    public String getInfo(){
        return "TestingSite{" +
                "\nid= '" + id + '\'' +
                ", \nname= '" + name + '\'' +
                ", \nphoneNumber= '" + phoneNumber + '\'' +
                ", \naddress= '" + address.toString() + '\'' +
                ", \nfacilityType= '" + facilityType + '\'' +
                ", \noperationTime= '" + operationTime + '\'' +
                ", \nwaitingTime= '" + waitingTime + '\'' +
                "\n}";
    }
}