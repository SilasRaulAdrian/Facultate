package org.example.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.model.FriendRequest;
import org.example.model.PopupFriendRequest;
import org.example.model.PopupNotification;
import org.example.model.User;
import org.example.model.paging.Page;
import org.example.model.paging.Pageable;
import org.example.observer.Observer;
import org.example.observer.Signal;
import org.example.service.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller pentru fereastra de gestionare a prietenilor.
 */
public class FriendsManagerController implements Observer {
    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> usersUsernameCol;
    @FXML private TableView<User> friendsTable;
    @FXML private TableColumn<User, String> friendsUsernameCol;
    @FXML private Label requestsCountLabel;
    @FXML private Button usersPrevBtn;
    @FXML private Button usersNextBtn;
    @FXML private Label usersPageLabel;
    @FXML private Button friendsPrevBtn;
    @FXML private Button friendsNextBtn;
    @FXML private Label friendsPageLabel;
    @FXML private Button viewProfileFromUsersBtn;
    @FXML private Button viewProfileFromFriendsBtn;

    private User loggedUser;
    private UserService userService;
    private FriendshipService friendshipService;
    private FriendRequestService friendRequestService;
    private CardService cardService;
    private MessageService messageService;
    private NotificationService notificationService;
    private EventService eventService;
    private Stage stage;

    private List<User> currentFriendsSnapshot = new ArrayList<>();
    private boolean isInitiatingRemoval = false;

    private static final int PAGE_SIZE = 10;

    private int usersPage = 0;
    private int friendsPage = 0;

    /**
     * Seteaza service-urile necesare pentru controller.
     *
     * @param us  service-ul utilizator
     * @param fs  service-ul prietenii
     * @param frs service-ul cereri de prietenie
     * @param cs  service-ul carduri
     * @param ms  service-ul mesaje
     * @param ns  service-ul notificari
     * @param es  service-ul evenimente
     */
    public void setServices(UserService us, FriendshipService fs, FriendRequestService frs, CardService cs, MessageService ms, NotificationService ns, EventService es) {
        this.userService = us;
        this.friendshipService = fs;
        this.friendRequestService = frs;
        this.cardService = cs;
        this.messageService = ms;
        this.notificationService = ns;
        this.eventService = es;

        friendRequestService.addObserver(this);
    }

    /**
     * Seteaza utilizatorul autentificat si actualizeaza interfata in consecinta.
     *
     * @param user utilizatorul autentificat
     */
    public void setLoggedUser(User user) {
        this.loggedUser = user;
        initTables();

        usersPage = 0;
        friendsPage = 0;

        currentFriendsSnapshot = friendshipService.getFriendsOf(loggedUser.getId());

        loadUsersPage();
        loadFriendsPage();
        updateRequestsCount();

        friendsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            viewProfileFromFriendsBtn.setDisable(newSelection == null);
        });

        usersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            viewProfileFromUsersBtn.setDisable(newSelection == null);
        });
    }

    /**
     * Seteaza fereastra curenta si afiseaza notificarile necitite.
     *
     * @param stage fereastra curenta
     */
    public void setStage(Stage stage) {
        this.stage = stage;

        stage.setOnCloseRequest(event -> cleanup());

        Platform.runLater(() -> {
            deselectRowInTable();

            notificationService.getUnseen(loggedUser.getId())
                    .forEach(n -> {
                        PopupNotification.show(stage, n.getMessage(), "💔");
                        notificationService.markSeen(n.getId());
                    });
        });
    }

    /**
     * Initializeaza coloanele tabelelor.
     */
    private void initTables() {
        usersUsernameCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getUsername()));

        friendsUsernameCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getUsername()));
    }

    /**
     * Incarca pagina curenta de utilizatori care nu sunt prieteni.
     */
    private void loadUsersPage() {
        Page<User> page = userService.getUsersNotFriendsPage(
                loggedUser.getId(),
                new Pageable(usersPage, PAGE_SIZE)
        );

        usersTable.setItems(
                FXCollections.observableArrayList(page.getElementsOnPage())
        );

        int totalPages = Math.max(1,
                (int) Math.ceil((double) page.getElementCount() / PAGE_SIZE));

        usersPageLabel.setText(
                "Page " + (usersPage + 1) + " / " + totalPages
        );

        usersPrevBtn.setDisable(usersPage == 0);
        usersNextBtn.setDisable(usersPage >= totalPages - 1);
    }

    /**
     * Incarca pagina curenta de prieteni.
     */
    private void loadFriendsPage() {
        Page<User> page = friendshipService.getFriendsPage(
                loggedUser.getId(),
                new Pageable(friendsPage, PAGE_SIZE)
        );

        friendsTable.setItems(
                FXCollections.observableArrayList(page.getElementsOnPage())
        );

        int totalPages = Math.max(1,
                (int) Math.ceil((double) page.getElementCount() / PAGE_SIZE));

        friendsPageLabel.setText(
                "Page " + (friendsPage + 1) + " / " + totalPages
        );

        friendsPrevBtn.setDisable(friendsPage == 0);
        friendsNextBtn.setDisable(friendsPage >= totalPages - 1);
    }

    /**
     * Trimite o cerere de prietenie catre utilizatorul selectat.
     */
    @FXML
    private void sendRequest() {
        User selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please select a user to send a friend request.");
            alert.initOwner(stage);
            alert.showAndWait();
            return;
        }

        if (friendRequestService.requestExists(loggedUser, selected)) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "A friend request to " + selected.getUsername() + " already exists.");
            alert.initOwner(stage);
            alert.showAndWait();
            return;
        }

        friendRequestService.sendRequest(loggedUser, selected);
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "Friend request sent to " + selected.getUsername());
        alert.initOwner(stage);
        alert.showAndWait();
    }

    /**
     * Elimina prietenul selectat.
     */
    @FXML
    private void removeFriend() {
        User selected = friendsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please select a friend to remove.");
            alert.initOwner(stage);
            alert.showAndWait();
            return;
        }

        isInitiatingRemoval = true;

        try {
            Long friendshipId = friendshipService.findFriendshipId(loggedUser.getId(), selected.getId());
            friendshipService.removeFriendship(friendshipId, loggedUser.getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "You have removed " + selected.getUsername() + " from your friends.");
            alert.initOwner(stage);
            alert.showAndWait();
        } finally {
            isInitiatingRemoval = false;
        }
    }

    /**
     * Deschide fereastra de cereri de prietenie.
     *
     * @throws Exception daca apare o eroare la incarcarea ferestrei
     */
    @FXML
    private void openRequests() throws Exception {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/friend-requests.fxml"));
        stage.setScene(new Scene(loader.load()));

        FriendRequestsController ctrl = loader.getController();
        ctrl.setServices(friendRequestService, friendshipService);
        ctrl.setLoggedUser(loggedUser);

        stage.setTitle("Friend Requests");
        stage.show();
    }

    /**
     * Actualizeaza numarul de cereri de prietenie in asteptare.
     */
    private void updateRequestsCount() {
        List<FriendRequest> pending = friendRequestService.getPending(loggedUser);
        requestsCountLabel.setText(String.valueOf(pending.size()));
    }

    /**
     * Deselecteaza randul selectat in tabel atunci cand se face clic in afara acestuia.
     */
    private void deselectRowInTable() {
        friendsTable.getScene().getRoot().setOnMousePressed(e -> {
            if (!friendsTable.isHover()) {
                friendsTable.getSelectionModel().clearSelection();
            }
        });

        usersTable.getScene().getRoot().setOnMousePressed(e -> {
            if (!usersTable.isHover()) {
                usersTable.getSelectionModel().clearSelection();
            }
        });
    }

    /**
     * Actualizeaza interfata atunci cand apare un semnal de schimbare.
     *
     * @param signal semnalul de schimbare
     */
    @Override
    public void update(Signal signal) {
        switch (signal.getType()) {
            case "NEW_FRIEND_REQUEST" -> {
                User from = signal.getFrom();
                User to = signal.getTo().get(0);

                if (to.equals(loggedUser)) {
                    Platform.runLater(() -> {
                        FriendRequest fr = friendRequestService
                                .getPending(loggedUser)
                                .stream()
                                .filter(r -> r.getFrom().equals(from))
                                .findFirst()
                                .orElse(null);

                        if (fr == null) return;

                        PopupFriendRequest.show(
                                stage,
                                from,
                                friendRequestService,
                                fr.getId()
                        );

                        updateRequestsCount();
                    });
                }
            }

            case "REQUEST_ACCEPTED", "REQUEST_DECLINED" -> {
                Platform.runLater(() -> {
                    loadFriendsPage();
                    loadUsersPage();
                    updateRequestsCount();
                });
            }

            case "FRIENDSHIP_REMOVED" -> {
                Platform.runLater(() -> {
                    notificationService.getUnseen(loggedUser.getId())
                            .forEach(n -> {
                                PopupNotification.show(stage, n.getMessage(), "💔");
                                notificationService.markSeen(n.getId());
                            });

                    loadFriendsPage();
                    loadUsersPage();
                    updateRequestsCount();
                });
            }
        }
    }

    /**
     * Deschide fereastra de profil a utilizatorului selectat.
     *
     * @throws Exception daca apare o eroare la incarcarea ferestrei
     */
    @FXML
    private void viewProfileFromUsers() throws Exception {
        User selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        cleanup();

        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/user-profile.fxml"));

        double width = 400;
        double height = 600;

        newStage.setScene(new Scene(loader.load(), width, height));

        UserProfileController ctrl = loader.getController();
        ctrl.setServices(userService, friendshipService, cardService, messageService,
                friendRequestService, notificationService, eventService);
        ctrl.setLoggedUser(loggedUser);
        ctrl.setViewedUser(selected);
        ctrl.setStage(newStage);

        newStage.setTitle(selected.getUsername() + "'s Profile");
        newStage.show();
    }

    /**
     * Deschide fereastra de profil a prietenului selectat.
     *
     * @throws Exception daca apare o eroare la incarcarea ferestrei
     */
    @FXML
    private void viewProfileFromFriends() throws Exception {
        User selected = friendsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        cleanup();

        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/user-profile.fxml"));

        double width = 400;
        double height = 600;

        newStage.setScene(new Scene(loader.load(), width, height));

        UserProfileController ctrl = loader.getController();
        ctrl.setServices(userService, friendshipService, cardService, messageService,
                friendRequestService, notificationService, eventService);
        ctrl.setLoggedUser(loggedUser);
        ctrl.setViewedUser(selected);
        ctrl.setStage(newStage);

        newStage.setTitle(selected.getUsername() + "'s Profile");
        newStage.show();
    }

    /**
     * Inchide fereastra curenta si revine la fereastra principala.
     *
     * @throws IOException daca apare o eroare la incarcarea ferestrei principale
     */
    @FXML
    private void goBack() throws IOException {
        cleanup();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main-window.fxml"));
        Stage mainStage = new Stage();

        double width = 800;
        double height = 600;

        mainStage.setScene(new Scene(loader.load(), width, height));

        MainController mainCtrl = loader.getController();

        mainCtrl.setServices(
                userService,
                friendshipService,
                cardService,
                messageService,
                friendRequestService,
                notificationService,
                eventService
        );

        mainCtrl.setLoggedUser(loggedUser);

        mainStage.setTitle("Main Window");
        mainStage.show();

        stage.close();
    }

    /**
     * Mergi la pagina urmatoare de utilizatori.
     */
    @FXML
    private void usersNext() {
        usersPage++;
        loadUsersPage();
    }

    /**
     * Mergi la pagina anterioara de utilizatori.
     */
    @FXML
    private void usersPrev() {
        usersPage--;
        loadUsersPage();
    }

    /**
     * Mergi la pagina urmatoare de prieteni.
     */
    @FXML
    private void friendsNext() {
        friendsPage++;
        loadFriendsPage();
    }

    /**
     * Mergi la pagina anterioara de prieteni.
     */
    @FXML
    private void friendsPrev() {
        friendsPage--;
        loadFriendsPage();
    }

    /**
     * Curata resursele si elimina observer-ul cand fereastra se inchide.
     */
    public void cleanup() {
        if (friendRequestService != null) {
            friendRequestService.removeObserver(this);
        }
    }
}