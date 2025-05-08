package org.example.timetableassistant.model;

public enum Semiyear {
    SEM_1A("1A"),
    SEM_1B("1B"),
    SEM_2A("2A"),
    SEM_2B("2B"),
    SEM_3A("3A"),
    SEM_3B("3B");

    private final String value;

    Semiyear(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Semiyear fromString(String value) {
        for (Semiyear sem : Semiyear.values()) {
            if (sem.getValue().equals(value)) {
                return sem;
            }
        }
        throw new IllegalArgumentException("Unknown semiyear: " + value);
    }
}
