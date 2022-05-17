package com.example.application.data.entity.Meeting;

public class GoogleMeet implements MeetingMethod{

    @Override
    public String generateUrl(String id) {
        return "https://googlemeet.com/"+id;
    }
}
