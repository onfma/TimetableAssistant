package org.example.timetableassistant.database.crud;
import org.example.timetableassistant.database.DatabaseConfig;
import org.example.timetableassistant.database.OperationResult;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ClassCRUD {
    private static final String URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUser();
    private static final String PASSWORD = DatabaseConfig.getPassword();


    public OperationResult insertClass(int disciplineId, int classTypeId, int roomId, int timeSlotId, int studyYear, String groupName, int teacherId) {
        String query = "INSERT INTO classes (discipline_id, class_type_id, room_id, time_slot_id, study_year, group_name, teacher_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, disciplineId);
            stmt.setInt(2, classTypeId);
            stmt.setInt(3, roomId);
            stmt.setInt(4, timeSlotId);
            stmt.setInt(5, studyYear);
            stmt.setString(6, groupName);
            stmt.setInt(7, teacherId);
            stmt.executeUpdate();
            return new OperationResult(true, "Clasa a fost adăugată cu succes.");
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la inserarea clasei: " + e.getMessage());
        }
    }


    public OperationResult getClassById(int id) {
        String query = "SELECT * FROM classes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Map<String, Object> classInfo = new HashMap<>();
                classInfo.put("id", rs.getInt("id"));
                classInfo.put("discipline_id", rs.getInt("discipline_id"));
                classInfo.put("class_type_id", rs.getInt("class_type_id"));
                classInfo.put("room_id", rs.getInt("room_id"));
                classInfo.put("time_slot_id", rs.getInt("time_slot_id"));
                classInfo.put("study_year", rs.getInt("study_year"));
                classInfo.put("group_name", rs.getString("group_name"));
                classInfo.put("teacher_id", rs.getInt("teacher_id"));
                return new OperationResult(true, classInfo);
            } else {
                return new OperationResult(false, "Clasa cu ID-ul " + id + " nu a fost găsită.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea clasei: " + e.getMessage());
        }
    }


    public OperationResult updateClass(int id, int disciplineId, int classTypeId, int roomId, int timeSlotId, int studyYear, String groupName, int teacherId) {
        String query = "UPDATE classes SET discipline_id = ?, class_type_id = ?, room_id = ?, time_slot_id = ?, study_year = ?, group_name = ?, teacher_id = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, disciplineId);
            stmt.setInt(2, classTypeId);
            stmt.setInt(3, roomId);
            stmt.setInt(4, timeSlotId);
            stmt.setInt(5, studyYear);
            stmt.setString(6, groupName);
            stmt.setInt(7, teacherId);
            stmt.setInt(8, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Clasa a fost actualizată cu succes.");
            } else {
                return new OperationResult(false, "Clasa cu ID-ul " + id + " nu a fost găsită.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la actualizarea clasei: " + e.getMessage());
        }
    }


    public OperationResult deleteClass(int id) {
        String query = "DELETE FROM classes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Clasa a fost ștearsă cu succes.");
            } else {
                return new OperationResult(false, "Clasa cu ID-ul " + id + " nu a fost găsită.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la ștergerea clasei: " + e.getMessage());
        }
    }
}
