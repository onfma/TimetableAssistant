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

            String createStudents = """
                CREATE TABLE IF NOT EXISTS students (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    study_year INT NOT NULL,
                    group_name VARCHAR(10) NOT NULL
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
                    name VARCHAR(100) NOT NULL,
                    teacher_id INT REFERENCES teachers(id)
                );
                """;

            String createClassTypes = """
                CREATE TABLE IF NOT EXISTS class_types (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(50) NOT NULL
                );
                """;

            String createRooms = """
                CREATE TABLE IF NOT EXISTS rooms (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(50) NOT NULL,
                    capacity INT NOT NULL,
                    type VARCHAR(20) NOT NULL
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
                    study_year INT,
                    group_name VARCHAR(10),
                    teacher_id INT REFERENCES teachers(id)
                );
                """;

            stmt.execute(createStudents);
            stmt.execute(createTeachers);
            stmt.execute(createDisciplines);
            stmt.execute(createClassTypes);
            stmt.execute(createRooms);
            stmt.execute(createTimeSlots);
            stmt.execute(createClasses);

            System.out.println("Tabelele au fost create cu succes în baza de date 'TimetableAssistant'!");

        } catch (SQLException e) {
            System.out.println("Eroare la crearea tabelelor: " + e.getMessage());
        }
    }
}
