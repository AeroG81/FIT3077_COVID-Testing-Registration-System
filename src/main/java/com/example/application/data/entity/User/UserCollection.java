package com.example.application.data.entity.User;

import com.example.application.data.entity.HttpHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;

public class UserCollection {
    private List<String> collection = new ArrayList<>();

    public UserCollection(){

        try {
            getUsersService();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }



    public String searchUserId(String username, String password){
        // if (verifyUser(username,password)) {
        //     collection.forEach(user->{
        //         if (user.getUsername() == username && user.getPassword() == password) {
        //             return user.getUserId();
        //         }
        //     });
        // }
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
            //TestingSite site = new TestingSite(node.get("id").asText(),node.get("name").asText(),node.get("description").asText(),node.get("websiteUrl").asText(),node.get("phoneNumber").asText(),node.get("address").get("latitude").asDouble(),node.get("address").get("longitude").asDouble(),node.get("address").get("unitNumber").asInt(),node.get("address").get("street").asText(),node.get("address").get("street2").asText(),node.get("address").get("suburb").asText(),node.get("address").get("state").asText(),node.get("address").get("postcode").asText(),node.get("additionalInfo").get("facilityType").asText(),node.get("additionalInfo").get("openTime").asText(),node.get("additionalInfo").get("closeTime").asText(),node.get("additionalInfo").get("waitingTime").asText());
            //collection.add(site); Add User
        }
    }

    public boolean verifyUser(String username, String password){
        boolean userIsValid = false;
        String jsonString = "{"+
                "\"userName\":\"" + username + "\"," +
                "\"password\":\"" + password + "\""+
                "}";

        String url = "https://fit3077.com/api/v1/user/login?jwt=false";

        try {
            HttpResponse<String> response = new HttpHelper().postService(url,jsonString);
            if (response.statusCode()==200){;
                userIsValid = true;
            }
            else{
                userIsValid = false;
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        return userIsValid;
    }


}
