package org.example.timetableassistant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import org.example.timetableassistant.model.Room;

public class RoomsViewController {

    @FXML private TableView<Room> roomsTable;
    @FXML private TableColumn<Room, String> roomNameColumn;
    @FXML private TableColumn<Room, String> roomTypeColumn;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private final ObservableList<Room> rooms = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        roomNameColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        roomTypeColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getType()));

        //Example
        rooms.addAll(
                new Room(1, "C101", "curs"),
                new Room(2, "C102", "laborator")
        );

        roomsTable.setItems(rooms);

        addButton.setOnAction(e -> handleAdd());
        editButton.setOnAction(e -> handleEdit());
        deleteButton.setOnAction(e -> handleDelete());
    }

    private void handleAdd() {
        Dialog<Room> dialog = new Dialog<>();
        dialog.setTitle("Add room");

        VBox vbox = new VBox(10);

        TextField nameField = new TextField();
        nameField.setPromptText("Sala");

        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("curs", "laborator");
        typeComboBox.setPromptText("Selectați tipul");

        vbox.getChildren().addAll(new Label("Nume sală:"), nameField, new Label("Tip sală:"), typeComboBox);

        dialog.getDialogPane().setContent(vbox);

        ButtonType okButton = new ButtonType("Adaugă", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Anulează", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButton) {
                String name = nameField.getText();
                String type = typeComboBox.getValue();
                Room newRoom = new Room(rooms.size() + 1, name, type);
                return newRoom;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(Room -> {
            rooms.add(Room);
        });
    }

    private void handleEdit() {
        Room selected = roomsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Dialog<Room> dialog = new Dialog<>();
        dialog.setTitle("Editează sală");

        VBox vbox = new VBox(10);

        TextField nameField = new TextField(selected.getName());
        nameField.setPromptText("Modificați numele sălii");

        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("curs", "laborator");
        typeComboBox.setValue(selected.getType());
        typeComboBox.setPromptText("Modificați tipul");

        vbox.getChildren().addAll(new Label("Nume sală:"), nameField, new Label("Tip sală:"), typeComboBox);

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

        dialog.showAndWait().ifPresent(Room -> {
            roomsTable.refresh();
        });
    }

    private void handleDelete() {
        Room selected = roomsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Ești sigur că vrei să ștergi sala " + selected.getName() + "?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.YES) {
                rooms.remove(selected);
            }
        });
    }
}
