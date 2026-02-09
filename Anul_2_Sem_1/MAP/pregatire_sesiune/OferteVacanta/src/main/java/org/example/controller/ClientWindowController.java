package org.example.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.Client;
import org.example.domain.ClientOfferRow;
import org.example.domain.Hotel;
import org.example.observer.Observer;
import org.example.observer.VacantaEvent;
import org.example.observer.VacantaEventType;
import org.example.service.VacantaService;

import java.time.LocalDate;

import static javafx.collections.FXCollections.observableArrayList;

public class ClientWindowController implements Observer<VacantaEvent> {
    private final VacantaService service;
    private final long clientId;
    private final Stage stage = new Stage();

    private final Label lblHeader = new Label();
    private final Label notification = new Label();

    private final TableView<ClientOfferRow> tblOffers = new TableView<>();

    private final ComboBox<Hotel> cbHotel = new ComboBox<>();
    private final DatePicker dpStart = new DatePicker(LocalDate.now().plusDays(1));
    private final Spinner<Integer> spNights = new Spinner<>(1, 30, 2);

    private final Label err = new Label();

    public ClientWindowController(VacantaService service, long clientId) {
        this.service = service;
        this.clientId = clientId;

        Client c = service.getClient(clientId);
        stage.setTitle("Client: " + c.getName());

        lblHeader.setText("Logged: " + c.getName()
                + " (fidelity=" + c.getFidelityGrade()
                + ", hobby=" + c.getHobby() + ")");

        notification.setStyle("-fx-text-fill: blue;");
        err.setStyle("-fx-text-fill: red;");

        setupOffersTable();
        loadOffers();

        setupReservationForm();

        VBox root = new VBox(12,
                lblHeader,
                new Label("Oferte active: "),
                tblOffers,
                new Separator(),
                new Label("Rezervare:"),
                buildReservationPane(),
                notification,
                err
        );
        root.setPadding(new Insets(10));

        stage.setScene(new Scene(root, 900, 600));

        service.addObserver(this);
        stage.setOnCloseRequest(e -> service.removeObserver(this));
    }

    private void setupOffersTable() {
        TableColumn<ClientOfferRow, String> cH = new TableColumn<>("Hotel");
        cH.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().hotelName()));

        TableColumn<ClientOfferRow, String> cL = new TableColumn<>("Location");
        cL.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().locationName()));

        TableColumn<ClientOfferRow, LocalDate> cS = new TableColumn<>("Start");
        cS.setCellValueFactory(x -> new SimpleObjectProperty<>(x.getValue().startDate()));

        TableColumn<ClientOfferRow, LocalDate> cE = new TableColumn<>("End");
        cE.setCellValueFactory(x -> new SimpleObjectProperty<>(x.getValue().endDate()));

        TableColumn<ClientOfferRow, Number> cP = new TableColumn<>("Percent");
        cP.setCellValueFactory(x -> new SimpleIntegerProperty(x.getValue().percents()));

        tblOffers.getColumns().addAll(cH, cL, cS, cE, cP);
        tblOffers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblOffers.setPrefHeight(280);
    }

    private void loadOffers() {
        tblOffers.setItems(observableArrayList(service.getActiveOffersForClient(clientId)));
    }

    private void setupReservationForm() {
        cbHotel.setItems(observableArrayList(service.getAllHotels()));
        cbHotel.setPrefWidth(300);

        tblOffers.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel == null) return;
            cbHotel.getItems().stream()
                    .filter(h -> h.getHotelName().equals(sel.hotelName()))
                    .findFirst()
                    .ifPresent(h -> cbHotel.getSelectionModel().select(h));
        });

        spNights.setEditable(true);
    }

    private Pane buildReservationPane() {
        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);

        gp.addRow(0, new Label("Hotel:"), cbHotel);
        gp.addRow(1, new Label("Start date:"), dpStart);
        gp.addRow(2, new Label("No. nights:"), spNights);

        Button btnReserve = new Button("Reserve");
        btnReserve.setOnAction(e -> onReserve());
        gp.add(btnReserve, 1, 3);

        return gp;
    }

    private void onReserve() {
        err.setText("");
        notification.setText("");

        Hotel h = cbHotel.getSelectionModel().getSelectedItem();
        if (h == null) {
            err.setText("Selecteaza un hotel.");
            return;
        }

        LocalDate start = dpStart.getValue();
        if (start == null) {
            err.setText("Alege data de inceput.");
            return;
        }
        if (start.isBefore(LocalDate.now())) {
            err.setText("Data de inceput trebuie sa fie azi sau in viitor.");
            return;
        }

        Integer nights = spNights.getValue();
        if (nights == null || nights <= 0) {
            err.setText("Numar nopti invalid.");
            return;
        }

        try {
            service.makeReservation(clientId, h.getHotelId(), start, nights);
            notification.setText("Rezervare facuta la " + h.getHotelName() + " (" + nights + " nopti).");
            loadOffers();
        } catch (Exception ex) {
            err.setText(ex.getMessage());
        }
    }

    @Override
    public void update(VacantaEvent event) {
        if (event.type() != VacantaEventType.RESERVATIONS_CHANGED) return;
        if (event.targetClientId() == null || event.targetClientId() != clientId) return;

        Platform.runLater(() -> {
            notification.setText(event.message());
            loadOffers();
        });
    }

    public void show() {
        stage.show();
    }
}
