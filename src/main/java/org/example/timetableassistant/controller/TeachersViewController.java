package org.example.timetableassistant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import org.example.timetableassistant.model.Teacher;

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

    @FXML
    public void initialize() {
        teacherNameColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        disciplinesColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.join(", ", cell.getValue().getDisciplines())));

        teachers.addAll(
                new Teacher(1, "Prof. Popescu", Arrays.asList("Matematică", "Informatică")),
                new Teacher(2, "Prof. Ionescu", Arrays.asList("Fizică", "Chimie"))
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
                Teacher newTeacher = new Teacher(teachers.size() + 1, name, disciplines);
                return newTeacher;
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
                selected.setName(name);
                selected.setDisciplines(disciplines);
                return selected;
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
            }
        });
    }
}
