package unitTests;

import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.RoomTypesCRUD;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.MockedStatic;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomTypeCRUD_Test {

    private final RoomTypesCRUD crud = new RoomTypesCRUD();
    private final Connection mockConn = mock(Connection.class);
    private final PreparedStatement mockStmt = mock(PreparedStatement.class);
    private final ResultSet mockRs = mock(ResultSet.class);

    @Test
    void testInsertRoomType_Success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertRoomType("Laborator");

            assertTrue(result.isSuccess());
            assertEquals("Tipul de sală adăugat cu succes.", result.getMessage());
            verify(mockStmt).setString(1, "Laborator");
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testInsertRoomType_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Insert error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertRoomType("Amfiteatru");

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la inserarea tipului de sală"));
        }
    }

    @Test
    void testGetRoomTypeById_Success() throws Exception {
        int testId = 1;
        String testName = "Laborator";

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("id")).thenReturn(testId);
        when(mockRs.getString("name")).thenReturn(testName);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getRoomTypeById(testId);

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
    void testGetRoomTypeById_NotFound() throws Exception {
        int testId = 2;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getRoomTypeById(testId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("nu a fost găsit"));
        }
    }

    @Test
    void testGetRoomTypeById_SQLException() throws Exception {
        int testId = 3;

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Query error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getRoomTypeById(testId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la obținerea tipului de sală"));
        }
    }

    @Test
    void testUpdateRoomType_Success() throws Exception {
        int testId = 1;
        String newName = "Seminar";

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateRoomType(testId, newName);

            assertTrue(result.isSuccess());
            assertEquals("Tipul de sală a fost actualizat cu succes.", result.getMessage());
            verify(mockStmt).setString(1, newName);
            verify(mockStmt).setInt(2, testId);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testUpdateRoomType_NotFound() throws Exception {
        int testId = 2;
        String newName = "Seminar";

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateRoomType(testId, newName);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("nu a fost găsit"));
            verify(mockStmt).setString(1, newName);
            verify(mockStmt).setInt(2, testId);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testUpdateRoomType_SQLException() throws Exception {
        int testId = 3;
        String newName = "Seminar";

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Update error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateRoomType(testId, newName);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la actualizarea tipului de sală"));
        }
    }

    @Test
    void testDeleteRoomType_Success() throws Exception {
        int testId = 1;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteRoomType(testId);

            assertTrue(result.isSuccess());
            assertEquals("Tipul de sală a fost șters cu succes.", result.getMessage());
            verify(mockStmt).setInt(1, testId);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testDeleteRoomType_NotFound() throws Exception {
        int testId = 2;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteRoomType(testId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("nu a fost găsit"));
            verify(mockStmt).setInt(1, testId);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testDeleteRoomType_SQLException() throws Exception {
        int testId = 3;

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Delete error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteRoomType(testId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la ștergerea tipului de sală"));
        }
    }

    @Test
    void testGetAllRoomTypes_Success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        // Simulate two room types in the result set
        when(mockRs.next()).thenReturn(true, true, false);
        when(mockRs.getInt("id")).thenReturn(1, 2);
        when(mockRs.getString("name")).thenReturn("Laborator", "Amfiteatru");

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getAllRoomTypes();

            assertTrue(result.isSuccess());
            assertInstanceOf(List.class, result.message);

            Map<Integer, String> list = new HashMap<>();

            if (result.success) {
                List<?> rawList = (List<?>) result.message;
                for (Object obj : rawList) {
                    if (obj instanceof Map<?, ?> map) {
                        int id = (int) map.get("id");
                        String name = (String) map.get("name");

                        list.put(id, name);
                    }
                }
            }

            assertEquals(2, list.size());

            String type1_name = list.get(1);
            assertEquals("Laborator", type1_name);

            String type2_name = list.get(2);
            assertEquals("Amfiteatru", type2_name);
        }
    }

    @Test
    void testGetAllRoomTypes_Empty() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getAllRoomTypes();

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Nu există tipuri de săli"));
        }
    }

    @Test
    void testGetAllRoomTypes_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Query error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getAllRoomTypes();

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la obținerea tipurilor de săli"));
        }
    }
}
