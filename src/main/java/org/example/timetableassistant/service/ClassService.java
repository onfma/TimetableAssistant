package org.example.timetableassistant.service;

import org.example.timetableassistant.database.crud.ClassType;
import org.example.timetableassistant.model.Class;
import org.example.timetableassistant.model.Semiyear;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ClassService {
    private static final String BASE_URL = "http://localhost:4567/db/class";

    public static String createClass(int disciplineId, ClassType classType, int roomId, int timeSlotId, Semiyear semiyear, Integer groupId, int teacherId) throws Exception {
        if (classType == classType.COURSE){
            groupId = null;
        } else{
            semiyear = null;
        }

        String query = String.format("?discipline_id=%d&class_type=%s&room_id=%d&time_slot_id=%d&teacher_id=%d",
                disciplineId, URLEncoder.encode(classType.name(), "UTF-8"), roomId, timeSlotId, teacherId);
        if ( semiyear != null ) {
            query += "&semiyear=" + URLEncoder.encode(semiyear.name(), "UTF-8");
        }
        if (groupId != null) {
            query += String.format("&group_id=%d", groupId);
        }

        URL url = new URI(BASE_URL + query).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            return "Class created successfully";
        } else {
            throw new Exception("Failed to create class. HTTP error code: " + responseCode);
        }
    }

    public static List<Class> getAllClasses() throws Exception {
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
            JSONArray classesArray = jsonResponse.getJSONArray("message");
            List<Class> classes = new ArrayList<>();
            for (int i = 0; i < classesArray.length(); i++) {
                JSONObject classObject = classesArray.getJSONObject(i);
                Class cls = new Class(
                        classObject.getInt("id"),
                        classObject.getInt("discipline_id"),
                        ClassType.valueOf(classObject.getString("class_type")),
                        classObject.getInt("room_id"),
                        classObject.getInt("time_slot_id"),
                        classObject.opt("group_id") instanceof Integer ? classObject.getInt("group_id") : null,
                        classObject.optString("semiyear", null) != null ? Semiyear.valueOf(classObject.getString("semiyear")) : null,
                        classObject.getInt("teacher_id")
                );
                classes.add(cls);
            }
            return classes;
        } else {
            throw new Exception("Failed to get all classes. HTTP error code: " + responseCode);
        }
    }

    public static String updateClass(int id, int disciplineId, ClassType classType, int roomId, int timeSlotId, Semiyear semiyear, Integer groupId, int teacherId) throws Exception {
        String query = String.format("?discipline_id=%d&class_type=%s&room_id=%d&time_slot_id=%d&teacher_id=%d",
                disciplineId, URLEncoder.encode(classType.name(), "UTF-8"), roomId, timeSlotId, teacherId);
        if (semiyear != null) {
            query += "&semiyear=" + URLEncoder.encode(semiyear.name(), "UTF-8");
        }
        if (groupId != null) {
            query += "&group_id=" + groupId;
        }

        URL url = new URI(BASE_URL + "/" + id + query).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return "Class updated successfully";
        } else {
            throw new Exception("Failed to update class. HTTP error code: " + responseCode);
        }
    }

    public static String deleteClass(int id) throws Exception {
        URL url = new URI(BASE_URL + "/" + id).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return "Class deleted successfully";
        } else {
            throw new Exception("Failed to delete class. HTTP error code: " + responseCode);
        }
    }
}
