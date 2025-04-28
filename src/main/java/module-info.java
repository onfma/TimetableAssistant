module org.example.timetableassistant {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.timetableassistant to javafx.fxml;
    exports org.example.timetableassistant;
}