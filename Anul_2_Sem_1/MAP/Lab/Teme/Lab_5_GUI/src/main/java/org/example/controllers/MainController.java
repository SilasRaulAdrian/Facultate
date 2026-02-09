package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.model.User;
import org.example.service.CardService;
import org.example.service.FriendshipService;
import org.example.service.MessageService;
import org.example.service.UserService;

/**
 * Controller pentru fereastra principala a aplicatiei.
 */
public class MainController {
    @FXML private Button manageDucksButton;
    @FXML private Button managePersonsButton;
    @FXML private Button manageFriendshipButton;
    @FXML private Label welcomeLabel;

    private UserService userService;
    private FriendshipService friendshipService;
    private CardService cardService;
    private User loggedUser;
    private MessageService messageService;

    /**
     * Seteaza serviciile necesare pentru controller.
     *
     * @param us service-ul utilizator
     * @param fs service-ul prietenii
     * @param cs service-ul carduri
     * @param ms service-ul mesaje
     */
    public void setServices(UserService us, FriendshipService fs, CardService cs, MessageService ms) {
        this.userService = us;
        this.friendshipService = fs;
        this.cardService = cs;
        this.messageService = ms;
    }

    /**
     * Seteaza service-ul utilizator.
     *
     * @param us service-ul utilizator
     */
    public void setService(UserService us) {
        this.userService = us;
    }

    /**
     * Seteaza utilizatorul autentificat si actualizeaza interfata in consecinta.
     *
     * @param user utilizatorul autentificat
     */
    public void setLoggedUser(User user) {
        this.loggedUser = user;

        welcomeLabel.setText("Welcome, " + user.getUsername() + "!");

        boolean isAdmin = user.getUsername().equals("admin");

        managePersonsButton.setDisable(!isAdmin);
        manageDucksButton.setDisable(!isAdmin);
        manageFriendshipButton.setDisable(!isAdmin);
    }

    /**
     * Deschide fereastra pentru gestionarea ratelor.
     *
     * @throws Exception daca apare o eroare la incarcarea ferestrei
     */
    @FXML
    public void openDucksWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ducks-window.fxml"));
        Stage stage = new Stage();

        double width = 712;
        double height = 600;

        stage.setScene(new Scene(loader.load(), width, height));

        DucksController ctrl = loader.getController();
        ctrl.setService(userService, cardService, friendshipService, messageService);
        ctrl.setLoggedUser(loggedUser);

        stage.setTitle("Ducks");
        stage.show();

        Stage currentStage = (Stage) manageDucksButton.getScene().getWindow();
        currentStage.close();
    }

    /**
     * Deschide fereastra pentru gestionarea persoanelor.
     *
     * @throws Exception daca apare o eroare la incarcarea ferestrei
     */
    @FXML
    public void openPersonsWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/person-window.fxml"));
        Stage stage = new Stage();

        double width = 865;
        double height = 600;

        stage.setScene(new Scene(loader.load(), width, height));

        PersonsController ctrl = loader.getController();
        ctrl.setService(userService, friendshipService, cardService, messageService);
        ctrl.setLoggedUser(loggedUser);

        stage.setTitle("Persons");
        stage.show();

        Stage currentStage = (Stage) managePersonsButton.getScene().getWindow();
        currentStage.close();
    }

    /**
     * Deschide fereastra pentru gestionarea prieteniilor.
     *
     * @throws Exception daca apare o eroare la incarcarea ferestrei
     */
    @FXML
    public void openFriendshipsWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/friendships-window.fxml"));
        Stage stage = new Stage();

        double width = 682;
        double height = 600;

        stage.setScene(new Scene(loader.load(), width, height));

        FriendshipsController ctrl = loader.getController();
        ctrl.setServices(friendshipService, userService, cardService, messageService);
        ctrl.setLoggedUser(loggedUser);

        stage.setTitle("Friendships");
        stage.show();

        Stage currentStage = (Stage) manageFriendshipButton.getScene().getWindow();
        currentStage.close();
    }

    /**
     * Deschide fereastra pentru gestionarea mesajelor.
     *
     * @throws Exception daca apare o eroare la incarcarea ferestrei
     */
    @FXML
    public void openMessagesWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/messages-window.fxml"));
        Stage stage = new Stage();

        double width = 800;
        double height = 600;

        stage.setScene(new Scene(loader.load(), width, height));

        MessagesController ctrl = loader.getController();
        ctrl.setServices(userService, messageService, friendshipService, cardService);
        ctrl.setLoggedUser(loggedUser);
        ctrl.setStage(stage);

        stage.setTitle("Messages");
        stage.show();

        Stage currentStage = (Stage) manageDucksButton.getScene().getWindow();
        currentStage.close();
    }

    /**
     * Delogheaza utilizatorul curent si redeschide fereastra de login.
     */
    @FXML
    public void handleLogOut() {
        try {
            Stage currentStage = (Stage) manageDucksButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Eroare la delogare: " + e.getMessage());
        }
    }
}
