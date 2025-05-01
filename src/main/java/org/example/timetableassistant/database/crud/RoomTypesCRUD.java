package org.example.timetableassistant.database.crud;

import org.example.timetableassistant.database.DatabaseConfig;
import org.example.timetableassistant.database.OperationResult;

import java.sql.*;
import java.util.HashMap;

public class RoomTypesCRUD {
    private static final String URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUser();
    private static final String PASSWORD = DatabaseConfig.getPassword();


    public OperationResult insertRoomType(String name) {
        String query = "INSERT INTO room_types (name) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            return new OperationResult(true, "Tipul de sală adăugat cu succes.");
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la inserarea tipului de sală: " + e.getMessage());
        }
    }


    public OperationResult getRoomTypeById(int id) {
        String query = "SELECT * FROM room_types WHERE id = ?";
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
                return new OperationResult(false, "Tipul de sală cu ID-ul " + id + " nu a fost găsit.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea tipului de sală: " + e.getMessage());
        }
    }


    public OperationResult updateRoomType(int id, String newName) {
        String query = "UPDATE room_types SET name = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newName);
            stmt.setInt(2, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Tipul de sală a fost actualizat cu succes.");
            } else {
                return new OperationResult(false, "Tipul de sală cu ID-ul " + id + " nu a fost găsit.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la actualizarea tipului de sală: " + e.getMessage());
        }
    }


    public OperationResult deleteRoomType(int id) {
        String query = "DELETE FROM room_types WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Tipul de sală a fost șters cu succes.");
            } else {
                return new OperationResult(false, "Tipul de sală cu ID-ul " + id + " nu a fost găsit.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la ștergerea tipului de sală: " + e.getMessage());
        }
    }
}
