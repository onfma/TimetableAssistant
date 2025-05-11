package unitTests;

import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.DisciplineCRUD;
import org.example.timetableassistant.database.crud.TeacherCRUD;
import org.example.timetableassistant.model.Discipline;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.MockedStatic;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DisciplineCRUD_Test {

    private final DisciplineCRUD crud = new DisciplineCRUD();
    private final Connection mockConn = mock(Connection.class);
    private final PreparedStatement mockStmt = mock(PreparedStatement.class);
    private final ResultSet mockRs = mock(ResultSet.class);


    @Test
    void testInsertDiscipline_Success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertDiscipline("Matematica");

            assertTrue(result.isSuccess());
            assertEquals("Disciplină adăugată cu succes.", result.getMessage());
            verify(mockStmt).setString(1, "Matematica");
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testInsertDiscipline_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Insert error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertDiscipline("Matematica");

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la inserarea disciplinei"));
        }
    }

    @Test
    void testGetDisciplineById_Success() throws Exception {
        int id = 1;
        String name = "Informatica";

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("id")).thenReturn(id);
        when(mockRs.getString("name")).thenReturn(name);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getDisciplineById(id);

            assertTrue(result.isSuccess());

            if (result.success && result.message instanceof Map<?, ?> map) {
                assertEquals(id, map.get("id"));
                assertEquals(name, map.get("name"));
            }
        }
    }

    @Test
    void testGetDisciplineById_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Query error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getDisciplineById(1);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la obținerea disciplinei"));
        }
    }

    @Test
    void testUpdateDiscipline_Success() throws Exception {
        int id = 1;
        String newName = "Fizica";

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateDiscipline(id, newName);

            assertTrue(result.isSuccess());
            assertEquals("Disciplină actualizată cu succes.", result.getMessage());
            verify(mockStmt).setString(1, newName);
            verify(mockStmt).setInt(2, id);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testUpdateDiscipline_NotFound() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateDiscipline(999, "Biologie");

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("nu a fost găsită"));
        }
    }

    @Test
    void testUpdateDiscipline_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Update error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateDiscipline(1, "Chimie");

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la actualizarea disciplinei"));
        }
    }

    @Test
    void testDeleteDiscipline_Success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteDiscipline(1);

            assertTrue(result.isSuccess());
            assertEquals("Disciplină ștearsă cu succes.", result.getMessage());
        }
    }

    @Test
    void testDeleteDiscipline_NotFound() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteDiscipline(2);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("nu a fost găsită"));
        }
    }

    @Test
    void testDeleteDiscipline_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Delete error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteDiscipline(3);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la ștergerea disciplinei"));
        }
    }

    @Test
    void testGetAllDisciplines_Success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        when(mockRs.next()).thenReturn(true, true, false);
        when(mockRs.getInt("id")).thenReturn(1, 2);
        when(mockRs.getString("name")).thenReturn("Matematica", "Informatica");

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getAllDisciplines();

            assertTrue(result.isSuccess());
            assertInstanceOf(List.class, result.message);

            List<Map<String, Object>> list = (List<Map<String, Object>>) result.message;
            assertEquals(2, list.size());
            assertEquals("Matematica", list.get(0).get("name"));
            assertEquals("Informatica", list.get(1).get("name"));
        }
    }
}
