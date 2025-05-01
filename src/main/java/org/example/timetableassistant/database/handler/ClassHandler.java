package org.example.timetableassistant.database.handler;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.ClassCRUD;
import spark.Request;
import spark.Response;

public class ClassHandler {
    private static final ClassCRUD classCRUD = new ClassCRUD();
    public static String createClass(Request req, Response res) {
        int disciplineId = Integer.parseInt(req.queryParams("discipline_id"));
        int classTypeId = Integer.parseInt(req.queryParams("class_type_id"));
        int roomId = Integer.parseInt(req.queryParams("room_id"));
        int timeSlotId = Integer.parseInt(req.queryParams("time_slot_id"));
        int studyYear = Integer.parseInt(req.queryParams("study_year"));
        String groupName = req.queryParams("group_name");
        int teacherId = Integer.parseInt(req.queryParams("teacher_id"));

        if (groupName == null) {
            res.status(400);  // Bad Request
            return "Missing required fields.";
        }

        OperationResult result = classCRUD.insertClass(disciplineId, classTypeId, roomId, timeSlotId, studyYear, groupName, teacherId);

        if (result.success) {
            res.status(201);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }


    public static String getClassById(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));

        OperationResult result = classCRUD.getClassById(id);

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

    public static String updateClass(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        int disciplineId = Integer.parseInt(req.queryParams("discipline_id"));
        int classTypeId = Integer.parseInt(req.queryParams("class_type_id"));
        int roomId = Integer.parseInt(req.queryParams("room_id"));
        int timeSlotId = Integer.parseInt(req.queryParams("time_slot_id"));
        int studyYear = Integer.parseInt(req.queryParams("study_year"));
        String groupName = req.queryParams("group_name");
        int teacherId = Integer.parseInt(req.queryParams("teacher_id"));

        if (groupName == null) {
            res.status(400);  // Bad Request
            return "Missing required fields.";
        }

        OperationResult result = classCRUD.updateClass(id, disciplineId, classTypeId, roomId, timeSlotId, studyYear, groupName, teacherId);

        if (result.success) {
            res.status(201);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }


    public static String deleteClass(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));

        OperationResult result = classCRUD.deleteClass(id);

        if (result.success) {
            res.status(201);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }
}
