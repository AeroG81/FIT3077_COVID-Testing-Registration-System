package com.example.application.data.entity.TestingSite;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;

public class TestingSiteCollection {
    private List<TestingSite> collection = new ArrayList<TestingSite>();

    public TestingSiteCollection(){
        try {
            this.getSitesService();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void getSitesService() throws Exception{
        String myApiKey = "7WwqfjwcprP7HPqLRmnmQ8QNzg9MWj";
        String testingSiteUrl = "https://fit3077.com/api/v1/testing-site";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(testingSiteUrl))
                .setHeader("Authorization", myApiKey)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Error checking for this sample code. You can check the status code of your request, as part of performing error handling in your assignment.
        if (response.statusCode() != 200) {
            throw new Exception("Please specify your API key in line 21 to continue using this sample code.");
        }

        // The GET /user endpoint returns a JSON array, so we can loop through the response as we could with a normal array/list.
        ObjectNode[] jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);

        for (ObjectNode node: jsonNodes) {
            TestingSite site = new TestingSite(node.get("id").asText(),node.get("name").asText(),node.get("description").asText(),node.get("websiteUrl").asText(),node.get("phoneNumber").asText(),node.get("address").get("latitude").asDouble(),node.get("address").get("longitude").asDouble(),node.get("address").get("unitNumber").asInt(),node.get("address").get("street").asText(),node.get("address").get("street2").asText(),node.get("address").get("suburb").asText(),node.get("address").get("state").asText(),node.get("address").get("postcode").asText(),node.get("additionalInfo").asText());
            collection.add(site);
        }
    }

    public List<TestingSite> getCollection() {
        return collection;
    }

    public boolean updateCollection() {
        collection.clear();
        try {
            this.getSitesService();
        }
        catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public List<TestingSite> searchCollection(String keyword) {
        List<TestingSite> result = new ArrayList<TestingSite>();

        collection.forEach(testingSite -> {
            if (testingSite.getName().contains(keyword)) {
                result.add(testingSite);
            }
        });

        return result;
    }
}
