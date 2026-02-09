package org.example.controllers;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.model.Friendship;
import org.example.model.User;
import org.example.model.paging.Page;
import org.example.model.paging.Pageable;
import org.example.observer.Observer;
import org.example.observer.Signal;
import org.example.service.*;

/**
 * Controller pentru fereastra de gestionare a prieteniilor.
 */
public class FriendshipsController implements Observer {
    @FXML private TableView<Friendship> table;
    @FXML private TableColumn<Friendship, Long> colId;
    @FXML private TableColumn<Friendship, Long> colUser1Id;
    @FXML private TableColumn<Friendship, String> colUser1Name;
    @FXML private TableColumn<Friendship, Long> colUser2Id;
    @FXML private TableColumn<Friendship, String> colUser2Name;

    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label pageInfoLabel;

    private final ObservableList<Friendship> model = FXCollections.observableArrayList();

    private FriendshipService friendshipService;
    private UserService userService;
    private CardService cardService;
    private MessageService messageService;
    private FriendRequestService friendRequestService;
    private NotificationService notificationService;
    private EventService eventService;
    private User loggedUser;

    private int currentPage = 0;
    private int totalPages = 0;
    private static final int PAGE_SIZE = 5;

    /**
     * Seteaza service-urile necesare si initializeaza tabela.
     *
     * @param fs service-ul de prietenii
     * @param us service-ul de utilizatori
     * @param cs service-ul de carduri
     * @param ms service-ul de mesaje
     * @param frs service-ul de cereri de prietenie
     * @param ns service-ul de notificari
     * @param es service-ul de evenimente
     */
    public void setServices(FriendshipService fs, UserService us, CardService cs, MessageService ms, FriendRequestService frs, NotificationService ns, EventService es) {
        this.friendshipService = fs;
        this.userService = us;
        this.cardService = cs;
        this.messageService = ms;
        this.friendRequestService = frs;
        this.notificationService = ns;
        this.eventService = es;

        friendshipService.addObserver(this);
        initTable();
        loadData();
        deselectRowInTable();
    }

    /**
     * Initializeaza tabela cu datele prietenilor.
     */
    private void initTable() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colId.setCellValueFactory(c -> new SimpleLongProperty(c.getValue().getId()).asObject());
        colUser1Id.setCellValueFactory(c -> new SimpleLongProperty(c.getValue().getU1().getId()).asObject());
        colUser1Name.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getU1().getUsername()));
        colUser2Id.setCellValueFactory(c -> new SimpleLongProperty(c.getValue().getU2().getId()).asObject());
        colUser2Name.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getU2().getUsername()));

        table.setItems(model);
    }

    /**
     * Incarca datele prietenilor in tabela.
     */
    private void loadData() {
        Pageable pageable = new Pageable(currentPage, PAGE_SIZE);
        Page<Friendship> page = friendshipService.findAllOnPage(pageable);

        int totalElements = page.getElementCount();
        totalPages = (int) Math.ceil((double) totalElements / PAGE_SIZE);

        if (currentPage >= totalPages && totalPages > 0) {
            currentPage = totalPages - 1;
            loadData();
            return;
        }

        model.setAll(page.getElementsOnPage());
        updatePaginationControls();
    }

    /**
     * Deselecteaza randul selectat in tabela atunci cand se face click in afara acesteia.
     */
    private void deselectRowInTable() {
        table.getScene().getRoot().setOnMousePressed(e -> {
            if (!table.isHover()) {
                table.getSelectionModel().clearSelection();
            }
        });
    }

    /**
     * Actualizeaza controalele de paginare in functie de pagina curenta.
     */
    private void updatePaginationControls() {
        prevButton.setDisable(currentPage == 0);
        nextButton.setDisable(currentPage >= totalPages - 1);

        if (totalPages == 0)
            pageInfoLabel.setText("No Results");
        else
            pageInfoLabel.setText("Page " + (currentPage + 1) + " of " + totalPages);
    }

    /**
     * Mergi la pagina anterioara.
     */
    @FXML private void handlePrevious() {
        if (currentPage > 0) {
            currentPage--;
            loadData();
        }
    }

    /**
     * Mergi la pagina urmatoare.
     */
    @FXML private void handleNext() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            loadData();
        }
    }

    /**
     * Sterge prietenia selectata din tabela.
     */
    @FXML private void deleteSelected() {
        Friendship f = table.getSelectionModel().getSelectedItem();
        if (f == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No user selected");
            alert.setContentText("Please select a user before deleting.");
            alert.showAndWait();
            return;
        }

        friendshipService.removeFriendshipByAdmin(f.getId());
    }

    /**
     * Deschide fereastra pentru adaugarea unei noi prietenii.
     */
    @FXML private void openAddWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/add-friendship-window.fxml"));
        Stage st = new Stage();

        st.setScene(new Scene(loader.load(), 300, 200));

        AddFriendshipController ctrl = loader.getController();
        ctrl.setServices(friendshipService, st);

        st.setTitle("Add Friendship");
        st.show();
    }

    /**
     * Actualizeaza tabela atunci cand apare un semnal de schimbare.
     */
    @Override
    public void update(Signal signal) {
        currentPage = 0;
        loadData();
    }

    /**
     * Afiseaza numarul de comunitati.
     */
    @FXML
    private void handleNumberOfCommunities() {
        int nr = friendshipService.numberOfCommunities();
        new Alert(Alert.AlertType.INFORMATION,
                "Number of communities: " + nr).show();
    }

    /**
     * Afiseaza cea mai sociabila comunitate.
     */
    @FXML
    private void handleMostSociableCommunity() {
        var community = friendshipService.mostSociableCommunity();

        if (community.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION,
                    "No communities found.").show();
            return;
        }

        StringBuilder sb = new StringBuilder("Most sociable community:\n\n");
        community.forEach(u -> sb.append("- ")
                .append(u.getUsername())
                .append(" (ID: ").append(u.getId()).append(")\n"));

        new Alert(Alert.AlertType.INFORMATION, sb.toString()).show();
    }

    /**
     * Inchide fereastra curenta.
     */
    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main-window.fxml"));
            Stage mainStage = new Stage();

            mainStage.setScene(new Scene(loader.load(), 800, 600));

            MainController ctrl = loader.getController();
            ctrl.setServices(userService, friendshipService, cardService, messageService, friendRequestService, notificationService, eventService);
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
     * Seteaza utilizatorul logat.
     *
     * @param user utilizatorul logat
     */
    public void setLoggedUser(User user) {
        this.loggedUser = user;
    }
}
