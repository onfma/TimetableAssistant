package org.example.timetableassistant.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {
    private static final String URL_DEFAULT = "jdbc:postgresql://localhost:5432/postgres";
    private static final String URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUser();
    private static final String PASSWORD = DatabaseConfig.getPassword();

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            createDatabaseIfNotExists();
            createTables();
        } catch (ClassNotFoundException e) {
            System.out.println("Nu am găsit driverul PostgreSQL JDBC: " + e.getMessage());
        }
    }

    private static void createDatabaseIfNotExists() {
        try (Connection conn = DriverManager.getConnection(URL_DEFAULT, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            String createDb = "CREATE DATABASE TimetableAssistant";
            stmt.executeUpdate(createDb);
            System.out.println("Baza de date 'TimetableAssistant' a fost creată!");

        } catch (SQLException e) {
            if (e.getSQLState().equals("42P04")) {
                System.out.println("Baza de date 'TimetableAssistant' există deja.");
            } else {
                System.out.println("Eroare la crearea bazei de date: " + e.getMessage());
            }
        }
    }

    private static void createTables() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            String createSemiYears = """
                CREATE TABLE IF NOT EXISTS semiyears (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(2) NOT NULL,           -- A, B, E
                    study_year INT NOT NULL             -- 1, 2, 3
                );
            """;

            String createGroups = """
                CREATE TYPE semiyear_enum AS ENUM ('1A', '1B', '2A', '2B', '3A', '3B');
            
                CREATE TABLE IF NOT EXISTS groups (
                    id SERIAL PRIMARY KEY,
                    number INT NOT NULL,
                    semiyear semiyear_enum NOT NULL,
                    UNIQUE (number, semiyear)
                );
            """;

            String createTeachers = """
                CREATE TABLE IF NOT EXISTS teachers (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(100) NOT NULL
                );
                """;

            String createDisciplines = """
                CREATE TABLE IF NOT EXISTS disciplines (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(100) NOT NULL
                );
                """;

            String createClassTypes = """
                CREATE TABLE IF NOT EXISTS class_types (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(50) NOT NULL
                );
                """;

            String createDisciplineAllocations = """
                    CREATE TABLE IF NOT EXISTS discipline_allocations (
                        id SERIAL PRIMARY KEY,
                        discipline_id INT REFERENCES disciplines(id),
                        teacher_id INT REFERENCES teachers(id),
                        class_type_id INT NOT NULL,
                        hours_per_week INT NOT NULL
                    );           
                    """;

            String createRoomTypes = """
                CREATE TABLE IF NOT EXISTS room_types (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(20) NOT NULL UNIQUE,
                    CHECK (name IN ('curs', 'seminar', 'laborator'))
                );
                INSERT INTO room_types (name) VALUES ('curs');
                INSERT INTO room_types (name) VALUES ('seminar');
                INSERT INTO room_types (name) VALUES ('laborator');
                """;

            String createRooms = """
                CREATE TABLE IF NOT EXISTS rooms (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(50) NOT NULL,
                    capacity INT NOT NULL,
                    room_type_id INT REFERENCES room_types(id)
                );
                """;


            String createTimeSlots = """
                CREATE TABLE IF NOT EXISTS time_slots (
                    id SERIAL PRIMARY KEY,
                    day_of_week VARCHAR(10) NOT NULL,
                    start_time TIME NOT NULL,
                    end_time TIME NOT NULL
                );
                """;

            String createClasses = """
            CREATE TABLE IF NOT EXISTS classes (
                    id SERIAL PRIMARY KEY,
                    discipline_id INT REFERENCES disciplines(id),
                    class_type_id INT REFERENCES class_types(id),
                    room_id INT REFERENCES rooms(id),
                    time_slot_id INT REFERENCES time_slots(id),
                    teacher_id INT REFERENCES teachers(id),
                    semiyear semiyear_enum NULL,
                    group_id INT NULL REFERENCES groups(id),
                    CHECK (
                        (class_type_id = 1 AND semiyear IS NOT NULL AND group_id IS NULL) OR  -- curs
                        (class_type_id IN (2, 3) AND group_id IS NOT NULL AND semiyear IS NULL) -- sem/lab
                    )
                );
            
            ALTER TABLE discipline_allocations DROP CONSTRAINT IF EXISTS discipline_allocations_class_type_id_fkey;
            ALTER TABLE discipline_allocations ADD CONSTRAINT valid_class_type CHECK (class_type_id IN (1, 2, 3));
            """;

            stmt.execute(createSemiYears);
            stmt.execute(createGroups);
            stmt.execute(createTeachers);
            stmt.execute(createDisciplines);
            stmt.execute(createClassTypes);
            stmt.execute(createDisciplineAllocations);
            stmt.execute(createRoomTypes);
            stmt.execute(createRooms);
            stmt.execute(createTimeSlots);
            stmt.execute(createClasses);

            System.out.println("Tabelele au fost create cu succes în baza de date 'TimetableAssistant'!");

            try {
                String dbPopulationScript = new String(java.nio.file.Files.readAllBytes(
                        java.nio.file.Paths.get("src/main/java/org/example/timetableassistant/database/db_population.sql")
                ));
                stmt.execute(dbPopulationScript);
                System.out.println("Scriptul 'db_population.sql' a fost executat cu succes!");
            } catch (java.io.IOException e) {
                System.out.println("Eroare la citirea scriptului 'db_population.sql': " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Eroare la crearea tabelelor: " + e.getMessage());
        }
    }
}
