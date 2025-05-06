package org.example.timetableassistant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import org.example.timetableassistant.database.crud.ClassType;
import org.example.timetableassistant.model.*;
import org.example.timetableassistant.service.DisciplineAllocationService;
import org.example.timetableassistant.service.DisciplineService;
import org.example.timetableassistant.service.TeacherService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TeachersViewController {

    @FXML private TableView<Teacher> teacherTable;
    @FXML private TableColumn<Teacher, String> teacherNameColumn;
    @FXML private TableColumn<Teacher, String> disciplinesColumn;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @FXML private Button addDisciplineToTeacherButton;
    @FXML private Button removeDisciplineToTeacherButton;

    private final ObservableList<Teacher> teachers = FXCollections.observableArrayList();
    private final TeacherService teacherService = new TeacherService();

    @FXML
    public void initialize() throws Exception {
        teacherNameColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        disciplinesColumn.setCellValueFactory(cell -> {
            try {
                return new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getDisciplines().entrySet().stream()
                                .map(entry -> entry.getKey() + " " + entry.getValue())
                                .collect(Collectors.joining(", "))
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        teachers.addAll(
                teacherService.getAllTeachers()
        );

        teacherTable.setItems(teachers);

        addButton.setOnAction(e -> handleAdd());
        editButton.setOnAction(e -> handleEdit());
        deleteButton.setOnAction(e -> handleDelete());

        addDisciplineToTeacherButton.setOnAction(e -> {
            try {
                handleAddDiscipline();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        removeDisciplineToTeacherButton.setOnAction(e -> {
            try {
                handleRemoveDiscipline();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
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

//        TextField disciplinesField = new TextField(String.join(", ", selected.getDisciplines()));
//        disciplinesField.setPromptText("Modificați disciplinele separate prin virgulă");

        vbox.getChildren().addAll(new Label("Nume profesor:"), nameField);

        dialog.getDialogPane().setContent(vbox);

        ButtonType okButton = new ButtonType("Salvează", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Anulează", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButton) {
                String name = nameField.getText();
                try {
                    teacherService.editTeacher(selected.getId(), name);
                    selected.setName(name);
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

    private void handleAddDiscipline() throws Exception {
        Teacher selected = teacherTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Dialog<Teacher> dialog = new Dialog<>();
        dialog.setTitle("Asociaza o materie profesorului");

        VBox vbox = new VBox(10);

        ComboBox<Discipline> disciplineBox = new ComboBox<>();
        List<Discipline> disciplines = DisciplineService.getAllDisciplines();
        disciplineBox.getItems().addAll(disciplines);
        disciplineBox.setPromptText("Selectați materia");

        disciplineBox.setCellFactory(cb -> new ListCell<Discipline>() {
            @Override
            protected void updateItem(Discipline item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        disciplineBox.setButtonCell(new ListCell<Discipline>() {
            @Override
            protected void updateItem(Discipline item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        ComboBox<ClassType> classTypeBox = new ComboBox<>();
        List<ClassType> classTypes = Arrays.asList(ClassType.values());
        classTypeBox.getItems().addAll(classTypes);
        classTypeBox.setPromptText("Selectați tipul de clasă");

        classTypeBox.setCellFactory(cb -> new ListCell<ClassType>() {
            @Override
            protected void updateItem(ClassType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.name());
            }
        });
        classTypeBox.setButtonCell(new ListCell<ClassType>() {
            @Override
            protected void updateItem(ClassType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.name());
            }
        });

        vbox.getChildren().addAll(
                new Label("Materie:"), disciplineBox,
                new Label("Tip clasa:"), classTypeBox
        );

        dialog.getDialogPane().setContent(vbox);

        ButtonType okButton = new ButtonType("Adaugă", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Anulează", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButton) {
                Discipline discipline = disciplineBox.getValue();
                ClassType classType = classTypeBox.getValue();
                try {
                    DisciplineAllocationService.createDisciplineAllocation(discipline.getId(), selected.getId(), classType.getValue(), 1);

//                    selected.addDiscipline(discipline, classType);
                } catch (Exception ex) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Eroare la asocierea materiei cu profesorul: " + ex.getMessage());
                    errorAlert.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(Room -> {
            teacherTable.refresh();
        });
    }

    private void handleRemoveDiscipline() throws Exception {
        Teacher selected = teacherTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Dialog<Teacher> dialog = new Dialog<>();
        dialog.setTitle("Sterge o asociere de materie a profesorului");

        VBox vbox = new VBox(10);

        ComboBox<DisciplineAllocation> allocationsBox = new ComboBox<>();
        List<DisciplineAllocation> allocs = DisciplineAllocationService.getByTeacherId(selected.getId());
        allocationsBox.getItems().addAll(allocs);
        allocationsBox.setPromptText("Selectați asocierea");

        allocationsBox.setCellFactory(cb -> new ListCell<DisciplineAllocation>() {
            @Override
            protected void updateItem(DisciplineAllocation item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDiscipline().getName() + " - " + item.getClassType().name());
            }
        });
        allocationsBox.setButtonCell(new ListCell<DisciplineAllocation>() {
            @Override
            protected void updateItem(DisciplineAllocation item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDiscipline().getName() + " - " + item.getClassType().name());
            }
        });

        vbox.getChildren().addAll(
                new Label("Asociere:"), allocationsBox
        );

        dialog.getDialogPane().setContent(vbox);

        ButtonType okButton = new ButtonType("Sterge", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Anulează", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButton) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Ești sigur că vrei să ștergi asocierea?",
                        ButtonType.YES, ButtonType.NO);
                alert.showAndWait().ifPresent(type -> {
                    if (type == ButtonType.YES) {
                        try {
                            DisciplineAllocationService.deleteAllocation(allocationsBox.getValue().getId());
                        } catch (Exception ex) {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Eroare la ștergerea asocierii: " + ex.getMessage());
                            errorAlert.showAndWait();
                        }
                    }
                });
            }
            return null;
        });

        dialog.showAndWait().ifPresent(Room -> {
            teacherTable.refresh();
        });
    }
}
