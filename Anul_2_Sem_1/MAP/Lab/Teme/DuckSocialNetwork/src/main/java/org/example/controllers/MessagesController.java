package org.example.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.example.model.User;
import org.example.service.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller pentru fereastra de mesaje.
 */
public class MessagesController {
    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> colName;
    @FXML private TableColumn<User, String> colEmail;

    private UserService userService;
    private MessageService messageService;
    private FriendshipService friendshipService;
    private CardService cardService;
    private FriendRequestService friendRequestService;
    private NotificationService notificationService;
    private EventService eventService;
    private User loggedUser;
    private Stage stage;

    /**
     * Seteaza service-urile necesare pentru controller.
     *
     * @param us service-ul utilizator
     * @param ms service-ul mesaje
     * @param fs service-ul prietenii
     * @param cs service-ul carduri
     * @param frs service-ul cereri de prietenie
     * @param ns service-ul notificari
     * @param es service-ul evenimente
     */
    public void setServices(UserService us, MessageService ms, FriendshipService fs, CardService cs, FriendRequestService frs, NotificationService ns, EventService es) {
        this.userService = us;
        this.messageService = ms;
        this.friendshipService = fs;
        this.cardService = cs;
        this.friendRequestService = frs;
        this.notificationService = ns;
        this.eventService = es;
    }

    /**
     * Seteaza fereastra curenta.
     *
     * @param stage fereastra curenta
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Seteaza utilizatorul autentificat.
     *
     * @param u utilizatorul autentificat
     */
    public void setLoggedUser(User u) {
        this.loggedUser = u;
        usersTable.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        loadUsers();
        deselectRowInTable();
    }

    /**
     * Incarca utilizatorii in tabela, excluzand utilizatorul autentificat.
     */
    private void loadUsers() {
        var users = userService.getAllUsers()
                .stream()
                .filter(u -> !u.getId().equals(loggedUser.getId()))
                .filter(u -> !"admin".equalsIgnoreCase(u.getUsername()))
                .toList();

        colName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUsername()));
        colEmail.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));

        usersTable.setItems(FXCollections.observableArrayList(users));
    }

    /**
     * Deselecteaza randul selectat in tabela atunci cand se face click in afara tabelei.
     */
    private void deselectRowInTable() {
        usersTable.getScene().getRoot().setOnMousePressed(e -> {
            if (!usersTable.isHover()) {
                usersTable.getSelectionModel().clearSelection();
            }
        });
    }

    /**
     * Deschide fereastra de chat cu utilizatorul selectat.
     */
    @FXML
    public void openChat() {
        List<User> selected = new ArrayList<>(usersTable.getSelectionModel().getSelectedItems());
        if (selected.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertisment");
            alert.setHeaderText("Nu a fost selectat niciun utilizator.");
            alert.setContentText("Va rugam selectati un utilizator din tabel pentru a deschide fereastra de chat.");
            alert.showAndWait();

            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/chat-window.fxml"));
            Stage stage = new Stage();

            stage.setScene(new Scene(loader.load(), 600, 600));

            ChatController ctrl = loader.getController();
            ctrl.setServices(userService, messageService, friendshipService, cardService, friendRequestService, notificationService, eventService);
            ctrl.setStage(stage);
            ctrl.loadChat(loggedUser, selected);

            String title = "Logged in as: " + loggedUser.getUsername();
            stage.setTitle(title);
            stage.show();

            Stage currentStage = (Stage) usersTable.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inchide fereastra curenta.
     */
    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main-window.fxml"));
            Stage mainStage = new Stage();

            double width = 800;
            double height = 600;

            mainStage.setScene(new Scene(loader.load(), width, height));

            MainController mainController = loader.getController();
            mainController.setServices(userService, friendshipService, cardService, messageService, friendRequestService, notificationService, eventService);
            mainController.setLoggedUser(loggedUser);

            mainStage.setTitle("Duck Social Network");
            mainStage.show();

            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
