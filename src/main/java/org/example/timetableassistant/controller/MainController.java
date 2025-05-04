package org.example.timetableassistant.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class MainController {

    @FXML
    private StackPane mainContent;

    @FXML private Button btnStudents;
    @FXML private Button btnTeachers;
    @FXML private Button btnDisciplines;
    @FXML private Button btnRooms;
    @FXML private Button btnSchedule;
    @FXML private Button btnExport;

    @FXML
    public void initialize() {
        btnStudents.setOnAction(e -> loadView("/org/example/timetableassistant/view/GroupsView.fxml"));
        btnTeachers.setOnAction(e -> loadView("/org/example/timetableassistant/view/TeachersView.fxml"));
        btnDisciplines.setOnAction(e -> loadView("/org/example/timetableassistant/view/DisciplinesView.fxml"));
        btnRooms.setOnAction(e -> loadView("/org/example/timetableassistant/view/RoomsView.fxml"));
        btnSchedule.setOnAction(e -> loadView("/org/example/timetableassistant/view/ScheduleView.fxml"));
        btnExport.setOnAction(e -> loadView("/org/example/timetableassistant/view/ExportView.fxml"));
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            mainContent.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
