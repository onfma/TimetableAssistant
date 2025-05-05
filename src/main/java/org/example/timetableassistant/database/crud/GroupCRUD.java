package org.example.timetableassistant.database.crud;


import org.example.timetableassistant.database.DatabaseConfig;
import org.example.timetableassistant.database.OperationResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupCRUD {
    private static final String URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUser();
    private static final String PASSWORD = DatabaseConfig.getPassword();

    public OperationResult insertGroup(String name, int semiyearId) {
        String query = "INSERT INTO groups (name, semiyear_id) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setInt(2, semiyearId);
            stmt.executeUpdate();
            return new OperationResult(true, "Grupa a fost adăugată cu succes.");
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la inserarea grupei: " + e.getMessage());
        }
    }


    public OperationResult getGroupById(int id) {
        String query = "SELECT * FROM groups WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("id", rs.getInt("id"));
                data.put("name", rs.getString("name"));
                data.put("semiyear_id", rs.getInt("semiyear_id"));
                return new OperationResult(true, data);
            } else {
                return new OperationResult(false, "Grupa cu ID-ul " + id + " nu a fost găsită.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea grupei: " + e.getMessage());
        }
    }

    public OperationResult getGroupByName(String name) {
        String query = "SELECT * FROM groups WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("id", rs.getInt("id"));
                data.put("name", rs.getString("name"));
                data.put("semiyear_id", rs.getInt("semiyear_id"));
                return new OperationResult(true, data);
            } else {
                return new OperationResult(false, "Grupa cu numele " + name + " nu a fost găsită.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea grupei: " + e.getMessage());
        }
    }

    public OperationResult getAllGroups() {
        String query = "SELECT * FROM groups";
        List<Map<String, Object>> groups = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", rs.getInt("id"));
                data.put("name", rs.getString("name"));
                data.put("semiyear_id", rs.getInt("semiyear_id"));
                groups.add(data);
            }

            if (groups.isEmpty()) {
                return new OperationResult(false, "Nu există grupe înregistrate.");
            }

            return new OperationResult(true, groups);

        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea grupelor: " + e.getMessage());
        }
    }



    public OperationResult updateGroup(int id, String newName, int newSemiyearId) {
        String query = "UPDATE groups SET name = ?, semiyear_id = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newName);
            stmt.setInt(2, newSemiyearId);
            stmt.setInt(3, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Grupa a fost actualizată cu succes.");
            } else {
                return new OperationResult(false, "Grupa cu ID-ul " + id + " nu a fost găsită.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la actualizarea grupei: " + e.getMessage());
        }
    }


    public OperationResult deleteGroup(int id) {
        String query = "DELETE FROM groups WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Grupa a fost ștearsă cu succes.");
            } else {
                return new OperationResult(false, "Grupa cu ID-ul " + id + " nu a fost găsită.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la ștergerea grupei: " + e.getMessage());
        }
    }
}
