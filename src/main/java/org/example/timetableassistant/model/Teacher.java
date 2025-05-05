package org.example.timetableassistant.model;

import java.util.ArrayList;
import java.util.List;

public class Teacher {
    private int id;
    private String name;
    private List<Discipline> disciplines;

    public Teacher(int id, String name) {
        this.id = id;
        this.name = name;
        this.disciplines = new ArrayList<>();
    }

    public Teacher(int id, String name, List<Discipline> disciplines) {
        this.id = id;
        this.name = name;
        this.disciplines = disciplines;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public List<String> getDisciplines() {
        List<String> disciplineNames = new ArrayList<>();
        for(Discipline discipline : disciplines) {
            disciplineNames.add(discipline.getName());
        };
        return disciplineNames;
    }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void addDisciplines(List<Discipline> disciplines) { this.disciplines = disciplines; }
}
