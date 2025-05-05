package org.example.timetableassistant.service;

import org.example.timetableassistant.model.Room;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class RoomService {
    private static final String BASE_URL = "http://localhost:4567/db/room";

    public static String createRoom(String name, int capacity, int type) throws Exception {
        URL url = new URI(BASE_URL + "?name=" + name + "&capacity=" + capacity+ "&room_type_id=" + type).toURL();
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

    public List<Room> getAllRooms() throws Exception {
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
            JSONArray roomsArray = jsonResponse.getJSONArray("message");
            List<Room> rooms = new ArrayList<>();
            for (int i = 0; i < roomsArray.length(); i++) {
                JSONObject roomObj = roomsArray.getJSONObject(i);
                int id = roomObj.getInt("id");
                String name = roomObj.getString("name");
                int typeId = roomObj.getInt("room_type_id");
                RoomTypeService roomTypeService = new RoomTypeService();
                String typeName = roomTypeService.getRoomTypeById(typeId).getName();
                int capacity = roomObj.getInt("capacity");
                rooms.add(new Room(id, name, typeName, capacity));
            }
            return rooms;
        } else {
            throw new Exception("Failed to get rooms. HTTP error code: " + responseCode);
        }
    }

    public String editRoom(int id, String name, int capacity, int type) throws Exception {
        URL url = new URI(BASE_URL + "/" + id + "?name=" + name + "&capacity=" + capacity + "&room_type_id=" + type).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return "Room updated successfully.";
        } else {
            throw new Exception("Failed to update room. HTTP error code: " + responseCode);
        }
    }
    
}
