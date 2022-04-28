package com.example.application.data.entity.BookingMethod;

import com.example.application.data.entity.Booking.Booking;
import com.example.application.data.entity.Booking.OnlineTesting;
import com.example.application.data.entity.HttpHelper;
import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.User.User;

import java.net.http.HttpResponse;

/**
 * addBooking with TestingSite is for onSiteTesting, return null on default
 * addBooking without TestingSite if for onlineBooking
 * */
public class SystemBookingMethod implements BookingMethod {
    @Override
    public HttpResponse<String> addBooking(TestingSite site, String startTime, User user, String notes) throws Exception {
        return null;
    }

    @Override
    public HttpResponse<String> addBooking(String startTime, User user, String notes) throws Exception {
        Booking booking = new OnlineTesting(startTime,user,notes);
        String jsonString = "{" +
                "\"customerId\":\"" + booking.getCustomer().getId() + "\"," +
                "\"startTime\":\"" + booking.getStartTime() + "\"";
        if (notes != null && !notes.isBlank())
            jsonString += ",\"notes\":\"" + notes + "\"";
        jsonString += ",\"additionalInfo\": " + booking.getAdditionalInfo();
        jsonString += "}";
        String url = "https://fit3077.com/api/v1/booking";
        return new HttpHelper().postService(url,jsonString);
    }
}
