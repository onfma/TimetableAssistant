package org.example.timetableassistant.database.handler;
import com.google.gson.Gson;
import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.StudentCRUD;
import spark.Request;
import spark.Response;
import java.util.HashMap;
import java.util.Map;

public class StudentHandler {
    private static final StudentCRUD studentCRUD = new StudentCRUD();

    public static String createStudent(Request req, Response res) {
        String name = req.queryParams("name");
        String groupIdStr = req.queryParams("group_id");

        if (name == null || groupIdStr == null) {
            res.status(400);
            return "{\"error\":\"Missing required fields (name, group_id).\"}";
        }

        int groupId = Integer.parseInt(groupIdStr);
        OperationResult result = studentCRUD.insertStudent(name, groupId);

        if (result.success) {
            res.status(201);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }

    public static String getStudentById(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        OperationResult result = studentCRUD.getStudentById(id);
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

    public static String getAllStudents(Request req, Response res) {
        OperationResult result = studentCRUD.getAllStudents();
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


    public static String updateStudent(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        String newName = req.queryParams("name");
        String newGroupIdStr = req.queryParams("group_id");

        if (newName == null || newGroupIdStr == null) {
            res.status(400);
            return "{\"error\":\"Missing required fields (name, group_id).\"}";
        }

        int newGroupId = Integer.parseInt(newGroupIdStr);
        OperationResult result = studentCRUD.updateStudent(id, newName, newGroupId);

        if (result.success) {
            res.status(200);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }

    public static String deleteStudent(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        OperationResult result = studentCRUD.deleteStudent(id);

        if (result.success) {
            res.status(200);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(404);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }

    public static String getStudentsByGroupId(Request req, Response res) {
        int groupId = Integer.parseInt(req.params(":group_id"));
        OperationResult result = studentCRUD.getStudentsByGroupId(groupId);
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
