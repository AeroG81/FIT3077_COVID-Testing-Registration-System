package com.example.application.data.entity.Meeting;

public class Zoom implements MeetingMethod{

    @Override
    public String generateUrl(String id) {
        return "https://zoom.us/"+id;
    }
}
