package org.example.timetableassistant.service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;

public class ClassService {
    private static final String BASE_URL = "http://localhost:4567/db/class";

    public static String createClass(String jsonPayload) throws Exception {
        URL url = new URI(BASE_URL).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(jsonPayload.getBytes());
            os.flush();
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            return "Class created successfully.";
        } else {
            throw new Exception("Failed to create class. Response code: " + responseCode);
        }
    }
}
