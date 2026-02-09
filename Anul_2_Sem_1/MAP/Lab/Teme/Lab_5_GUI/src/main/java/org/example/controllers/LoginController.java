package org.example.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.hash.PasswordHasher;
import org.example.model.User;
import org.example.service.CardService;
import org.example.service.FriendshipService;
import org.example.service.MessageService;
import org.example.service.UserService;

/**
 * Controller pentru fereastra de autentificare.
 */
public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private UserService userService;
    private FriendshipService friendshipService;
    private CardService cardService;
    private MessageService messageService;

    /**
     * Seteaza service-urile necesare pentru controller.
     *
     * @param userService       service-ul utilizator
     * @param friendshipService service-ul prietenii
     * @param cardService       service-ul carduri
     * @param messageService    service-ul mesaje
     */
    public void setServices(UserService userService, FriendshipService friendshipService, CardService cardService, MessageService messageService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.cardService = cardService;
        this.messageService = messageService;
    }

    /**
     * Seteaza service-ul utilizator.
     *
     * @param userService service-ul utilizator
     */
    public void setService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Gestioneaza evenimentul de autentificare a utilizatorului.
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            User user = userService.findByUsername(username);

            if (!PasswordHasher.verifyPassword(password, user.getPassword())) {
                errorLabel.setText("Wrong Password");
                return;
            }

            clearFields();
            openMainWindow(user);
            usernameField.requestFocus();
        } catch (Exception e) {
            errorLabel.setText("User not found!");
            usernameField.requestFocus();
        }
    }

    /**
     * Curata campurile de input si mesajele de eroare.
     */
    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }

    /**
     * Deschide fereastra principala a aplicatiei dupa autentificare.
     *
     * @param user utilizatorul autentificat
     */
    private void openMainWindow(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main-window.fxml"));
            Stage stage = new Stage();

            double width = 800;
            double height = 600;

            stage.setScene(new Scene(loader.load(), width, height));

            MainController ctrl = loader.getController();
            ctrl.setServices(userService, friendshipService, cardService, messageService);
            ctrl.setLoggedUser(user);

            stage.setTitle("Logged in as: " + user.getUsername());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deschide fereastra de inregistrare a unui nou utilizator.
     */
    @FXML
    private void openRegisterWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/register-window.fxml"));
            Stage stage = new Stage();

            double width = 400;
            double height = 600;

            stage.setScene(new Scene(loader.load(), width, height));

            RegisterController ctrl = loader.getController();
            ctrl.setService(userService, stage);

            stage.setTitle("Register");
            stage.show();

            ((Stage) usernameField.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Seteaza comportamentul la inchiderea ferestrei principale.
     *
     * @param stage scena principala a aplicatiei
     */
    public void setMainStage(Stage stage) {
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * Inchide aplicatia.
     */
    @FXML
    public void handleExit() {
        Platform.exit();
        System.exit(0);
    }
}
