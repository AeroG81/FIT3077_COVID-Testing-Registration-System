package com.example.application.data.entity.Booking;


import com.example.application.data.entity.TestingType.TestingType;
import com.example.application.data.entity.User.User;

public abstract class Booking {
    private String bookingId;
    private User user;
    private String startTime;
    private String notes;
    private String status;
    private String smsPin;
    private String additionalInfo; // additionalInfo should be store in { } form with {} included
    private TestingType testType;

    public Booking(String startTime, User user, String notes) {
        this.startTime = startTime;
        this.user = user;
        this.notes = notes;
        this.bookingId = null;
        this.status = null;
        this.smsPin = null;
        this.additionalInfo = null;
        this.testType = null;
    }

    public Booking(String startTime, User user, String status, String smsPin, String additionalInfo) {
        this.bookingId = null;
        this.startTime = startTime;
        this.user = user;
        this.notes = null;
        this.status = status;
        this.smsPin = smsPin;
        this.additionalInfo = additionalInfo;
        this.testType = null;
    }

    public Booking(String bookingId, String startTime, User user, String status, String smsPin, String additionalInfo) {
        this.bookingId = bookingId;
        this.startTime = startTime;
        this.user = user;
        this.notes = null;
        this.status = status;
        this.smsPin = smsPin;
        this.additionalInfo = additionalInfo;
        this.testType = null;
    }

    public Booking(String bookingId, String startTime, User user, String notes, String status, String smsPin, String additionalInfo) {
        this.bookingId = bookingId;
        this.startTime = startTime;
        this.user = user;
        this.notes = notes;
        this.status = status;
        this.smsPin = smsPin;
        this.additionalInfo = additionalInfo;
        this.testType = null;
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
        return additionalInfo;
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
                ", user=" + user.toString() +
                ", startTime='" + startTime + '\'' +
                ", notes='" + notes + '\'' +
                ", status='" + status + '\'' +
                ", smsPin='" + smsPin + '\'' +
                ", additionalInfo='" + additionalInfo + '\'' +
                ", testType=" + testType;
    }
}
