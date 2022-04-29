package com.example.application.data.entity.User;

import com.example.application.data.entity.HttpHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.http.HttpResponse;

import java.util.ArrayList;

import java.util.List;

public class UserCollection {
    private List<User> collection = new ArrayList<User>();

    public UserCollection(){
        try {
            getUsersService();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public List<User> getCollection() {
        return collection;
    }

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

    public void getUsersService() throws Exception{
        String userUrl = "https://fit3077.com/api/v1/user";

        HttpResponse<String> response = new HttpHelper().getService(userUrl);

        // Error checking for this sample code. You can check the status code of your request, as part of performing error handling in your assignment.
        if (response.statusCode() != 200) {
            throw new Exception("Please specify your API key in line 21 to continue using this sample code.");
        }

        // The GET /user endpoint returns a JSON array, so we can loop through the response as we could with a normal array/list.
        ObjectNode[] jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);

        for (ObjectNode node: jsonNodes) {
            User user = null;
            if(node.get("isCustomer").asBoolean())
                user = new Resident(node.get("id").asText(),node.get("givenName").asText(),node.get("familyName").asText(),node.get("userName").asText(),node.get("phoneNumber").asText());
            else if (node.get("isReceptionist").asBoolean())
                user = new FacilityStaff(node.get("id").asText(),node.get("givenName").asText(),node.get("familyName").asText(),node.get("userName").asText(),node.get("phoneNumber").asText());
            else if (node.get("isHealthcareWorker").asBoolean())
                user = new ExpertStaff(node.get("id").asText(),node.get("givenName").asText(),node.get("familyName").asText(),node.get("userName").asText(),node.get("phoneNumber").asText());
            if(user!=null)
                collection.add(user);
        }
    }

    public boolean checkIsCustomer(String username) throws Exception {
        String userUrl = "https://fit3077.com/api/v1/user";

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

    public boolean checkIsReceptionist(String username) throws Exception {
        String userUrl = "https://fit3077.com/api/v1/user";

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

    public boolean checkIsHealthcareWorker(String username) throws Exception {
        String userUrl = "https://fit3077.com/api/v1/user";

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
    private boolean verifyUserService(String username, String password){
        boolean userIsValid;
        String jsonString = "{"+
                "\"userName\":\"" + username + "\"," +
                "\"password\":\"" + password + "\""+
                "}";

        String url = "https://fit3077.com/api/v1/user/login?jwt=false";

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

    public User addUserService(String givenName,String familyName, String userName, String password, String phoneNumber, boolean isCustomer, boolean isAdmin, boolean isHealthCareWorker, String additionalInfo) throws Exception{
        String url = "https://fit3077.com/api/v1/user";
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

    public User addUserService(String givenName,String familyName, String userName, String password, String phoneNumber, boolean isCustomer, boolean isAdmin, boolean isHealthCareWorker) throws Exception{
        String url = "https://fit3077.com/api/v1/user";
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

    private User createUser(ObjectNode mappedResponse){
        User user = null;
        if(mappedResponse.get("isCustomer").asBoolean())
            user = new Resident(mappedResponse.get("id").asText(),mappedResponse.get("givenName").asText(),mappedResponse.get("familyName").asText(),mappedResponse.get("userName").asText(),mappedResponse.get("phoneNumber").asText());
        else if (mappedResponse.get("isReceptionist").asBoolean())
            user = new FacilityStaff(mappedResponse.get("id").asText(),mappedResponse.get("givenName").asText(),mappedResponse.get("familyName").asText(),mappedResponse.get("userName").asText(),mappedResponse.get("phoneNumber").asText());
        else if (mappedResponse.get("isHealthcareWorker").asBoolean())
            user = new ExpertStaff(mappedResponse.get("id").asText(),mappedResponse.get("givenName").asText(),mappedResponse.get("familyName").asText(),mappedResponse.get("userName").asText(),mappedResponse.get("phoneNumber").asText());
        return user;
    }
}
