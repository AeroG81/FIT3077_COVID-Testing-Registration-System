package com.example.application.data.entity.Booking;

import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.User.User;

/**
 * This is the children OnSiteTesting class extending Booking abstract class
 */
public class OnSiteTesting extends Booking {
    private TestingSite testingSite;

    /**
     * Constructor for OnSiteTesting
     * @param site site of the booking
     * @param startTime booking appointment time
     * @param user customer for the booking
     * @param notes notes provided for the booking
     */
    public OnSiteTesting(TestingSite site, String startTime, User user, String notes) {
        super(startTime, user, notes);
        this.testingSite = site;
    }

    /**
     * Constructor for OnSiteTesting
     * @param bookingId booking ID
     * @param site site of the booking
     * @param startTime booking appointment time
     * @param user customer for the booking
     * @param notes  notes provided for the booking
     * @param status status of the booking
     * @param smsPin PIN code to verify the booking
     * @param qrcode QR code to verify the booking
     */
    public OnSiteTesting(String bookingId, TestingSite site, String startTime, User user, String notes, String status, String smsPin, String qrcode) {
        super(bookingId, startTime, user, notes, status, smsPin, qrcode);
        this.testingSite = site;
    }

    /**
     * Getter of the testingSite
     * @return testing site
     */
    public TestingSite getTestingSite() {
        return testingSite;
    }

    /**
     * Getter for Additional Info of booking in JSON String format
     * @return QR code
     */
    public String getAdditionalInfo(){
        return "{"+
                "\n\"qrcode\":\"" + super.getQrcode() + "\"" +
                "}";
    }

    /**
     * toString method
     * @return String form of the class
     */
    @Override
    public String toString() {
        return "OnSiteTesting {\n" +
                super.toString() +
                ", \nqrcode='" + super.getQrcode() + '\'' +
                ",\ntestingSite=" + testingSite.getInfo() +
                "\n}";
    }
}
