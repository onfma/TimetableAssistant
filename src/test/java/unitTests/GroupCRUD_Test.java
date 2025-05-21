package unitTests;

import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.GroupCRUD;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GroupCRUD_Test {

    private final GroupCRUD crud = new GroupCRUD();
    private final Connection mockConn = mock(Connection.class);
    private final PreparedStatement mockStmt = mock(PreparedStatement.class);
    private final ResultSet mockRs = mock(ResultSet.class);

    @Test
    void testInsertGroup_Success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertGroup(101, "SEM_1A");

            assertTrue(result.isSuccess());
            assertEquals("Grupa a fost adăugată cu succes.", result.getMessage());
        }
    }

    @Test
    void testInsertGroup_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Fail"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertGroup(101, "SEM_1A");

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la inserarea grupei"));
        }
    }

    @Test
    void testGetGroupById_Found() throws Exception {
        int testId = 1;
        int testNumber = 101;
        String testSemiyear = "SEM_1A";

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("id")).thenReturn(testId);
        when(mockRs.getInt("number")).thenReturn(testNumber);
        when(mockRs.getString("semiyear")).thenReturn(testSemiyear);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getGroupById(testId);

            assertTrue(result.isSuccess());
            assertNotNull(result.message);
            assertTrue(result.message instanceof Map<?, ?>);

            Map<?, ?> map = (Map<?, ?>) result.message;

            int id = (int) map.get("id");
            int number = (int) map.get("number");
            String semiyear = (String) map.get("semiyear");

            assertEquals(testId, id);
            assertEquals(testNumber, number);
            assertEquals(testSemiyear, semiyear);
        }
    }

    @Test
    void testGetGroupById_NotFound() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getGroupById(99);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("nu a fost găsită"));
        }
    }

    @Test
    void testGetGroupById_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Boom"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getGroupById(1);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la obținerea grupei"));
        }
    }

    @Test
    void testGetAllGroups_Empty() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getAllGroups();

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Nu există grupe"));
        }
    }

    @Test
    void testGetAllGroups_Success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        when(mockRs.next()).thenReturn(true, true, false);
        when(mockRs.getInt("id")).thenReturn(1, 2);
        when(mockRs.getInt("number")).thenReturn(101, 102);
        when(mockRs.getString("semiyear")).thenReturn("SEM_1A", "SEM_1B");

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getAllGroups();

            assertTrue(result.isSuccess());
            assertInstanceOf(List.class, result.message);

            List<Map<String, Object>> list = new ArrayList<>();

            List<?> rawList = (List<?>) result.message;
            for (Object obj : rawList) {
                if (obj instanceof Map<?, ?> map) {
                    int id = (int) map.get("id");
                    int number = (int) map.get("number");
                    String semiyear = (String) map.get("semiyear");

                    list.add(Map.of("id", id, "number", number, "semiyear", semiyear));
                }
            }

            assertEquals(2, list.size());

            Map<String, Object> group1 = list.get(0);
            assertEquals(1, group1.get("id"));
            assertEquals(101, group1.get("number"));
            assertEquals("SEM_1A", group1.get("semiyear"));

            Map<String, Object> group2 = list.get(1);
            assertEquals(2, group2.get("id"));
            assertEquals(102, group2.get("number"));
            assertEquals("SEM_1B", group2.get("semiyear"));
        }
    }

    @Test
    void testGetAllGroups_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("fail"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getAllGroups();

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la obținerea grupelor"));
        }
    }

    @Test
    void testUpdateGroup_Success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateGroup(1, 999, "SEM_2A");

            assertTrue(result.isSuccess());
            assertEquals("Grupa a fost actualizată cu succes.", result.getMessage());
        }
    }

    @Test
    void testUpdateGroup_NotFound() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateGroup(5, 202, "SEM_2A");

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("nu a fost găsită"));
        }
    }

    @Test
    void testUpdateGroup_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Update fail"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateGroup(1, 999, "SEM_2A");

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la actualizarea"));
        }
    }

    @Test
    void testDeleteGroup_NotFound() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteGroup(123);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("nu a fost găsită"));
        }
    }

    @Test
    void testDeleteGroup_Success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteGroup(123);

            assertTrue(result.isSuccess());
            assertEquals("Grupa a fost ștearsă cu succes.", result.getMessage());
        }
    }

    @Test
    void testDeleteGroup_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Delete boom"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteGroup(123);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la ștergerea"));
        }
    }

    @Test
    void testGetGroupByNumberAndSemiyear_Found() throws Exception {
        int expectedId = 7;
        int expectedNumber = 101;
        String expectedSemiyear = "SEM_1A";

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("id")).thenReturn(expectedId);
        when(mockRs.getInt("number")).thenReturn(expectedNumber);
        when(mockRs.getString("semiyear")).thenReturn(expectedSemiyear);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getGroupByNumberAndSemiyear(expectedNumber, expectedSemiyear);

            assertTrue(result.isSuccess());
            assertNotNull(result.message);
            assertTrue(result.message instanceof Map<?, ?>);

            Map<?, ?> dataMap = (Map<?, ?>) result.message;

            assertEquals(expectedId, dataMap.get("id"));
            assertEquals(expectedNumber, dataMap.get("number"));
            assertEquals(expectedSemiyear, dataMap.get("semiyear"));
        }
    }

    @Test
    void testGetGroupByNumberAndSemiyear_NotFound() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getGroupByNumberAndSemiyear(404, "SEM_2B");

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("nu a fost găsită"));
        }
    }

    @Test
    void testGetGroupByNumberAndSemiyear_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("boom"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getGroupByNumberAndSemiyear(101, "SEM_1A");

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la obținerea"));
        }
    }
}
