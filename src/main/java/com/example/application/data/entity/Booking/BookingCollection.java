package com.example.application.data.entity.Booking;

import com.example.application.data.entity.HttpHelper;
import com.example.application.data.entity.User.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.http.HttpResponse;

import java.util.ArrayList;
import java.util.Arrays;
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
}
