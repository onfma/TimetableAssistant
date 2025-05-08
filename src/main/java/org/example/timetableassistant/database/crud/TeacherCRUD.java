package org.example.timetableassistant.database.crud;
import org.example.timetableassistant.database.DatabaseConfig;
import org.example.timetableassistant.database.OperationResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherCRUD {
    private static final String URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUser();
    private static final String PASSWORD = DatabaseConfig.getPassword();


    public OperationResult insertTeacher(String name) {
        String query = "INSERT INTO teachers (name) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            return new OperationResult(true, "Profesorul a fost adăugat cu succes.");
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la inserarea profesorului: " + e.getMessage());
        }
    }


    public OperationResult getTeacherById(int id) {
        String query = "SELECT * FROM teachers WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("id", rs.getInt("id"));
                data.put("name", rs.getString("name"));
                return new OperationResult(true, data);
            } else {
                return new OperationResult(false, "Profesorul cu ID-ul " + id + " nu a fost găsit.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea profesorului: " + e.getMessage());
        }
    }

    public OperationResult getAllTeachers() {
        String query = "SELECT * FROM teachers";
        List<Map<String, Object>> teachers = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", rs.getInt("id"));
                data.put("name", rs.getString("name"));
                teachers.add(data);
            }

            if (teachers.isEmpty()) {
                return new OperationResult(false, "Nu există profesori în baza de date.");
            }

            return new OperationResult(true, teachers);

        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea profesorilor: " + e.getMessage());
        }
    }



    public OperationResult updateTeacher(int id, String newName) {
        String query = "UPDATE teachers SET name = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newName);
            stmt.setInt(2, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Profesorul a fost actualizat cu succes.");
            } else {
                return new OperationResult(false, "Profesorul cu ID-ul " + id + " nu a fost găsit.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la actualizarea profesorului: " + e.getMessage());
        }
    }


    public OperationResult deleteTeacher(int id) {
        String query = "DELETE FROM teachers WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Profesorul a fost șters cu succes.");
            } else {
                return new OperationResult(false, "Profesorul cu ID-ul " + id + " nu a fost găsit.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la ștergerea profesorului: " + e.getMessage());
        }
    }


    public OperationResult getTeacherByName(String name) {
        String query = "SELECT * FROM teachers WHERE name LIKE ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            HashMap<Integer, HashMap<String, Object>> teachers = new HashMap<>();
            int count = 1;
            while (rs.next()) {
                HashMap<String, Object> teacherData = new HashMap<>();
                teacherData.put("id", rs.getInt("id"));
                teacherData.put("name", rs.getString("name"));
                teachers.put(count++, teacherData);
            }
            return teachers.isEmpty() ?
                    new OperationResult(false, "Nu au fost găsiți profesori cu numele " + name) :
                    new OperationResult(true, teachers);
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea profesorului: " + e.getMessage());
        }
    }
}
