package org.example.timetableassistant.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;

public class DisciplineService {
    private static final String BASE_URL = "http://localhost:4567/db/discipline";

    public static String createDiscipline(String name, int teacherId) throws Exception {
        URL url = new URI(BASE_URL + "?name=" + name + "&teacher_id=" + teacherId).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            return "Discipline created successfully.";
        } else {
            throw new Exception("Failed to create discipline. HTTP error code: " + responseCode);
        }
    }
}
