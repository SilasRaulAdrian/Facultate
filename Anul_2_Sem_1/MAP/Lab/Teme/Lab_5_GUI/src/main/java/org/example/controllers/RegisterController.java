package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.hash.PasswordHasher;
import org.example.model.*;
import org.example.service.UserService;

import java.time.LocalDate;

/**
 * Controller pentru fereastra de inregistrare a unui utilizator.
 */
public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML private ComboBox<String> typeCombo;

    @FXML private VBox personBox;
    @FXML private TextField numeField;
    @FXML private TextField prenumeField;
    @FXML private DatePicker dataNField;
    @FXML private TextField ocupatieField;
    @FXML private TextField empatieField;

    @FXML private VBox duckBox;
    @FXML private ComboBox<String> duckTypeCombo;
    @FXML private TextField vitezaField;
    @FXML private TextField rezistentaField;

    @FXML private Label errorLabel;

    private UserService service;
    private Stage stage;

    /**
     * Seteaza service-urile necesare si etapa curenta.
     *
     * @param s service-ul utilizator
     * @param st etapa curenta
     */
    public void setService(UserService s, Stage st) {
        this.service = s;
        this.stage = st;

        typeCombo.getItems().addAll("Person", "Duck");
        duckTypeCombo.getItems().addAll("FLYING", "SWIMMING", "FLYING_AND_SWIMMING");

        typeCombo.valueProperty().addListener((obs, old, val) -> updateBoxes());
    }

    /**
     * Actualizeaza vizibilitatea casetelor in functie de tipul selectat.
     */
    private void updateBoxes() {
        boolean isPerson = "Person".equals(typeCombo.getValue());
        boolean isDuck   = "Duck".equals(typeCombo.getValue());

        personBox.setVisible(isPerson);
        personBox.setManaged(isPerson);

        duckBox.setVisible(isDuck);
        duckBox.setManaged(isDuck);
    }

    /**
     * Gestioneaza evenimentul de inregistrare a unui utilizator.
     */
    @FXML
    private void handleRegister() {
        try {
            String u = usernameField.getText();
            String email = emailField.getText();
            String p = passwordField.getText();
            String p2 = confirmPasswordField.getText();

            if (!p.equals(p2)) {
                errorLabel.setText("Passwords do not match!");
                return;
            }

            if (typeCombo.getValue() == null) {
                errorLabel.setText("Select user type!");
                return;
            }

            if (typeCombo.getValue().equals("Person")) {
                String nume = numeField.getText();
                String prenume = prenumeField.getText();
                String ocupatie = ocupatieField.getText();
                double empatie = Double.parseDouble(empatieField.getText());
                LocalDate dataN = dataNField.getValue();
                service.addPerson(u, email, PasswordHasher.hashPassword(p), nume, prenume, dataN, ocupatie, empatie);
                goBack();
            } else {
                double v = Double.parseDouble(vitezaField.getText());
                double r = Double.parseDouble(rezistentaField.getText());

                String dt = duckTypeCombo.getValue();
                TipRata tip = TipRata.valueOf(dt);
                service.addDuck(u, email, PasswordHasher.hashPassword(p), tip, v, r);
                goBack();
            }

        } catch (Exception ex) {
            errorLabel.setText("Error: " + ex.getMessage());
        }
    }

    /**
     * Inchide fereastra curenta si revine la fereastra de autentificare.
     */
    @FXML
    private void goBack() {
        try {
            stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login-window.fxml"));
            Stage loginStage = new Stage();

            double width = 400;
            double height = 400;

            loginStage.setScene(new Scene(loader.load(), width, height));

            LoginController ctrl = loader.getController();
            ctrl.setService(service);

            loginStage.setTitle("Login");
            loginStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
