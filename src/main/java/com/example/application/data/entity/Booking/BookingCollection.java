package com.example.application.data.entity.Booking;

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

/**
 * This is the booking collection storing a list of Booking
 */
public class BookingCollection {
    private List<Booking> collection = new ArrayList<Booking>();

    /**
     * Constructor of BookingCollection
     */
    public BookingCollection(){
        try {
            // Populate list
            getBookingsService();
        }
        catch (Exception e){
            System.out.println("BC error");
            System.out.println(e);
        }
    }

    /**
     * Getter for collection of bookings
     * @return collection of bookings
     */
    public List<Booking> getCollection() {
        return collection;
    }

    /**
     * HTTP request to API to populate the list of collection
     * @throws Exception for Error in request
     */
    public void getBookingsService() throws Exception {
        String userUrl = "https://fit3077.com/api/v1/booking";

        HttpResponse<String> response = new HttpHelper().getService(userUrl);

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
                booking = new OnSiteTesting(node.get("id").asText(),testingSite, node.get("startTime").asText(), user, node.get("notes").asText(), node.get("status").asText(), node.get("smsPin").asText(), node.get("additionalInfo").get("qrcode").asText());
            } else {
                booking = new OnlineTesting(node.get("id").asText(), node.get("startTime").asText(), user, node.get("notes").asText(), node.get("status").asText(), node.get("smsPin").asText(), node.get("additionalInfo").get("qrcode").asText(), node.get("additionalInfo").get("url").asText());
            }
            collection.add(booking);
        }
    }

    /**
     * Verify PIN code which will return Booking
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

    /**
     * Verify QR code which will return Booking
     * */
    public Booking verifyQr(String qr) {
        Booking userBookingMethod = null;
        int i = 0;
        boolean endLoop = false;
        while (i<collection.size() && !endLoop){
            if (collection.get(i).getQrcode().equals(qr)) {
                userBookingMethod = collection.get(i);
                endLoop = true;
            }
            i++;
        }
        return userBookingMethod;
    }

}
