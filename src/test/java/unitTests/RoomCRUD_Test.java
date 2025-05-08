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
}
