package com.example.application.data.entity.TestingSite;

public class Address {
    private double latitude;
    private double longitude;
    private int unitNumber;
    private String street;
    private String street2;
    private String suburb;
    private String state;
    private String postcode;

    public Address(double latitude, double longitude, int unitNumber, String street, String street2, String suburb, String state, String postcode) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.unitNumber = unitNumber;
        this.street = street;
        this.street2 = street2;
        this.suburb = suburb;
        this.state = state;
        this.postcode = postcode;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getUnitNumber() {
        return unitNumber;
    }

    public String getStreet() {
        return street;
    }

    public String getStreet2() {
        return street2;
    }

    public String getSuburb() {
        return suburb;
    }

    public String getState() {
        return state;
    }

    public String getPostcode() {
        return postcode;
    }

    @Override
    public String toString() {
        if (street2 == "null"){
            return street + ", " +
                    postcode + ", " +
                    suburb + ", " +
                    state;
        }
        else {
            return street + " "+ street2 + ", " +
                    postcode + ", " +
                    suburb + ", " +
                    state;
        }
    }
}
