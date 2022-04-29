package com.example.application.data.entity.Booking;

import com.example.application.data.entity.Meeting.Zoom;
import com.example.application.data.entity.User.User;

/**
 * This is the children OnlineTesting class extending Booking abstract class
 */
public class OnlineTesting extends Booking {
    private String url;

    /**
     * Constructor for OnlineTesting
     * @param startTime booking appointment time
     * @param user customer for the booking
     * @param notes notes provided for the booking
     */
    public OnlineTesting(String startTime, User user, String notes) {
        super(startTime, user, notes);
        generateUrl();
    }

    /**
     * Constructor for OnlineTesting
     * @param bookingId booking ID
     * @param startTime booking appointment time
     * @param user customer for the booking
     * @param notes  notes provided for the booking
     * @param status status of the booking
     * @param smsPin PIN code to verify the booking
     * @param qrcode QR code to verify the booking
     * @param url url for the meeting with experts
     */
    public OnlineTesting(String bookingId, String startTime, User user, String notes, String status, String smsPin, String qrcode, String url) {
        super(bookingId, startTime, user, notes, status, smsPin, qrcode);
        this.url = url;
    }

    /**
     * Method to generate a URL for meeting
     */
    private void generateUrl(){
        url = new Zoom().generateUrl(super.getCustomer().getId()+"-"+super.getStartTime());
    }

    /**
     * Getter for Additional Info of booking in JSON String format
     * @return QR code
     */
    public String getAdditionalInfo(){
        return "{"+
                "\n\"qrcode\":\"" + super.getQrcode() + "\"," +
                "\n\"url\":\"" + this.getUrl() + "\"" +
                "}";
    }

    /**
     * Getter of the url
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * toString method
     * @return String form of the class
     */
    @Override
    public String toString() {
        return "OnlineTesting{\n" +
                super.toString() +
                ", \nqrcode='" + super.getQrcode() + '\'' +
                ", \nurl='" + url + '\'' +
                "\n}";
    }
}
