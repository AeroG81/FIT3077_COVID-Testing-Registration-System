package com.example.application.data.entity.Booking;

import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.User.User;

public class OnSiteTesting extends Booking {
    private TestingSite testingSite;
    public OnSiteTesting(TestingSite site, String startTime, User user, String notes) {
        super(startTime, user, notes);
        this.testingSite = site;
    }

    public OnSiteTesting(TestingSite site, String startTime, User user, String status, String smsPin, String additionalInfo) {
        super(startTime, user, status, smsPin, additionalInfo);
        this.testingSite = site;
    }

    public OnSiteTesting(String bookingId, TestingSite site, String startTime, User user, String status, String smsPin, String additionalInfo) {
        super(bookingId, startTime, user, status, smsPin, additionalInfo);
        this.testingSite = site;
    }

    public OnSiteTesting(String bookingId, TestingSite site, String startTime, User user, String notes, String status, String smsPin, String additionalInfo) {
        super(bookingId, startTime, user, notes, status, smsPin, additionalInfo);
        this.testingSite = site;
    }

    public TestingSite getTestingSite() {
        return testingSite;
    }

    @Override
    public String toString() {
        return "OnSiteTesting{" +
                super.toString() +
                ",testingSite=" + testingSite.toString() +
                '}';
    }
}
