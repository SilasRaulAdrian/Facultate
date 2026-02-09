package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.model.TipRata;
import org.example.service.UserService;

/**
 * Clasa controller pentru fereastra de adaugare a unei rate.
 */
public class AddDuckController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<TipRata> typeCombo;
    @FXML private TextField speedField;
    @FXML private TextField staminaField;

    private UserService service;
    private Stage stage;

    /**
     * Seteaza serviciile necesare si etapa curenta.
     *
     * @param s serviciul utilizator
     * @param st etapa curenta
     */
    public void setServices(UserService s, Stage st) {
        this.service = s;
        this.stage = st;
        typeCombo.getItems().addAll(TipRata.values());
    }

    /**
     * Gestioneaza evenimentul de adaugare a unei rate.
     */
    @FXML
    private void handleAdd() {
        try {
            service.addDuck(
                    usernameField.getText(),
                    emailField.getText(),
                    passwordField.getText(),
                    typeCombo.getValue(),
                    Double.parseDouble(speedField.getText()),
                    Double.parseDouble(staminaField.getText())
            );
            stage.close();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    /**
     * Inchide fereastra curenta.
     */
    @FXML
    private void goBack() {
        stage.close();
    }
}
