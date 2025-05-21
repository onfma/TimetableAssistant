package unitTests;

import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.ClassCRUD;
import org.example.timetableassistant.database.crud.ClassType;
import org.example.timetableassistant.model.Semiyear;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.MockedStatic;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class ClassCRUD_Test {

    private final ClassCRUD crud = new ClassCRUD();
    private final Connection mockConn = mock(Connection.class);
    private final PreparedStatement mockStmt = mock(PreparedStatement.class);
    private final ResultSet mockRs = mock(ResultSet.class);

    @Test
    void testInsertClass_SuccessForCourse() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertClass(
                    1,
                    ClassType.COURSE,
                    101,
                    5,
                    Semiyear.SEM_1A,
                    null,
                    10
            );

            assertTrue(result.isSuccess());
            assertEquals("Clasa a fost adăugată cu succes.", result.getMessage());

            verify(mockStmt).setInt(1, 1);
            verify(mockStmt).setInt(2, ClassType.COURSE.getValue());
            verify(mockStmt).setInt(3, 101);
            verify(mockStmt).setInt(4, 5);
            verify(mockStmt).setObject(6, Semiyear.SEM_1A.name(), Types.OTHER);
            verify(mockStmt).setNull(7, Types.INTEGER);
            verify(mockStmt).setInt(5, 10);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testInsertClass_SuccessForLab() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertClass(
                    2,
                    ClassType.LABORATORY,
                    202,
                    3,
                    null,
                    15,
                    20
            );

            assertTrue(result.isSuccess());
            assertEquals("Clasa a fost adăugată cu succes.", result.getMessage());

            verify(mockStmt).setInt(1, 2);
            verify(mockStmt).setInt(2, ClassType.LABORATORY.getValue());
            verify(mockStmt).setInt(3, 202);
            verify(mockStmt).setInt(4, 3);
            verify(mockStmt).setInt(5, 20);
            verify(mockStmt).setNull(6, Types.OTHER);
            verify(mockStmt).setInt(7, 15);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testInsertClass_SuccessForSeminar() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertClass(
                    3,
                    ClassType.SEMINAR,
                    303,
                    4,
                    null,
                    25,
                    30
            );

            assertTrue(result.isSuccess());
            assertEquals("Clasa a fost adăugată cu succes.", result.getMessage());

            verify(mockStmt).setInt(1, 3);
            verify(mockStmt).setInt(2, ClassType.SEMINAR.getValue());
            verify(mockStmt).setInt(3, 303);
            verify(mockStmt).setInt(4, 4);
            verify(mockStmt).setInt(5, 30);
            verify(mockStmt).setNull(6, Types.OTHER);
            verify(mockStmt).setInt(7, 25);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testInsertClass_Failure_CourseWithGroupId() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertClass(
                    1,
                    ClassType.COURSE,
                    101,
                    5,
                    Semiyear.SEM_1A,
                    15,
                    10
            );

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Pentru tipul CURS"));
            verify(mockConn, never()).prepareStatement(anyString());
        }
    }

    @Test
    void testInsertClass_Failure_CourseWithoutSemiyear() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertClass(
                    1,
                    ClassType.COURSE,
                    101,
                    5,
                    null,
                    null,
                    10
            );

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Pentru tipul CURS"));
        }
    }

    @Test
    void testInsertClass_Failure_LabWithSemiyear() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertClass(
                    2,
                    ClassType.LABORATORY,
                    202,
                    3,
                    Semiyear.SEM_2A,
                    15,
                    10
            );

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Pentru tipuri non-CURS"));
            verify(mockConn, never()).prepareStatement(anyString());
        }
    }

    @Test
    void testInsertClass_Failure_LabWithoutGroupId() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertClass(
                    2,
                    ClassType.LABORATORY,
                    202,
                    3,
                    null,
                    null,
                    10
            );

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Pentru tipuri non-CURS"));
        }
    }

    @Test
    void testInsertClass_Failure_SeminarWithSemiyear() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertClass(
                    2,
                    ClassType.SEMINAR,
                    202,
                    3,
                    Semiyear.SEM_2A,
                    15,
                    10
            );

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Pentru tipuri non-CURS"));
        }
    }

    @Test
    void testInsertClass_Failure_SeminarWithoutGroupId() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertClass(
                    2,
                    ClassType.SEMINAR,
                    202,
                    3,
                    null,
                    null,
                    10
            );

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Pentru tipuri non-CURS"));
        }
    }

    @Test
    void testInsertClass_SQLExceptionCourse() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Insert error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertClass(
                    1,
                    ClassType.COURSE,
                    101,
                    2,
                    Semiyear.SEM_1A,
                    null,
                    10
            );

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la inserarea clasei"));
        }
    }

    @Test
    void testInsertClass_SQLExceptionLab() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Insert error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertClass(
                    1,
                    ClassType.LABORATORY,
                    101,
                    2,
                    null,
                    5,
                    10
            );

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la inserarea clasei"));
        }
    }

    @Test
    void testInsertClass_SQLExceptionSeminar() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Insert error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertClass(
                    1,
                    ClassType.SEMINAR,
                    101,
                    2,
                    null,
                    15,
                    10
            );

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la inserarea clasei"));
        }
    }

    @Test
    void testGetClassById_Success() throws Exception {
        int testId = 1;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("id")).thenReturn(testId);
        when(mockRs.getInt("discipline_id")).thenReturn(10);
        when(mockRs.getInt("class_type_id")).thenReturn(ClassType.LABORATORY.getValue());
        when(mockRs.getInt("room_id")).thenReturn(101);
        when(mockRs.getInt("time_slot_id")).thenReturn(5);
        when(mockRs.getString("semiyear")).thenReturn(Semiyear.SEM_1A.getValue());
        when(mockRs.getObject("group_id")).thenReturn(20);
        when(mockRs.getInt("teacher_id")).thenReturn(30);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getClassById(testId);

            assertTrue(result.isSuccess());
            assertTrue(result.message instanceof java.util.Map);

            @SuppressWarnings("unchecked")
            var map = (java.util.Map<String, Object>) result.message;

            assertEquals(testId, map.get("id"));
            assertEquals(10, map.get("discipline_id"));
            assertEquals(ClassType.LABORATORY.name(), map.get("class_type"));
            assertEquals(101, map.get("room_id"));
            assertEquals(5, map.get("time_slot_id"));
            assertEquals(Semiyear.SEM_1A, map.get("semiyear"));
            assertEquals(20, map.get("group_id"));
            assertEquals(30, map.get("teacher_id"));
        }
    }

    @Test
    void testGetClassById_NotFound() throws Exception {
        int testId = 2;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getClassById(testId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("nu a fost găsită"));
        }
    }

    @Test
    void testGetClassById_GroupIdNull() throws Exception {
        int testId = 5;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("id")).thenReturn(testId);
        when(mockRs.getInt("discipline_id")).thenReturn(10);
        when(mockRs.getInt("class_type_id")).thenReturn(ClassType.COURSE.getValue());
        when(mockRs.getInt("room_id")).thenReturn(101);
        when(mockRs.getInt("time_slot_id")).thenReturn(5);
        when(mockRs.getString("semiyear")).thenReturn(Semiyear.SEM_1A.getValue());
        when(mockRs.getObject("group_id")).thenReturn(null);
        when(mockRs.getInt("teacher_id")).thenReturn(30);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getClassById(testId);

            assertTrue(result.isSuccess());
            assertTrue(result.message instanceof java.util.Map);

            @SuppressWarnings("unchecked")
            var map = (java.util.Map<String, Object>) result.message;

            assertEquals(testId, map.get("id"));
            assertEquals(10, map.get("discipline_id"));
            assertEquals(ClassType.COURSE.name(), map.get("class_type"));
            assertEquals(101, map.get("room_id"));
            assertEquals(5, map.get("time_slot_id"));
            assertEquals(Semiyear.SEM_1A, map.get("semiyear"));
            assertNull(map.get("group_id"));
            assertEquals(30, map.get("teacher_id"));
        }
    }

    @Test
    void testGetClassById_SQLException() throws Exception {
        int testId = 3;

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Query error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getClassById(testId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la obținerea clasei"));
        }
    }

    @Test
    void testUpdateClass_Lab_Success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateClass(
                    1,
                    2,
                    ClassType.LABORATORY,
                    203,
                    4,
                    null,
                    12,
                    22
            );

            assertTrue(result.isSuccess());
            assertEquals("Clasa a fost actualizată cu succes.", result.getMessage());

            verify(mockStmt).setInt(1, 2);
            verify(mockStmt).setInt(2, ClassType.LABORATORY.getValue());
            verify(mockStmt).setInt(3, 203);
            verify(mockStmt).setInt(4, 4);
            verify(mockStmt).setObject(5, null, Types.VARCHAR);
            verify(mockStmt).setObject(6, 12, Types.INTEGER);
            verify(mockStmt).setInt(7, 22);
            verify(mockStmt).setInt(8, 1);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testUpdateClass_Course_Success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateClass(
                    15,
                    8,
                    ClassType.COURSE,
                    204,
                    6,
                    Semiyear.SEM_2A,
                    null,
                    44
            );

            assertTrue(result.isSuccess());
            assertEquals("Clasa a fost actualizată cu succes.", result.getMessage());

            verify(mockStmt).setInt(1, 8);
            verify(mockStmt).setInt(2, ClassType.COURSE.getValue());
            verify(mockStmt).setInt(3, 204);
            verify(mockStmt).setInt(4, 6);
            verify(mockStmt).setObject(5, Semiyear.SEM_2A.getValue(), Types.VARCHAR);
            verify(mockStmt).setObject(6, null, Types.INTEGER);
            verify(mockStmt).setInt(7, 44);
            verify(mockStmt).setInt(8, 15);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testUpdateClass_NotFound() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateClass(
                    2,
                    3,
                    ClassType.COURSE,
                    105,
                    6,
                    Semiyear.SEM_2A,
                    null,
                    25
            );

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("nu a fost găsită"));
        }
    }

    @Test
    void testUpdateClass_Course_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Update error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateClass(
                    1,
                    2,
                    ClassType.COURSE,
                    101,
                    3,
                    Semiyear.SEM_1A,
                    null,
                    10
            );

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la actualizarea clasei"));
        }
    }

    @Test
    void testUpdateClass_Lab_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Mock SQL error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateClass(
                    10,
                    5,
                    ClassType.LABORATORY,
                    301,
                    7,
                    null,
                    20,
                    33
            );

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la actualizarea clasei"));
            assertTrue(result.getMessage().contains("Mock SQL error"));
        }
    }

    @Test
    void testDeleteClass_Success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteClass(1);

            assertTrue(result.isSuccess());
            assertEquals("Clasa a fost ștearsă cu succes.", result.getMessage());

            verify(mockStmt).setInt(1, 1);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testDeleteClass_NotFound() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteClass(2);

            assertFalse(result.isSuccess());
            assertEquals("Clasa cu ID-ul 2 nu a fost găsită.", result.getMessage());

            verify(mockStmt).setInt(1, 2);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testDeleteClass_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Delete error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteClass(3);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la ștergerea clasei"));
        }
    }

    @Test
    void testGetClassesByGroupId_Success() throws Exception {
        int groupId = 10;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getInt("id")).thenReturn(1);
        when(mockRs.getInt("discipline_id")).thenReturn(5);
        when(mockRs.getInt("class_type_id")).thenReturn(ClassType.COURSE.getValue());
        when(mockRs.getInt("room_id")).thenReturn(20);
        when(mockRs.getInt("time_slot_id")).thenReturn(3);
        when(mockRs.getString("semiyear")).thenReturn(Semiyear.SEM_1A.getValue());
        when(mockRs.getObject("group_id")).thenReturn(groupId);
        when(mockRs.getInt("teacher_id")).thenReturn(7);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getClassesByGroupId(groupId);

            assertTrue(result.isSuccess());
            List<?> classes = (List<?>) result.message;
            assertEquals(1, classes.size());

            Map<String, Object> classInfo = (Map<String, Object>) classes.get(0);
            assertEquals(1, classInfo.get("id"));
            assertEquals(5, classInfo.get("discipline_id"));
            assertEquals("COURSE", classInfo.get("class_type"));
            assertEquals(20, classInfo.get("room_id"));
            assertEquals(3, classInfo.get("time_slot_id"));
            assertEquals(Semiyear.SEM_1A, classInfo.get("semiyear"));
            assertEquals(groupId, classInfo.get("group_id"));
            assertEquals(7, classInfo.get("teacher_id"));
        }
    }

    @Test
    void testGetClassesByGroupId_NoResults() throws Exception {
        int groupId = 999;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        when(mockRs.next()).thenReturn(false);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getClassesByGroupId(groupId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().toString().contains("Nu au fost găsite clase"));
        }
    }

    @Test
    void testGetClassesByGroupId_SQLException() throws Exception {
        int groupId = 1;

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("DB down"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getClassesByGroupId(groupId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().toString().contains("Eroare"));
        }
    }

    @Test
    void testGetClassesByRoomId_Success() throws Exception {
        int roomId = 15;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getInt("id")).thenReturn(2);
        when(mockRs.getInt("discipline_id")).thenReturn(8);
        when(mockRs.getInt("class_type_id")).thenReturn(ClassType.LABORATORY.getValue());
        when(mockRs.getInt("room_id")).thenReturn(roomId);
        when(mockRs.getInt("time_slot_id")).thenReturn(4);
        when(mockRs.getString("semiyear")).thenReturn(Semiyear.SEM_2B.getValue());
        when(mockRs.getObject("group_id")).thenReturn(12);
        when(mockRs.getInt("teacher_id")).thenReturn(3);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getClassesByRoomId(roomId);

            assertTrue(result.isSuccess());
            List<?> classes = (List<?>) result.message;
            assertEquals(1, classes.size());

            Map<String, Object> classInfo = (Map<String, Object>) classes.get(0);
            assertEquals(2, classInfo.get("id"));
            assertEquals(8, classInfo.get("discipline_id"));
            assertEquals(ClassType.LABORATORY.name(), classInfo.get("class_type"));
            assertEquals(roomId, classInfo.get("room_id"));
            assertEquals(4, classInfo.get("time_slot_id"));
            assertEquals(Semiyear.SEM_2B, classInfo.get("semiyear"));
            assertEquals(12, classInfo.get("group_id"));
            assertEquals(3, classInfo.get("teacher_id"));
        }
    }

    @Test
    void testGetClassesByRoomId_NoResults() throws Exception {
        int roomId = 9999;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        when(mockRs.next()).thenReturn(false);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getClassesByRoomId(roomId);

            assertFalse(result.isSuccess());
            assertEquals("Nu au fost găsite clase pentru sala cu ID-ul " + roomId, result.getMessage());
        }
    }

    @Test
    void testGetClassesByRoomId_SQLException() throws Exception {
        int roomId = 1;

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getClassesByRoomId(roomId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().toString().contains("Eroare"));
        }
    }


    @Test
    void testGetClassesBySemiyear_Success() throws Exception {
        Semiyear semiyear = Semiyear.SEM_1A;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getInt("id")).thenReturn(3);
        when(mockRs.getInt("discipline_id")).thenReturn(6);
        when(mockRs.getInt("class_type_id")).thenReturn(ClassType.SEMINAR.getValue());
        when(mockRs.getInt("room_id")).thenReturn(25);
        when(mockRs.getInt("time_slot_id")).thenReturn(5);
        when(mockRs.getString("semiyear")).thenReturn(semiyear.getValue());
        when(mockRs.getObject("group_id")).thenReturn(14);
        when(mockRs.getInt("teacher_id")).thenReturn(8);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getClassesBySemiyear(semiyear);

            assertTrue(result.isSuccess());
            List<?> classes = (List<?>) result.message;
            assertEquals(1, classes.size());

            Map<String, Object> classInfo = (Map<String, Object>) classes.get(0);
            assertEquals(3, classInfo.get("id"));
            assertEquals(6, classInfo.get("discipline_id"));
            assertEquals("SEMINAR", classInfo.get("class_type"));
            assertEquals(25, classInfo.get("room_id"));
            assertEquals(5, classInfo.get("time_slot_id"));
            assertEquals(semiyear, classInfo.get("semiyear"));
            assertEquals(14, classInfo.get("group_id"));
            assertEquals(8, classInfo.get("teacher_id"));
        }
    }

    @Test
    void testGetClassesBySemiyear_NoResults() throws Exception {
        Semiyear semiyear = Semiyear.SEM_2B;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        when(mockRs.next()).thenReturn(false);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getClassesBySemiyear(semiyear);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().toString().contains("Nu au fost găsite clase"));
        }
    }

    @Test
    void testGetClassesBySemiyear_SQLException() throws Exception {
        Semiyear semiyear = Semiyear.SEM_1A;

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("DB fail"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getClassesBySemiyear(semiyear);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().toString().contains("Eroare"));
        }
    }

    @Test
    void testGetClassesByTeacherId_Success() throws Exception {
        int teacherId = 20;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getInt("id")).thenReturn(4);
        when(mockRs.getInt("discipline_id")).thenReturn(9);
        when(mockRs.getInt("class_type_id")).thenReturn(ClassType.COURSE.getValue());
        when(mockRs.getInt("room_id")).thenReturn(30);
        when(mockRs.getInt("time_slot_id")).thenReturn(7);
        when(mockRs.getString("semiyear")).thenReturn(Semiyear.SEM_1B.getValue());
        when(mockRs.getObject("group_id")).thenReturn(18);
        when(mockRs.getInt("teacher_id")).thenReturn(teacherId);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getClassesByTeacherId(teacherId);

            assertTrue(result.isSuccess());
            List<?> classes = (List<?>) result.message;
            assertEquals(1, classes.size());

            Map<String, Object> classInfo = (Map<String, Object>) classes.get(0);
            assertEquals(4, classInfo.get("id"));
            assertEquals(9, classInfo.get("discipline_id"));
            assertEquals("COURSE", classInfo.get("class_type"));
            assertEquals(30, classInfo.get("room_id"));
            assertEquals(7, classInfo.get("time_slot_id"));
            assertEquals(Semiyear.SEM_1B, classInfo.get("semiyear"));
            assertEquals(18, classInfo.get("group_id"));
            assertEquals(teacherId, classInfo.get("teacher_id"));
        }
    }

    @Test
    void testGetClassesByTeacherId_NoResults() throws Exception {
        int teacherId = 999;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        when(mockRs.next()).thenReturn(false);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getClassesByTeacherId(teacherId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().toString().contains("Nu au fost găsite clase"));
        }
    }

    @Test
    void testGetClassesByTeacherId_SQLException() throws Exception {
        int teacherId = 1;

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getClassesByTeacherId(teacherId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().toString().contains("Eroare"));
        }
    }

    @Test
    void testGetClassesByDisciplineId_Success() throws Exception {
        int disciplineId = 5;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getInt("id")).thenReturn(5);
        when(mockRs.getInt("discipline_id")).thenReturn(disciplineId);
        when(mockRs.getInt("class_type_id")).thenReturn(ClassType.LABORATORY.getValue());
        when(mockRs.getInt("room_id")).thenReturn(22);
        when(mockRs.getInt("time_slot_id")).thenReturn(8);
        when(mockRs.getString("semiyear")).thenReturn(Semiyear.SEM_2A.getValue());
        when(mockRs.getObject("group_id")).thenReturn(13);
        when(mockRs.getInt("teacher_id")).thenReturn(6);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getClassesByDisciplineId(disciplineId);

            assertTrue(result.isSuccess());
            List<?> classes = (List<?>) result.message;
            assertEquals(1, classes.size());

            Map<String, Object> classInfo = (Map<String, Object>) classes.get(0);
            assertEquals(5, classInfo.get("id"));
            assertEquals(disciplineId, classInfo.get("discipline_id"));
            assertEquals(ClassType.LABORATORY.name(), classInfo.get("class_type"));
            assertEquals(22, classInfo.get("room_id"));
            assertEquals(8, classInfo.get("time_slot_id"));
            assertEquals(Semiyear.SEM_2A, classInfo.get("semiyear"));
            assertEquals(13, classInfo.get("group_id"));
            assertEquals(6, classInfo.get("teacher_id"));
        }
    }

    @Test
    void testGetClassesByDisciplineId_NoResults() throws Exception {
        int disciplineId = 999;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        when(mockRs.next()).thenReturn(false);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getClassesByDisciplineId(disciplineId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().toString().contains("Nu au fost găsite clase"));
        }
    }

    @Test
    void testGetClassesByDisciplineId_SQLException() throws Exception {
        int disciplineId = 1;

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("DB fail"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getClassesByDisciplineId(disciplineId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().toString().contains("Eroare"));
        }
    }

    @Test
    void testGetAllClasses_Success() throws Exception {

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        when(mockRs.next()).thenReturn(true, true, false);

        when(mockRs.getInt("id")).thenReturn(10, 20);
        when(mockRs.getInt("discipline_id")).thenReturn(3, 4);
        when(mockRs.getInt("class_type_id")).thenReturn(ClassType.SEMINAR.getValue(), ClassType.COURSE.getValue());
        when(mockRs.getInt("room_id")).thenReturn(11, 12);
        when(mockRs.getInt("time_slot_id")).thenReturn(1, 2);
        when(mockRs.getString("semiyear")).thenReturn(Semiyear.SEM_1B.getValue(), Semiyear.SEM_2A.getValue());
        when(mockRs.getObject("group_id")).thenReturn(null, 5);
        when(mockRs.getInt("teacher_id")).thenReturn(4, 7);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getAllClasses();

            assertTrue(result.isSuccess());
            List<?> classes = (List<?>) result.message;
            assertEquals(2, classes.size());

            Map<String, Object> classInfo1 = (Map<String, Object>) classes.get(0);
            assertEquals(10, classInfo1.get("id"));
            assertEquals(3, classInfo1.get("discipline_id"));
            assertEquals("SEMINAR", classInfo1.get("class_type"));
            assertEquals(11, classInfo1.get("room_id"));
            assertEquals(1, classInfo1.get("time_slot_id"));
            assertEquals(Semiyear.SEM_1B, classInfo1.get("semiyear"));
            assertNull(classInfo1.get("group_id"));
            assertEquals(4, classInfo1.get("teacher_id"));

            Map<String, Object> classInfo2 = (Map<String, Object>) classes.get(1);
            assertEquals(20, classInfo2.get("id"));
            assertEquals(4, classInfo2.get("discipline_id"));
            assertEquals("COURSE", classInfo2.get("class_type"));
            assertEquals(12, classInfo2.get("room_id"));
            assertEquals(2, classInfo2.get("time_slot_id"));
            assertEquals(Semiyear.SEM_2A, classInfo2.get("semiyear"));
            assertEquals(5, classInfo2.get("group_id"));
            assertEquals(7, classInfo2.get("teacher_id"));
        }
    }

    @Test
    void testGetAllClasses_NoResults() throws Exception {

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        when(mockRs.next()).thenReturn(false);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getAllClasses();

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().toString().contains("Nu există clase"));
        }
    }

    @Test
    void testGetAllClasses_SQLException() throws Exception {

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("DB down"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getAllClasses();

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().toString().contains("Eroare"));
        }
    }

}
