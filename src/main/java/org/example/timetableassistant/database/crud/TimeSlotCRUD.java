package org.example.timetableassistant.database.crud;
import org.example.timetableassistant.database.DatabaseConfig;
import org.example.timetableassistant.database.OperationResult;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class TimeSlotCRUD {
    private static final String URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUser();
    private static final String PASSWORD = DatabaseConfig.getPassword();


    public OperationResult insertTimeSlot(String dayOfWeek, Time startTime, Time endTime) {
        String query = "INSERT INTO time_slots (day_of_week, start_time, end_time) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, dayOfWeek);
            stmt.setTime(2, startTime);
            stmt.setTime(3, endTime);
            stmt.executeUpdate();
            return new OperationResult(true, "Slotul de timp a fost adăugat cu succes.");
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la inserarea slotului de timp: " + e.getMessage());
        }
    }


    public OperationResult getTimeSlotById(int id) {
        String query = "SELECT * FROM time_slots WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Map<String, Object> slotInfo = new HashMap<>();
                slotInfo.put("id", rs.getInt("id"));
                slotInfo.put("day_of_week", rs.getString("day_of_week"));
                slotInfo.put("start_time", rs.getTime("start_time").toString().substring(0,5)); // HH:mm
                slotInfo.put("end_time", rs.getTime("end_time").toString().substring(0,5));     // HH:mm
                return new OperationResult(true, slotInfo);
            } else {
                return new OperationResult(false, "Slotul de timp cu ID-ul " + id + " nu a fost găsit.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea slotului de timp: " + e.getMessage());
        }
    }




    public OperationResult updateTimeSlot(int id, String newDayOfWeek, Time newStartTime, Time newEndTime) {
        String query = "UPDATE time_slots SET day_of_week = ?, start_time = ?, end_time = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newDayOfWeek);
            stmt.setTime(2, newStartTime);
            stmt.setTime(3, newEndTime);
            stmt.setInt(4, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Slotul de timp a fost actualizat cu succes.");
            } else {
                return new OperationResult(false, "Slotul de timp cu ID-ul " + id + " nu a fost găsit.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la actualizarea slotului de timp: " + e.getMessage());
        }
    }

    public OperationResult deleteTimeSlot(int id) {
        String query = "DELETE FROM time_slots WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Slotul de timp a fost șters cu succes.");
            } else {
                return new OperationResult(false, "Slotul de timp cu ID-ul " + id + " nu a fost găsit.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la ștergerea slotului de timp: " + e.getMessage());
        }
    }
}
