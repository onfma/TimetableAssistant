package org.example.timetableassistant.database.crud;
import org.example.timetableassistant.database.DatabaseConfig;
import org.example.timetableassistant.database.OperationResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisciplineCRUD {
    private static final String URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUser();
    private static final String PASSWORD = DatabaseConfig.getPassword();

    public OperationResult insertDiscipline(String name) {
        String query = "INSERT INTO disciplines (name) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
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
                return new OperationResult(true, data);
            } else {
                return new OperationResult(false, "Disciplină cu ID-ul " + id + " nu a fost găsită.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea disciplinei: " + e.getMessage());
        }
    }

    public OperationResult getAllDisciplines() {
        String query = "SELECT * FROM disciplines";
        List<Map<String, Object>> disciplines = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", rs.getInt("id"));
                data.put("name", rs.getString("name"));
                disciplines.add(data);
            }

            if (disciplines.isEmpty()) {
                return new OperationResult(false, "Nu există discipline înregistrate.");
            }

            return new OperationResult(true, disciplines);

        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea disciplinelor: " + e.getMessage());
        }
    }



    public OperationResult updateDiscipline(int id, String newName) {
        String query = "UPDATE disciplines SET name = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newName);
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
