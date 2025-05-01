package org.example.timetableassistant.database.crud;
import org.example.timetableassistant.database.DatabaseConfig;
import org.example.timetableassistant.database.OperationResult;

import java.sql.*;
import java.util.HashMap;

public class StudentCRUD {
    private static final String URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUser();
    private static final String PASSWORD = DatabaseConfig.getPassword();


    public OperationResult insertStudent(String name, int studyYear, String groupName) {
        String query = "INSERT INTO students (name, study_year, group_name) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setInt(2, studyYear);
            stmt.setString(3, groupName);
            stmt.executeUpdate();
            return new OperationResult(true, "Studentul a fost adăugat cu succes.");
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la inserarea studentului: " + e.getMessage());
        }
    }

    public OperationResult getStudentById(int id) {
        String query = "SELECT * FROM students WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("id", rs.getInt("id"));
                data.put("name", rs.getString("name"));
                data.put("study_year", rs.getInt("study_year"));
                data.put("group_name", rs.getString("group_name"));
                return new OperationResult(true, data);
            } else {
                return new OperationResult(false, "Studentul cu ID-ul " + id + " nu a fost găsit.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea studentului: " + e.getMessage());
        }
    }


    public OperationResult updateStudent(int id, String newName, int newStudyYear, String newGroupName) {
        String query = "UPDATE students SET name = ?, study_year = ?, group_name = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newName);
            stmt.setInt(2, newStudyYear);
            stmt.setString(3, newGroupName);
            stmt.setInt(4, id);
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


    public OperationResult getStudentsByGroup(String groupName) {
        String query = "SELECT * FROM students WHERE group_name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, groupName);
            ResultSet rs = stmt.executeQuery();
            HashMap<Integer, HashMap<String, Object>> students = new HashMap<>();
            int count = 1;
            while (rs.next()) {
                HashMap<String, Object> studentData = new HashMap<>();
                studentData.put("id", rs.getInt("id"));
                studentData.put("name", rs.getString("name"));
                studentData.put("study_year", rs.getInt("study_year"));
                studentData.put("group_name", rs.getString("group_name"));
                students.put(count++, studentData);
            }
            return students.isEmpty() ?
                    new OperationResult(false, "Nu au fost găsiți studenți în grupul " + groupName) :
                    new OperationResult(true, students);
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea studenților: " + e.getMessage());
        }
    }
}
