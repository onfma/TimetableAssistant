package org.example.timetableassistant.database.crud;

import org.example.timetableassistant.database.DatabaseConfig;
import org.example.timetableassistant.database.OperationResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SemiyearCRUD {
    private static final String URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUser();
    private static final String PASSWORD = DatabaseConfig.getPassword();


    public OperationResult insertSemiyear(String name, int studyYear) {
        String query = "INSERT INTO semiyears (name, study_year) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setInt(2, studyYear);
            stmt.executeUpdate();
            return new OperationResult(true, "Semianul a fost adăugat cu succes.");
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la inserarea semianului: " + e.getMessage());
        }
    }

    public OperationResult getSemiyearById(int id) {
        String query = "SELECT * FROM semiyears WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("id", rs.getInt("id"));
                data.put("name", rs.getString("name"));
                data.put("study_year", rs.getInt("study_year"));
                return new OperationResult(true, data);
            } else {
                return new OperationResult(false, "Semianul cu ID-ul " + id + " nu a fost găsit.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea semianului: " + e.getMessage());
        }
    }


    public OperationResult getAllSemiyears() {
        String query = "SELECT * FROM semiyears";
        List<Map<String, Object>> semiyears = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", rs.getInt("id"));
                data.put("name", rs.getString("name"));
                data.put("study_year", rs.getInt("study_year"));
                semiyears.add(data);
            }

            if (semiyears.isEmpty()) {
                return new OperationResult(false, "Nu există semiane înregistrate.");
            }

            return new OperationResult(true, semiyears);

        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea semianilor: " + e.getMessage());
        }
    }



    public OperationResult getSemiyearByNameAndYear(String name, int studyYear) {
        String query = "SELECT * FROM semiyears WHERE name = ? AND study_year = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setInt(2, studyYear);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("id", rs.getInt("id"));
                data.put("name", rs.getString("name"));
                data.put("study_year", rs.getInt("study_year"));
                return new OperationResult(true, data);
            } else {
                return new OperationResult(false, "Nu a fost găsit niciun semian cu numele " + name + " și anul " + studyYear + ".");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la căutarea semianului: " + e.getMessage());
        }
    }


    public OperationResult updateSemiyear(int id, String newName, int newStudyYear) {
        String query = "UPDATE semiyears SET name = ?, study_year = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newName);
            stmt.setInt(2, newStudyYear);
            stmt.setInt(3, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Semianul a fost actualizat cu succes.");
            } else {
                return new OperationResult(false, "Semianul cu ID-ul " + id + " nu a fost găsit.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la actualizarea semianului: " + e.getMessage());
        }
    }

    public OperationResult deleteSemiyear(int id) {
        String query = "DELETE FROM semiyears WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Semianul a fost șters cu succes.");
            } else {
                return new OperationResult(false, "Semianul cu ID-ul " + id + " nu a fost găsit.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la ștergerea semianului: " + e.getMessage());
        }
    }
}
