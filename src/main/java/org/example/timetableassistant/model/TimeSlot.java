package org.example.timetableassistant.model;

public class TimeSlot {
    private final int id;
    private final String dayOfWeek;
    private final String startTime;
    private final String endTime;

    public TimeSlot(int id, String dayOfWeek, String startTime, String endTime) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return dayOfWeek + " " + startTime + "-" + endTime;
    }
}
