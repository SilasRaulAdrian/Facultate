module org.example {
    requires java.sql;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.media;
    requires static lombok;
    requires org.postgresql.jdbc;
    requires javafx.base;
//    requires org.example;
//    requires gradle.api;

    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.controllers;
    opens org.example.controllers to javafx.fxml;
}