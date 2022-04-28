package com.example.application.data.entity.Booking;

import com.example.application.data.entity.Meeting.Zoom;
import com.example.application.data.entity.User.User;

public class OnlineTesting extends Booking {
    private String url;
    public OnlineTesting(String startTime, User user, String notes) {
        super(startTime, user, notes);
        generateUrl();
    }

    public OnlineTesting(String bookingId, String startTime, User user, String status, String smsPin, String qrcode, String url) {
        super(bookingId, startTime, user, status, smsPin, qrcode);
        this.url = url;
    }

    public OnlineTesting(String bookingId, String startTime, User user, String notes, String status, String smsPin, String qrcode, String url) {
        super(bookingId, startTime, user, notes, status, smsPin, qrcode);
        this.url = url;
    }

    private void generateUrl(){
        url = new Zoom().generateUrl(super.getCustomer().getId()+"-"+super.getStartTime());
    }

    public String getAdditionalInfo(){
        return "{"+
                "\n\"qrcode\":\"" + super.getQrcode() + "\"," +
                "\n\"url\":\"" + this.getUrl() + "\"" +
                "}";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "OnlineTesting{" +
                super.toString() +
                ", \nqrcode='" + super.getQrcode() + '\'' +
                ", \nurl='" + url + '\'' +
                "\n}";
    }
}
