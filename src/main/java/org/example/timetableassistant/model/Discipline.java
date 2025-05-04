package org.example.timetableassistant.model;

public class Discipline {
    private int id;
    private String name;
    private String type;

    public Discipline(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }

    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
