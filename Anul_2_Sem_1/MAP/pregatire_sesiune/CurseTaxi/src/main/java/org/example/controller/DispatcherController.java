package org.example.controller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.service.TaxiService;

public class DispatcherController {
    private final TaxiService service;
    private final Stage stage = new Stage();

    public DispatcherController(TaxiService service) {
        this.service = service;

        stage.setTitle("Dispatcher");

        Label err = new Label();
        err.setStyle("-fx-text-fill: red;");

        TextField tfPickup = new TextField();
        TextField tfDest = new TextField();
        TextField tfClient = new TextField();

        Button add = new Button("Add order");

        add.setOnAction(e -> {
            err.setText("");

            String p = tfPickup.getText().trim();
            String d = tfDest.getText().trim();
            String c = tfClient.getText().trim();

            if (p.isEmpty() || d.isEmpty() || c.isEmpty()) {
                err.setText("Completeaza toate campurile!");
                return;
            }

            service.addOrder(p, d, c);

            tfPickup.clear();
            tfDest.clear();
            tfClient.clear();
        });

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        form.add(new Label("Pickup address:"), 0, 0);
        form.add(tfPickup, 1, 0);

        form.add(new Label("Destination address:"), 0, 1);
        form.add(tfDest, 1, 1);

        form.add(new Label("Client name:"), 0, 2);
        form.add(tfClient, 1, 2);

        GridPane.setHgrow(tfPickup, Priority.ALWAYS);
        GridPane.setHgrow(tfDest, Priority.ALWAYS);
        GridPane.setHgrow(tfClient, Priority.ALWAYS);

        VBox root = new VBox(12,
                new Label("Add new order"),
                form,
                add,
                err
        );

        root.setPadding(new Insets(10));
        stage.setScene(new Scene(root, 450, 220));
    }

    public void show() {
        stage.show();
    }
}
