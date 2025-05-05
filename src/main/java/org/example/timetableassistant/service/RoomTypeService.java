package org.example.timetableassistant.service;
import org.example.timetableassistant.model.RoomType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class RoomTypeService {
    private static final String BASE_URL = "http://localhost:4567/db/roomType";

    public List<RoomType> getAllRoomTypes() {
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
                    List<RoomType> roomTypes = new ArrayList<>();
                    if (messageNode != null && messageNode.isArray()) {
                        for (JsonNode node : messageNode) {
                            RoomType roomType = new RoomType();
                            roomType.setName(node.get("name").asText());
                            // Set id if present
                            if (node.has("id")) {
                                // RoomType class needs a setId method for this to work
                                try {
                                    java.lang.reflect.Method setId = RoomType.class.getDeclaredMethod("setId", int.class);
                                    setId.invoke(roomType, node.get("id").asInt());
                                } catch (NoSuchMethodException e) {
                                    // Ignore if setter doesn't exist
                                }
                            }
                            roomTypes.add(roomType);
                        }
                    }
                    return roomTypes;
                }
            } else {
                throw new RuntimeException("Failed : HTTP error code : " + responseCode);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching room types", e);
        }
    }

    public String createRoomTypes(String name) throws Exception {
        URL url = new URI(BASE_URL + "?name=" + name).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            return "RoomType created successfully.";
        } else {
            throw new Exception("Failed to create room. HTTP error code: " + responseCode);
        }
    }
}
