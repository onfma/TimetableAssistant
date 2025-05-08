package org.example.timetableassistant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.example.timetableassistant.service.Exporter;

public class ExportViewController {
    private final Exporter exporter = new Exporter();

    @FXML
    private void handleExportStudents() {
        exporter.generateGroupTimetable();
        showExportAlert("Export Orar Studenți");
    }

    @FXML
    private void handleExportTeachers() {
        exporter.generateTeacherTimetable();
        showExportAlert("Export Orar Profesori");
    }

    @FXML
    private void handleExportRooms() {
        exporter.generateRoomTimetable();
        showExportAlert("Export Orar Săli");
    }

    @FXML
    private void handleExportDisciplines() {
        //exporter.generateDisciplineTimeTable();
        showExportAlert("Export Orar Discipline");
    }

    private void showExportAlert(String title) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Export Successful");
        alert.setHeaderText(null);
        alert.setContentText(title + " a fost exportat cu succes!");
        alert.showAndWait();
    }
}
