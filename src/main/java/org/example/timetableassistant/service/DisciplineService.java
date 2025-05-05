package org.example.timetableassistant.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.timetableassistant.model.Discipline;
import org.example.timetableassistant.model.RoomType;
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

    public Discipline getDisciplineById(int id) throws Exception {
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
            JSONArray disciplinesArray = jsonResponse.getJSONArray("message");
            List<Discipline> disciplines = new ArrayList<>();
            for (int i = 0; i < disciplinesArray.length(); i++) {
                JSONObject roomObj = disciplinesArray.getJSONObject(i);
                int idDiscipline = roomObj.getInt("id");
                if (idDiscipline == id) {
                    String name = roomObj.getString("name");
                    disciplines.add(new Discipline(idDiscipline, name));
                    return disciplines.getFirst();
                }
            }
        } else {
            throw new Exception("Failed to get discipline. HTTP error code: " + responseCode);
        }
        return null;
    }

    public List<Discipline> getAllDisciplines() throws Exception {
        try {
            URL url = new URI(BASE_URL + "/get-all").toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (java.util.Scanner scanner = new java.util.Scanner(conn.getInputStream())) {
                    scanner.useDelimiter("\\A");
                    String json = scanner.hasNext() ? scanner.next() : "";

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(json);
                    JsonNode messageNode = root.get("message");
                    List<Discipline> disciplines = new ArrayList<>();
                    if (messageNode != null && messageNode.isArray()) {
                        for (JsonNode node : messageNode) {
                            Discipline discipline = new Discipline();
                            discipline.setName(node.get("name").asText());
                            // Set id if present
                            if (node.has("id")) {
                                try {
                                    java.lang.reflect.Method setId = Discipline.class.getDeclaredMethod("setId", int.class);
                                    setId.invoke(discipline, node.get("id").asInt());
                                } catch (NoSuchMethodException e) {
                                    // Ignore if setter doesn't exist
                                }
                            }
                            disciplines.add(discipline);
                        }
                    }
                    return disciplines;
                }
            } else {
                throw new RuntimeException("Failed : HTTP error code : " + responseCode);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching disciplines", e);
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
