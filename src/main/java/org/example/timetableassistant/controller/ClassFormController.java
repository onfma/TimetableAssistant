package org.example.timetableassistant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.timetableassistant.model.ScheduleEntry;

public class ClassFormController {

    @FXML private ComboBox<String> groupComboBox;
    @FXML private ComboBox<String> disciplineComboBox;
    @FXML private ComboBox<String> teacherComboBox;
    @FXML private ComboBox<String> roomComboBox;
    @FXML private ComboBox<String> timeSlotComboBox;
    @FXML private ComboBox<String> classTypeComboBox;

    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private ScheduleEntry entryToEdit;

    private boolean isSaveClicked = false;

    public boolean isSaveClicked() {
        return isSaveClicked;
    }

    public ScheduleEntry getScheduleEntry() {
        return new ScheduleEntry(
                timeSlotComboBox.getValue(),
                timeSlotComboBox.getValue(),
                disciplineComboBox.getValue(),
                teacherComboBox.getValue(),
                roomComboBox.getValue(),
                groupComboBox.getValue(),
                classTypeComboBox.getValue()
        );
    }

    public void setEntryToEdit(ScheduleEntry entry) {
        this.entryToEdit = entry;

        if (entry != null) {
            groupComboBox.setValue(entry.getGroup());
            disciplineComboBox.setValue(entry.getSubject());
            teacherComboBox.setValue(entry.getTeacher());
            roomComboBox.setValue(entry.getRoom());
            timeSlotComboBox.setValue(entry.getTime());
            classTypeComboBox.setValue(entry.getClassType());
        }
    }

    @FXML
    private void initialize() {
        //Dummy data
        groupComboBox.getItems().addAll("Grupa 1", "Grupa 2", "Grupa 3");
        disciplineComboBox.getItems().addAll("Matematică", "Informatica", "Fizică");
        teacherComboBox.getItems().addAll("Profesor A", "Profesor B", "Profesor C");
        roomComboBox.getItems().addAll("Sala 101", "Sala 102", "Sala 103");
        timeSlotComboBox.getItems().addAll("08:00-10:00", "10:00-12:00", "12:00-14:00", "14:00-16:00");
        classTypeComboBox.getItems().addAll("Curs", "Seminar", "Laborator");

        cancelButton.setOnAction(e -> ((Stage) cancelButton.getScene().getWindow()).close());

        saveButton.setOnAction(e -> {
            isSaveClicked = true;
            ((Stage) saveButton.getScene().getWindow()).close();
        });
    }
}
