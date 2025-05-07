package org.example.timetableassistant.service;

import org.example.timetableassistant.model.Group;
import org.example.timetableassistant.model.Semiyear;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GroupService {
    private static final String BASE_URL = "http://localhost:4567/db/group";

    public static String createGroup(int number, Semiyear semiyear) throws Exception {
        String encodedSemiyear = java.net.URLEncoder.encode(semiyear.getValue(), "UTF-8");
        URL url = new URI(BASE_URL + "?number=" + number + "&semiyear=" + encodedSemiyear).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            return "Group created successfully";
        } else {
            throw new Exception("Failed to create group. HTTP error code: " + responseCode);
        }
    }

    public static List<Group> getAllGroups() throws Exception {
        URL url = new URI(BASE_URL + "/get-all").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray groupsArray = jsonResponse.getJSONArray("message");
            List<Group> groups = new ArrayList<Group>();
            for (int i = 0; i < groupsArray.length(); i++) {
                JSONObject groupObject = groupsArray.getJSONObject(i);
                int groupId = groupObject.getInt("id");
                int groupNumber = groupObject.getInt("number");
                String groupSemiyear = groupObject.getString("semiyear");

                groups.add(new Group(groupId, groupNumber, Semiyear.fromString(groupSemiyear)));
            }
            return groups;
        } else {
            throw new Exception("Failed to get all groups. HTTP error code: " + responseCode);
        }
    }

    public static String updateGroup(int groupId, int number, Semiyear semiyear) throws Exception {
        String encodedSemiyear = java.net.URLEncoder.encode(semiyear.getValue(), "UTF-8");
        URL url = new URI(BASE_URL + "/" + groupId + "?number=" + number + "&semiyear=" + encodedSemiyear).toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return "Group updated successfully";
        } else {
            throw new Exception("Failed to update group. HTTP error code: " + responseCode);
        }
    }

    public static String deleteGroup(int groupId) throws Exception {
        URL url = new URI(BASE_URL + "/" + groupId).toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return "Group deleted successfully";
        } else {
            throw new Exception("Failed to delete group. HTTP error code: " + responseCode);
        }
    }
}