package org.example.timetableassistant.model;

import org.example.timetableassistant.service.DisciplineAllocationService;

import java.util.ArrayList;
import java.util.List;

public class Teacher {
    private int id;
    private String name;

    public Teacher(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }

    public List<String> getDisciplines() throws Exception {
        List<String> disciplines = new ArrayList<>();
        DisciplineAllocationService.getAllDisciplineAllocations().forEach(disciplineAllocation -> {
            if (disciplineAllocation.getTeacher().getId() == this.id) {
                disciplines.add(disciplineAllocation.getDiscipline().getName());
            }
        });
        return disciplines;
    }
}
