package com.example.application.data.entity.CovidTest;


import com.example.application.data.entity.Booking.Booking;
import com.example.application.data.entity.Booking.OnSiteTesting;
import com.example.application.data.entity.Booking.OnlineTesting;
import com.example.application.data.entity.HttpHelper;
import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.TestingType.PCR;
import com.example.application.data.entity.TestingType.RAT;
import com.example.application.data.entity.TestingType.TestingType;
import com.example.application.data.entity.User.HealthcareWorker;
import com.example.application.data.entity.User.Receptionist;
import com.example.application.data.entity.User.Customer;
import com.example.application.data.entity.User.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the covid test collection storing a list of CovidTest
 */
public class CovidTestCollection {
    private List<CovidTest> collection = new ArrayList<>();

    /**
     * Constructor of BookingCollection
     */
    public CovidTestCollection(){
        try {
            // Populate list
            getCovidTestService();
        }
        catch (Exception e){
            System.out.println("CTC error");
            System.out.println(e);
        }
    }

    /**
     * Verify the QR code for booking stored in covidTest
     * @param qr qrcode
     * @return CovidTest,  if null means no CovidTest related to the Booking QR code was created, if there is a value means CovidTest related to the Booking QR code was created
     */
    public CovidTest verifyQrCovidTest(String qr) {
        CovidTest covidTest = null;
        int i = 0;
        boolean endLoop = false;
        while (i<collection.size() && !endLoop){
            if (collection.get(i).getBooking().getQrcode().equals(qr)) {
                covidTest = collection.get(i);
                endLoop = true;
            }
            i++;
        }
        return covidTest;
    }

    /**
     * HTTP request to API to add a new covid test
     * @param type type string of test
     * @param patientId patient id of the covid test
     * @param administererId administerer id of the covid test
     * @param bookingId booking id of the covid test
     * @param result result of the covid test
     * @param status status of the covid test
     * @param notes notes of the covid test
     * @return HTTP response from the API
     * @throws Exception for Error in request
     */
    public HttpResponse<String> addCovidTestService(String type, String patientId, String administererId, String bookingId, String result, String status, String notes) throws Exception{
        String jsonString = "{" +
                "\"type\":\"" + type + "\"," +
                "\"patientId\":\"" + patientId + "\"";
        if (administererId != null && !administererId.isBlank())
            jsonString += ",\"administererId\":\"" + administererId + "\"";
        if (bookingId != null && !bookingId.isBlank())
            jsonString += ",\"bookingId\":\"" + bookingId + "\"";
        if (result != null && !result.isBlank())
            jsonString += ",\"result\":\"" + result + "\"" ;
        if (status != null && !status.isBlank())
            jsonString += ",\"status\":\"" + status + "\"";
        if (notes != null && !notes.isBlank())
            jsonString += ",\"notes\":\"" + notes + "\"" ;
        jsonString += "}";
        String testingSiteUrl = "https://fit3077.com/api/v2/covid-test";

        return new HttpHelper().postService(testingSiteUrl,jsonString);
    }

    /**
     * Getter for collection of covid tests
     * @return collection of covid tests
     */
    public List<CovidTest> getCollection() {
        return collection;
    }

    /**
     * HTTP request to API to populate the list of collection
     * @throws Exception for Error in request
     */
    public void getCovidTestService() throws Exception {
        String userUrl = "https://fit3077.com/api/v2/covid-test";

        HttpResponse<String> response = new HttpHelper().getService(userUrl);

        // The GET /user endpoint returns a JSON array, so we can loop through the response as we could with a normal array/list.
        ObjectNode[] jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);
        for (ObjectNode node : jsonNodes) {
            JsonNode patientNode = node.get("patient");
            JsonNode administererNode = node.get("administerer");
            JsonNode bookingNode = node.get("booking");
            JsonNode bookingCustomerNode =bookingNode.get("customer");
            Booking booking;
            TestingType type = null;

            User patient = this.createUser(patientNode);
            User administerer = this.createUser(administererNode);
            User bookingCustomer = this.createUser(bookingCustomerNode);

            List<String> history = new ArrayList<>();
            JsonNode historyNode = node.get("additionalInfo").get("history");
            if (historyNode==null)
                history = null;
            else if (historyNode.isArray()){
                for (JsonNode obj: historyNode){
                    history.add(obj.asText());
                }
            }
            else
                history = null;

            if (!bookingNode.get("testingSite").asText().equals("null")) {
                JsonNode testingSiteNode = bookingNode.get("testingSite");
                TestingSite testingSite = new TestingSite(testingSiteNode.get("id").asText(), testingSiteNode.get("name").asText(), testingSiteNode.get("description").asText(), testingSiteNode.get("websiteUrl").asText(), testingSiteNode.get("phoneNumber").asText(), testingSiteNode.get("address").get("latitude").asDouble(), testingSiteNode.get("address").get("longitude").asDouble(), testingSiteNode.get("address").get("unitNumber").asInt(), testingSiteNode.get("address").get("street").asText(), testingSiteNode.get("address").get("street2").asText(), testingSiteNode.get("address").get("suburb").asText(), testingSiteNode.get("address").get("state").asText(), testingSiteNode.get("address").get("postcode").asText(), testingSiteNode.get("additionalInfo").get("facilityType").asText(), testingSiteNode.get("additionalInfo").get("openTime").asText(), testingSiteNode.get("additionalInfo").get("closeTime").asText(), testingSiteNode.get("additionalInfo").get("waitingTime").asText());
                booking = new OnSiteTesting(bookingNode.get("id").asText(), testingSite, bookingNode.get("startTime").asText(), bookingCustomer, bookingNode.get("notes").asText(), bookingNode.get("status").asText(), bookingNode.get("smsPin").asText(), bookingNode.get("additionalInfo").get("qrcode").asText(),history);
            } else {
                booking = new OnlineTesting(bookingNode.get("id").asText(), bookingNode.get("startTime").asText(), bookingCustomer, bookingNode.get("notes").asText(), bookingNode.get("status").asText(), bookingNode.get("smsPin").asText(), bookingNode.get("additionalInfo").get("qrcode").asText(), bookingNode.get("additionalInfo").get("url").asText(), history);
            }
            if (node.get("type").asText().equals("PCR"))
                type = new PCR();
            else if (node.get("type").asText().equals("RAT"))
                type = new RAT();
            CovidTest covidTest = new CovidTest(node.get("id").asText(), type, patient, administerer, booking, node.get("result").asText(), node.get("status").asText(), node.get("notes").asText(), node.get("additionalInfo").asText());
            collection.add(covidTest);
        }
    }

    /**
     * Helper method to create user based on the isCustomer, isReceptionist and isHealthcareWorker attribute
     * @param node node to identify the attribute
     * @return corresponding user
     */
    private User createUser(JsonNode node){
        User user = null;
        if (!node.asText().equals("null"))
            if (node.get("isCustomer").asBoolean())
                user = new Customer(node.get("id").asText(), node.get("givenName").asText(), node.get("familyName").asText(), node.get("userName").asText(), node.get("phoneNumber").asText());
            else if (node.get("isReceptionist").asBoolean())
                user = new Receptionist(node.get("id").asText(), node.get("givenName").asText(), node.get("familyName").asText(), node.get("userName").asText(), node.get("phoneNumber").asText());
            else if (node.get("isHealthcareWorker").asBoolean())
                user = new HealthcareWorker(node.get("id").asText(), node.get("givenName").asText(), node.get("familyName").asText(), node.get("userName").asText(), node.get("phoneNumber").asText());
        return user;
    }
}
