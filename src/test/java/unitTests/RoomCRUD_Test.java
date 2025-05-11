package unitTests;

import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.RoomCRUD;
import org.example.timetableassistant.database.crud.TeacherCRUD;
import org.example.timetableassistant.model.Room;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.MockedStatic;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomCRUD_Test {

    private final RoomCRUD crud = new RoomCRUD();
    private final Connection mockConn = mock(Connection.class);
    private final PreparedStatement mockStmt = mock(PreparedStatement.class);
    private final ResultSet mockRs = mock(ResultSet.class);


    @Test
    void testInsertRoom_Success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertRoom("Room A", 30, 1);

            assertTrue(result.isSuccess());
            assertEquals("Camera a fost adăugată cu succes.", result.getMessage());
            verify(mockStmt).setString(1, "Room A");
            verify(mockStmt).setInt(2, 30);
            verify(mockStmt).setInt(3, 1);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testInsertRoom_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Insert error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.insertRoom("Room B", 40, 2);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la inserarea camerei"));
        }
    }

    @Test
    void testGetRoomById_Success() throws Exception {
        int testId = 1;
        String testName = "Room A";
        int testCapacity = 30;
        int testRoomTypeId = 1;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("id")).thenReturn(testId);
        when(mockRs.getString("name")).thenReturn(testName);
        when(mockRs.getInt("capacity")).thenReturn(testCapacity);
        when(mockRs.getInt("room_type_id")).thenReturn(testRoomTypeId);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getRoomById(testId);

            assertTrue(result.isSuccess());
            if (result.success && result.message instanceof Map<?, ?>) {
                Map<?, ?> map = (Map<?, ?>) result.message;
                int id = (int) map.get("id");
                String name = (String) map.get("name");
                int capacity = (int) map.get("capacity");
                int roomTypeId = (int) map.get("room_type_id");

                assertEquals(testId, id);
                assertEquals(testName, name);
                assertEquals(testCapacity, capacity);
                assertEquals(testRoomTypeId, roomTypeId);
            }
        }
    }

    @Test
    void testGetRoomById_NotFound() throws Exception {
        int testId = 2;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getRoomById(testId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("nu a fost găsită"));
        }
    }

    @Test
    void testGetRoomById_SQLException() throws Exception {
        int testId = 3;

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Query error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getRoomById(testId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la obținerea camerei"));
        }
    }


    @Test
    void testGetAllRooms_Success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        // Simulate two rooms in the result set
        when(mockRs.next()).thenReturn(true, true, false);
        when(mockRs.getInt("id")).thenReturn(1, 2);
        when(mockRs.getString("name")).thenReturn("Room A", "Room B");
        when(mockRs.getInt("capacity")).thenReturn(30, 40);
        when(mockRs.getInt("room_type_id")).thenReturn(1, 2);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getAllRooms();

            assertTrue(result.isSuccess());
            assertInstanceOf(List.class, result.message);

            List<Map<String, Object>> list = new ArrayList<>();

            if (result.success) {
                List<?> rawList = (List<?>) result.message;
                for (Object obj : rawList) {
                    if (obj instanceof Map<?, ?> map) {
                        int id = (int) map.get("id");
                        String name = (String) map.get("name");
                        int capacity = (int) map.get("capacity");
                        int roomTypeId = (int) map.get("room_type_id");

                        list.add(Map.of("id", id, "name", name, "capacity", capacity, "room_type_id", roomTypeId));
                    }
                }
            }

            assertEquals(2, list.size());

            Map<String, Object> room1 = list.get(0);
            assertEquals(1, room1.get("id"));
            assertEquals("Room A", room1.get("name"));
            assertEquals(30, room1.get("capacity"));
            assertEquals(1, room1.get("room_type_id"));

            Map<String, Object> room2 = list.get(1);
            assertEquals(2, room2.get("id"));
            assertEquals("Room B", room2.get("name"));
            assertEquals(40, room2.get("capacity"));
            assertEquals(2, room2.get("room_type_id"));
        }
    }

    @Test
    void testGetAllRooms_NotFound() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getAllRooms();

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Nu există camere înregistrate"));
        }
    }

    @Test
    void testGetAllRooms_SQLException() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Query error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.getAllRooms();

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la obținerea camerelor"));
        }
    }


    @Test
    void testUpdateRoom_Success() throws Exception {
        int testId = 1;
        String newName = "Room C";
        int newCapacity = 50;
        int newRoomTypeId = 2;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateRoom(testId, newName, newCapacity, newRoomTypeId);

            assertTrue(result.isSuccess());
            assertEquals("Camera a fost actualizată cu succes.", result.getMessage());
            verify(mockStmt).setString(1, newName);
            verify(mockStmt).setInt(2, newCapacity);
            verify(mockStmt).setInt(3, newRoomTypeId);
            verify(mockStmt).setInt(4, testId);
            verify(mockStmt).executeUpdate();
        }
    }


    @Test
    void testUpdateRoom_NotFound() throws Exception {
        int testId = 2;
        String newName = "Room D";
        int newCapacity = 60;
        int newRoomTypeId = 3;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);
            OperationResult result = crud.updateRoom(testId, newName, newCapacity, newRoomTypeId);

            assertFalse(result.isSuccess());
            assertEquals("Camera cu ID-ul " + testId + " nu a fost găsită.", result.getMessage());
        }
    }

    @Test
    void testUpdateRoom_SQLException() throws Exception {
        int testId = 3;
        String newName = "Room E";
        int newCapacity = 70;
        int newRoomTypeId = 4;

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Update error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.updateRoom(testId, newName, newCapacity, newRoomTypeId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la actualizarea camerei"));
        }
    }

    @Test
    void testDeleteRoom_Success() throws Exception {
        int testId = 1;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteRoom(testId);

            assertTrue(result.isSuccess());
            assertEquals("Camera a fost ștearsă cu succes.", result.getMessage());
            verify(mockStmt).setInt(1, testId);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testDeleteRoom_NotFound() throws Exception {
        int testId = 2;

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteRoom(testId);

            assertFalse(result.isSuccess());
            assertEquals("Camera cu ID-ul " + testId + " nu a fost găsită.", result.getMessage());
        }
    }

    @Test
    void testDeleteRoom_SQLException() throws Exception {
        int testId = 3;

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Delete error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            OperationResult result = crud.deleteRoom(testId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la ștergerea camerei"));
        }
    }
}
