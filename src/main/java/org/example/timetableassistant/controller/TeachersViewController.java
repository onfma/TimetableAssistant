package org.example.timetableassistant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import org.example.timetableassistant.model.Teacher;
import org.example.timetableassistant.service.TeacherService;

import java.util.Arrays;
import java.util.List;

public class TeachersViewController {

    @FXML private TableView<Teacher> teacherTable;
    @FXML private TableColumn<Teacher, String> teacherNameColumn;
    @FXML private TableColumn<Teacher, String> disciplinesColumn;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private final ObservableList<Teacher> teachers = FXCollections.observableArrayList();
    private final TeacherService teacherService = new TeacherService();

    @FXML
    public void initialize() throws Exception {
        teacherNameColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        disciplinesColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.join(", ", cell.getValue().getDisciplines())));

        teachers.addAll(
                teacherService.getAllTeachers()
        );

        teacherTable.setItems(teachers);

        addButton.setOnAction(e -> handleAdd());
        editButton.setOnAction(e -> handleEdit());
        deleteButton.setOnAction(e -> handleDelete());
    }

    private void handleAdd() {
        Dialog<Teacher> dialog = new Dialog<>();
        dialog.setTitle("Adaugă profesor");

        VBox vbox = new VBox(10);

        TextField nameField = new TextField();
        nameField.setPromptText("Introduceți numele profesorului");

        TextField disciplinesField = new TextField();
        disciplinesField.setPromptText("Introduceți disciplinele separate prin virgulă");

        vbox.getChildren().addAll(new Label("Nume profesor:"), nameField, new Label("Discipline:"), disciplinesField);

        dialog.getDialogPane().setContent(vbox);

        ButtonType okButton = new ButtonType("Adaugă", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Anulează", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButton) {
                String name = nameField.getText();
                String disciplinesText = disciplinesField.getText();
                List<String> disciplines = Arrays.asList(disciplinesText.split(","));

                try {
                    TeacherService.createTeacher(name);
                    return new Teacher(teachers.size() + 1, name);
                } catch (Exception ex) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Eroare la adăugarea profesorului: " + ex.getMessage());
                    errorAlert.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(teacher -> {
            teachers.add(teacher);
        });
    }

    private void handleEdit() {
        Teacher selected = teacherTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Dialog<Teacher> dialog = new Dialog<>();
        dialog.setTitle("Editează profesor");

        VBox vbox = new VBox(10);

        TextField nameField = new TextField(selected.getName());
        nameField.setPromptText("Modificați numele profesorului");

        TextField disciplinesField = new TextField(String.join(", ", selected.getDisciplines()));
        disciplinesField.setPromptText("Modificați disciplinele separate prin virgulă");

        vbox.getChildren().addAll(new Label("Nume profesor:"), nameField, new Label("Discipline:"), disciplinesField);

        dialog.getDialogPane().setContent(vbox);

        ButtonType okButton = new ButtonType("Salvează", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Anulează", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButton) {
                String name = nameField.getText();
                String disciplinesText = disciplinesField.getText();
                List<String> disciplines = Arrays.asList(disciplinesText.split(","));

                try {
                    teacherService.editTeacher(selected.getId(), name);
                    selected.setName(name);
//                    selected.addDisciplines(disciplines);
                    return selected;
                } catch (Exception ex) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Eroare la editarea profesorului: " + ex.getMessage());
                    errorAlert.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(teacher -> {
            teacherTable.refresh();
        });
    }

    private void handleDelete() {
        Teacher selected = teacherTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Ești sigur că vrei să ștergi profesorul " + selected.getName() + "?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.YES) {
                teachers.remove(selected);
                try {
                    teacherService.deleteTeacher(selected.getId());
                } catch (Exception ex) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Eroare la ștergerea profesorului: " + ex.getMessage());
                    errorAlert.showAndWait();
                }
            }
        });
    }
}
