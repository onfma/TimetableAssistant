package org.example.timetableassistant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import org.example.timetableassistant.model.Discipline;

public class DisciplinesViewController {

    @FXML private TableView<Discipline> disciplineTable;
    @FXML private TableColumn<Discipline, String> disciplineNameColumn;
    @FXML private TableColumn<Discipline, String> disciplineTypeColumn;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private final ObservableList<Discipline> disciplines = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        disciplineNameColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        disciplineTypeColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getType()));

        disciplines.addAll(
                new Discipline(1, "Matematică", "seminar"),
                new Discipline(2, "Chimie", "laborator")
        );

        disciplineTable.setItems(disciplines);

        addButton.setOnAction(e -> handleAdd());
        editButton.setOnAction(e -> handleEdit());
        deleteButton.setOnAction(e -> handleDelete());
    }

    private void handleAdd() {
        Dialog<Discipline> dialog = new Dialog<>();
        dialog.setTitle("Adaugă disciplină");

        VBox vbox = new VBox(10);

        TextField nameField = new TextField();
        nameField.setPromptText("Introduceți numele disciplinei");

        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("seminar", "laborator");
        typeComboBox.setPromptText("Selectați tipul");

        vbox.getChildren().addAll(new Label("Nume disciplină:"), nameField, new Label("Tip disciplină:"), typeComboBox);

        dialog.getDialogPane().setContent(vbox);

        ButtonType okButton = new ButtonType("Adaugă", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Anulează", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButton) {
                String name = nameField.getText();
                String type = typeComboBox.getValue();
                Discipline newDiscipline = new Discipline(disciplines.size() + 1, name, type);
                return newDiscipline;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(discipline -> {
            disciplines.add(discipline);
        });
    }

    private void handleEdit() {
        Discipline selected = disciplineTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Dialog<Discipline> dialog = new Dialog<>();
        dialog.setTitle("Editează disciplină");

        VBox vbox = new VBox(10);

        TextField nameField = new TextField(selected.getName());
        nameField.setPromptText("Modificați numele disciplinei");

        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("seminar", "laborator");
        typeComboBox.setValue(selected.getType());
        typeComboBox.setPromptText("Modificați tipul");

        vbox.getChildren().addAll(new Label("Nume disciplină:"), nameField, new Label("Tip disciplină:"), typeComboBox);

        dialog.getDialogPane().setContent(vbox);

        ButtonType okButton = new ButtonType("Salvează", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Anulează", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButton) {
                String name = nameField.getText();
                String type = typeComboBox.getValue();
                selected.setName(name);
                selected.setType(type);
                return selected;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(discipline -> {
            disciplineTable.refresh();
        });
    }

    private void handleDelete() {
        Discipline selected = disciplineTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Ești sigur că vrei să ștergi disciplina " + selected.getName() + "?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.YES) {
                disciplines.remove(selected);
            }
        });
    }
}
