package org.example.timetableassistant.model;

import java.util.List;

public class Teacher {
    private int id;
    private String name;
    private List<String> disciplines;

    public Teacher(int id, String name, List<String> disciplines) {
        this.id = id;
        this.name = name;
        this.disciplines = disciplines;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public List<String> getDisciplines() { return disciplines; }

    public void setName(String name) { this.name = name; }
    public void setDisciplines(List<String> disciplines) { this.disciplines = disciplines; }

    @Override
    public String toString() {
        return name + " - " + String.join(", ", disciplines);
    }
}
