package org.example.timetableassistant.database.handler;

import com.google.gson.Gson;
import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.DisciplineAllocationsCRUD;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class DisciplineAllocationHandler {
    private static final DisciplineAllocationsCRUD disciplineAllocationCRUD = new DisciplineAllocationsCRUD();

    public static String createDisciplineAllocation(Request req, Response res) {
        int disciplineId = Integer.parseInt(req.queryParams("discipline_id"));
        int teacherId = Integer.parseInt(req.queryParams("teacher_id"));
        int classTypeId = Integer.parseInt(req.queryParams("class_type_id"));
        int hoursPerWeek = Integer.parseInt(req.queryParams("hours_per_week"));

        if (disciplineId == 0 || teacherId == 0 || classTypeId == 0 || hoursPerWeek == 0) {
            res.status(400);  // Bad Request
            return "Missing required fields.";
        }

        OperationResult result = disciplineAllocationCRUD.insertDisciplineAllocation(disciplineId, teacherId, classTypeId, hoursPerWeek);

        if (result.success) {
            res.status(201);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }

    public static String getDisciplineAllocationById(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));

        OperationResult result = disciplineAllocationCRUD.getDisciplineAllocationById(id);

        Gson gson = new Gson();

        if (result.success) {
            res.status(200);
            Map<String, Object> response = new HashMap<>();
            response.put("message", result.message);
            return gson.toJson(response);
        } else {
            res.status(404);
            Map<String, Object> response = new HashMap<>();
            response.put("error", result.message);
            return gson.toJson(response);
        }
    }

    public static String getAllDisciplineAllocations(Request req, Response res) {
        OperationResult result = disciplineAllocationCRUD.getAllDisciplineAllocations();

        Gson gson = new Gson();
        Map<String, Object> response = new HashMap<>();

        if (result.success) {
            res.status(200);
            response.put("message", result.message);
        } else {
            res.status(404);
            response.put("error", result.message);
        }
        return gson.toJson(response);
    }

    public static String getAllDisciplineAllocationsByTeacherId(Request req, Response res) {
        int teacherId;
        try {
            teacherId = Integer.parseInt(req.params(":id"));
        } catch (NumberFormatException e) {
            res.status(400);
            return "{\"error\":\"ID invalid pentru profesor.\"}";
        }

        OperationResult result = disciplineAllocationCRUD.getAllDisciplineAllocationsByTeacherId(teacherId);

        Gson gson = new Gson();
        Map<String, Object> response = new HashMap<>();

        if (result.success) {
            res.status(200);
            response.put("message", result.message);
        } else {
            res.status(404);
            response.put("error", result.message);
        }

        return gson.toJson(response);
    }


    public static String updateDisciplineAllocation(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        int newDisciplineId = Integer.parseInt(req.queryParams("discipline_id"));
        int newTeacherId = Integer.parseInt(req.queryParams("teacher_id"));
        int newClassTypeId = Integer.parseInt(req.queryParams("class_type_id"));
        int newHoursPerWeek = Integer.parseInt(req.queryParams("hours_per_week"));

        if (newDisciplineId == 0 || newTeacherId == 0 || newClassTypeId == 0 || newHoursPerWeek == 0) {
            res.status(400);  // Bad Request
            return "Missing required fields.";
        }

        OperationResult result = disciplineAllocationCRUD.updateDisciplineAllocation(id, newDisciplineId, newTeacherId, newClassTypeId, newHoursPerWeek);

        if (result.success) {
            res.status(201);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }

    public static String deleteDisciplineAllocation(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));

        OperationResult result = disciplineAllocationCRUD.deleteDisciplineAllocation(id);

        if (result.success) {
            res.status(201);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }

}
