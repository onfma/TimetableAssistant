package org.example.timetableassistant.database.handler;
import com.google.gson.Gson;
import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.DisciplineCRUD;
import spark.Request;
import spark.Response;
import java.util.HashMap;
import java.util.Map;

public class DisciplineHandler {
    private static final DisciplineCRUD disciplineCRUD = new DisciplineCRUD();


    public static String createDiscipline(Request req, Response res) {
        String name = req.queryParams("name");
        int teacherId = Integer.parseInt(req.queryParams("teacher_id"));

        if (name == null || teacherId == 0) {
            res.status(400);  // Bad Request
            return "Missing required fields (name, teacher_id).";
        }

        OperationResult result = disciplineCRUD.insertDiscipline(name, teacherId);

        if (result.success) {
            res.status(201);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }


    public static String getDisciplineById(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));

        OperationResult result = disciplineCRUD.getDisciplineById(id);

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

    public static String updateDiscipline(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        String newName = req.queryParams("name");
        int newTeacherId = Integer.parseInt(req.queryParams("teacher_id"));

        if (newName == null || newTeacherId == 0) {
            res.status(400);  // Bad Request
            return "Missing required fields (name, teacher_id).";
        }

        OperationResult result = disciplineCRUD.updateDiscipline(id, newName, newTeacherId);

        if (result.success) {
            res.status(200);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }


    public static String deleteDiscipline(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));

        OperationResult result = disciplineCRUD.deleteDiscipline(id);

        if (result.success) {
            res.status(200);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }
}
