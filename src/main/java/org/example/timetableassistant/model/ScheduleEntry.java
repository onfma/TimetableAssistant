package org.example.timetableassistant.model;

public class ScheduleEntry {
    private String day, time, subject, teacher, room, group, classType;

    public ScheduleEntry(String day, String time, String subject, String teacher, String room, String group, String classType) {
        this.day = day;
        this.time = time;
        this.subject = subject;
        this.teacher = teacher;
        this.room = room;
        this.group = group;
        this.classType = classType;
    }

    public String getDay() { return day; }
    public String getTime() { return time; }
    public String getSubject() { return subject; }
    public String getTeacher() { return teacher; }
    public String getRoom() { return room; }
    public String getGroup() { return group; }
    public String getClassType() { return classType; }
}