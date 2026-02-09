package org.example.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.example.exceptions.CardNotFoundException;
import org.example.model.*;
import org.example.model.paging.Page;
import org.example.model.paging.Pageable;
import org.example.observer.Observer;
import org.example.observer.Signal;
import org.example.service.*;

import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Clasa controller pentru fereastra de gestionare a ratelor.
 */
public class DucksController implements Observer {
    @FXML private TableView<User> table;
    @FXML private TableColumn<User, Long> colId;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<Duck, String> colType;
    @FXML private TableColumn<Duck, Double> colSpeed;
    @FXML private TableColumn<Duck, Double> colStamina;
    @FXML private TableColumn<Duck, String> colCard;
    @FXML private ComboBox<DuckTypeFilter> typeFilterCombo;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label pageInfoLabel;

    private final ObservableList<User> model = FXCollections.observableArrayList();
    private UserService service;
    private CardService cardService;
    private FriendshipService friendshipService;
    private MessageService messageService;
    private FriendRequestService friendRequestService;
    private NotificationService notificationService;
    private EventService eventService;

    private final ObservableList<DuckTypeFilter> filterModel = FXCollections.observableArrayList();
    private MediaPlayer mediaPlayer;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 5;
    private int totalPages = 0;
    private User loggedUser;

    /**
     * Seteaza service-ul utilizator si initializeaza componentele UI.
     *
     * @param service service-ul utilizator
     * @param cardService service-ul carduri
     * @param friendshipService service-ul prietenii
     * @param messageService service-ul mesaje
     * @param friendRequestService service-ul cereri de prietenie
     * @param notificationService service-ul notificari
     * @param eventService service-ul evenimente
     */
    public void setService(UserService service, CardService cardService, FriendshipService friendshipService, MessageService messageService, FriendRequestService friendRequestService, NotificationService notificationService, EventService eventService) {
        this.cardService = cardService;
        this.service = service;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.friendRequestService = friendRequestService;
        this.notificationService = notificationService;
        this.eventService = eventService;

        service.addObserver(this);
        initTable();
        initFilter();
        loadData();
        deselectRowInTable();

        playBackgroundMusic();
    }

    /**
     * Initializeaza tabelul cu datele corespunzatoare.
     */
    private void initTable() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colId.setCellValueFactory(c -> new SimpleLongProperty(c.getValue().getId()).asObject());
        colUsername.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUsername()));
        colEmail.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));

        colType.setCellValueFactory(c -> {
            Duck d = (Duck) c.getValue();
            return new SimpleStringProperty(d.getTip().name());
        });

        colSpeed.setCellValueFactory(c -> new SimpleDoubleProperty(((Duck) c.getValue()).getViteza()).asObject());
        colStamina.setCellValueFactory(c -> new SimpleDoubleProperty(((Duck) c.getValue()).getRezistenta()).asObject());

        colCard.setCellValueFactory(c -> {
            Duck d = (Duck) c.getValue();

            String cardName = "No Card";

            if (d.getCardId() != null) {
                try {
                    Card<? extends Duck> card = cardService.findCardById(d.getCardId());
                    cardName = card.getNumeCard();
                } catch (CardNotFoundException e) {
                    cardName = "Unknown";
                }
            }

            return new SimpleStringProperty(cardName);
        });

        table.setItems(model);
    }

    /**
     * Initializeaza filtrul pentru tipurile de rate.
     */
    private void initFilter() {
        filterModel.add(DuckTypeFilter.all());

        Arrays.stream(TipRata.values())
                .map(DuckTypeFilter::of)
                .forEach(filterModel::add);

        typeFilterCombo.setItems(filterModel);

        typeFilterCombo.getSelectionModel().select(0);

        typeFilterCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadData();
            }
        });
    }

    /**
     * Deselecteaza randul selectat in tabel atunci cand se face clic in afara acestuia.
     */
    private void deselectRowInTable() {
        table.getScene().getRoot().setOnMousePressed(e -> {
            if (!table.isHover()) {
                table.getSelectionModel().clearSelection();
            }
        });
    }

    /**
     * Reda muzica de fundal in bucla.
     */
    private void playBackgroundMusic() {
        URL resource = getClass().getResource("/audio/background.mp3");
        if (resource == null) {
            System.out.println("Audio file not found!");
            return;
        }

        Media media = new Media(resource.toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.5);
        mediaPlayer.play();

        Platform.runLater(() -> {
            Stage stage = (Stage) table.getScene().getWindow();

            stage.setOnCloseRequest(event -> {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
            });
        });
    }

    /**
     * Incarca datele in tabel pe baza paginarii si a filtrului selectat.
     */
    private void loadData() {
        DuckTypeFilter selectedFilter = typeFilterCombo.getSelectionModel().getSelectedItem();
        TipRata filterTip = (selectedFilter != null) ? selectedFilter.getTipRata() : null;

        Pageable pageable = new Pageable(currentPage, PAGE_SIZE);

        Page<Duck> page = service.findAllDucksOnPage(pageable, filterTip);

        int totalElements = page.getElementCount();
        totalPages = (int) Math.ceil((double) totalElements / PAGE_SIZE);

        if (currentPage >= totalPages && totalPages > 0) {
            currentPage = totalPages - 1;
            loadData();
            return;
        } else if (totalPages == 0) {
            currentPage = 0;
        }

        model.setAll(page.getElementsOnPage().stream().collect(Collectors.toList()));

        updatePaginationControls();
    }

    /**
     * Actualizeaza controalele de paginare in functie de pagina curenta si numarul total de pagini.
     */
    private void updatePaginationControls() {
        prevButton.setDisable(currentPage == 0);
        nextButton.setDisable(currentPage >= totalPages - 1 || totalPages <= 1);

        if (totalPages > 0) {
            pageInfoLabel.setText("Page " + (currentPage + 1) + " of " + totalPages);
        } else {
            pageInfoLabel.setText("No Results");
        }
    }

    /**
     * Metoda apelata atunci cand se primeste un semnal de actualizare.
     *
     * @param signal semnalul de actualizare
     */
    @Override
    public void update(Signal signal) {
        currentPage = 0;
        loadData();
    }

    /**
     * Sterge utilizatorul selectat din tabel.
     */
    @FXML
    private void deleteSelected() {
        User u = table.getSelectionModel().getSelectedItem();
        if (u == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No user selected");
            alert.setContentText("Please select a user before deleting.");
            alert.showAndWait();
            return;
        }

        service.removeUser(u.getId());
    }

    /**
     * Deschide fereastra pentru adaugarea unei noi rate.
     *
     * @throws Exception daca apare o eroare la incarcarea ferestrei
     */
    @FXML
    private void openAddWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/add-duck-window.fxml"));
        Stage st = new Stage();

        double width = 350;
        double height = 420;

        st.setScene(new Scene(loader.load(), width, height));

        AddDuckController ctrl = loader.getController();
        ctrl.setServices(service, st);

        st.setTitle("Add Duck");
        st.show();
    }

    /**
     * Mergi la pagina anterioara in tabel.
     */
    @FXML
    private void handlePrevious() {
        if (currentPage > 0) {
            currentPage--;
            loadData();
        }
    }

    /**
     * Mergi la pagina urmatoare in tabel.
     */
    @FXML
    private void handleNext() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            loadData();
        }
    }

    /**
     * Inchide fereastra curenta si opreste muzica de fundal.
     */
    @FXML
    private void goBack() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main-window.fxml"));
            Stage mainStage = new Stage();

            mainStage.setScene(new Scene(loader.load(), 800, 600));

            MainController ctrl = loader.getController();
            ctrl.setServices(service, friendshipService, cardService, messageService, friendRequestService, notificationService, eventService);
            ctrl.setLoggedUser(loggedUser);

            mainStage.setTitle("Main Window");
            mainStage.show();

            Stage currentStage = (Stage) table.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Seteaza utilizatorul logat curent.
     *
     * @param user utilizatorul logat
     */
    public void setLoggedUser(User user) {
        this.loggedUser = user;
    }
}
