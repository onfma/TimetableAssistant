package org.example.timetableassistant.database.handler;
import com.google.gson.Gson;
import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.RoomCRUD;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class RoomHandler {
    private static final RoomCRUD roomCRUD = new RoomCRUD();

    public static String createRoom(Request req, Response res) {
        String name = req.queryParams("name");
        int capacity = Integer.parseInt(req.queryParams("capacity"));
        String type = req.queryParams("type");

        if (name == null || type == null) {
            res.status(400);  // Bad Request
            return "Missing required fields (name, capacity, type).";
        }

        OperationResult result = roomCRUD.insertRoom(name, capacity, type);

        if (result.success) {
            res.status(201);  // Created
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);  // Internal Server Error
            return "{\"error\":\"" + result.message + "\"}";
        }
    }

    public static String getRoomById(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));

        OperationResult result = roomCRUD.getRoomById(id);

        Gson gson = new Gson();
        Map<String, Object> response = new HashMap<>();

        if (result.success) {
            res.status(200);
            response.put("message", result.message);
            return gson.toJson(response);
        } else {
            res.status(404);
            response.put("error", result.message);
            return gson.toJson(response);
        }
    }

    public static String updateRoom(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        String newName = req.queryParams("name");
        int newCapacity = Integer.parseInt(req.queryParams("capacity"));
        String newType = req.queryParams("type");

        if (newName == null || newType == null) {
            res.status(400);  // Bad Request
            return "Missing required fields (name, capacity, type).";
        }

        OperationResult result = roomCRUD.updateRoom(id, newName, newCapacity, newType);

        if (result.success) {
            res.status(200);  // OK
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);  // Internal Server Error
            return "{\"error\":\"" + result.message + "\"}";
        }
    }

    public static String deleteRoom(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));

        OperationResult result = roomCRUD.deleteRoom(id);

        if (result.success) {
            res.status(200);  // OK
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);  // Internal Server Error
            return "{\"error\":\"" + result.message + "\"}";
        }
    }
}
