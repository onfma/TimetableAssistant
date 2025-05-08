package unitTests;

import org.example.timetableassistant.database.OperationResult;
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
}
