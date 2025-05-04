package org.example.timetableassistant.model;

public class Group {
    private int id;
    private String name;
    private String semiyearId;

    public Group(int id, String name, String semiyearId) {
        this.id = id;
        this.name = name;
        this.semiyearId = semiyearId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSemiyearId() { return semiyearId; }

    public void setName(String name) { this.name = name; }
    public void setSemiyearId(String semiyearId) { this.semiyearId = semiyearId; }

    @Override
    public String toString() {
        return name;
    }
}
