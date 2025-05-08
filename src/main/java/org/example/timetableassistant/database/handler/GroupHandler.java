package org.example.timetableassistant.database.handler;

import com.google.gson.Gson;
import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.GroupCRUD;
import org.example.timetableassistant.database.crud.TeacherCRUD;
import org.example.timetableassistant.model.Semiyear;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class GroupHandler {
    private static final GroupCRUD groupCRUD = new GroupCRUD();

    public static String createGroup(Request req, Response res) {
        String numberStr = req.queryParams("number");
        String semiyearVal = req.queryParams("semiyear");

        if (numberStr == null || semiyearVal == null) {
            res.status(400);
            return "{\"error\":\"Missing required fields (number, semiyear).\"}";
        }

        try{
            int number = Integer.parseInt(numberStr);
            Semiyear semiyear = Semiyear.fromString(semiyearVal);
            OperationResult result = groupCRUD.insertGroup(number, semiyear.getValue());

            if (result.success) {
                res.status(201);
                return "{\"message\":\"" + result.message + "\"}";
            } else {
                res.status(500);
                return "{\"error\":\"" + result.message + "\"}";
            }
        } catch (IllegalArgumentException e) {
            res.status(400);
            return "{\"error\":\"Invalid semiyear value: " + semiyearVal + "\"}";
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
        String newNumberStr = req.queryParams("number");
        String newSemiyear = req.queryParams("semiyear");

        if (newNumberStr == null || newSemiyear == null) {
            res.status(400);
            return "{\"error\":\"Missing required fields (number, semiyear).\"}";
        }

        try {
            int newNumber = Integer.parseInt(newNumberStr);
            Semiyear semiyear = Semiyear.fromString(newSemiyear);
            OperationResult result = groupCRUD.updateGroup(id, newNumber, semiyear.getValue());

            if (result.success) {
                res.status(200);
                return "{\"message\":\"" + result.message + "\"}";
            } else {
                res.status(500);
                return "{\"error\":\"" + result.message + "\"}";
            }
        } catch ( IllegalArgumentException e){
                res.status(400);
                return "{\"error\":\"Invalid semiyear value: " + newSemiyear + "\"}";
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

    public static String getGroupByNumberAndSemiyear(Request req, Response res) {
        String numberStr = req.queryParams("number");
        String semiyear = req.queryParams("semiyear");

        if (numberStr == null || semiyear == null) {
            res.status(400);
            return "{\"error\":\"Missing required field (name).\"}";
        }

        int number = Integer.parseInt(numberStr);
        OperationResult result = groupCRUD.getGroupByNumberAndSemiyear(number, semiyear);
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
