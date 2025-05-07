package org.example.timetableassistant.service;

import org.example.timetableassistant.model.Discipline;
import org.example.timetableassistant.model.Teacher;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class TeacherService {
    private static final String BASE_URL = "http://localhost:4567/db/teacher";

    public static String createTeacher(String name) throws Exception{
        String encodedName = java.net.URLEncoder.encode(name, "UTF-8");
        URL url = new URI(BASE_URL + "?name=" + encodedName).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            return "Teacher created successfully.";
        } else {
            throw new Exception("Failed to create teacher. HTTP error code: " + responseCode);
        }
    }

    public static List<Teacher> getAllTeachers() throws Exception{
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
            JSONArray teachersArray = jsonResponse.getJSONArray("message");
            List<Teacher> teachers = new ArrayList<>();
            for (int i = 0; i < teachersArray.length(); i++) {
                JSONObject roomObj = teachersArray.getJSONObject(i);
                int id = roomObj.getInt("id");
                String name = roomObj.getString("name");
                Teacher newTeacher = new Teacher(id, name);
                teachers.add(newTeacher);
            }
            return teachers;
        } else {
            throw new Exception("Failed to get teachers. HTTP error code: " + responseCode);
        }
    }

    public static Teacher getTeacherById(int id) throws Exception{
        URL url = new URI(BASE_URL + "/get-by-id/" + id).toURL();
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

            if (message instanceof JSONArray) {
                JSONArray teachersArray = (JSONArray) message;
                for (int i = 0; i < teachersArray.length(); i++) {
                    JSONObject teacherObj = teachersArray.getJSONObject(i);
                    int idTeacher = teacherObj.getInt("id");
                    String name = teacherObj.getString("name");
                    if (idTeacher == id) {
                        return new Teacher(idTeacher, name);
                    }
                }
            } else if (message instanceof JSONObject) {
                JSONObject teacherObj = (JSONObject) message;
                int idTeacher = teacherObj.getInt("id");
                String name = teacherObj.getString("name");
                if (idTeacher == id) {
                    return new Teacher(idTeacher, name);
                }
            } else {
                throw new Exception("Unexpected message format in response.");
            }
        } else {
            throw new Exception("Failed to get teacher. HTTP error code: " + responseCode);
        }
        return null;
    }

    public String editTeacher(int id, String name) throws Exception{
        String encodedName = java.net.URLEncoder.encode(name, "UTF-8");
        URL url = new URI(BASE_URL + "/" + id + "?name=" + encodedName).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return "Teacher updated successfully.";
        } else {
            throw new Exception("Failed to update teacher. HTTP error code: " + responseCode);
        }
    }

    public String deleteTeacher(int id) throws Exception{
        URL url = new URI(BASE_URL + "/" + id).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return "Teacher deleted successfully.";
        } else {
            throw new Exception("Failed to delete teacher. HTTP error code: " + responseCode);
        }
    }

}
