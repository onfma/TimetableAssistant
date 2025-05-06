package org.example.timetableassistant.model;

import org.example.timetableassistant.database.crud.ClassType;
import org.example.timetableassistant.service.DisciplineAllocationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, ClassType> getDisciplines() throws Exception {
        Map<String, ClassType> disciplines = new HashMap<>();
        DisciplineAllocationService.getByTeacherId(this.id).forEach(disciplineAllocation -> {
            disciplines.put(disciplineAllocation.getDiscipline().getName(), disciplineAllocation.getClassType());
        });
        return disciplines;
    }
}
