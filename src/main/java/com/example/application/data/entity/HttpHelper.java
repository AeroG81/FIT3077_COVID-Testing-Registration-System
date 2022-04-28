package com.example.application.data.entity;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpHelper {
    private final String APIKEY;

    public HttpHelper(){
        APIKEY = "7WwqfjwcprP7HPqLRmnmQ8QNzg9MWj";
    }

    public HttpResponse<String> postService(String url, String jsonString) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(url))
                .setHeader("Authorization", APIKEY)
                .header("Content-Type","application/json") // This header needs to be set when sending a JSON request body.
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getService(String url) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(url))
                .setHeader("Authorization", APIKEY)
                .header("Content-Type","application/json") // This header needs to be set when sending a JSON request body.
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> patchService(String url, String jsonString, String id) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(url+"/"+id))
                .setHeader("Authorization", APIKEY)
                .header("Content-Type","application/json") // This header needs to be set when sending a JSON request body.
                .method("PATCH",HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> deleteService(String url) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(url))
                .setHeader("Authorization", APIKEY)
                .header("Content-Type","application/json") // This header needs to be set when sending a JSON request body.
                .DELETE()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

}
