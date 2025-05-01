package org.example.timetableassistant.database.crud;

import org.example.timetableassistant.database.DatabaseConfig;
import org.example.timetableassistant.database.OperationResult;

import java.sql.*;
import java.util.HashMap;

public class RoomCRUD {
    private static final String URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUser();
    private static final String PASSWORD = DatabaseConfig.getPassword();


    public OperationResult insertRoom(String name, int capacity, String type) {
        String query = "INSERT INTO rooms (name, capacity, type) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setInt(2, capacity);
            stmt.setString(3, type);
            stmt.executeUpdate();
            return new OperationResult(true, "Camera a fost adăugată cu succes.");
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la inserarea camerei: " + e.getMessage());
        }
    }


    public OperationResult getRoomById(int id) {
        String query = "SELECT * FROM rooms WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("id", rs.getInt("id"));
                data.put("name", rs.getString("name"));
                data.put("capacity", rs.getInt("capacity"));
                data.put("type", rs.getString("type"));
                return new OperationResult(true, data);
            } else {
                return new OperationResult(false, "Camera cu ID-ul " + id + " nu a fost găsită.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea camerei: " + e.getMessage());
        }
    }


    public OperationResult updateRoom(int id, String newName, int newCapacity, String newType) {
        String query = "UPDATE rooms SET name = ?, capacity = ?, type = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newName);
            stmt.setInt(2, newCapacity);
            stmt.setString(3, newType);
            stmt.setInt(4, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Camera a fost actualizată cu succes.");
            } else {
                return new OperationResult(false, "Camera cu ID-ul " + id + " nu a fost găsită.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la actualizarea camerei: " + e.getMessage());
        }
    }


    public OperationResult deleteRoom(int id) {
        String query = "DELETE FROM rooms WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Camera a fost ștearsă cu succes.");
            } else {
                return new OperationResult(false, "Camera cu ID-ul " + id + " nu a fost găsită.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la ștergerea camerei: " + e.getMessage());
        }
    }
}
