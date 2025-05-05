package org.example.timetableassistant.database.handler;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.ClassCRUD;
import org.example.timetableassistant.database.crud.ClassType;
import spark.Request;
import spark.Response;

public class ClassHandler {
    private static final ClassCRUD classCRUD = new ClassCRUD();
    public static String createClass(Request req, Response res) {
        int disciplineId = Integer.parseInt(req.queryParams("discipline_id"));
        String classTypeString = req.queryParams("class_type");
        int roomId = Integer.parseInt(req.queryParams("room_id"));
        int timeSlotId = Integer.parseInt(req.queryParams("time_slot_id"));
        int teacherId = Integer.parseInt(req.queryParams("teacher_id"));

        // Valori opționale
        Integer semiyearId = req.queryParams("semiyear_id") != null ? Integer.parseInt(req.queryParams("semiyear_id")) : null;
        Integer groupId = req.queryParams("group_id") != null ? Integer.parseInt(req.queryParams("group_id")) : null;

        if (disciplineId == 0 || classTypeString == null || roomId == 0 || timeSlotId == 0 || teacherId == 0) {
            res.status(400);
            return "Missing required fields.";
        }

        ClassType classType;
        try {
            classType = ClassType.valueOf(classTypeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            res.status(400);
            return "{\"error\":\"Invalid class type. Please use COURSE, LABORATORY, or SEMINAR.\"}";
        }

        OperationResult result = classCRUD.insertClass(
                disciplineId,
                classType,
                roomId,
                timeSlotId,
                semiyearId,
                groupId,
                teacherId
        );

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
        String classTypeString = req.queryParams("class_type");
        int roomId = Integer.parseInt(req.queryParams("room_id"));
        int timeSlotId = Integer.parseInt(req.queryParams("time_slot_id"));
        int teacherId = Integer.parseInt(req.queryParams("teacher_id"));

        Integer semiyearId = req.queryParams("semiyear_id") != null ? Integer.parseInt(req.queryParams("semiyear_id")) : null;
        Integer groupId = req.queryParams("group_id") != null ? Integer.parseInt(req.queryParams("group_id")) : null;

        if (disciplineId == 0 || classTypeString == null || roomId == 0 || timeSlotId == 0 || teacherId == 0) {
            res.status(400);
            return "Missing required fields.";
        }

        ClassType classType;
        try {
            classType = ClassType.valueOf(classTypeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            res.status(400);
            return "{\"error\":\"Invalid class type. Please use COURSE, LABORATORY, or SEMINAR.\"}";
        }

        OperationResult result = classCRUD.updateClass(
                id,
                disciplineId,
                classType,
                roomId,
                timeSlotId,
                semiyearId,
                groupId,
                teacherId
        );

        if (result.success) {
            res.status(200);
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


    public static String getClassesByGroupId(Request req, Response res) {
        int groupId = Integer.parseInt(req.params(":groupId"));

        OperationResult result = classCRUD.getClassesByGroupId(groupId);

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

    public static String getClassesByRoomId(Request req, Response res) {
        int roomId = Integer.parseInt(req.params(":roomId"));

        OperationResult result = classCRUD.getClassesByRoomId(roomId);

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

    public static String getClassesBySemiyearId(Request req, Response res) {
        int semiyearId = Integer.parseInt(req.params(":semiyearId"));

        OperationResult result = classCRUD.getClassesBySemiyearId(semiyearId);

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

    public static String getClassesByTeacherId(Request req, Response res) {
        int teacherId = Integer.parseInt(req.params(":teacherId"));

        OperationResult result = classCRUD.getClassesByTeacherId(teacherId);

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

    public static String getAllClasses(Request req, Response res) {
        OperationResult result = classCRUD.getAllClasses();

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
}
