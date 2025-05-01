package org.example.timetableassistant.database.crud;
import org.example.timetableassistant.database.DatabaseConfig;
import org.example.timetableassistant.database.OperationResult;

import java.sql.*;
import java.util.HashMap;

public class DisciplineCRUD {
    private static final String URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUser();
    private static final String PASSWORD = DatabaseConfig.getPassword();

    public OperationResult insertDiscipline(String name, int teacherId) {
        String query = "INSERT INTO disciplines (name, teacher_id) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setInt(2, teacherId);
            stmt.executeUpdate();
            return new OperationResult(true, "Disciplină adăugată cu succes.");
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la inserarea disciplinei: " + e.getMessage());
        }
    }


    public OperationResult getDisciplineById(int id) {
        String query = "SELECT * FROM disciplines WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("id", rs.getInt("id"));
                data.put("name", rs.getString("name"));
                data.put("teacher_id", rs.getInt("teacher_id"));
                return new OperationResult(true, data);
            } else {
                return new OperationResult(false, "Disciplină cu ID-ul " + id + " nu a fost găsită.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea disciplinei: " + e.getMessage());
        }
    }


    public OperationResult updateDiscipline(int id, String newName, int newTeacherId) {
        String query = "UPDATE disciplines SET name = ?, teacher_id = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newName);
            stmt.setInt(2, newTeacherId);
            stmt.setInt(3, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Disciplină actualizată cu succes.");
            } else {
                return new OperationResult(false, "Disciplină cu ID-ul " + id + " nu a fost găsită.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la actualizarea disciplinei: " + e.getMessage());
        }
    }


    public OperationResult deleteDiscipline(int id) {
        String query = "DELETE FROM disciplines WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Disciplină ștearsă cu succes.");
            } else {
                return new OperationResult(false, "Disciplină cu ID-ul " + id + " nu a fost găsită.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la ștergerea disciplinei: " + e.getMessage());
        }
    }
}
