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
import org.example.domain.Client;
import org.example.service.FlightService;

public class LoginController {
    private final FlightService service;
    private final Stage stage = new Stage();

    public LoginController(FlightService service) {
        this.service = service;

        stage.setTitle("Login");

        TextField tfUser = new TextField();
        Label err = new Label();
        err.setStyle("-fx-text-fill:red;");

        Button btn = new Button("Login");
        btn.setOnAction(e -> {
            err.setText("");
            try {
                Client c = service.login(tfUser.getText());
                new FlightsController(service, c).show();
            } catch (Exception ex) {
                err.setText(ex.getMessage());
            }
        });

        GridPane gp = new GridPane();
        gp.setHgap(10); gp.setVgap(10);
        gp.add(new Label("Username:"), 0, 0);
        gp.add(tfUser, 1, 0);
        GridPane.setHgrow(tfUser, Priority.ALWAYS);

        VBox root = new VBox(10, gp, btn, err);
        root.setPadding(new Insets(10));

        stage.setScene(new Scene(root, 420, 160));
    }

    public void show() {
        stage.show();
    }
}
