package org.example.timetableassistant.constroller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;

public class MainController {
    @FXML
    private TextField eventNameField;

    @FXML
    private TextField eventTimeField;

    @FXML
    private GridPane timetableGrid;

    @FXML
    public void initialize() {
        createTimetableSkeleton();
    }

    private void createTimetableSkeleton() {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        int hours = 10; // 8:00 to 18:00

        // Top row: Days
        for (int i = 0; i < days.length; i++) {
            timetableGrid.add(new Label(days[i]), i + 1, 0);
        }

        // First column: Time slots
        for (int i = 0; i <= hours; i++) {
            int hour = 8 + i;
            timetableGrid.add(new Label(hour + ":00"), 0, i + 1);
        }
    }

    @FXML
    private void handleAddEvent() {
        String eventName = eventNameField.getText();
        String eventTime = eventTimeField.getText();
        if (!eventName.isEmpty() && !eventTime.isEmpty()) {
            Label eventLabel = new Label(eventName);
            eventLabel.getStyleClass().add("event-label");

            // Example: always add to Monday 09:00
            timetableGrid.add(eventLabel, 1, 2); // (columnIndex, rowIndex)
        }
    }
}
