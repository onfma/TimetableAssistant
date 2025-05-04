module org.example.timetableassistant {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens org.example.timetableassistant.controller to javafx.fxml;
    opens org.example.timetableassistant.model to javafx.base;
    exports org.example.timetableassistant;
}
