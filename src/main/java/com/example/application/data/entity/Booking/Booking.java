package com.example.application.data.entity.Booking;


import com.example.application.data.entity.User.User;

import java.util.UUID;

public abstract class Booking {
    private String bookingId;
    private User customer;
    private String startTime;
    private String notes;
    private String status;
    private String smsPin;
    private String qrcode;

    public Booking(String startTime, User customer, String notes) {
        this.startTime = startTime;
        this.customer = customer;
        this.notes = notes;
        this.bookingId = null;
        this.status = null;
        this.smsPin = null;
        qrcode = UUID.randomUUID().toString();
    }

    public Booking(String bookingId, String startTime, User customer, String status, String smsPin, String qrcode) {
        this.bookingId = bookingId;
        this.startTime = startTime;
        this.customer = customer;
        this.notes = null;
        this.status = status;
        this.smsPin = smsPin;
        this.qrcode = qrcode;
    }

    public Booking(String bookingId, String startTime, User customer, String notes, String status, String smsPin, String qrcode) {
        this.bookingId = bookingId;
        this.startTime = startTime;
        this.customer = customer;
        this.notes = notes;
        this.status = status;
        this.smsPin = smsPin;
        this.qrcode = qrcode;
    }

    public String getQrcode() {
        return qrcode;
    }

    public String getBookingId() {
        return bookingId;
    }

    public User getCustomer() {
        return customer;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getNotes() {
        return notes;
    }

    public String getStatus() {
        return status;
    }

    public String getSmsPin() {
        return smsPin;
    }

    public String getAdditionalInfo() {
        return qrcode;
    }

    @Override
    public String toString() {
        return "bookingId='" + bookingId + '\'' +
                ", \nuser= " + customer.toString() +
                ", \nstartTime= '" + startTime + '\'' +
                ", \nnotes= '" + notes + '\'' +
                ", \nstatus= '" + status + '\'' +
                ", \nsmsPin= '" + smsPin + '\'';
    }
}
