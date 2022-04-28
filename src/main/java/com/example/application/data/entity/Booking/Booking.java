package com.example.application.data.entity.Booking;


import com.example.application.data.entity.TestingType.TestingType;
import com.example.application.data.entity.User.User;

import java.util.UUID;

public abstract class Booking {
    private String bookingId;
    private User user;
    private String startTime;
    private String notes;
    private String status;
    private String smsPin;
    private TestingType testType;
    private String qrcode;

    public Booking(String startTime, User user, String notes) {
        this.startTime = startTime;
        this.user = user;
        this.notes = notes;
        this.bookingId = null;
        this.status = null;
        this.smsPin = null;
        this.testType = null;
        qrcode = UUID.randomUUID().toString();
    }



    public Booking(String bookingId, String startTime, User user, String status, String smsPin, String qrcode) {
        this.bookingId = bookingId;
        this.startTime = startTime;
        this.user = user;
        this.notes = null;
        this.status = status;
        this.smsPin = smsPin;
        this.testType = null;
        this.qrcode = qrcode;
    }

    public Booking(String bookingId, String startTime, User user, String notes, String status, String smsPin, String qrcode) {
        this.bookingId = bookingId;
        this.startTime = startTime;
        this.user = user;
        this.notes = notes;
        this.status = status;
        this.smsPin = smsPin;
        this.testType = null;
        this.qrcode = qrcode;
    }

    public String getQrcode() {
        return qrcode;
    }

    public String getBookingId() {
        return bookingId;
    }

    public User getUser() {
        return user;
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

    public TestingType getTestType() {
        return testType;
    }

    public void setTestType(TestingType testType) {
        this.testType = testType;
    }

    @Override
    public String toString() {
        return "bookingId='" + bookingId + '\'' +
                ", \ntestType=" + testType +
                ", \nuser= " + user.toString() +
                ", \nstartTime= '" + startTime + '\'' +
                ", \nnotes= '" + notes + '\'' +
                ", \nstatus= '" + status + '\'' +
                ", \nsmsPin= '" + smsPin + '\'';
    }
}
