package org.example.timetableassistant.model;

import org.example.timetableassistant.database.crud.ClassType;

public class Class {
    private int classId;
    private int disciplineId;
    private ClassType classType;
    private int roomId;
    private int timeSlotId;
    private int teacherId;
    private Semiyear semiyear;
    private Integer groupId;

    public Class(int classId, int roomId, ClassType classType, int disciplineId, int timeSlotId, Integer groupId, Semiyear semiyear, int teacherId) {
        this.classId = classId;
        this.roomId = roomId;
        this.classType = classType;
        this.disciplineId = disciplineId;
        this.timeSlotId = timeSlotId;
        this.groupId = groupId;
        this.semiyear = semiyear;
        this.teacherId = teacherId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(int disciplineId) {
        this.disciplineId = disciplineId;
    }

    public ClassType getClassType() {
        return classType;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(int timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public Semiyear getSemiyear() {
        return semiyear;
    }

    public void setSemiyear(Semiyear semiyear) {
        this.semiyear = semiyear;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
