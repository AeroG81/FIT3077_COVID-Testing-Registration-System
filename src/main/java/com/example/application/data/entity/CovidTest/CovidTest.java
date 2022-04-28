package com.example.application.data.entity.CovidTest;

import com.example.application.data.entity.Booking.Booking;
import com.example.application.data.entity.TestingType.TestingType;
import com.example.application.data.entity.User.User;

public class CovidTest {
    enum Result {
        POSITIVE, NEGATIVE, INVALID, INITIATED, PENDING
    };
    enum TestType {
        PCR, RAT
    }

    private String id;
    private TestingType testingType;
    private User patient;
    private User administerer;
    private Booking booking;
    private String result;
    private String status;
    private String notes;
    private String additionalInfo;

    public CovidTest(TestingType testingType, User patient, Booking booking, String result, String status, String notes, String additionalInfo) {
        this.id = null;
        this.testingType = testingType;
        this.patient = patient;
        this.administerer = null;
        this.booking = booking;
        this.result = result;
        this.status = status;
        this.notes = notes;
        this.additionalInfo = additionalInfo;
    }

    public CovidTest(TestingType testingType, User patient, User administerer, String result, String status, String notes, String additionalInfo) {
        this.id = null;
        this.testingType = testingType;
        this.patient = patient;
        this.administerer = administerer;
        this.booking = null;
        this.result = result;
        this.status = status;
        this.notes = notes;
        this.additionalInfo = additionalInfo;
    }

    public CovidTest(TestingType testingType, User patient, User administerer, Booking booking, String result, String status, String notes, String additionalInfo) {
        this.id = null;
        this.testingType = testingType;
        this.patient = patient;
        this.administerer = administerer;
        this.booking = booking;
        this.result = result;
        this.status = status;
        this.notes = notes;
        this.additionalInfo = additionalInfo;
    }

    public CovidTest(String id, TestingType testingType, User patient, User administerer, Booking booking, String result, String status, String notes, String additionalInfo) {
        this.id = id;
        this.testingType = testingType;
        this.patient = patient;
        this.administerer = administerer;
        this.booking = booking;
        this.result = result;
        this.status = status;
        this.notes = notes;
        this.additionalInfo = additionalInfo;
    }

    public CovidTest(String id, TestingType testingType, User patient, User administerer, Booking booking, String result, String status, String notes) {
        this.id = id;
        this.testingType = testingType;
        this.patient = patient;
        this.administerer = administerer;
        this.booking = booking;
        this.result = result;
        this.status = status;
        this.notes = notes;
        this.additionalInfo = null;
    }

    public String getId() {
        return id;
    }

    public TestingType getTestingType() {
            return testingType;
    }

    public User getPatient() {
        return patient;
    }

    public User getAdministerer() {
        return administerer;
    }

    public Booking getBooking() {
        return booking;
    }

    public String getResult() {
        return result;
    }

    public String getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    @Override
    public String toString() {
        return "CovidTest{" +
                "id='" + id + '\'' +
                "\n, testingType=" + testingType.getType() +
                "\n, patient=" + patient.toString() +
                "\n, administerer=" + administerer.toString() +
                "\n, booking=" + booking.toString() +
                "\n, result='" + result + '\'' +
                "\n, status='" + status + '\'' +
                "\n, notes='" + notes + '\'' +
                "\n, additionalInfo='" + additionalInfo + '\'' +
                '}';
    }
}
