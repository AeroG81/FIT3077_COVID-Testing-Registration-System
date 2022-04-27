package com.example.application.data.entity.Booking;

import com.example.application.data.entity.User.User;

public class OnlineTesting extends Booking {
    private String qrcode;
    private String url;
    public OnlineTesting(String startTime, User user, String notes) {
        super(startTime, user, notes);
    }

    public OnlineTesting(String startTime, User user, String status, String smsPin, String additionalInfo) {
        super(startTime, user, status, smsPin, additionalInfo);
    }

    public OnlineTesting(String bookingId, String startTime, User user, String status, String smsPin, String additionalInfo) {
        super(bookingId, startTime, user, status, smsPin, additionalInfo);
    }

    public OnlineTesting(String bookingId, String startTime, User user, String notes, String status, String smsPin, String additionalInfo) {
        super(bookingId, startTime, user, notes, status, smsPin, additionalInfo);
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
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
                ",qrcode='" + qrcode + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
