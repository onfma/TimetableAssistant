package org.example.timetableassistant.model;

public class Event {
    private String name;
    private String time;

    public Event(String name, String time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }
}
