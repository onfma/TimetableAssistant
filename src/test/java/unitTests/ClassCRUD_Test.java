package unitTests;

import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.ClassCRUD;
import org.example.timetableassistant.database.crud.TeacherCRUD;
import org.example.timetableassistant.model.Class;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.MockedStatic;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassCRUD_Test {

    private final ClassCRUD crud = new ClassCRUD();
    private final Connection mockConn = mock(Connection.class);
    private final PreparedStatement mockStmt = mock(PreparedStatement.class);
    private final ResultSet mockRs = mock(ResultSet.class);
}
