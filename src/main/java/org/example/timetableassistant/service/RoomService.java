package org.example.timetableassistant.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;

public class RoomService {
    private static final String BASE_URL = "http://localhost:4567/db/room";

    public static String createRoom(String name, int capacity, String type) throws Exception {
        URL url = new URI(BASE_URL + "?name=" + name + "&capacity=" + capacity+ "&type=" + capacity).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            return "Room created successfully.";
        } else {
            throw new Exception("Failed to create room. HTTP error code: " + responseCode);
        }
    }
    
}
