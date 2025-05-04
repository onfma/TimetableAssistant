package org.example.timetableassistant.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.timetableassistant.model.ScheduleEntry;

import java.io.IOException;
import java.util.List;

public class ScheduleViewController {

    @FXML
    private ComboBox<String> filterTypeComboBox;
    @FXML
    private ComboBox<String> filterValueComboBox;
    @FXML
    private TableView<ScheduleEntry> scheduleTable;

    @FXML
    private TableColumn<ScheduleEntry, String> dayColumn;
    @FXML
    private TableColumn<ScheduleEntry, String> timeColumn;
    @FXML
    private TableColumn<ScheduleEntry, String> subjectColumn;
    @FXML
    private TableColumn<ScheduleEntry, String> teacherColumn;
    @FXML
    private TableColumn<ScheduleEntry, String> roomColumn;
    @FXML
    private TableColumn<ScheduleEntry, String> groupColumn;
    @FXML
    private TableColumn<ScheduleEntry, String> classTypeColumn;

    private ObservableList<ScheduleEntry> allEntries = FXCollections.observableArrayList();
    private ObservableList<ScheduleEntry> filteredEntries = FXCollections.observableArrayList();

    @FXML
    private Button openAddFormButton;

    @FXML
    private Button editButton;

    @FXML
    public void initialize() {
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("room"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        classTypeColumn.setCellValueFactory(new PropertyValueFactory<>("classType"));

        scheduleTable.setItems(filteredEntries);

        filterTypeComboBox.setItems(FXCollections.observableArrayList("Profesor", "Grupă", "Sală", "Disciplina"));
        filterTypeComboBox.setOnAction(event -> updateFilterValues());

        filterValueComboBox.setOnAction(event -> applyFilter());

        // Dummy data
        allEntries.addAll(
                new ScheduleEntry("Luni", "10:00 - 12:00", "POO", "Prof. Ionescu", "C101", "1A2", "Curs"),
                new ScheduleEntry("Marți", "12:00 - 14:00", "BD", "Prof. Popescu", "C201", "2B1", "Laborator")
        );

        filteredEntries.setAll(allEntries);
    }

    private void updateFilterValues() {
        String selected = filterTypeComboBox.getValue();
        if (selected == null) return;

        List<String> values = switch (selected) {
            case "Profesor" -> allEntries.stream().map(ScheduleEntry::getTeacher).distinct().toList();
            case "Grupă" -> allEntries.stream().map(ScheduleEntry::getGroup).distinct().toList();
            case "Sală" -> allEntries.stream().map(ScheduleEntry::getRoom).distinct().toList();
            case "Disciplina" -> allEntries.stream().map(ScheduleEntry::getSubject).distinct().toList();
            default -> List.of();
        };

        filterValueComboBox.setItems(FXCollections.observableArrayList(values));
    }

    private void applyFilter() {
        String filterType = filterTypeComboBox.getValue();
        String filterValue = filterValueComboBox.getValue();

        if (filterType == null || filterValue == null) return;

        filteredEntries.setAll(allEntries.stream().filter(entry -> {
            return switch (filterType) {
                case "Profesor" -> entry.getTeacher().equals(filterValue);
                case "Grupă" -> entry.getGroup().equals(filterValue);
                case "Sală" -> entry.getRoom().equals(filterValue);
                case "Disciplina" -> entry.getSubject().equals(filterValue);
                default -> true;
            };
        }).toList());
    }

    @FXML
    private void handleOpenAddForm() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/timetableassistant/view/ClassFormView.fxml"));
        VBox addClassForm = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Adaugă Oră");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(openAddFormButton.getScene().getWindow());

        Scene scene = new Scene(addClassForm);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();

        ClassFormController controller = loader.getController();
        if (controller.isSaveClicked()) {
            ScheduleEntry newEntry = controller.getScheduleEntry();
            allEntries.add(newEntry);
            filteredEntries.setAll(allEntries);
        }
    }

    @FXML
    private void handleEditEntry() throws IOException {
        ScheduleEntry selectedEntry = scheduleTable.getSelectionModel().getSelectedItem();

        if (selectedEntry == null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/timetableassistant/view/ClassFormView.fxml"));
        VBox editClassForm = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Editează Oră");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(editButton.getScene().getWindow());

        Scene scene = new Scene(editClassForm);
        dialogStage.setScene(scene);

        ClassFormController controller = loader.getController();
        controller.setEntryToEdit(selectedEntry);

        dialogStage.showAndWait();

        if (controller.isSaveClicked()) {
            ScheduleEntry updatedEntry = controller.getScheduleEntry();

            int index = allEntries.indexOf(selectedEntry);
            if (index >= 0) {
                allEntries.set(index, updatedEntry);
                filteredEntries.setAll(allEntries);
            }
        }
    }
}
