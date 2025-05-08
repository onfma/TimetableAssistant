package org.example.timetableassistant.model;

import org.example.timetableassistant.database.crud.ClassType;

public class DisciplineAllocation {
    private int id;
    private Discipline discipline;
    private Teacher teacher;
    private ClassType classType;

    public DisciplineAllocation(int id, Discipline discipline, Teacher teacher, ClassType classType) {
        this.id = id;
        this.discipline = discipline;
        this.teacher = teacher;
        this.classType = classType;
    }

    public int getId() {return id;}
    public Discipline getDiscipline() {return discipline;}
    public Teacher getTeacher() {return teacher;}
    public ClassType getClassType() {return classType;}

    public void setId(int id) {this.id = id;}
    public void setDiscipline(Discipline discipline) {this.discipline = discipline;}
    public void setTeacher(Teacher teacher) {this.teacher = teacher;}
    public void setClassType(ClassType classType) {this.classType = classType;}

}
