package org.example.timetableassistant.controller;

import javafx.beans.property.SimpleStringProperty;
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
import org.example.timetableassistant.model.*;
import org.example.timetableassistant.model.Class;
import org.example.timetableassistant.service.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleViewController {

    @FXML private ComboBox<String> filterTypeComboBox;
    @FXML private ComboBox<String> filterValueComboBox;
    @FXML private TableView<Class> classTable;

    @FXML private TableColumn<Class, String> dayColumn;
    @FXML private TableColumn<Class, String> timeColumn;
    @FXML private TableColumn<Class, String> subjectColumn;
    @FXML private TableColumn<Class, String> teacherColumn;
    @FXML private TableColumn<Class, String> roomColumn;
    @FXML private TableColumn<Class, String> groupColumn;
    @FXML private TableColumn<Class, String> classTypeColumn;
    @FXML private TableColumn<Class, String> semiyearColumn;

    @FXML private Button openAddFormButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private ObservableList<Class> classes = FXCollections.observableArrayList();
    private ObservableList<Class> filteredEntries = FXCollections.observableArrayList();

    private List<Teacher> allTeachers;
    private List<Group> allGroups;
    private List<Room> allRooms;
    private List<Discipline> allDisciplines;
    private List<TimeSlot> allTimeSlots;

    @FXML
    public void initialize() {
        try {
            allGroups = GroupService.getAllGroups();
            allRooms = RoomService.getAllRooms();
            allTeachers = TeacherService.getAllTeachers();
            allDisciplines = DisciplineService.getAllDisciplines();
            allTimeSlots = TimeSlotService.getAllTimeSlots();
            classes.addAll(ClassService.getAllClasses());
        } catch (Exception e) {
            e.printStackTrace();
        }

        dayColumn.setCellValueFactory(cellData -> {
            TimeSlot ts = getTimeSlotById(cellData.getValue().getTimeSlotId());
            return new SimpleStringProperty(ts != null ? ts.getDayOfWeek() : "");
        });

        timeColumn.setCellValueFactory(cellData -> {
            TimeSlot ts = getTimeSlotById(cellData.getValue().getTimeSlotId());
            return new SimpleStringProperty(ts != null ? ts.getStartTime() + " - " + ts.getEndTime() : "");
        });

        subjectColumn.setCellValueFactory(cellData -> {
            Discipline d = getDisciplineById(cellData.getValue().getDisciplineId());
            return new SimpleStringProperty(d != null ? d.getName() : "");
        });

        teacherColumn.setCellValueFactory(cellData -> {
            Teacher t = getTeacherById(cellData.getValue().getTeacherId());
            return new SimpleStringProperty(t != null ? t.getName() : "");
        });

        roomColumn.setCellValueFactory(cellData -> {
            Room r = getRoomById(cellData.getValue().getRoomId());
            return new SimpleStringProperty(r != null ? r.getName() : "");
        });

        groupColumn.setCellValueFactory(cellData -> {
            Group g = getGroupById(cellData.getValue().getGroupId());
            return new SimpleStringProperty(g != null ? g.toString() : "");
        });

        classTypeColumn.setCellValueFactory(new PropertyValueFactory<>("classType"));
        semiyearColumn.setCellValueFactory(new PropertyValueFactory<>("semiyear"));

        classTable.setItems(classes);
        filterTypeComboBox.setItems(FXCollections.observableArrayList("Profesor", "Grupă", "Sală", "Disciplina"));
        filterTypeComboBox.setOnAction(event -> updateFilterValues());

        filterValueComboBox.setOnAction(event -> applyFilter());
        updateFilterValues();
    }

    private void updateFilterValues() {
        String selected = filterTypeComboBox.getValue();
        if (selected == null) return;

        List<String> values = switch (selected) {
            case "Profesor" -> allTeachers.stream().map(Teacher::getName).distinct().toList();
            case "Grupă" -> allGroups.stream().map(Group::toString).distinct().toList();
            case "Sală" -> allRooms.stream().map(Room::getName).distinct().toList();
            case "Disciplina" -> allDisciplines.stream().map(Discipline::getName).distinct().toList();
            default -> List.of();
        };

        filterValueComboBox.setItems(FXCollections.observableArrayList(values));
    }

    private void applyFilter() {
        String filterType = filterTypeComboBox.getValue();
        String filterValue = filterValueComboBox.getValue();

        if (filterType == null || filterValue == null) return;

        filteredEntries.setAll(classes.stream().filter(entry -> {
            return switch (filterType) {
                case "Profesor" -> {
                    Teacher t = getTeacherById(entry.getTeacherId());
                    yield t != null && t.getName().equals(filterValue);
                }
                case "Grupă" -> {
                    Group g = getGroupById(entry.getGroupId());
                    yield g != null && g.toString().equals(filterValue);
                }
                case "Sală" -> {
                    Room r = getRoomById(entry.getRoomId());
                    yield r != null && r.getName().equals(filterValue);
                }
                case "Disciplina" -> {
                    Discipline d = getDisciplineById(entry.getDisciplineId());
                    yield d != null && d.getName().equals(filterValue);
                }
                default -> true;
            };
        }).collect(Collectors.toList()));

        classTable.setItems(filteredEntries);
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
            Class newClass = controller.getClassEntry();
            classes.add(newClass);
            updateFilterValues();
        }
    }

    @FXML
    private void handleEditEntry() throws IOException {
        Class selectedClass = classTable.getSelectionModel().getSelectedItem();

        if (selectedClass == null) {
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
        controller.setEntryToEdit(selectedClass);

        dialogStage.showAndWait();

        if (controller.isSaveClicked()) {
            Class updatedClass = controller.getClassEntry();
            int index = classes.indexOf(selectedClass);
            if (index >= 0) {
                classes.set(index, updatedClass);
                updateFilterValues();
            }
        }
    }

    @FXML
    private void handleDeleteEntry() {
        Class selectedClass = classTable.getSelectionModel().getSelectedItem();

        if (selectedClass != null) {
            classes.remove(selectedClass);
            updateFilterValues();
        }
    }

    private Teacher getTeacherById(int id) {
        return allTeachers.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private Group getGroupById(int id) {
        return allGroups.stream()
                .filter(g -> g.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private Room getRoomById(int id) {
        return allRooms.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private Discipline getDisciplineById(int id) {
        return allDisciplines.stream()
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private TimeSlot getTimeSlotById(int id) {
        return allTimeSlots.stream()
                .filter(ts -> ts.getId() == id)
                .findFirst()
                .orElse(null);
    }

}
