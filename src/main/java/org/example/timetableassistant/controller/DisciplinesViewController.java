package org.example.timetableassistant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import org.example.timetableassistant.model.Discipline;
import org.example.timetableassistant.service.DisciplineService;

public class DisciplinesViewController {

    @FXML private TableView<Discipline> disciplineTable;
    @FXML private TableColumn<Discipline, String> disciplineNameColumn;
    @FXML private TableColumn<Discipline, String> disciplineTypeColumn;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private final ObservableList<Discipline> disciplines = FXCollections.observableArrayList();
    private final DisciplineService disciplineService = new DisciplineService();

    @FXML
    public void initialize() throws Exception {
        disciplineNameColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));

        disciplines.addAll(
                disciplineService.getAllDisciplines()
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

        vbox.getChildren().addAll(new Label("Nume disciplină:"), nameField);

        dialog.getDialogPane().setContent(vbox);

        ButtonType okButton = new ButtonType("Adaugă", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Anulează", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButton) {
                String name = nameField.getText();
                try {
                    DisciplineService.createDiscipline(name);
                    return DisciplineService.getAllDisciplines().getLast();
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Eroare la crearea disciplinei: " + ex.getMessage());
                    alert.showAndWait();
                }
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

        vbox.getChildren().addAll(new Label("Nume disciplină:"), nameField);

        dialog.getDialogPane().setContent(vbox);

        ButtonType okButton = new ButtonType("Salvează", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Anulează", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButton) {
                String name = nameField.getText();
                try {
                    DisciplineService.editDiscipline(selected.getId(), name);
                    selected.setName(name);
                    return selected;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
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
                try {
                    disciplines.remove(selected);
                    System.out.println("################################################");
                    System.out.println("################################################");
                    System.out.println(selected.getId());
                    System.out.println("################################################");
                    System.out.println("################################################");
                    DisciplineService.deleteDiscipline(selected.getId());
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Eroare la ștergerea disciplinei: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        });
    }
}
