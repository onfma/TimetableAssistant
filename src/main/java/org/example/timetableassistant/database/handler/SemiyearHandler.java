package org.example.timetableassistant.database.handler;

import com.google.gson.Gson;
import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.SemiyearCRUD;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class SemiyearHandler {
    private static final SemiyearCRUD semiyearsCRUD = new SemiyearCRUD();

    public static String createSemiyear(Request req, Response res) {
        String name = req.queryParams("name");
        String yearParam = req.queryParams("study_year");

        if (name == null || name.isEmpty() || yearParam == null) {
            res.status(400);
            return "{\"error\":\"Missing required fields (name, study_year).\"}";
        }

        int studyYear;
        try {
            studyYear = Integer.parseInt(yearParam);
        } catch (NumberFormatException e) {
            res.status(400);
            return "{\"error\":\"Invalid study_year. Must be an integer.\"}";
        }

        OperationResult result = semiyearsCRUD.insertSemiyear(name, studyYear);

        if (result.success) {
            res.status(201);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }

    public static String getSemiyearById(Request req, Response res) {
        int id;
        try {
            id = Integer.parseInt(req.params(":id"));
        } catch (NumberFormatException e) {
            res.status(400);
            return "{\"error\":\"Invalid ID format.\"}";
        }

        OperationResult result = semiyearsCRUD.getSemiyearById(id);
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

    public static String getSemiyearByNameAndYear(Request req, Response res) {
        String name = req.queryParams("name");
        String yearParam = req.queryParams("study_year");

        if (name == null || name.isEmpty() || yearParam == null) {
            res.status(400);
            return "{\"error\":\"Missing required fields (name, study_year).\"}";
        }

        int studyYear;
        try {
            studyYear = Integer.parseInt(yearParam);
        } catch (NumberFormatException e) {
            res.status(400);
            return "{\"error\":\"Invalid study_year. Must be an integer.\"}";
        }

        OperationResult result = semiyearsCRUD.getSemiyearByNameAndYear(name, studyYear);
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

    public static String updateSemiyear(Request req, Response res) {
        int id;
        try {
            id = Integer.parseInt(req.params(":id"));
        } catch (NumberFormatException e) {
            res.status(400);
            return "{\"error\":\"Invalid ID format.\"}";
        }

        String newName = req.queryParams("name");
        String newStudyYearStr = req.queryParams("study_year");

        if (newName == null || newName.isEmpty() || newStudyYearStr == null) {
            res.status(400);
            return "{\"error\":\"Missing required fields (name, study_year).\"}";
        }

        int newStudyYear;
        try {
            newStudyYear = Integer.parseInt(newStudyYearStr);
        } catch (NumberFormatException e) {
            res.status(400);
            return "{\"error\":\"Invalid study_year. Must be an integer.\"}";
        }

        OperationResult result = semiyearsCRUD.updateSemiyear(id, newName, newStudyYear);

        if (result.success) {
            res.status(200);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(404);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }

    public static String deleteSemiyear(Request req, Response res) {
        int id;
        try {
            id = Integer.parseInt(req.params(":id"));
        } catch (NumberFormatException e) {
            res.status(400);
            return "{\"error\":\"Invalid ID format.\"}";
        }

        OperationResult result = semiyearsCRUD.deleteSemiyear(id);

        if (result.success) {
            res.status(200);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(404);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }
}
