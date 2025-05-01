package org.example.timetableassistant.database;

public class OperationResult {
    public boolean success;
    public Object message;

    public OperationResult(boolean success, Object message) {
        this.success = success;
        this.message = message;
    }
}
