package org.example.controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.domain.User;
import org.example.domain.UserRole;
import org.example.service.CarsService;

public class LoginController {
    private final CarsService service;
    private Stage stage = new Stage();

    public LoginController(CarsService service) {
        this.service = service;
        stage.setTitle("Login");

        TextField tfUser = new TextField();
        PasswordField tfPass = new PasswordField();
        Button btnLogin = new Button("Login");
        Label err = new Label();

        btnLogin.setOnAction(e -> {
            err.setText("");
            String user = tfUser.getText();
            String pass = tfPass.getText();
            if (user.isEmpty() || pass.isEmpty()) {
                err.setText("Completeaza username/parola.");
                return;
            }
            try {
                User u = service.login(user, pass);
                if (u.getRole() == UserRole.ADMIN) {
                    new AdminController(service, u).show();
                } else {
                    new DealerController(service, u).show();
                }
            } catch (Exception ex) {
                err.setText(ex.getMessage());
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Username:"), tfUser);
        grid.addRow(1, new Label("Parola:"), tfPass);
        grid.addRow(2, btnLogin);
        grid.addRow(3, err);
        stage.setScene(new javafx.scene.Scene(grid, 300, 200));
    }

    public void show() {
        stage.show();
    }
}
