package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.config.ConnectionManager;
import org.example.controllers.LoginController;
import org.example.controllers.MainController;
import org.example.repository.*;
import org.example.service.*;

/**
 * Clasa principala pentru aplicatie
 */
public class Main extends Application {
    /**
     * Metoda pentru pornirea aplicatiei
     * @param stage Stage, scena principala a aplicatiei
     */
    @Override
    public void start(Stage stage) {
        try {
            ConnectionManager.init(
                    "jdbc:postgresql://localhost:5432/DuckSocialNetwork",
                    "postgres",
                    "root"
            );

            UserRepository userRepo = new UserRepository();
            FriendshipRepository friendshipRepo = new FriendshipRepository(userRepo);
            EventRepository eventRepo = new EventRepository(userRepo);
            CardRepository cardRepo = new CardRepository(userRepo);
            MessageRepository messageRepo = new MessageRepository(userRepo);
            FriendRequestDBRepository friendRequestRepo = new FriendRequestDBRepository(userRepo);
            NotificationRepository notificationRepo = new NotificationRepository();

            NotificationService notificationService = new NotificationService(notificationRepo);
            FriendshipService friendshipService = new FriendshipService(userRepo, friendshipRepo, notificationService);
            UserService userService = new UserService(userRepo, friendshipRepo);
            EventService eventService = new EventService(userRepo, eventRepo);
            CardService cardService = new CardService(cardRepo, userRepo);
            MessageService messageService = new MessageService(messageRepo);
            FriendRequestService friendRequestService = new FriendRequestService(friendRequestRepo, friendshipService);

            userService.migratePasswordsToHash();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login-window.fxml"));
            Scene scene = new Scene(loader.load(), 400, 400);
            LoginController controller = loader.getController();
            controller.setServices(userService, friendshipService, cardService, messageService, friendRequestService, notificationService, eventService);
            controller.setMainStage(stage);

            stage.setTitle("Duck Social Network");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda principala pentru pornirea aplicatiei
     * @param args String[], argumentele liniei de comanda
     */
    public static void main(String[] args) {
        launch(args);
    }
}