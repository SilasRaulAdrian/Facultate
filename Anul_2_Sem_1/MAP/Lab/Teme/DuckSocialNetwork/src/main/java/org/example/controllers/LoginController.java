package org.example.controllers;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.hash.PasswordHasher;
import org.example.model.User;
import org.example.service.*;

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
    private FriendRequestService friendRequestService;
    private NotificationService notificationService;
    private EventService eventService;

    /**
     * Seteaza service-urile necesare pentru controller.
     *
     * @param userService       service-ul utilizator
     * @param friendshipService service-ul prietenii
     * @param cardService       service-ul carduri
     * @param messageService    service-ul mesaje
     * @param friendRequestService service-ul cereri de prietenie
     * @param notificationService service-ul notificari
     * @param eventService      service-ul evenimente
     */
    public void setServices(UserService userService, FriendshipService friendshipService, CardService cardService, MessageService messageService, FriendRequestService friendRequestService, NotificationService notificationService, EventService eventService) {
        this.notificationService = notificationService;
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.cardService = cardService;
        this.messageService = messageService;
        this.friendRequestService = friendRequestService;
        this.eventService = eventService;
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
                showErrorTimed("Incorrect data!");
                clearFields();
                usernameField.requestFocus();
                return;
            }

            clearFields();
            openMainWindow(user);
            usernameField.requestFocus();
        } catch (Exception e) {
            showErrorTimed("Incorrect data!");
            clearFields();
            usernameField.requestFocus();
        }
    }

    /**
     * Afiseaza un mesaj de eroare pentru o perioada limitata de timp.
     *
     * @param message mesajul de eroare
     */
    private void showErrorTimed(String message) {
        errorLabel.setText(message);
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> errorLabel.setText(""));
        delay.play();
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
            ctrl.setServices(userService, friendshipService, cardService, messageService, friendRequestService, notificationService, eventService);
            ctrl.setLoggedUser(user);

            stage.setTitle("Main Window");
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
     * Deschide fereastra pentru resetarea parolei.
     */
    @FXML
    private void openForgotPasswordWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/forgot-password.fxml"));
            Stage stage = new Stage();

            stage.setScene(new Scene(loader.load(), 400, 300));

            ForgotPasswordController ctrl = loader.getController();
            ctrl.setService(userService);

            stage.setTitle("Reset Password");
            stage.show();
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
