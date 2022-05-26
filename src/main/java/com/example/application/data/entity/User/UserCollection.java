package com.example.application.data.entity.User;

import com.example.application.data.entity.HttpHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vaadin.flow.router.NotFoundException;

import java.net.http.HttpResponse;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

public class UserCollection {

    // List of all Users from API
    private List<User> collection = new ArrayList<>();

    // Constructor which GETS all users from API and stores into collection
    public UserCollection(){
        try {
            getUsersService();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    // Returns collection of Users
    public List<User> getCollection() {
        return collection;
    }

    // Verifies if User is in the collection using username and password
    public User verifyUserId(String username, String password){
        User user = null;
        if (verifyUserService(username,password)) {
            int i = 0;
            boolean endLoop = false;
            while (i<collection.size() && !endLoop){
                if (collection.get(i).getUserName().equals(username)) {
                    user = collection.get(i);
                    endLoop = true;
                }
                i++;
            }
        }
        return user;
    }

    // GETS all users from API
    public void getUsersService() throws Exception{
        String userUrl = "https://fit3077.com/api/v2/user";

        HttpResponse<String> response = new HttpHelper().getService(userUrl);

        // Error checking for this sample code. You can check the status code of your request, as part of performing error handling in your assignment.
        if (response.statusCode() != 200) {
            throw new Exception("Please specify your API key in line 21 to continue using this sample code.");
        }

        // The GET /user endpoint returns a JSON array, so we can loop through the response as we could with a normal array/list.
        ObjectNode[] jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);

        // Creates Users of specific roles based on attributes of the User
        for (ObjectNode node: jsonNodes) {
            User user = null;
            // Create Customer object if User is a Customer
            if(node.get("isCustomer").asBoolean())
                user = new Customer(node.get("id").asText(),node.get("givenName").asText(),node.get("familyName").asText(),node.get("userName").asText(),node.get("phoneNumber").asText());
            // Create FacilityStaff object if User is a Receptionist
            if (node.get("isReceptionist").asBoolean()) {
                ArrayList<String> notifications = new ArrayList<>();
                for (int i = 0; i < node.get("additionalInfo").get("notifications").size(); i++) {
                    notifications.add(node.get("additionalInfo").get("notifications").get(i).asText());
                }

                user = new Receptionist(node.get("id").asText(), node.get("givenName").asText(), node.get("familyName").asText(), node.get("userName").asText(), node.get("phoneNumber").asText(), node.get("additionalInfo").get("testingSiteId").asText(), notifications);
            }
            // Create HealthcareWorker object if User is a HealthcareWorker
            if (node.get("isHealthcareWorker").asBoolean())
                user = new HealthcareWorker(node.get("id").asText(),node.get("givenName").asText(),node.get("familyName").asText(),node.get("userName").asText(),node.get("phoneNumber").asText());
            if(user!=null)
                collection.add(user);
        }
    }

    // Checks if User is a Customer/Customer using User's username
    public boolean checkIsCustomer(String username) throws Exception {
        String userUrl = "https://fit3077.com/api/v2/user";

        HttpResponse<String> response = new HttpHelper().getService(userUrl);

        // Error checking for this sample code. You can check the status code of your request, as part of performing error handling in your assignment.
        if (response.statusCode() != 200) {
            throw new Exception("Please specify your API key in line 21 to continue using this sample code.");
        }

        // The GET /user endpoint returns a JSON array, so we can loop through the response as we could with a normal array/list.
        ObjectNode[] jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);

        for (ObjectNode node: jsonNodes) {
            if (node.get("userName").asText().equals(username)) {
                return node.get("isCustomer").asBoolean();
            }
        }
        return false;
    }

    // Checks if User is a Receptionist/FacilityStaff using User's username
    public boolean checkIsReceptionist(String username) throws Exception {
        String userUrl = "https://fit3077.com/api/v2/user";

        HttpResponse<String> response = new HttpHelper().getService(userUrl);

        // Error checking for this sample code. You can check the status code of your request, as part of performing error handling in your assignment.
        if (response.statusCode() != 200) {
            throw new Exception("Please specify your API key in line 21 to continue using this sample code.");
        }

        // The GET /user endpoint returns a JSON array, so we can loop through the response as we could with a normal array/list.
        ObjectNode[] jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);

        for (ObjectNode node: jsonNodes) {
            if (node.get("userName").asText().equals(username)) {
                return node.get("isReceptionist").asBoolean();
            }
        }
        return false;
    }

    // Checks if User is a HealthcareWorker/HealthcareWorker using User's username
    public boolean checkIsHealthcareWorker(String username) throws Exception {
        String userUrl = "https://fit3077.com/api/v2/user";

        HttpResponse<String> response = new HttpHelper().getService(userUrl);

        // Error checking for this sample code. You can check the status code of your request, as part of performing error handling in your assignment.
        if (response.statusCode() != 200) {
            throw new Exception("Please specify your API key in line 21 to continue using this sample code.");
        }

        // The GET /user endpoint returns a JSON array, so we can loop through the response as we could with a normal array/list.
        ObjectNode[] jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);

        for (ObjectNode node: jsonNodes) {
            if (node.get("userName").asText().equals(username)) {
                return node.get("isHealthcareWorker").asBoolean();
            }
        }
        return false;
    }

    // Get user object by ID
    public User getUserById(String userId) throws Exception {
        for (User user: collection){
            if (user.getId().equals(userId)){
                return user;
            }
        }
        return null;
    }

    public ArrayList<Receptionist> getReceptionists() throws Exception {
        ArrayList<Receptionist> receptionists = new ArrayList<>();

        String userUrl = "https://fit3077.com/api/v2/user";

        HttpResponse<String> response = new HttpHelper().getService(userUrl);

        // Error checking for this sample code. You can check the status code of your request, as part of performing error handling in your assignment.
        if (response.statusCode() != 200) {
            throw new Exception("Please specify your API key in line 21 to continue using this sample code.");
        }

        // The GET /user endpoint returns a JSON array, so we can loop through the response as we could with a normal array/list.
        ObjectNode[] jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);

        for (User user: collection) {
            for (ObjectNode node : jsonNodes) {
                if (node.get("isReceptionist").asBoolean() && node.get("id").asText().equals(user.getId())) {
                    if (user instanceof Receptionist) {
                        receptionists.add((Receptionist) user);
                        break;
                    }
                }
            }
        }
        return receptionists;
    }

    // Get notifications for receptionist by their ID
    public ArrayList<String> getNotificationsByReceptionistId (String receptionistId) throws Exception {
        ArrayList<Receptionist> receptionistList = this.getReceptionists();

        for (Receptionist rec: receptionistList){
            if (rec.getId().equals(receptionistId)){
                return rec.getNotifications();
            }
        }
        return null;
    }

    // Verifies is User exists in collection of Users using username and password
    public boolean verifyUserService(String username, String password){
        boolean userIsValid;
        String jsonString = "{"+
                "\"userName\":\"" + username + "\"," +
                "\"password\":\"" + password + "\""+
                "}";

        String url = "https://fit3077.com/api/v2/user/login?jwt=false";

        try {
            HttpResponse<String> response = new HttpHelper().postService(url,jsonString);
            if (response.statusCode()==200){
                userIsValid = true;
            }
            else{
                userIsValid = false;
            }
        }
        catch (Exception e){
            System.out.println(e);
            userIsValid = false;
        }
        return userIsValid;
    }

    // POSTS and stores a User to the API  (with additional info)
    public User addUserService(String givenName,String familyName, String userName, String password, String phoneNumber, boolean isCustomer, boolean isAdmin, boolean isHealthCareWorker, String additionalInfo) throws Exception{
        String url = "https://fit3077.com/api/v2/user";
        String jsonString = "{" +
                "\"givenName\":\"" + givenName + "\"," +
                "\"familyName\":\"" + familyName + "\"," +
                "\"userName\":\"" + userName + "\"," +
                "\"password\":\"" + password + "\"," +
                "\"phoneNumber\":\"" + phoneNumber + "\"," +
                "\"isCustomer\":" + isCustomer + "," +
                "\"isAdmin\":" + isAdmin + "," +
                "\"isHealthCareWorker\":" + isHealthCareWorker + "," +
                "\"additionalInfo\":" + additionalInfo +
                "}";
        HttpResponse<String> response = new HttpHelper().postService(url,jsonString);
        ObjectNode mappedResponse = new ObjectMapper().readValue(response.body(),ObjectNode.class);
        return createUser(mappedResponse);
    }

    // POSTS and stores a User to the API  (without additional info)
    public User addUserService(String givenName,String familyName, String userName, String password, String phoneNumber, boolean isCustomer, boolean isAdmin, boolean isHealthCareWorker) throws Exception{
        String url = "https://fit3077.com/api/v2/user";
        String jsonString = "{" +
                "\"givenName\":\"" + givenName + "\"," +
                "\"familyName\":\"" + familyName + "\"," +
                "\"userName\":\"" + userName + "\"," +
                "\"password\":\"" + password + "\"," +
                "\"phoneNumber\":\"" + phoneNumber + "\"," +
                "\"isCustomer\":" + isCustomer + "," +
                "\"isAdmin\":" + isAdmin + "," +
                "\"isHealthCareWorker\":" + isHealthCareWorker +
                "}";
        HttpResponse<String> response = new HttpHelper().postService(url,jsonString);
        ObjectNode mappedResponse = new ObjectMapper().readValue(response.body(),ObjectNode.class);
        return createUser(mappedResponse);
    }

    // Create a User using response from API
    private User createUser(ObjectNode mappedResponse){
        User user = null;
        if(mappedResponse.get("isCustomer").asBoolean())
            user = new Customer(mappedResponse.get("id").asText(),mappedResponse.get("givenName").asText(),mappedResponse.get("familyName").asText(),mappedResponse.get("userName").asText(),mappedResponse.get("phoneNumber").asText());
        else if (mappedResponse.get("isReceptionist").asBoolean()) {
            ArrayList<String> notifications = new ArrayList<>();
            for (int i = 0; i < mappedResponse.get("additionalInfo").get("notifications").size(); i++) {
                notifications.add(mappedResponse.get("additionalInfo").get("notifications").get(i).asText());
            }
            user = new Receptionist(mappedResponse.get("id").asText(), mappedResponse.get("givenName").asText(), mappedResponse.get("familyName").asText(), mappedResponse.get("userName").asText(), mappedResponse.get("phoneNumber").asText(), mappedResponse.get("additionalInfo").get("testingSiteId").asText(), notifications);
        }
        else if (mappedResponse.get("isHealthcareWorker").asBoolean())
            user = new HealthcareWorker(mappedResponse.get("id").asText(),mappedResponse.get("givenName").asText(),mappedResponse.get("familyName").asText(),mappedResponse.get("userName").asText(),mappedResponse.get("phoneNumber").asText());
        return user;
    }
}
