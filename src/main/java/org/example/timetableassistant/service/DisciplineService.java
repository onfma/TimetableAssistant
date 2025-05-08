package org.example.timetableassistant.service;

import org.example.timetableassistant.model.Discipline;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class DisciplineService {
    private static final String BASE_URL = "http://localhost:4567/db/discipline";

    public static String createDiscipline(String name) throws Exception {
        String encodedName = java.net.URLEncoder.encode(name, "UTF-8");
        URL url = new URI(BASE_URL + "?name=" + encodedName).toURL();
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

    public static Discipline getDisciplineById(int id) throws Exception {
        URL url = new URI(BASE_URL + "/" + id).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            Object message = jsonResponse.get("message");

            if (message instanceof JSONArray disciplinesArray) {
                for (int i = 0; i < disciplinesArray.length(); i++) {
                    JSONObject roomObj = disciplinesArray.getJSONObject(i);
                    int idDiscipline = roomObj.getInt("id");
                    if (idDiscipline == id) {
                        String name = roomObj.getString("name");
                        return new Discipline(idDiscipline, name);
                    }
                }
            } else if (message instanceof JSONObject disciplineObj) {
                int idDiscipline = disciplineObj.getInt("id");
                String name = disciplineObj.getString("name");
                if (idDiscipline == id) {
                    return new Discipline(idDiscipline, name);
                }
            } else {
                throw new Exception("Unexpected message format in response.");
            }
        } else {
            throw new Exception("Failed to get discipline. HTTP error code: " + responseCode);
        }
        return null;
    }

    public static List<Discipline> getAllDisciplines() throws Exception {
        URL url = new URI(BASE_URL + "/get-all").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray disciplinesArray = jsonResponse.getJSONArray("message");
            List<Discipline> disciplines = new ArrayList<>();
            for (int i = 0; i < disciplinesArray.length(); i++) {
                JSONObject disciplineObj = disciplinesArray.getJSONObject(i);
                int id = disciplineObj.getInt("id");
                String name = disciplineObj.getString("name");
                disciplines.add(new Discipline(id, name));
            }
            return disciplines;
        } else {
            throw new Exception("Failed to get all disciplines. HTTP error code: " + responseCode);
        }
    }

    public static String editDiscipline(int id, String newName) throws Exception {
        String encodedName = java.net.URLEncoder.encode(newName, "UTF-8");
        URL url = new URI(BASE_URL + "/" + id + "?name=" + encodedName).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return "Discipline updated successfully.";
        } else {
            throw new Exception("Failed to update discipline. HTTP error code: " + responseCode);
        }
    }

    public static String deleteDiscipline(int id) throws Exception {
        URL url = new URI(BASE_URL + "/" + id).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return "Discipline deleted successfully.";
        } else {
            throw new Exception("Failed to delete discipline. HTTP error code: " + responseCode);
        }
    }
}
