package org.example.timetableassistant.database;

public class DatabaseConfig {
    private static final String URL = "jdbc:postgresql://localhost:5432/timetableassistant";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static String getUrl() {
        return URL;
    }

    public static String getUser() {
        return USER;
    }

    public static String getPassword() {
        return PASSWORD;
    }
}
