package org.example.timetableassistant.database.handler;

import com.google.gson.Gson;
import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.RoomCRUD;
import org.example.timetableassistant.database.crud.RoomTypesCRUD;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class RoomTypesHandler {
    private static final RoomTypesCRUD roomTypeCRUD = new RoomTypesCRUD();


    public static String createRoomType(Request req, Response res) {
        String name = req.queryParams("name");

        if (name == null || name.isEmpty()) {
            res.status(400);  // Bad Request
            return "Missing required fields (name).";
        }

        OperationResult result = roomTypeCRUD.insertRoomType(name);

        if (result.success) {
            res.status(201);  // Created
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);  // Internal Server Error
            return "{\"error\":\"" + result.message + "\"}";
        }
    }

    public static String getRoomTypeById(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));

        OperationResult result = roomTypeCRUD.getRoomTypeById(id);

        Gson gson = new Gson();

        if (result.success) {
            res.status(200);  // OK
            Map<String, Object> response = new HashMap<>();
            response.put("message", result.message);
            return gson.toJson(response);
        } else {
            res.status(404);  // Not Found
            Map<String, Object> response = new HashMap<>();
            response.put("error", result.message);
            return gson.toJson(response);
        }
    }


    public static String updateRoomType(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        String newName = req.queryParams("name");

        if (newName == null || newName.isEmpty()) {
            res.status(400);  // Bad Request
            return "Missing required fields (name).";
        }

        OperationResult result = roomTypeCRUD.updateRoomType(id, newName);

        if (result.success) {
            res.status(200);  // OK
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);  // Internal Server Error
            return "{\"error\":\"" + result.message + "\"}";
        }
    }


    public static String deleteRoomType(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));

        OperationResult result = roomTypeCRUD.deleteRoomType(id);

        if (result.success) {
            res.status(200);  // OK
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);  // Internal Server Error
            return "{\"error\":\"" + result.message + "\"}";
        }
    }

    public static String getAllRoomTypes(Request req, Response res) {
        OperationResult result = roomTypeCRUD.getAllRoomTypes();
        Gson gson = new Gson();

        if (result.success) {
            res.status(200);  // OK
            Map<String, Object> response = new HashMap<>();
            response.put("message", result.message);
            return gson.toJson(response);
        } else {
            res.status(404);  // Not Found
            Map<String, Object> response = new HashMap<>();
            response.put("error", result.message);
            return gson.toJson(response);
        }
    }


}
