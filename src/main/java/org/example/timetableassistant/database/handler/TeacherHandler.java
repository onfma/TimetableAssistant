package org.example.timetableassistant.database.handler;
import com.google.gson.Gson;
import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.TeacherCRUD;
import spark.Request;
import spark.Response;
import java.util.HashMap;
import java.util.Map;

public class TeacherHandler {
    private static final TeacherCRUD teacherCRUD = new TeacherCRUD();

    public static String createTeacher(Request req, Response res) {
        String name = req.queryParams("name");

        if (name == null) {
            res.status(400); // Bad Request
            return "{\"error\":\"Missing required field (name).\"}";
        }

        OperationResult result = teacherCRUD.insertTeacher(name);

        if (result.success) {
            res.status(201); // Created
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }

    public static String getTeacherById(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));

        OperationResult result = teacherCRUD.getTeacherById(id);
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


    public static String updateTeacher(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        String newName = req.queryParams("name");

        if (newName == null) {
            res.status(400);
            return "{\"error\":\"Missing required field (name).\"}";
        }

        OperationResult result = teacherCRUD.updateTeacher(id, newName);

        if (result.success) {
            res.status(200);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }


    public static String deleteTeacher(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));

        OperationResult result = teacherCRUD.deleteTeacher(id);

        if (result.success) {
            res.status(200);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(404);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }


    public static String getTeacherByName(Request req, Response res) {
        String name = req.params("name");

        if (name == null) {
            res.status(400);
            return "{\"error\":\"Missing required field (name).\"}";
        }

        OperationResult result = teacherCRUD.getTeacherByName(name);
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
}
