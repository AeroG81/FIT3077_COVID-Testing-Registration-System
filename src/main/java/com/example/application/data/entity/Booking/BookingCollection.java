package com.example.application.data.entity.Booking;

import com.example.application.data.entity.HttpHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;

public class BookingCollection {
    private List<Booking> collection = new ArrayList<Booking>();

    public BookingCollection(){
        try {
            getBookingsService();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void getBookingsService() throws Exception{

    }

    /**
     * Verify PIN which will return Booking
     * */
    public Booking verifyPin(String pin) {
        Booking userBooking = null;
        int i = 0;
        boolean endLoop = false;
        while (i<collection.size() && !endLoop){
            if (collection.get(i).getSmsPin().equals(pin)) {
                userBooking = collection.get(i);
                endLoop = true;
            }
            i++;
        }
        return userBooking;
    }
}
