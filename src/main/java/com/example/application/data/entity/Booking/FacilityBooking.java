package com.example.application.data.entity.Booking;

import com.example.application.data.entity.Registration.CovidTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FacilityBooking extends Booking{
    public FacilityBooking(CovidTest testingMethod, String startTime, String user, String status, String smsPin, String additionalInfo) {
        super(testingMethod, startTime, user, status, smsPin, additionalInfo);
    }

    public FacilityBooking(String bookingId, CovidTest testingMethod, String startTime, String user, String status, String smsPin, String additionalInfo) {
        super(bookingId, testingMethod, startTime, user, status, smsPin, additionalInfo);
    }

    public FacilityBooking(String bookingId, CovidTest testingMethod, String startTime, String user, String notes, String status, String smsPin, String additionalInfo) {
        super(bookingId, testingMethod, startTime, user, notes, status, smsPin, additionalInfo);
    }

    public FacilityBooking(CovidTest testingMethod, String startTime, String user, String notes){
        super(testingMethod,startTime,user,notes);
    }

    @Override
    public void addBooking() throws Exception{
        String jsonString = "{" +
                "\"customerId\":\"" + super.getUser() + "\"," +
                "\"testingSiteId\":" + super.getTestingMethod().getDetails() + "," +
                "\"startTime\":" + super.getStartTime() + "," +
                "\"notes\":" + super.getNotes() + "," +
                "\"additionalInfo\": {"+ "}"+
                "}";
        String myApiKey = "7WwqfjwcprP7HPqLRmnmQ8QNzg9MWj";
        String testingSiteUrl = "https://fit3077.com/api/v1/testing-site";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(testingSiteUrl))
                .setHeader("Authorization", myApiKey)
                .header("Content-Type","application/json") // This header needs to be set when sending a JSON request body.
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
