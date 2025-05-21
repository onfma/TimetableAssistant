package org.example.timetableassistant.database.crud;
import org.example.timetableassistant.database.DatabaseConfig;
import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.model.Semiyear;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassCRUD {
    private static final String URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUser();
    private static final String PASSWORD = DatabaseConfig.getPassword();


    public OperationResult insertClass(int disciplineId, ClassType classType, int roomId, int timeSlotId,
                                       Semiyear semiyear, Integer groupId, int teacherId) {
        if (classType == ClassType.COURSE) {
            if (semiyear == null || groupId != null) {
                return new OperationResult(false, "Pentru tipul CURS trebuie să existe semiyearId și să NU existe groupId.");
            }
        } else {
            if (groupId == null || semiyear != null) {
                return new OperationResult(false, "Pentru tipuri non-CURS trebuie să existe groupId și să NU existe semiyearId.");
            }
        }

        String query = "INSERT INTO classes (discipline_id, class_type_id, room_id, time_slot_id, teacher_id, semiyear, group_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, disciplineId);
            stmt.setInt(2, classType.getValue()); // Using the enum's ID value
            stmt.setInt(3, roomId);
            stmt.setInt(4, timeSlotId);
            stmt.setInt(5, teacherId);
            if (semiyear != null) {
                stmt.setObject(6, semiyear.name(), Types.OTHER); // Use enum name and Types.OTHER for PostgreSQL enum
            } else {
                stmt.setNull(6, Types.OTHER);
            }
            if (groupId != null) {
                stmt.setInt(7, groupId);
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            stmt.executeUpdate();
            return new OperationResult(true, "Clasa a fost adăugată cu succes.");
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la inserarea clasei: " + e.getMessage());
        }
    }

    public OperationResult getClassById(int id) {
        String query = "SELECT * FROM classes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Map<String, Object> classInfo = new HashMap<>();
                classInfo.put("id", rs.getInt("id"));
                classInfo.put("discipline_id", rs.getInt("discipline_id"));
                classInfo.put("class_type", ClassType.fromInt(rs.getInt("class_type_id")).name());
                classInfo.put("room_id", rs.getInt("room_id"));
                classInfo.put("time_slot_id", rs.getInt("time_slot_id"));
                classInfo.put("teacher_id", rs.getInt("teacher_id"));
                String semiyearStr = rs.getString("semiyear");
                Semiyear semiyear = semiyearStr != null ? Semiyear.fromString(semiyearStr) : null;
                classInfo.put("semiyear", semiyear);
                classInfo.put("group_id", rs.getObject("group_id"));
                return new OperationResult(true, classInfo);
            } else {
                return new OperationResult(false, "Clasa cu ID-ul " + id + " nu a fost găsită.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea clasei: " + e.getMessage());
        }
    }


    public OperationResult updateClass(int id, int disciplineId, ClassType classType, int roomId, int timeSlotId,
                                       Semiyear semiyear, Integer groupId, int teacherId) {
        if (classType == ClassType.COURSE) {
            if (semiyear == null || groupId != null) {
                return new OperationResult(false, "Pentru tipul CURS trebuie să existe semiyearId și să NU existe groupId.");
            }
        } else {
            if (groupId == null || semiyear != null) {
                return new OperationResult(false, "Pentru tipuri non-CURS trebuie să existe groupId și să NU existe semiyearId.");
            }
        }

        String query = "UPDATE classes SET discipline_id = ?, class_type_id = ?, room_id = ?, time_slot_id = ?, " +
                "semiyear = ?, group_id = ?, teacher_id = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, disciplineId);
            stmt.setInt(2, classType.getValue());
            stmt.setInt(3, roomId);
            stmt.setInt(4, timeSlotId);
            stmt.setObject(5, semiyear != null ? semiyear.getValue() : null, Types.VARCHAR);
            stmt.setObject(6, groupId, Types.INTEGER);
            stmt.setInt(7, teacherId);
            stmt.setInt(8, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Clasa a fost actualizată cu succes.");
            } else {
                return new OperationResult(false, "Clasa cu ID-ul " + id + " nu a fost găsită.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la actualizarea clasei: " + e.getMessage());
        }
    }


    public OperationResult getClassesByTimeSlotId(int time_slot_id) {
        String query = "SELECT * FROM classes WHERE time_slot_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, time_slot_id);
            ResultSet rs = stmt.executeQuery();
            List<Map<String, Object>> classes = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> classInfo = new HashMap<>();
                classInfo.put("id", rs.getInt("id"));
                classInfo.put("discipline_id", rs.getInt("discipline_id"));
                classInfo.put("class_type", ClassType.fromInt(rs.getInt("class_type_id")).name());
                classInfo.put("room_id", rs.getInt("room_id"));
                classInfo.put("time_slot_id", rs.getInt("time_slot_id"));
                String semiyearStr = rs.getString("semiyear");
                Semiyear semiyear = semiyearStr != null ? Semiyear.fromString(semiyearStr) : null;
                classInfo.put("semiyear", semiyear);
                classInfo.put("group_id", rs.getObject("group_id"));
                classInfo.put("teacher_id", rs.getInt("teacher_id"));
                classes.add(classInfo);
            }
            return classes.isEmpty() ?
                    new OperationResult(false, "Nu au fost găsite clase pentru time slot-ul cu ID-ul " + time_slot_id) :
                    new OperationResult(true, classes);
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea claselor pentru time slot-ul cu ID-ul " + time_slot_id + ": " + e.getMessage());
        }
    }



    public OperationResult deleteClass(int id) {
        String query = "DELETE FROM classes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return new OperationResult(true, "Clasa a fost ștearsă cu succes.");
            } else {
                return new OperationResult(false, "Clasa cu ID-ul " + id + " nu a fost găsită.");
            }
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la ștergerea clasei: " + e.getMessage());
        }
    }

    public OperationResult getClassesByGroupId(int groupId) {
        String query = "SELECT * FROM classes WHERE group_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, groupId);
            ResultSet rs = stmt.executeQuery();
            List<Map<String, Object>> classes = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> classInfo = new HashMap<>();
                classInfo.put("id", rs.getInt("id"));
                classInfo.put("discipline_id", rs.getInt("discipline_id"));
                classInfo.put("class_type", ClassType.fromInt(rs.getInt("class_type_id")).name());
                classInfo.put("room_id", rs.getInt("room_id"));
                classInfo.put("time_slot_id", rs.getInt("time_slot_id"));
                String semiyearStr = rs.getString("semiyear");
                Semiyear semiyear = semiyearStr != null ? Semiyear.fromString(semiyearStr) : null;
                classInfo.put("semiyear", semiyear);
                classInfo.put("group_id", rs.getObject("group_id"));
                classInfo.put("teacher_id", rs.getInt("teacher_id"));
                classes.add(classInfo);
            }
            return classes.isEmpty() ?
                    new OperationResult(false, "Nu au fost găsite clase pentru grupa cu ID-ul " + groupId) :
                    new OperationResult(true, classes);
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea claselor pentru grupul cu ID-ul " + groupId + ": " + e.getMessage());
        }
    }

    public OperationResult getClassesByRoomId(int roomId) {
        String query = "SELECT * FROM classes WHERE room_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            List<Map<String, Object>> classes = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> classInfo = new HashMap<>();
                classInfo.put("id", rs.getInt("id"));
                classInfo.put("discipline_id", rs.getInt("discipline_id"));
                classInfo.put("class_type", ClassType.fromInt(rs.getInt("class_type_id")).name());
                classInfo.put("room_id", rs.getInt("room_id"));
                classInfo.put("time_slot_id", rs.getInt("time_slot_id"));
                String semiyearStr = rs.getString("semiyear");
                Semiyear semiyear = semiyearStr != null ? Semiyear.fromString(semiyearStr) : null;
                classInfo.put("semiyear", semiyear);
                classInfo.put("group_id", rs.getObject("group_id"));
                classInfo.put("teacher_id", rs.getInt("teacher_id"));
                classes.add(classInfo);
            }
            return classes.isEmpty() ?
                    new OperationResult(false, "Nu au fost găsite clase pentru sala cu ID-ul " + roomId) :
                    new OperationResult(true, classes);
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea claselor pentru sala cu ID-ul " + roomId + ": " + e.getMessage());
        }
    }

    public OperationResult getClassesBySemiyear(Semiyear semiyear) {
        String query = "SELECT * FROM classes WHERE semiyear = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, semiyear.getValue());
            ResultSet rs = stmt.executeQuery();
            List<Map<String, Object>> classes = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> classInfo = new HashMap<>();
                classInfo.put("id", rs.getInt("id"));
                classInfo.put("discipline_id", rs.getInt("discipline_id"));
                classInfo.put("class_type", ClassType.fromInt(rs.getInt("class_type_id")).name());
                classInfo.put("room_id", rs.getInt("room_id"));
                classInfo.put("time_slot_id", rs.getInt("time_slot_id"));
                String semiyearStr = rs.getString("semiyear");
                Semiyear auxSemiyear = semiyearStr != null ? Semiyear.fromString(semiyearStr) : null;
                classInfo.put("semiyear", auxSemiyear);
                classInfo.put("group_id", rs.getObject("group_id"));
                classInfo.put("teacher_id", rs.getInt("teacher_id"));
                classes.add(classInfo);
            }
            return classes.isEmpty() ?
                    new OperationResult(false, "Nu au fost găsite clase pentru semestrul " + semiyear.getValue()) :
                    new OperationResult(true, classes);
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea claselor pentru semestrul " + semiyear.getValue() + ": " + e.getMessage());
        }
    }

    public OperationResult getClassesByTeacherId(int teacherId) {
        String query = "SELECT * FROM classes WHERE teacher_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, teacherId);
            ResultSet rs = stmt.executeQuery();
            List<Map<String, Object>> classes = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> classInfo = new HashMap<>();
                classInfo.put("id", rs.getInt("id"));
                classInfo.put("discipline_id", rs.getInt("discipline_id"));
                classInfo.put("class_type", ClassType.fromInt(rs.getInt("class_type_id")).name());
                classInfo.put("room_id", rs.getInt("room_id"));
                classInfo.put("time_slot_id", rs.getInt("time_slot_id"));
                String semiyearStr = rs.getString("semiyear");
                Semiyear semiyear = semiyearStr != null ? Semiyear.fromString(semiyearStr) : null;
                classInfo.put("semiyear", semiyear);
                classInfo.put("group_id", rs.getObject("group_id"));
                classInfo.put("teacher_id", rs.getInt("teacher_id"));
                classes.add(classInfo);
            }
            return classes.isEmpty() ?
                    new OperationResult(false, "Nu au fost găsite clase pentru profesorul cu ID-ul " + teacherId) :
                    new OperationResult(true, classes);
        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea claselor pentru profesorul cu ID-ul " + teacherId + ": " + e.getMessage());
        }
    }


    public OperationResult getClassesByDisciplineId(int disciplineId) {
        String query = "SELECT * FROM classes WHERE discipline_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, disciplineId);
            ResultSet rs = stmt.executeQuery();
            List<Map<String, Object>> classes = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> classInfo = new HashMap<>();
                classInfo.put("id", rs.getInt("id"));
                classInfo.put("discipline_id", rs.getInt("discipline_id"));
                classInfo.put("class_type", ClassType.fromInt(rs.getInt("class_type_id")).name());
                classInfo.put("room_id", rs.getInt("room_id"));
                classInfo.put("time_slot_id", rs.getInt("time_slot_id"));
                String semiyearStr = rs.getString("semiyear");
                Semiyear semiyear = semiyearStr != null ? Semiyear.fromString(semiyearStr) : null;
                classInfo.put("semiyear", semiyear);
                classInfo.put("group_id", rs.getObject("group_id"));
                classInfo.put("teacher_id", rs.getInt("teacher_id"));
                classes.add(classInfo);
            }

            return classes.isEmpty() ?
                    new OperationResult(false, "Nu au fost găsite clase pentru disciplina cu ID-ul " + disciplineId) :
                    new OperationResult(true, classes);

        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea claselor pentru disciplina cu ID-ul " + disciplineId + ": " + e.getMessage());
        }
    }


    public OperationResult getAllClasses() {
        String query = "SELECT * FROM classes";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            List<Map<String, Object>> classes = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> classInfo = new HashMap<>();
                classInfo.put("id", rs.getInt("id"));
                classInfo.put("discipline_id", rs.getInt("discipline_id"));
                classInfo.put("class_type", ClassType.fromInt(rs.getInt("class_type_id")).name());
                classInfo.put("room_id", rs.getInt("room_id"));
                classInfo.put("time_slot_id", rs.getInt("time_slot_id"));
                String semiyearStr = rs.getString("semiyear");
                Semiyear semiyear = semiyearStr != null ? Semiyear.fromString(semiyearStr) : null; // poate fi null
                classInfo.put("semiyear", semiyear);
                classInfo.put("group_id", rs.getObject("group_id"));        // poate fi null
                classInfo.put("teacher_id", rs.getInt("teacher_id"));
                classes.add(classInfo);
            }

            return classes.isEmpty() ?
                    new OperationResult(false, "Nu există clase înregistrate.") :
                    new OperationResult(true, classes);

        } catch (SQLException e) {
            return new OperationResult(false, "Eroare la obținerea claselor: " + e.getMessage());
        }
    }


}
