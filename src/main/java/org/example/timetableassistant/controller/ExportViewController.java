package org.example.timetableassistant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ExportViewController {

    @FXML
    private void handleExportStudents() {
        showExportAlert("Export Orar Studenți");
    }

    @FXML
    private void handleExportTeachers() {
        showExportAlert("Export Orar Profesori");
    }

    @FXML
    private void handleExportRooms() {
        showExportAlert("Export Orar Săli");
    }

    @FXML
    private void handleExportDisciplines() {
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
