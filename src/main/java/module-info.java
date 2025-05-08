module org.example.timetableassistant {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.google.gson;
    requires spark.core;
    requires com.fasterxml.jackson.databind;
    requires org.json;

    opens org.example.timetableassistant to javafx.fxml;
    opens org.example.timetableassistant.controller to javafx.fxml;
    opens org.example.timetableassistant.model to javafx.base;
    exports org.example.timetableassistant;
}