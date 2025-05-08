package org.example.timetableassistant.model;

public class Group {
    private int id;
    private int number;
    private Semiyear semiyear;

    public Group(int id, int number, Semiyear semiyear) {
        this.id = id;
        this.number = number;
        this.semiyear = semiyear;
    }

    public int getId() { return id; }
    public int getNumber() { return number; }
    public Semiyear getSemiyear() { return semiyear; }

    public void setNumber(int number) { this.number = number; }
    public void setSemiyear(Semiyear semiyear) { this.semiyear = semiyear; }

    @Override
    public String toString() {
        return semiyear.getValue() + number;
    }
}
