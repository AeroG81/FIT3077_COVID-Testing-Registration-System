package com.example.application.data.entity.Booking;

import com.example.application.data.entity.Registration.CovidTest;
import com.example.application.data.entity.User.User;

import java.net.http.HttpResponse;

public abstract class Booking {
    private String bookingId;
    private User user;
    private CovidTest testingMethod;
    private String startTime;
    private String notes;
    private String status;
    private String smsPin;
    private String additionalInfo; // additionalInfo should be store in { } form with {} included


    public Booking(CovidTest testingMethod, String startTime, User user, String notes) {
        this.testingMethod = testingMethod;
        this.startTime = startTime;
        this.user = user;
        this.notes = notes;
        this.bookingId = null;
        this.status = null;
        this.smsPin = null;
        this.additionalInfo = null;
    }

    public Booking(CovidTest testingMethod, String startTime, User user, String status, String smsPin, String additionalInfo) {
        this.bookingId = null;
        this.testingMethod = testingMethod;
        this.startTime = startTime;
        this.user = user;
        this.notes = null;
        this.status = status;
        this.smsPin = smsPin;
        this.additionalInfo = additionalInfo;
    }

    public Booking(String bookingId, CovidTest testingMethod, String startTime, User user, String status, String smsPin, String additionalInfo) {
        this.bookingId = bookingId;
        this.testingMethod = testingMethod;
        this.startTime = startTime;
        this.user = user;
        this.notes = null;
        this.status = status;
        this.smsPin = smsPin;
        this.additionalInfo = additionalInfo;
    }

    public Booking(String bookingId, CovidTest testingMethod, String startTime, User user, String notes, String status, String smsPin, String additionalInfo) {
        this.bookingId = bookingId;
        this.testingMethod = testingMethod;
        this.startTime = startTime;
        this.user = user;
        this.notes = notes;
        this.status = status;
        this.smsPin = smsPin;
        this.additionalInfo = additionalInfo;
    }

    public String getBookingId() {
        return bookingId;
    }

    public User getUser() {
        return user;
    }

    public CovidTest getTestingMethod() {
        return testingMethod;
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

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTestingMethod(CovidTest testingMethod) {
        this.testingMethod = testingMethod;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSmsPin(String smsPin) {
        this.smsPin = smsPin;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public abstract HttpResponse<String> addBooking() throws Exception;
}
