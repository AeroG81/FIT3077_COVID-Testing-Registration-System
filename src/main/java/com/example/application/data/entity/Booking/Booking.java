package com.example.application.data.entity.Booking;

import com.example.application.data.entity.Registration.CovidTest;

import java.time.Instant;

public abstract class Booking {
    private String bookingId;
    private String user;
    private CovidTest testingMethod;
    private Instant startTime;
    private String notes;
    private String status;
    private String smsPin;
    private String additionalInfo;

    public Booking(CovidTest testingMethod, String startTime, String user, String notes) {
        this.testingMethod = testingMethod;
        this.startTime = Instant.parse(startTime);
        this.user = user;
        this.notes = notes;
    }

    public Booking(CovidTest testingMethod, String startTime, String user, String status, String smsPin, String additionalInfo) {
        this.testingMethod = testingMethod;
        this.startTime = Instant.parse(startTime);
        this.user = user;
        this.status = status;
        this.smsPin = smsPin;
        this.additionalInfo = additionalInfo;
    }

    public Booking(String bookingId,CovidTest testingMethod, String startTime, String user, String status, String smsPin, String additionalInfo) {
        this.bookingId = bookingId;
        this.testingMethod = testingMethod;
        this.startTime = Instant.parse(startTime);
        this.user = user;
        this.status = status;
        this.smsPin = smsPin;
        this.additionalInfo = additionalInfo;
    }

    public Booking(String bookingId, CovidTest testingMethod, String startTime, String user, String notes, String status, String smsPin, String additionalInfo) {
        this.bookingId = bookingId;
        this.testingMethod = testingMethod;
        this.startTime = Instant.parse(startTime);
        this.user = user;
        this.notes = notes;
        this.status = status;
        this.smsPin = smsPin;
        this.additionalInfo = additionalInfo;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getUser() {
        return user;
    }

    public CovidTest getTestingMethod() {
        return testingMethod;
    }

    public Instant getStartTime() {
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

    public abstract void addBooking() throws Exception;
}
