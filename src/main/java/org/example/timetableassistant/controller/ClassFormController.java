package org.example.timetableassistant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.timetableassistant.database.crud.ClassType;
import org.example.timetableassistant.model.*;
import org.example.timetableassistant.model.Class;
import org.example.timetableassistant.service.*;

public class ClassFormController {

    @FXML private ComboBox<Group> groupComboBox;
    @FXML private ComboBox<Discipline> disciplineComboBox;
    @FXML private ComboBox<Teacher> teacherComboBox;
    @FXML private ComboBox<Room> roomComboBox;
    @FXML private ComboBox<TimeSlot> timeSlotComboBox;
    @FXML private ComboBox<String> classTypeComboBox;

    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private Class entryToEdit;
    private boolean isSaveClicked = false;

    public boolean isSaveClicked() {
        return isSaveClicked;
    }

    public Class getClassEntry() {
        Group group = groupComboBox.getValue();
        Semiyear semiyear = entryToEdit != null ? entryToEdit.getSemiyear() : Semiyear.SEM_1A;
        return new Class(
                entryToEdit != null ? entryToEdit.getClassId() : null,
                disciplineComboBox.getValue().getId(),
                ClassType.valueOf(classTypeComboBox.getValue()),
                roomComboBox.getValue().getId(),
                timeSlotComboBox.getValue().getId(),
                group != null ? group.getId() : null,
                semiyear,
                teacherComboBox.getValue().getId()
        );
    }

    public void setEntryToEdit(Class entry) {
        this.entryToEdit = entry;

        if (entry != null) {
            if (entry.getGroupId() != null) {
                groupComboBox.setValue(getGroupById(entry.getGroupId()));
            }
            disciplineComboBox.setValue(getDisciplineById(entry.getDisciplineId()));
            teacherComboBox.setValue(getTeacherById(entry.getTeacherId()));
            roomComboBox.setValue(getRoomById(entry.getRoomId()));
            TimeSlot selectedSlot = timeSlotComboBox.getItems().stream()
                    .filter(slot -> slot.getId() == entry.getTimeSlotId())
                    .findFirst()
                    .orElse(null);
            timeSlotComboBox.setValue(selectedSlot);
            classTypeComboBox.setValue(entry.getClassType().name());
        }
    }


    @FXML
    private void initialize() throws Exception {
        disciplineComboBox.getItems().addAll(DisciplineService.getAllDisciplines());
        teacherComboBox.getItems().addAll(TeacherService.getAllTeachers());
        roomComboBox.getItems().addAll(RoomService.getAllRooms());
        groupComboBox.getItems().addAll(GroupService.getAllGroups());
        timeSlotComboBox.getItems().addAll(TimeSlotService.getAllTimeSlots());
        classTypeComboBox.getItems().addAll("Curs", "Seminar", "Laborator");

        cancelButton.setOnAction(e -> ((Stage) cancelButton.getScene().getWindow()).close());

        saveButton.setOnAction(e -> {
            isSaveClicked = true;
            try {
                Group group = groupComboBox.getValue();
                Discipline discipline = disciplineComboBox.getValue();
                Teacher teacher = teacherComboBox.getValue();
                Room room = roomComboBox.getValue();
                String classTypeStr = classTypeComboBox.getValue();
                TimeSlot timeSlot = timeSlotComboBox.getValue();
                Semiyear semiyear = null;
                if (group != null) {
                    semiyear = group.getSemiyear();
                }

                if (entryToEdit == null) {
                    String response = ClassService.createClass(
                            discipline.getId(),
                            ClassType.valueOf(classTypeStr),
                            room.getId(),
                            timeSlot.getId(),
                            semiyear,
                            group != null ? group.getId() : null,
                            teacher.getId()
                    );
                    System.out.println(response);
                } else {
                    String response = ClassService.updateClass(
                            entryToEdit.getClassId(),
                            discipline.getId(),
                            ClassType.valueOf(classTypeStr),
                            room.getId(),
                            timeSlot.getId(),
                            semiyear,
                            group != null ? group.getId() : null,
                            teacher.getId()
                    );
                    System.out.println(response);
                }
                ((Stage) saveButton.getScene().getWindow()).close();
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to save class: " + ex.getMessage());
            }
        });
    }

    private void showAlert(String error, String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(error);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

    private Group getGroupById(int id) {
        return groupComboBox.getItems().stream()
                .filter(g -> g.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private Teacher getTeacherById(int id) {
        return teacherComboBox.getItems().stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private Discipline getDisciplineById(int id) {
        return disciplineComboBox.getItems().stream()
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private Room getRoomById(int id) {
        return roomComboBox.getItems().stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
