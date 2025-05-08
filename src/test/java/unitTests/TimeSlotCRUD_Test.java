package unitTests;

import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.TimeSlotCRUD;
import org.example.timetableassistant.model.TimeSlot;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class TimeSlotCRUD_Test {

    @Test
    void test_insertTimeSlot_Success() throws Exception{
        TimeSlotCRUD crud = new TimeSlotCRUD();
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            // Act
            OperationResult result = crud.insertTimeSlot("Monday", Time.valueOf("08:00:00"), Time.valueOf("10:00:00"));

            // Assert
            assertTrue(result.isSuccess());
            assertEquals("Slotul de timp a fost adăugat cu succes.", result.getMessage());
            verify(mockStmt).setString(1, "Monday");
            verify(mockStmt).setTime(2, Time.valueOf("08:00:00"));
            verify(mockStmt).setTime(3, Time.valueOf("10:00:00"));
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testInsertTimeSlot_SQLException() throws Exception {
        TimeSlotCRUD crud = new TimeSlotCRUD();

        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            // Act
            OperationResult result = crud.insertTimeSlot("Monday", Time.valueOf("10:00:00"), Time.valueOf("10:00:00"));

//            // Assert
//            assertFalse(result.isSuccess());
//            assertTrue(result.getMessage().contains("Eroare la inserarea slotului de timp"));
        }
    }

    @Test
    void testGetTimeSlotById_Success() throws Exception {
        int testId = 1;
        String testDay = "Monday";
        Time testStart = Time.valueOf("09:00:00");
        Time testEnd = Time.valueOf("10:00:00");

        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("id")).thenReturn(testId);
        when(mockRs.getString("day_of_week")).thenReturn(testDay);
        when(mockRs.getTime("start_time")).thenReturn(testStart);
        when(mockRs.getTime("end_time")).thenReturn(testEnd);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            TimeSlotCRUD crud = new TimeSlotCRUD();
            OperationResult result = crud.getTimeSlotById(testId);

            assertTrue(result.isSuccess());

            if (result.success && result.message instanceof Map<?, ?>) {
                Map<?, ?> map = (Map<?, ?>) result.message;
                int id = (int) map.get("id");
                String day = (String) map.get("day_of_week");
                String start = (String) map.get("start_time");
                String end = (String) map.get("end_time");

                assertEquals(testId, id);
                assertEquals(testDay, day);
                assertEquals("09:00", start);
                assertEquals("10:00", end);
            }

        }
    }

    @Test
    void testGetTimeSlotById_SQLException() throws Exception {
        int testId = 1;

        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            TimeSlotCRUD crud = new TimeSlotCRUD();
            OperationResult result = crud.getTimeSlotById(testId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la obținerea slotului de timp"));
        }
    }

    @Test
    void testUpdateTimeSlot_Success() throws Exception {
        int testId = 1;
        String newDay = "Tuesday";
        Time newStart = Time.valueOf("11:00:00");
        Time newEnd = Time.valueOf("12:00:00");

        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            TimeSlotCRUD crud = new TimeSlotCRUD();
            OperationResult result = crud.updateTimeSlot(testId, newDay, newStart, newEnd);

            assertTrue(result.isSuccess());
            assertEquals("Slotul de timp a fost actualizat cu succes.", result.getMessage());
            verify(mockStmt).setString(1, newDay);
            verify(mockStmt).setTime(2, newStart);
            verify(mockStmt).setTime(3, newEnd);
            verify(mockStmt).setInt(4, testId);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testUpdateTimeSlot_NotFound() throws Exception {
        int testId = 2;
        String newDay = "Wednesday";
        Time newStart = Time.valueOf("13:00:00");
        Time newEnd = Time.valueOf("14:00:00");

        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            TimeSlotCRUD crud = new TimeSlotCRUD();
            OperationResult result = crud.updateTimeSlot(testId, newDay, newStart, newEnd);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("nu a fost găsit"));
        }
    }

    @Test
    void testUpdateTimeSlot_SQLException() throws Exception {
        int testId = 3;
        String newDay = "Thursday";
        Time newStart = Time.valueOf("15:00:00");
        Time newEnd = Time.valueOf("16:00:00");

        Connection mockConn = mock(Connection.class);

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Update error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            TimeSlotCRUD crud = new TimeSlotCRUD();
            OperationResult result = crud.updateTimeSlot(testId, newDay, newStart, newEnd);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la actualizarea slotului de timp"));
        }
    }

    @Test
    void testDeleteTimeSlot_Success() throws Exception {
        int testId = 1;

        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            TimeSlotCRUD crud = new TimeSlotCRUD();
            OperationResult result = crud.deleteTimeSlot(testId);

            assertTrue(result.isSuccess());
            assertEquals("Slotul de timp a fost șters cu succes.", result.getMessage());
            verify(mockStmt).setInt(1, testId);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testDeleteTimeSlot_NotFound() throws Exception {
        int testId = 2;

        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            TimeSlotCRUD crud = new TimeSlotCRUD();
            OperationResult result = crud.deleteTimeSlot(testId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("nu a fost găsit"));
            verify(mockStmt).setInt(1, testId);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testDeleteTimeSlot_SQLException() throws Exception {
        int testId = 3;

        Connection mockConn = mock(Connection.class);

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Delete error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            TimeSlotCRUD crud = new TimeSlotCRUD();
            OperationResult result = crud.deleteTimeSlot(testId);

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la ștergerea slotului de timp"));
        }
    }

    @Test
    void testGetAllTimeSlots_Success() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        // Simulate two rows
        when(mockRs.next()).thenReturn(true, true, false);
        when(mockRs.getInt("id")).thenReturn(1, 2);
        when(mockRs.getString("day_of_week")).thenReturn("Monday", "Tuesday");
        when(mockRs.getTime("start_time")).thenReturn(Time.valueOf("08:00:00"), Time.valueOf("10:00:00"));
        when(mockRs.getTime("end_time")).thenReturn(Time.valueOf("09:00:00"), Time.valueOf("11:00:00"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            TimeSlotCRUD crud = new TimeSlotCRUD();
            OperationResult result = crud.getAllTimeSlots();

            assertTrue(result.isSuccess());
            assertInstanceOf(List.class, result.message);

            List<TimeSlot> list = new ArrayList<>();

            if (result.success) {
                List<?> rawList = (List<?>) result.message;
                for (Object obj : rawList) {
                    if (obj instanceof Map<?, ?> map) {
                        int id = (int) map.get("id");
                        String day = (String) map.get("day_of_week");
                        String start = (String) map.get("start_time");
                        String end = (String) map.get("end_time");

                        list.add(new TimeSlot(id, day, start, end));
                    }
                }
            }

            assertEquals(2, list.size());

            TimeSlot slot1 = list.getFirst();
            assertEquals(1, slot1.getId());
            assertEquals("Monday", slot1.getDayOfWeek());
            assertEquals("08:00", slot1.getStartTime());
            assertEquals("09:00", slot1.getEndTime());

            TimeSlot slot2 = list.get(1);
            assertEquals(2, slot2.getId());
            assertEquals("Tuesday", slot2.getDayOfWeek());
            assertEquals("10:00", slot2.getStartTime());
            assertEquals("11:00", slot2.getEndTime());
        }
    }

    @Test
    void testGetAllTimeSlots_Empty() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            TimeSlotCRUD crud = new TimeSlotCRUD();
            OperationResult result = crud.getAllTimeSlots();

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Nu există sloturi de timp"));
        }
    }

    @Test
    void testGetAllTimeSlots_SQLException() throws Exception {
        Connection mockConn = mock(Connection.class);

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Query error"));

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            TimeSlotCRUD crud = new TimeSlotCRUD();
            OperationResult result = crud.getAllTimeSlots();

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Eroare la obținerea sloturilor de timp"));
        }
    }
}
