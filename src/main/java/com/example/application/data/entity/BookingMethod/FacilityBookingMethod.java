package com.example.application.data.entity.BookingMethod;

import com.example.application.data.entity.HttpHelper;
import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.User.User;

import java.net.http.HttpResponse;

/**
 * testingSiteId will not be null since the customer register on site
 * */
public class FacilityBookingMethod implements BookingMethod {
    @Override
    public HttpResponse<String> addBooking(TestingSite site, String startTime, User user, String notes) throws Exception{
        String jsonString = "{" +
                "\"customerId\":\"" + user.getId() + "\"," +
                "\"testingSiteId\":\"" + site.getId() + "\"," +
                "\"startTime\":\"" + startTime + "\"";
        if (notes != null && !notes.isBlank())
            jsonString += ",\"notes\":\"" + notes + "\"";
//        if (super.getAdditionalInfo() != null && !super.getAdditionalInfo().isBlank())
//            jsonString += ",\"additionalInfo\":" + super.getAdditionalInfo();
        jsonString += "}";
        String url = "https://fit3077.com/api/v1/booking";
        return new HttpHelper().postService(url,jsonString);
    }

    @Override
    public HttpResponse<String> addBooking(String startTime, User user, String notes) throws Exception {
        return null;
    }
}
