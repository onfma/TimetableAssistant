module org.example.timetableassistant {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.timetableassistant.constroller to javafx.fxml;
    exports org.example.timetableassistant;
}
