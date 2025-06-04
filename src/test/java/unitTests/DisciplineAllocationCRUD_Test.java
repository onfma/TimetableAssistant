package unitTests;

import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.ClassType;
import org.example.timetableassistant.database.crud.DisciplineAllocationsCRUD;
import org.example.timetableassistant.database.crud.TeacherCRUD;
import org.example.timetableassistant.model.DisciplineAllocation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.MockedStatic;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DisciplineAllocationCRUD_Test {

    private final DisciplineAllocationsCRUD crud = new DisciplineAllocationsCRUD();
    private final Connection mockConn = mock(Connection.class);
    private final PreparedStatement mockStmt = mock(PreparedStatement.class);
    private final ResultSet mockRs = mock(ResultSet.class);

    @Test
    void testInsertDisciplineAllocation_Success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertDisciplineAllocation(1, 2, 3, 4);

            assertTrue(result.isSuccess());
            assertEquals("Alocare disciplină adăugată cu succes.", result.getMessage());
            verify(mockStmt).setInt(1, 1);
            verify(mockStmt).setInt(2, 2);
            verify(mockStmt).setInt(3, 3);
            verify(mockStmt).setInt(4, 4);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testInsertDisciplineAllocation_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Insert error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertDisciplineAllocation(1, 2, 3, 4);


            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la inserarea alocării disciplinei: "));
        }
    }
    @Test
    void testUpdateDisciplineAllocation_Success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateDisciplineAllocation(1, 2, 2, 3, 4);
            assertTrue(result.isSuccess());
            assertEquals("Alocare actualizată cu succes.", result.getMessage());
            verify(mockStmt).setInt(1, 1);
            verify(mockStmt).setInt(2, 2);
            verify(mockStmt).setInt(3, 2);
            verify(mockStmt).setInt(4, 3);
            verify(mockStmt).setInt(5, 4);
            verify(mockStmt).executeUpdate();

        }
    }
    @Test
    void testUpdateDisciplineAllocation_NotFound() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0); // Simulate no rows updated

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateDisciplineAllocation(999, 2, 2, 3, 4);
            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("nu a fost găsită"));
            verify(mockStmt).executeUpdate();
        }
    }
    @Test
    void testUpdateDisciplineAllocation_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenThrow(new SQLException("Update error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateDisciplineAllocation(1, 2, 2, 3, 4);
            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().startsWith("Eroare la actualizarea alocării:"));
            verify(mockStmt).executeUpdate();
        }
    }
    @Test
    void testDeleteDisciplineAllocation_Success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1); // 1 row affected

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteDisciplineAllocation(1);

            assertTrue(result.isSuccess());
            assertEquals("Alocare ștearsă cu succes.", result.getMessage());
            verify(mockStmt).setInt(1, 1);
            verify(mockStmt).executeUpdate();
        }
    }
    @Test
    void testDeleteDisciplineAllocation_NotFound() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0); // No rows affected

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteDisciplineAllocation(99);

            assertFalse(result.isSuccess());
            assertEquals("Alocare cu ID-ul 99 nu a fost găsită.", result.getMessage());
            verify(mockStmt).setInt(1, 99);
            verify(mockStmt).executeUpdate();
        }
    }
    @Test
    void testDeleteDisciplineAllocation_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteDisciplineAllocation(1);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la ștergerea alocării"));
        }
    }


}
