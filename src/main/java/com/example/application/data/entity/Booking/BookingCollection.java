package com.example.application.data.entity.Booking;

import com.example.application.data.entity.BookingMethod.BookingMethod;
import com.example.application.data.entity.BookingMethod.FacilityBookingMethod;
import com.example.application.data.entity.HttpHelper;
import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.User.ExpertStaff;
import com.example.application.data.entity.User.FacilityStaff;
import com.example.application.data.entity.User.Resident;
import com.example.application.data.entity.User.User;
import com.fasterxml.jackson.databind.JsonNode;
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
            System.out.println("BC error");
            System.out.println(e);
        }
    }

    public List<Booking> getCollection() {
        return collection;
    }

    public void getBookingsService() throws Exception {
        String userUrl = "https://fit3077.com/api/v1/booking";

        HttpResponse<String> response = new HttpHelper().getService(userUrl);

        // Error checking for this sample code. You can check the status code of your request, as part of performing error handling in your assignment.
        if (response.statusCode() != 200) {
            throw new Exception("Please specify your API key in line 21 to continue using this sample code.");
        }

        // The GET /user endpoint returns a JSON array, so we can loop through the response as we could with a normal array/list.
        ObjectNode[] jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);
        for (ObjectNode node : jsonNodes) {
            JsonNode userNode = node.get("customer");
            User user = null;
            Booking booking;
            if (userNode.get("isCustomer").asBoolean())
                user = new Resident(userNode.get("id").asText(), userNode.get("givenName").asText(), userNode.get("familyName").asText(), userNode.get("userName").asText(), userNode.get("phoneNumber").asText());
            else if (userNode.get("isReceptionist").asBoolean())
                user = new FacilityStaff(userNode.get("id").asText(), userNode.get("givenName").asText(), userNode.get("familyName").asText(), userNode.get("userName").asText(), userNode.get("phoneNumber").asText());
            else if (userNode.get("isHealthcareWorker").asBoolean())
                user = new ExpertStaff(userNode.get("id").asText(), userNode.get("givenName").asText(), userNode.get("familyName").asText(), userNode.get("userName").asText(), userNode.get("phoneNumber").asText());

            if (!node.get("testingSite").asText().equals("null")) {
                JsonNode testingSiteNode = node.get("testingSite");
                TestingSite testingSite = new TestingSite(testingSiteNode.get("id").asText(),
                        testingSiteNode.get("name").asText(),
                        testingSiteNode.get("description").asText(),
                        testingSiteNode.get("websiteUrl").asText(),
                        testingSiteNode.get("phoneNumber").asText(),
                        testingSiteNode.get("address").get("latitude").asDouble(),
                        testingSiteNode.get("address").get("longitude").asDouble(),
                        testingSiteNode.get("address").get("unitNumber").asInt(),
                        testingSiteNode.get("address").get("street").asText(),
                        testingSiteNode.get("address").get("street2").asText(),
                        testingSiteNode.get("address").get("suburb").asText(),
                        testingSiteNode.get("address").get("state").asText(),
                        testingSiteNode.get("address").get("postcode").asText(),
                        testingSiteNode.get("additionalInfo").get("facilityType").asText(),
                        testingSiteNode.get("additionalInfo").get("openTime").asText(),
                        testingSiteNode.get("additionalInfo").get("closeTime").asText(),
                        testingSiteNode.get("additionalInfo").get("waitingTime").asText()
                );
                booking = new OnSiteTesting(node.get("id").asText(),testingSite, node.get("startTime").asText(), user, node.get("notes").asText(), node.get("status").asText(), node.get("smsPin").asText(), node.get("additionalInfo").asText());
            } else {
                booking = new OnlineTesting(node.get("id").asText(), node.get("startTime").asText(), user, node.get("notes").asText(), node.get("status").asText(), node.get("smsPin").asText(), node.get("additionalInfo").asText());
            }
            collection.add(booking);
        }
    }

    /**
     * Verify PIN which will return BookingMethod
     * */
    public Booking verifyPin(String pin) {
        Booking userBookingMethod = null;
        int i = 0;
        boolean endLoop = false;
        while (i<collection.size() && !endLoop){
            if (collection.get(i).getSmsPin().equals(pin)) {
                userBookingMethod = collection.get(i);
                endLoop = true;
            }
            i++;
        }
        return userBookingMethod;
    }

    public Booking addBookingService(CovidTest testingMethod, String startTime, User user, String notes) throws Exception{
        String url = "https://fit3077.com/api/v1/booking";
        String jsonString = null;
//        if (user instanceof Resident) {
//            jsonString =
//                    "{" +
//                            "\"id\":\"" + testingMethod.getId() + "\"," +
//                            "{" +
//                                "\"customer\":\"" +
//                                    "{" +
//                                        "\"id\":\"" + user.getId() + "\"," +
//                                        "\"givenName\":\"" + user.getGivenName() + "\"," +
//                                        "\"familyName\":\"" + user.getFamilyName() + "\"," +
//                                        "\"userName\":\"" + user.getUserName() + "\"," +
//                                        "\"phoneNumber\":\"" + user.getPhoneNumber() + "\"," +
//                                        "\"isCustomer\":\"" + true + "\"," +
//                                        "\"isReceptionist\":\"" + false + "\"," +
//                                        "\"isHealthcareWorker\":\"" + false + "\"," +
//                                        "\"additionalInfo\":\"" + user.getAdditionalInfo() + "\"," +
//                                    "}" + "\"," +
//
//
//
//                            "\"familyName\":\"" + familyName + "\"," +
//                            "\"userName\":\"" + userName + "\"," +
//                            "\"password\":\"" + password + "\"," +
//                            "\"phoneNumber\":\"" + phoneNumber + "\"," +
//                            "\"isCustomer\":" + isCustomer + "," +
//                            "\"isAdmin\":" + isAdmin + "," +
//                            "\"isHealthCareWorker\":" + isHealthCareWorker + "," +
//                            "\"additionalInfo\":" + additionalInfo +
//                            "}";
//        }

        return null;
    }

    public Booking addBookingService(CovidTest testingMethod, String startTime, User user, String status, String smsPin, String additionalInfo) {
        return null;
    }

    public Booking addBookingService(String bookingId, CovidTest testingMethod, String startTime, User user, String status, String smsPin, String additionalInfo) {
        return null;
    }

    public Booking addBookingService(String bookingId, CovidTest testingMethod, String startTime, User user, String notes, String status, String smsPin, String additionalInfo) {
        return null;
    }


}

