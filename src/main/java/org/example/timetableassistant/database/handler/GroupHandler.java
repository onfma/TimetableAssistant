package org.example.timetableassistant.database.handler;

import com.google.gson.Gson;
import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.GroupCRUD;
import org.example.timetableassistant.database.crud.TeacherCRUD;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class GroupHandler {
    private static final GroupCRUD groupCRUD = new GroupCRUD();

    public static String createGroup(Request req, Response res) {
        String name = req.queryParams("name");
        String semiyearIdStr = req.queryParams("semiyear_id");

        if (name == null || semiyearIdStr == null) {
            res.status(400);
            return "{\"error\":\"Missing required fields (name, semiyear_id).\"}";
        }

        int semiyearId = Integer.parseInt(semiyearIdStr);
        OperationResult result = groupCRUD.insertGroup(name, semiyearId);

        if (result.success) {
            res.status(201);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }

    public static String getGroupById(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        OperationResult result = groupCRUD.getGroupById(id);
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

    public static String getAllGroups(Request req, Response res) {
        OperationResult result = groupCRUD.getAllGroups();

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


    public static String updateGroup(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        String newName = req.queryParams("name");
        String newSemiyearIdStr = req.queryParams("semiyear_id");

        if (newName == null || newSemiyearIdStr == null) {
            res.status(400);
            return "{\"error\":\"Missing required fields (name, semiyear_id).\"}";
        }

        int newSemiyearId = Integer.parseInt(newSemiyearIdStr);
        OperationResult result = groupCRUD.updateGroup(id, newName, newSemiyearId);

        if (result.success) {
            res.status(200);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }

    public static String deleteGroup(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        OperationResult result = groupCRUD.deleteGroup(id);

        if (result.success) {
            res.status(200);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(404);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }

    public static String getGroupByName(Request req, Response res) {
        String name = req.params(":name");

        if (name == null) {
            res.status(400);
            return "{\"error\":\"Missing required field (name).\"}";
        }

        OperationResult result = groupCRUD.getGroupByName(name);
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
