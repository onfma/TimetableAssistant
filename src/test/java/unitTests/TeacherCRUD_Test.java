package unitTests;

import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.TeacherCRUD;
import org.example.timetableassistant.model.Teacher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.MockedStatic;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TeacherCRUD_Test {

    private final TeacherCRUD crud = new TeacherCRUD();
    private final Connection mockConn = mock(Connection.class);
    private final PreparedStatement mockStmt = mock(PreparedStatement.class);
    private final ResultSet mockRs = mock(ResultSet.class);

    @Test
    void testInsertTeacher_Success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertTeacher("John Doe");

            assertTrue(result.isSuccess());
            assertEquals("Profesorul a fost adăugat cu succes.", result.getMessage());
            verify(mockStmt).setString(1, "John Doe");
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testInsertTeacher_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Insert error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertTeacher("Jane Doe");

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la inserarea profesorului"));
        }
    }

    @Test
    void testGetTeacherById_Success() throws Exception {
        int testId = 1;
        String testName = "John Doe";

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("id")).thenReturn(testId);
        when(mockRs.getString("name")).thenReturn(testName);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getTeacherById(testId);

            assertTrue(result.isSuccess());
            if (result.success && result.message instanceof Map<?, ?>) {
                Map<?, ?> map = (Map<?, ?>) result.message;
                int id = (int) map.get("id");
                String name = (String) map.get("name");

                assertEquals(testId, id);
                assertEquals(testName, name);
            }
        }
    }

    @Test
    void testGetTeacherById_NotFound() throws Exception {
        int testId = 2;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getTeacherById(testId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("nu a fost găsit"));
        }
    }

    @Test
    void testGetTeacherById_SQLException() throws Exception {
        int testId = 3;

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Query error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getTeacherById(testId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la obținerea profesorului"));
        }
    }

    @Test
    void testGetAllTeachers_Success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        // Simulate two teachers in the result set
        when(mockRs.next()).thenReturn(true, true, false);
        when(mockRs.getInt("id")).thenReturn(1, 2);
        when(mockRs.getString("name")).thenReturn("John Doe", "Jane Smith");

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getAllTeachers();

            assertTrue(result.isSuccess());
            assertInstanceOf(List.class, result.message);

            List<Teacher> list = new ArrayList<>();

            if (result.success) {
                List<?> rawList = (List<?>) result.message;
                for (Object obj : rawList) {
                    if (obj instanceof Map<?, ?> map) {
                        int id = (int) map.get("id");
                        String name = (String) map.get("name");

                        list.add(new Teacher(id, name));
                    }
                }
            }

            assertEquals(2, list.size());

            Teacher teacher1 = list.getFirst();
            assertEquals(1, teacher1.getId());
            assertEquals("John Doe", teacher1.getName());

            Teacher teacher2 = list.get(1);
            assertEquals(2, teacher2.getId());
            assertEquals("Jane Smith", teacher2.getName());
        }
    }

    @Test
    void testGetAllTeachers_NotFound() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getAllTeachers();

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Nu există profesori"));
        }
    }

    @Test
    void testGetAllTeachers_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Query error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getAllTeachers();

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la obținerea profesorilor"));
        }
    }

    @Test
    void testUpdateTeacher_Success() throws Exception {
        int testId = 1;
        String newName = "Jane Doe";

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateTeacher(testId, newName);

            assertTrue(result.isSuccess());
            assertEquals("Profesorul a fost actualizat cu succes.", result.getMessage());
            verify(mockStmt).setString(1, newName);
            verify(mockStmt).setInt(2, testId);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testUpdateTeacher_NotFound() throws Exception {
        int testId = 2;
        String newName = "Jane Doe";

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateTeacher(testId, newName);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("nu a fost găsit"));
            verify(mockStmt).setString(1, newName);
            verify(mockStmt).setInt(2, testId);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testUpdateTeacher_SQLException() throws Exception {
        int testId = 3;
        String newName = "Jane Doe";

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Update error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateTeacher(testId, newName);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la actualizarea profesorului"));
        }
    }

    @Test
    void testDeleteTeacher_Success() throws Exception {
        int testId = 1;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteTeacher(testId);

            assertTrue(result.isSuccess());
            assertEquals("Profesorul a fost șters cu succes.", result.getMessage());
            verify(mockStmt).setInt(1, testId);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testDeleteTeacher_NotFound() throws Exception {
        int testId = 2;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteTeacher(testId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("nu a fost găsit"));
            verify(mockStmt).setInt(1, testId);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testDeleteTeacher_SQLException() throws Exception {
        int testId = 3;

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Delete error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteTeacher(testId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la ștergerea profesorului"));
        }
    }
}
