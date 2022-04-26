package com.example.application.data.entity.Booking;

import com.example.application.data.entity.HttpHelper;
import com.example.application.data.entity.Registration.CovidTest;
import com.example.application.data.entity.User.User;

import java.net.http.HttpResponse;

public class FacilityBooking extends Booking{
    private int pin;
    public FacilityBooking(CovidTest testingMethod, String startTime, User user, String status, String smsPin, String additionalInfo) {
        super(testingMethod, startTime, user, status, smsPin, additionalInfo);
    }

    public FacilityBooking(String bookingId, CovidTest testingMethod, String startTime, User user, String status, String smsPin, String additionalInfo) {
        super(bookingId, testingMethod, startTime, user, status, smsPin, additionalInfo);
    }

    public FacilityBooking(String bookingId, CovidTest testingMethod, String startTime, User user, String notes, String status, String smsPin, String additionalInfo) {
        super(bookingId, testingMethod, startTime, user, notes, status, smsPin, additionalInfo);
    }

    public FacilityBooking(CovidTest testingMethod, String startTime, User user, String notes){
        super(testingMethod,startTime,user,notes);
    }

    @Override
    public HttpResponse<String> addBooking() throws Exception{
        String jsonString = "{" +
                "\"customerId\":\"" + super.getUser().getId() + "\"," +
                "\"testingSiteId\":\"" + super.getTestingMethod().getId() + "\"," +
                "\"startTime\":\"" + super.getStartTime() + "\"";
        if (super.getNotes() != null && !super.getNotes().isBlank())
            jsonString += ",\"notes\":\"" + super.getNotes() + "\"";
        if (super.getAdditionalInfo() != null && !super.getAdditionalInfo().isBlank())
            jsonString += ",\"additionalInfo\":" + super.getAdditionalInfo();
        jsonString += "}";
        String url = "https://fit3077.com/api/v1/booking";
        return new HttpHelper().postService(url,jsonString);
    }
}
