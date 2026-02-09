package org.example.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.event.RaceEvent;
import org.example.model.Lane;
import org.example.model.SwimmingDuck;
import org.example.model.User;
import org.example.observer.Observer;
import org.example.observer.Signal;
import org.example.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RaceEventController implements Observer {
    @FXML private ListView<String> eventsListView;
    @FXML private ListView<String> participantsListView;
    @FXML private ListView<String> subscribersListView;
    @FXML private TextArea raceLogTextArea;
    @FXML private Button createEventButton;
    @FXML private Button subscribeButton;
    @FXML private Button unsubscribeButton;
    @FXML private Button startRaceButton;
    @FXML private Button refreshButton;
    @FXML private Label statusLabel;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Button deleteEventButton;
    @FXML private Button participateButton;
    @FXML private Button withdrawButton;

    private EventService eventService;
    private UserService userService;
    private FriendshipService friendshipService;
    private CardService cardService;
    private MessageService messageService;
    private FriendRequestService friendRequestService;
    private NotificationService notificationService;
    private User loggedUser;
    private Stage stage;

    private ObservableList<RaceEvent> events = FXCollections.observableArrayList();
    private RaceEvent selectedEvent;

    /**
     * Initializeaza controller-ul
     */
    @FXML
    public void initialize() {
        eventsListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> onEventSelected()
        );

        if (progressIndicator != null) {
            progressIndicator.setVisible(false);
        }

        if (raceLogTextArea != null) {
            raceLogTextArea.setEditable(false);
            raceLogTextArea.setWrapText(true);
        }
    }

    /**
     * Seteaza serviciile necesare
     * @param eventService service-ul evenimente
     * @param userService service-ul utilizatori
     * @param friendshipService service-ul prietenii
     * @param cardService service-ul carduri
     * @param messageService service-ul mesaje
     * @param friendRequestService service-ul cereri de prietenie
     * @param notificationService service-ul notificari
     */
    public void setServices(EventService eventService, UserService userService, FriendshipService friendshipService, CardService cardService, MessageService messageService, FriendRequestService friendRequestService, NotificationService notificationService) {
        this.eventService = eventService;
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.cardService = cardService;
        this.messageService = messageService;
        this.friendRequestService = friendRequestService;
        this.notificationService = notificationService;

        this.eventService.addObserver(this);
        loadEvents();
    }

    /**
     * Seteaza utilizatorul autentificat
     * @param user utilizatorul autentificat
     */
    public void setLoggedUser(User user) {
        this.loggedUser = user;
        updateUIForUser();

        if (loggedUser != null) {
            loggedUser.setMessageListener(message -> {
                Platform.runLater(() -> {
                    if (message.trim().startsWith("=") || message.contains("🏁") || message.contains("🏆")) {
                        logToRaceLog(message);
                    } else {
                        logToRaceLog(message);
                    }
                });
            });

            logToRaceLog(loggedUser.getUsername() + " is now watching events...");
        }
    }

    /**
     * Seteaza stage-ul
     * @param stage stage-ul curent
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Actualizeaza UI-ul in functie de utilizator
     */
    private void updateUIForUser() {
        boolean isAdmin = loggedUser != null && loggedUser.getUsername().equals("admin");
        boolean isDuck = loggedUser instanceof SwimmingDuck;

        if (createEventButton != null) {
            createEventButton.setDisable(!isAdmin);
        }

        if (startRaceButton != null) {
            startRaceButton.setDisable(!isAdmin);
        }

        if (deleteEventButton != null) {
            deleteEventButton.setDisable(!isAdmin);
        }

        if (participateButton != null) {
            participateButton.setVisible(isDuck);
        }

        if (withdrawButton != null) {
            withdrawButton.setVisible(isDuck);
        }
    }

    /**
     * Incarca toate evenimentele
     */
    private void loadEvents() {
        try {
            events.clear();
            List<RaceEvent> allEvents = eventService.getAllEvents();
            events.addAll(allEvents);

            List<String> eventStrings = allEvents.stream()
                    .map(e -> String.format("Event #%d - %d ducks, %d lanes, %d subscribers",
                            e.getId(),
                            e.getDucks().size(),
                            e.getLanes().size(),
                            e.getSubscribers().size()))
                    .collect(Collectors.toList());

            eventsListView.getItems().setAll(eventStrings);

            updateStatus("Loaded " + allEvents.size() + " events");

        } catch (Exception e) {
            showError("Failed to load events: " + e.getMessage());
        }
    }

    /**
     * Handler pentru selectarea unui eveniment
     */
    private void onEventSelected() {
        int index = eventsListView.getSelectionModel().getSelectedIndex();
        if (index < 0 || index >= events.size()) {
            selectedEvent = null;
            participantsListView.getItems().clear();
            subscribersListView.getItems().clear();
            return;
        }

        selectedEvent = events.get(index);

        List<String> participants = selectedEvent.getDucks().stream()
                .map(duck -> String.format("%s (Speed: %.2f, Resistance: %.2f)",
                        duck.getUsername(), duck.getViteza(), duck.getRezistenta()))
                .collect(Collectors.toList());
        participantsListView.getItems().setAll(participants);

        List<String> subscribers = selectedEvent.getSubscribers().stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
        subscribersListView.getItems().setAll(subscribers);

        updateStatus("Selected Event #" + selectedEvent.getId());
    }

    /**
     * Creeaza un eveniment nou
     */
    @FXML
    private void handleCreateEvent() {
        Dialog<List<Lane>> dialog = new Dialog<>();
        dialog.setTitle("Create Race Event");
        dialog.setHeaderText("Configure the race lanes");

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        Spinner<Integer> laneCountSpinner = new Spinner<>(1, 10, 3);
        content.getChildren().addAll(
                new Label("Number of lanes:"),
                laneCountSpinner
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                int count = laneCountSpinner.getValue();
                List<Lane> lanes = new ArrayList<>();
                for (int i = 1; i <= count; i++) {
                    lanes.add(new Lane(i, 50.0 + (i * 10)));
                }
                return lanes;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(lanes -> {
            try {
                eventService.createEmptyRace(lanes);
                loadEvents();
                updateStatus("Event created successfully!");
            } catch (Exception e) {
                showError("Failed to create event: " + e.getMessage());
            }
        });
    }

    /**
     * Aboneaza utilizatorul curent la evenimentul selectat - ASINCRON
     */
    @FXML
    private void handleSubscribe() {
        if (selectedEvent == null) {
            showWarning("Please select an event first");
            return;
        }

        showProgress(true);
        updateStatus("Subscribing...");

        selectedEvent.subscribeAsync(loggedUser)
                .thenAccept(success -> {
                    if (success) {
                        Platform.runLater(() -> {
                            try {
                                eventService.subscribeUser(selectedEvent.getId(), loggedUser.getId());
                                updateStatus("Successfully subscribed");
                                logToRaceLog("You subscribed to Event #" + selectedEvent.getId());
                            } catch (Exception e) {
                                showError("Database update failed: " + e.getMessage());
                            }
                        });
                    } else {
                        Platform.runLater(() -> showWarning("Already subscribed"));
                    }
                })
                .thenRun(() -> Platform.runLater(() -> showProgress(false)))
                .exceptionally(ex -> {
                    Platform.runLater(() -> {
                        showError("Subscription error: " + ex.getMessage());
                        showProgress(false);
                    });
                    return null;
                });
    }

    /**
     * Dezaboneaza utilizatorul curent de la evenimentul selectat - ASINCRON
     */
    @FXML
    private void handleUnsubscribe() {
        if (selectedEvent == null) {
            showWarning("Please select an event first");
            return;
        }

        showProgress(true);
        updateStatus("Unsubscribing...");

        selectedEvent.unsubscribeAsync(loggedUser)
                .thenAccept(success -> {
                    if (success) {
                        Platform.runLater(() -> {
                            try {
                                eventService.unsubscribeUser(selectedEvent.getId(), loggedUser.getId());
                                updateStatus("Successfully unsubscribed");
                                logToRaceLog("You unsubscribed from Event #" + selectedEvent.getId());
                            } catch (Exception e) {
                                showError("Database update failed: " + e.getMessage());
                            }
                        });
                    } else {
                        Platform.runLater(() -> showWarning("Not subscribed to this event"));
                    }
                })
                .thenRun(() -> Platform.runLater(() -> showProgress(false)))
                .exceptionally(ex -> {
                    Platform.runLater(() -> {
                        showError("Unsubscription error: " + ex.getMessage());
                        showProgress(false);
                    });
                    return null;
                });
    }

    /**
     * Porneste cursa selectata - ASINCRON
     */
    @FXML
    private void handleStartRace() {
        if (selectedEvent == null) {
            showWarning("Please select an event first");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Start Race");
        confirm.setHeaderText("Start Event #" + selectedEvent.getId() + "?");
        confirm.setContentText("This will notify all " + selectedEvent.getSubscribers().size() + " subscribers.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                startRaceAsync();
            }
        });
    }

    /**
     * Porneste cursa in mod asincron si afiseaza notificarile in timp real
     */
    private void startRaceAsync() {
        showProgress(true);
        updateStatus("Starting race...");

        if (loggedUser != null) {
            boolean alreadySubscribed = selectedEvent.getSubscribers().stream()
                    .anyMatch(u -> u.getId().equals(loggedUser.getId()));

            if (!alreadySubscribed) {
                selectedEvent.subscribe(loggedUser);
            }
        }

        selectedEvent.startRaceAsync()
                .thenAccept(result -> Platform.runLater(() -> {
                    updateStatus("Race completed!");
                    showProgress(false);

                    Alert resultAlert = new Alert(Alert.AlertType.INFORMATION);
                    resultAlert.setTitle("Race Completed");
                    resultAlert.setHeaderText("Event #" + selectedEvent.getId() + " finished!");
                    resultAlert.setContentText(result);
                    resultAlert.showAndWait();
                }))
                .exceptionally(ex -> {
                    Platform.runLater(() -> {
                        showError("Race failed: " + ex.getMessage());
                        logToRaceLog("ERROR: " + ex.getMessage());
                        showProgress(false);
                    });
                    return null;
                });
    }

    /**
     * Sterge evenimentul selectat
     */
    @FXML
    private void handleDeleteEvent() {
        if (selectedEvent == null) {
            showWarning("Please select an event to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Event");
        alert.setHeaderText("Are you sure you want to delete Event #" + selectedEvent.getId() + "?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    eventService.removeRace(selectedEvent.getId());
                    updateStatus("Event #" + selectedEvent.getId() + " deleted.");
                    loadEvents();
                    selectedEvent = null;
                    participantsListView.getItems().clear();
                    subscribersListView.getItems().clear();
                } catch (Exception e) {
                    showError("Could not delete event: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Refresh lista de evenimente
     */
    @FXML
    private void handleRefresh() {
        loadEvents();
    }

    /**
     * Adauga mesaj in race log
     * @param message mesajul de adaugat
     */
    private void logToRaceLog(String message) {
        if (raceLogTextArea != null) {
            raceLogTextArea.appendText(message + "\n");
        }
    }

    /**
     * Afiseaza/ascunde progress indicator
     * @param show true pentru a afisa, false pentru a ascunde
     */
    private void showProgress(boolean show) {
        if (progressIndicator != null) {
            progressIndicator.setVisible(show);
        }
    }

    /**
     * Actualizeaza status label
     * @param message mesajul de afisat
     */
    private void updateStatus(String message) {
        if (statusLabel != null) {
            Platform.runLater(() -> statusLabel.setText(message));
        }
    }

    /**
     * Afiseaza eroare
     * @param message mesajul de eroare
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Afiseaza warning
     * @param message mesajul de warning
     */
    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Sterge continutul race log-ului
     */
    @FXML
    private void handleClearLog() {
        if (raceLogTextArea != null) {
            raceLogTextArea.clear();
            updateStatus("Log cleared");
        }
    }

    /**
     * Metoda care redeschide fereastra Main
     */
    private void showMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main-window.fxml"));
            Stage mainStage = new Stage();
            mainStage.setScene(new Scene(loader.load(), 800, 600));

            MainController ctrl = loader.getController();
            ctrl.setServices(userService, friendshipService, cardService, messageService, friendRequestService, notificationService, eventService);
            ctrl.setLoggedUser(loggedUser);

            mainStage.setTitle("Main Window");
            mainStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Participa la evenimentul selectat
     */
    @FXML
    private void handleParticipate() {
        if (selectedEvent == null) {
            showWarning("Please select an event first");
            return;
        }

        if (!(loggedUser instanceof SwimmingDuck)) {
            showError("Only ducks can participate");
            return;
        }

        try {
            eventService.registerDuckToEvent(
                    selectedEvent.getId(),
                    loggedUser.getId()
            );

            updateStatus("You are now a participant!");
            logToRaceLog("You joined the race for Event #" + selectedEvent.getId());

            loadEvents();
            onEventSelected();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }


    /**
     * Se retrage din evenimentul selectat
     */
    @FXML
    private void handleWithdraw() {
        if (selectedEvent == null) {
            showWarning("Please select an event first");
            return;
        }

        if (!(loggedUser instanceof SwimmingDuck)) {
            showError("Only ducks can withdraw");
            return;
        }

        try {
            eventService.unregisterDuckFromEvent(
                    selectedEvent.getId(),
                    loggedUser.getId()
            );

            updateStatus("You withdrew from the race.");
            logToRaceLog("You withdrew from the race for Event #" + selectedEvent.getId());

            loadEvents();
            onEventSelected();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }


    /**
     * Metoda apelata la primirea unui semnal de la Observable
     * @param signal semnalul primit
     */
    @Override
    public void update(Signal signal) {
        if (signal.getType().equals("EVENT_UPDATED") || signal.getType().equals("EVENT_CREATED") || signal.getType().equals("EVENT_DELETED")) {
            Platform.runLater(() -> {
                loadEvents();

                if (selectedEvent != null) {
                    Long selectedId = selectedEvent.getId();
                    for (RaceEvent e : events) {
                        if (e.getId().equals(selectedId)) {
                            eventsListView.getSelectionModel().select(events.indexOf(e));
                            break;
                        }
                    }
                }
            });
        }
    }

    /**
     * Inchide fereastra
     */
    @FXML
    private void handleClose() {
        if (loggedUser != null) {
            loggedUser.setMessageListener(null);
            logToRaceLog(loggedUser.getUsername() + " left the event window.");
        }

        if (stage != null) {
            showMainWindow();
            stage.close();
        }
    }
}
