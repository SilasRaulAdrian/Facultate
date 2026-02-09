package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.service.UserService;

/**
 * Clasa controller pentru fereastra de adaugare a unei persoane.
 */
public class AddPersonController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField numeField;
    @FXML private TextField prenumeField;
    @FXML private TextField ocupatieField;
    @FXML private TextField empatieField;
    @FXML private DatePicker dataPicker;

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
    }

    /**
     * Gestioneaza evenimentul de adaugare a unei persoane.
     */
    @FXML
    private void handleAdd() {
        try {
            service.addPerson(
                    usernameField.getText(),
                    emailField.getText(),
                    passwordField.getText(),
                    numeField.getText(),
                    prenumeField.getText(),
                    dataPicker.getValue(),
                    ocupatieField.getText(),
                    Integer.parseInt(empatieField.getText())
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
