package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.hash.PasswordHasher;
import org.example.model.User;
import org.example.service.UserService;

/**
 * Controller pentru fereastra de resetare a parolei.
 */
public class ForgotPasswordController {
    @FXML private TextField usernameField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label infoLabel;

    private UserService userService;

    /**
     * Seteaza service-ul utilizator.
     *
     * @param userService service-ul utilizator
     */
    public void setService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Gestioneaza evenimentul de resetare a parolei.
     */
    @FXML
    private void handleResetPassword() {
        String username = usernameField.getText();
        String newPass = newPasswordField.getText();
        String confirmPass = confirmPasswordField.getText();

        if (username.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            infoLabel.setText("All fields are required!");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            infoLabel.setText("Passwords do not match!");
            return;
        }

        try {
            User user = userService.findByUsername(username);

            String hashed = PasswordHasher.hashPassword(newPass);
            user.setPassword(hashed);

            userService.modifyUser(user.getId(), user);

            infoLabel.setStyle("-fx-text-fill: green;");
            infoLabel.setText("Password reset successfully!");

            clearFields();
        } catch (Exception e) {
            infoLabel.setText("User not found!");
        }
    }

    /**
     * Curata campurile de input.
     */
    private void clearFields() {
        usernameField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    /**
     * Inchide fereastra curenta.
     */
    @FXML
    private void goBack() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
