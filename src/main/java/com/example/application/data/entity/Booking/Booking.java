package com.example.application.data.entity.Booking;

import com.example.application.data.entity.User.User;
import java.util.UUID;

/**
 * This is the Booking abstract class act as the base for all bookings
 */
public abstract class Booking {
    private String bookingId;
    private User customer;
    private String startTime;
    private String notes;
    private String status;
    private String smsPin;
    private String qrcode;

    /**
     * Constructor of Booking
     * @param startTime booking appointment time
     * @param customer customer for the booking
     * @param notes notes provided for the booking
     */
    public Booking(String startTime, User customer, String notes) {
        this.startTime = startTime;
        this.customer = customer;
        this.notes = notes;
        this.bookingId = null;
        this.status = null;
        this.smsPin = null;
        qrcode = UUID.randomUUID().toString();
    }

    /**
     * Constructor of Booking
     * @param bookingId booking ID
     * @param startTime booking appointment time
     * @param customer customer for the booking
     * @param notes  notes provided for the booking
     * @param status status of the booking
     * @param smsPin PIN code to verify the booking
     * @param qrcode QR code to verify the booking
     */
    public Booking(String bookingId, String startTime, User customer, String notes, String status, String smsPin, String qrcode) {
        this.bookingId = bookingId;
        this.startTime = startTime;
        this.customer = customer;
        this.notes = notes;
        this.status = status;
        this.smsPin = smsPin;
        this.qrcode = qrcode;
    }

    /**
     * Getter for QR code
     * @return qrcode
     */
    public String getQrcode() {
        return qrcode;
    }

    /**
     * Getter for booking ID
     * @return booking ID
     */
    public String getBookingId() {
        return bookingId;
    }

    /**
     * Getter for Customer
     * @return Customer
     */
    public User getCustomer() {
        return customer;
    }

    /**
     * Getter for startTime
     * @return booking appointment time
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Getter for notes
     * @return notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Getter for status
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Getter for PIN code
     * @return PIN code
     */
    public String getSmsPin() {
        return smsPin;
    }

    /**
     * Getter for Additional Info of booking in JSON String format
     * @return QR code
     */
    public String getAdditionalInfo() {
        return qrcode;
    }

    /**
     * toString method
     * @return String form of the class
     */
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
