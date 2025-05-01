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
        int studyYear = Integer.parseInt(req.queryParams("study_year"));
        String groupName = req.queryParams("group_name");

        if (name == null || groupName == null) {
            res.status(400);  // Bad Request
            return "Missing required fields (name, study_year, group_name).";
        }

        OperationResult result = studentCRUD.insertStudent(name, studyYear, groupName);

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


    public static String updateStudent(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        String newName = req.queryParams("name");
        int newStudyYear = Integer.parseInt(req.queryParams("study_year"));
        String newGroupName = req.queryParams("group_name");

        if (newName == null || newGroupName == null) {
            res.status(400);  // Bad Request
            return "Missing required fields (name, study_year, group_name).";
        }

        OperationResult result = studentCRUD.updateStudent(id, newName, newStudyYear, newGroupName);

        if (result.success) {
            res.status(201);
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
            res.status(201);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }


    public static String getStudentsByGroup(Request req, Response res) {
        String groupName = req.params(":group_name");

        OperationResult result = studentCRUD.getStudentsByGroup(groupName);

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
