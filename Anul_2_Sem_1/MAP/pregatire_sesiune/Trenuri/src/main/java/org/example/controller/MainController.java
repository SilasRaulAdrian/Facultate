package org.example.controller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.service.TrainService;

public class MainController {
    private final TrainService service;
    private final Stage stage = new Stage();

    public MainController(TrainService service) {
        this.service = service;

        stage.setTitle("Train Routes");

        Button open = new Button("Open New Client Window");
        open.setOnAction(e -> new ClientController(service).show());

        VBox root = new VBox(10, open);
        root.setPadding(new Insets(10));
        stage.setScene(new Scene(root, 300, 120));
    }

    public void show() {
        stage.show();
    }
}
