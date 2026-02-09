package org.example.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.model.Person;
import org.example.model.User;
import org.example.model.paging.Page;
import org.example.model.paging.Pageable;
import org.example.observer.Observer;
import org.example.observer.Signal;
import org.example.service.*;

import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * Controller pentru fereastra de gestionare a persoanelor.
 */
public class PersonsController implements Observer {
    @FXML private TableView<User> table;
    @FXML private TableColumn<User, Long> colId;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<Person, String> colNume;
    @FXML private TableColumn<Person, String> colPrenume;
    @FXML private TableColumn<Person, String> colOcupatie;
    @FXML private TableColumn<Person, Double> colEmpatie;
    @FXML private TableColumn<Person, LocalDate> colData;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label pageInfoLabel;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 5;
    private int totalPages = 0;

    private final ObservableList<User> model = FXCollections.observableArrayList();
    private UserService service;
    private FriendshipService friendshipService;
    private CardService cardService;
    private MessageService messageService;
    private FriendRequestService friendRequestService;
    private NotificationService notificationService;
    private EventService eventService;
    private User loggedUser;

    /**
     * Seteaza service-ul utilizator si initializeaza componentele UI.
     *
     * @param service service-ul utilizator
     * @param fs service-ul prietenii
     * @param cs service-ul carduri
     * @param ms service-ul mesaje
     * @param frs service-ul cereri de prietenie
     * @param ns service-ul notificari
     * @param es service-ul evenimente
     */
    public void setService(UserService service, FriendshipService fs, CardService cs, MessageService ms, FriendRequestService frs, NotificationService ns, EventService es) {
        this.service = service;
        this.friendshipService = fs;
        this.cardService = cs;
        this.messageService = ms;
        this.friendRequestService = frs;
        this.notificationService = ns;
        this.eventService = es;

        service.addObserver(this);
        initTable();
        loadData();
        deselectRowInTable();
    }

    /**
     * Initializeaza tabela cu datele utilizatorilor.
     */
    private void initTable() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colId.setCellValueFactory(c -> new SimpleLongProperty(c.getValue().getId()).asObject());
        colUsername.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUsername()));
        colEmail.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));
        colNume.setCellValueFactory(c -> new SimpleStringProperty(((Person)c.getValue()).getNume()));
        colPrenume.setCellValueFactory(c -> new SimpleStringProperty(((Person)c.getValue()).getPrenume()));
        colOcupatie.setCellValueFactory(c -> new SimpleStringProperty(((Person)c.getValue()).getOcupatie()));
        colEmpatie.setCellValueFactory(c -> new SimpleDoubleProperty(((Person)c.getValue()).getNivelEmpatie()).asObject());
        colData.setCellValueFactory(c -> new SimpleObjectProperty<LocalDate>(((Person)c.getValue()).getDataNasterii()));

        table.setItems(model);
    }

    /**
     * Incarca datele utilizatorilor in tabela.
     */
    private void loadData() {
        Pageable pageable = new Pageable(currentPage, PAGE_SIZE);

        Page<Person> page = service.findAllPersonsOnPage(pageable);

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
     * Sterge utilizatorul selectat din tabela.
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
     * Deschide fereastra pentru adaugarea unei noi persoane.
     *
     * @throws Exception daca apare o eroare la incarcarea ferestrei
     */
    @FXML
    private void openAddWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/add-person-window.fxml"));
        Stage st = new Stage();

        double width = 350;
        double height = 420;

        st.setScene(new Scene(loader.load(), width, height));

        AddPersonController ctrl = loader.getController();
        ctrl.setServices(service, st);

        st.setTitle("Add Person");
        st.show();
    }

    /**
     * Mergi la pagina anterioara.
     */
    @FXML
    private void handlePrevious() {
        if (currentPage > 0) {
            currentPage--;
            loadData();
        }
    }

    /**
     * Mergi la pagina urmatoare.
     */
    @FXML
    private void handleNext() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            loadData();
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
     * Seteaza utilizatorul logat.
     *
     * @param user utilizatorul logat
     */
    public void setLoggedUser(User user) {
        this.loggedUser = user;
    }
}
