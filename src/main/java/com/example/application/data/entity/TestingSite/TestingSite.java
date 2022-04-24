package com.example.application.data.entity.TestingSite;

public class TestingSite {
    private String id;
    private String name;
    private String description;
    private String websiteUrl;
    private String phoneNumber;
    private Address address;
    private String additionalInfo;

    public TestingSite(String id, String name, String description, String websiteUrl, String phoneNumber, double latitude, double longitude, int unitNumber, String street, String street2, String suburb, String state, String postcode, String additionalInfo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.websiteUrl = websiteUrl;
        this.phoneNumber = phoneNumber;
        this.address = new Address(latitude, longitude, unitNumber, street, street2, suburb, state, postcode);
        this.additionalInfo = additionalInfo;
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

    public String getAddress() {
        return address.toString();
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }
}