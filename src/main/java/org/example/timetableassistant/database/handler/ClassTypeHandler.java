package org.example.timetableassistant.database.handler;
import com.google.gson.Gson;
import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.ClassTypeCRUD;
import spark.Request;
import spark.Response;
import java.util.HashMap;
import java.util.Map;

public class ClassTypeHandler {
    private static final ClassTypeCRUD classTypeCRUD = new ClassTypeCRUD();

    public static String createClassType(Request req, Response res) {
        String name = req.queryParams("name");

        if (name == null) {
            res.status(400);  // Bad Request
            return "Missing required field (name).";
        }

        OperationResult result = classTypeCRUD.insertClassType(name);

        if (result.success) {
            res.status(201);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }


    public static String getClassTypeById(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));

        OperationResult result = classTypeCRUD.getClassTypeById(id);

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


    public static String updateClassType(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        String newName = req.queryParams("name");

        if (newName == null) {
            res.status(400);  // Bad Request
            return "Missing required field (name).";
        }

        OperationResult result = classTypeCRUD.updateClassType(id, newName);

        if (result.success) {
            res.status(201);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }


    public static String deleteClassType(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));

        OperationResult result = classTypeCRUD.deleteClassType(id);

        if (result.success) {
            res.status(201);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }
}
