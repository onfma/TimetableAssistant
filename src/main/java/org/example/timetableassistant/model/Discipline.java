package org.example.timetableassistant.model;

public class Discipline {
    private int id;
    private String name;

    public Discipline(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Discipline() {

    }

    public int getId() { return id; }
    public String getName() { return name; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
}
