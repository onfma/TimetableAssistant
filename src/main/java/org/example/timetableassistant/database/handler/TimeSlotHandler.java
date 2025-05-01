package org.example.timetableassistant.database.handler;
import com.google.gson.Gson;
import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.TimeSlotCRUD;
import spark.Request;
import spark.Response;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class TimeSlotHandler {
    private static final TimeSlotCRUD timeSlotCRUD = new TimeSlotCRUD();

    public static String createTimeSlot(Request req, Response res) {
        String dayOfWeek = req.queryParams("day_of_week");
        String startTimeStr = req.queryParams("start_time");
        String endTimeStr = req.queryParams("end_time");

        if (dayOfWeek == null || startTimeStr == null || endTimeStr == null) {
            res.status(400);  // Bad Request
            return "Missing required fields (day_of_week, start_time, end_time).";
        }

        Time startTime = parseTime(startTimeStr);
        Time endTime = parseTime(endTimeStr);

        if (startTime == null || endTime == null) {
            res.status(400);  // Bad Request
            return "Missing required fields start_time, end_time.";
        }

        OperationResult result = timeSlotCRUD.insertTimeSlot(dayOfWeek, startTime, endTime);

        if (result.success) {
            res.status(201);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }

    public static String getTimeSlotById(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));

        OperationResult result = timeSlotCRUD.getTimeSlotById(id);

        Gson gson = new Gson();

        if (result.success) {
            res.status(200);
            Map<String, Object> response = new HashMap<>();
            response.put("message", result.message); // lăsăm ca obiect
            return gson.toJson(response);
        } else {
            res.status(404);
            Map<String, Object> response = new HashMap<>();
            response.put("error", result.message);
            return gson.toJson(response);
        }
    }

    public static String updateTimeSlot(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        String newDayOfWeek = req.queryParams("day_of_week");
        String newStartTimeStr = req.queryParams("start_time");
        String newEndTimeStr = req.queryParams("end_time");

        if (newDayOfWeek == null || newStartTimeStr == null || newEndTimeStr == null) {
            res.status(400);  // Bad Request
            return "Missing required fields (day_of_week, start_time, end_time).";
        }

        // Convert new start and end times from String to Time
        Time newStartTime = parseTime(newStartTimeStr);
        Time newEndTime = parseTime(newEndTimeStr);

        if (newStartTime == null || newEndTime == null) {
            res.status(400);  // Bad Request
            return "Invalid time format. Please use HH:mm format.";
        }

        OperationResult result = timeSlotCRUD.updateTimeSlot(id, newDayOfWeek, newStartTime, newEndTime);

        if (result.success) {
            res.status(201);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }

    public static String deleteTimeSlot(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));

        OperationResult result = timeSlotCRUD.deleteTimeSlot(id);

        if (result.success) {
            res.status(201);
            return "{\"message\":\"" + result.message + "\"}";
        } else {
            res.status(500);
            return "{\"error\":\"" + result.message + "\"}";
        }
    }


    public static Time parseTime(String timeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            return new Time(sdf.parse(timeStr).getTime());
        } catch (ParseException e) {
            return null;  // Return null if parsing fails
        }
    }
}
