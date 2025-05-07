package org.example.timetableassistant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import org.example.timetableassistant.model.Group;
import org.example.timetableassistant.model.Semiyear;
import org.example.timetableassistant.service.GroupService;

public class GroupsViewController {
    @FXML private TableView<Group> groupTable;
    @FXML private TableColumn<Group, String> groupNumberColumn;
    @FXML private TableColumn<Group, String> semiyearColumn;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private final ObservableList<Group> groups = FXCollections.observableArrayList();
    private final GroupService groupService = new GroupService();

    @FXML
    public void initialize() throws Exception {
        groupNumberColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().getNumber())));
        semiyearColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getSemiyear().getValue()));

        try {
            groups.addAll(GroupService.getAllGroups());
        } catch (Exception e) {
            groups.addAll(FXCollections.observableArrayList());
        }

        groupTable.setItems(groups);

        addButton.setOnAction(e -> {
            try {
                handleAdd();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        editButton.setOnAction(e -> handleEdit());
        deleteButton.setOnAction(e -> handleDelete());
    }

    private void refreshTable() {
        groupTable.refresh();
    }

    private void handleAdd() throws Exception {
        Dialog<Group> dialog = new Dialog<>();
        dialog.setTitle("Adaugă grupă");

        VBox vbox = new VBox(10);

        TextField numberField = new TextField();
        numberField.setPromptText("Introduceți numărul grupei");

        ComboBox<String> semiyearComboBox = new ComboBox<>();
        for (Semiyear s : Semiyear.values()) {
            semiyearComboBox.getItems().add(s.getValue());
        }
        semiyearComboBox.setValue(Semiyear.SEM_1A.getValue());

        vbox.getChildren().addAll(new Label("Număr grupă:"), numberField, new Label("Semian:"), semiyearComboBox);

        dialog.getDialogPane().setContent(vbox);

        ButtonType okButton = new ButtonType("Adaugă", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Anulează", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButton) {
                try{
                    int number = Integer.parseInt(numberField.getText());
                    String semiyearVal = semiyearComboBox.getValue();
                    Semiyear semiyear = Semiyear.fromString(semiyearVal);
                    GroupService.createGroup(number, semiyear);
                    return GroupService.getAllGroups().getLast();
                } catch (NumberFormatException e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Numărul grupei trebuie să fie un număr întreg.");
                    errorAlert.showAndWait();
                } catch (Exception ex) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Eroare la adăugarea grupei: " + ex.getMessage());
                    errorAlert.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(group -> {
            if (group != null) {
                groups.add(group);
            }
        });

        refreshTable();
    }

    private void handleEdit() {
        Group selected = groupTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Dialog<Group> dialog = new Dialog<>();
        dialog.setTitle("Editează grupă");

        VBox vbox = new VBox(10);

        TextField numberField  = new TextField(String.valueOf(selected.getNumber()));
        numberField .setPromptText("Modifică numărul grupei");

        ComboBox<String> semiyearComboBox = new ComboBox<>();
        for (Semiyear s : Semiyear.values()) {
            semiyearComboBox.getItems().add(s.getValue());
        }
        semiyearComboBox.setValue(selected.getSemiyear().getValue());

        vbox.getChildren().addAll(new Label("Număr grupă:"), numberField, new Label("Semian:"), semiyearComboBox);

        dialog.getDialogPane().setContent(vbox);

        ButtonType okButton = new ButtonType("Salvează", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Anulează", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButton) {
                int number = Integer.parseInt(numberField.getText());
                String semiyearVal = semiyearComboBox.getValue();
                try{
                    Semiyear semiyear = Semiyear.fromString(semiyearVal);
                    groupService.updateGroup(selected.getId(), number, semiyear);
                    selected.setNumber(number);
                    selected.setSemiyear(semiyear);
                    groupTable.refresh();
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Eroare la modificarea grupei: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(group -> {
            groupTable.refresh();
        });

        refreshTable();
    }

    private void handleDelete() {
        Group selected = groupTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Ești sigur că vrei să ștergi grupa " + selected.getSemiyear() + selected.getNumber() + "?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.YES) {
                try{
                    GroupService.deleteGroup(selected.getId());
                    groups.remove(selected);
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Eroare la ștergerea grupei: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        });

        refreshTable();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
