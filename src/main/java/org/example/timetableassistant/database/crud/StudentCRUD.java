package org.example.timetableassistant.database.crud;
import org.example.timetableassistant.database.DatabaseConfig;
import org.example.timetableassistant.database.OperationResult;

import java.sql.*;
import java.util.HashMap;

public class StudentCRUD {
    private static final String URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUser();
    private static final String PASSWORD = DatabaseConfig.getPassword();

    public OperationResult insertStudent(String name, int groupId) {
        String query = "INSERT INTO students (name, group_id) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setInt(2, groupId);
            stmt.executeUpdate();
            return new OperationResult(true, "Studentul a fost adăugat cu succes.");
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la inserarea studentului: " + e.getMessage());
        }
    }

    public OperationResult getStudentById(int id) {
        String query = "SELECT s.id, s.name, s.group_id " +
                "FROM students s WHERE s.id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("id", rs.getInt("id"));
                data.put("name", rs.getString("name"));
                data.put("group_id", rs.getInt("group_id"));
                return new OperationResult(true, data);
            } else {
                return new OperationResult(false, "Studentul cu ID-ul " + id + " nu a fost găsit.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea studentului: " + e.getMessage());
        }
    }

    public OperationResult updateStudent(int id, String newName, int newGroupId) {
        String query = "UPDATE students SET name = ?, group_id = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newName);
            stmt.setInt(2, newGroupId);
            stmt.setInt(3, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Studentul a fost actualizat cu succes.");
            } else {
                return new OperationResult(false, "Studentul cu ID-ul " + id + " nu a fost găsit.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la actualizarea studentului: " + e.getMessage());
        }
    }

    public OperationResult deleteStudent(int id) {
        String query = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Studentul a fost șters cu succes.");
            } else {
                return new OperationResult(false, "Studentul cu ID-ul " + id + " nu a fost găsit.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la ștergerea studentului: " + e.getMessage());
        }
    }

    public OperationResult getStudentsByGroupId(int groupId) {
        String query = "SELECT s.id, s.name, s.group_id " +
                "FROM students s WHERE s.group_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, groupId);
            ResultSet rs = stmt.executeQuery();
            HashMap<Integer, HashMap<String, Object>> students = new HashMap<>();
            int count = 1;
            while (rs.next()) {
                HashMap<String, Object> studentData = new HashMap<>();
                studentData.put("id", rs.getInt("id"));
                studentData.put("name", rs.getString("name"));
                studentData.put("group_id", rs.getInt("group_id"));
                students.put(count++, studentData);
            }
            return students.isEmpty() ?
                    new OperationResult(false, "Nu au fost găsiți studenți în grupa cu ID-ul " + groupId) :
                    new OperationResult(true, students);
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea studenților după group_id: " + e.getMessage());
        }
    }


}
