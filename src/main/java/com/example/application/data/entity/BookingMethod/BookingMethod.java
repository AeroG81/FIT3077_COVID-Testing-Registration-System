package com.example.application.data.entity.BookingMethod;


import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.User.User;

import java.net.http.HttpResponse;

public interface BookingMethod {
    HttpResponse<String> addBooking(TestingSite site, String startTime, User user, String notes) throws Exception;
    HttpResponse<String> addBooking(String startTime, User user, String notes) throws Exception;
}
