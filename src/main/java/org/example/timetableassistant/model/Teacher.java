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

    public Map<String, List<ClassType>> getDisciplines() throws Exception {
        Map<String, List<ClassType>> disciplines = new HashMap<>();
        try {
            List<DisciplineAllocation> allocations = DisciplineAllocationService.getByTeacherId(this.id);
            allocations.forEach(disciplineAllocation -> {
                disciplines.computeIfAbsent(disciplineAllocation.getDiscipline().getName(), k -> new ArrayList<>())
                        .add(disciplineAllocation.getClassType());
            });
        } catch (Exception e) {
            return disciplines;
        }
        return disciplines;
    }
}
