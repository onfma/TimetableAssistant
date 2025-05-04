package org.example.timetableassistant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import org.example.timetableassistant.model.Group;

public class GroupsViewController {
    @FXML private TableView<Group> groupTable;
    @FXML private TableColumn<Group, String> groupNameColumn;
    @FXML private TableColumn<Group, String> semiyearColumn;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private final ObservableList<Group> groups = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        groupNameColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        semiyearColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty("Sem. " + cell.getValue().getSemiyearId()));

        groups.addAll(
                new Group(1, "1A1", "1A"),
                new Group(2, "2B2", "2B")
        );

        groupTable.setItems(groups);

        addButton.setOnAction(e -> handleAdd());
        editButton.setOnAction(e -> handleEdit());
        deleteButton.setOnAction(e -> handleDelete());
    }

    private void handleAdd() {
        Dialog<Group> dialog = new Dialog<>();
        dialog.setTitle("Adaugă grupă");

        VBox vbox = new VBox(10);

        TextField nameField = new TextField();
        nameField.setPromptText("Introduceți numele grupei");

        ComboBox<String> semiyearComboBox = new ComboBox<>();
        semiyearComboBox.getItems().addAll("1A", "2B", "2A");
        semiyearComboBox.setValue("1A");

        vbox.getChildren().addAll(new Label("Nume grupă:"), nameField, new Label("Semian:"), semiyearComboBox);

        dialog.getDialogPane().setContent(vbox);

        ButtonType okButton = new ButtonType("Adaugă", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Anulează", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButton) {
                String name = nameField.getText();
                String semiyear = semiyearComboBox.getValue();
                Group newGroup = new Group(groups.size() + 1, name, semiyear);
                return newGroup;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(group -> {
            groups.add(group);
        });
    }

    private void handleEdit() {
        Group selected = groupTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Dialog<Group> dialog = new Dialog<>();
        dialog.setTitle("Editează grupă");

        VBox vbox = new VBox(10);

        TextField nameField = new TextField(selected.getName());
        nameField.setPromptText("Modifică numele grupei");

        ComboBox<String> semiyearComboBox = new ComboBox<>();
        semiyearComboBox.getItems().addAll("1A", "2B", "2A");
        semiyearComboBox.setValue(selected.getSemiyearId());

        vbox.getChildren().addAll(new Label("Nume grupă:"), nameField, new Label("Semian:"), semiyearComboBox);

        dialog.getDialogPane().setContent(vbox);

        ButtonType okButton = new ButtonType("Salvează", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Anulează", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButton) {
                String name = nameField.getText();
                String semiyear = semiyearComboBox.getValue();
                selected.setName(name);
                selected.setSemiyearId(semiyear);
                return selected;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(group -> {
            groupTable.refresh();
        });
    }

    private void handleDelete() {
        Group selected = groupTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Ești sigur că vrei să ștergi grupa " + selected.getName() + "?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.YES) {
                groups.remove(selected);
            }
        });
    }

}
