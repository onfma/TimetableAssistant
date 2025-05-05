package org.example.timetableassistant.database.crud;

import org.example.timetableassistant.database.DatabaseConfig;
import org.example.timetableassistant.database.OperationResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisciplineAllocationsCRUD {
    private static final String URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUser();
    private static final String PASSWORD = DatabaseConfig.getPassword();

    public OperationResult insertDisciplineAllocation(int disciplineId, int teacherId, int classTypeId, int hoursPerWeek) {
        String query = "INSERT INTO discipline_allocations (discipline_id, teacher_id, class_type_id, hours_per_week) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, disciplineId);
            stmt.setInt(2, teacherId);
            stmt.setInt(3, classTypeId);
            stmt.setInt(4, hoursPerWeek);
            stmt.executeUpdate();
            return new OperationResult(true, "Alocare disciplină adăugată cu succes.");
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la inserarea alocării disciplinei: " + e.getMessage());
        }
    }

    public OperationResult getDisciplineAllocationById(int id) {
        String query = "SELECT * FROM discipline_allocations WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("id", rs.getInt("id"));
                data.put("discipline_id", rs.getInt("discipline_id"));
                data.put("teacher_id", rs.getInt("teacher_id"));
                data.put("class_type_id", rs.getInt("class_type_id"));
                data.put("hours_per_week", rs.getInt("hours_per_week"));
                return new OperationResult(true, data);
            } else {
                return new OperationResult(false, "Alocare cu ID-ul " + id + " nu a fost găsită.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea alocării: " + e.getMessage());
        }
    }

    public OperationResult getAllDisciplineAllocations() {
        String query = "SELECT * FROM discipline_allocations";
        List<Map<String, Object>> allocations = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", rs.getInt("id"));
                data.put("discipline_id", rs.getInt("discipline_id"));
                data.put("teacher_id", rs.getInt("teacher_id"));
                data.put("class_type_id", rs.getInt("class_type_id"));
                data.put("hours_per_week", rs.getInt("hours_per_week"));
                allocations.add(data);
            }

            if (allocations.isEmpty()) {
                return new OperationResult(false, "Nu există alocări înregistrate.");
            }

            return new OperationResult(true, allocations);

        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea alocărilor: " + e.getMessage());
        }
    }



    public OperationResult updateDisciplineAllocation(int id, int newDisciplineId, int newTeacherId, int newClassTypeId, int newHoursPerWeek) {
        String query = "UPDATE discipline_allocations SET discipline_id = ?, teacher_id = ?, class_type_id = ?, hours_per_week = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, newDisciplineId);
            stmt.setInt(2, newTeacherId);
            stmt.setInt(3, newClassTypeId);
            stmt.setInt(4, newHoursPerWeek);
            stmt.setInt(5, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Alocare actualizată cu succes.");
            } else {
                return new OperationResult(false, "Alocare cu ID-ul " + id + " nu a fost găsită.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la actualizarea alocării: " + e.getMessage());
        }
    }


    public OperationResult deleteDisciplineAllocation(int id) {
        String query = "DELETE FROM discipline_allocations WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Alocare ștearsă cu succes.");
            } else {
                return new OperationResult(false, "Alocare cu ID-ul " + id + " nu a fost găsită.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la ștergerea alocării: " + e.getMessage());
        }
    }

}
