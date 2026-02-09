package org.example.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.Client;
import org.example.domain.FlightRow;
import org.example.observer.FlightEvent;
import org.example.observer.FlightEventType;
import org.example.observer.Observer;
import org.example.service.FlightService;

import java.time.LocalDate;

import static javafx.collections.FXCollections.observableArrayList;

public class FlightsController implements Observer<FlightEvent> {
    private final FlightService service;
    private final Client client;
    private final Stage stage = new Stage();

    // filter UI
    private final ComboBox<String> cbFrom = new ComboBox<>();
    private final ComboBox<String> cbTo = new ComboBox<>();
    private final DatePicker dpDay = new DatePicker();

    private final TableView<FlightRow> tv = new TableView<>();
    private final Label err = new Label();

    // paging
    private final int limit = 5;
    private int currentPage = 1;
    private int totalPages = 1;
    private int totalCount = 0;
    private final Label pageLbl = new Label();

    public FlightsController(FlightService service, Client client) {
        this.service = service;
        this.client = client;

        this.service.addObserver(this);

        stage.setTitle("Flights - logged: " + client.getName() + " (" + client.getUsername() + ")");

        cbFrom.setItems(observableArrayList(service.getFromLocations()));
        cbTo.setItems(observableArrayList(service.getToLocations()));

        setupTable();

        Button btnFilter = new Button("Filter");
        btnFilter.setOnAction(e -> {
            err.setText("");
            currentPage = 1;
            applyFilterAndReload();
        });

        Button btnBuy = new Button("Cumpara");
        btnBuy.setOnAction(e -> onBuy());

        Button btnPrev = new Button("< Prev");
        Button btnNext = new Button("Next >");

        btnPrev.setOnAction(e -> {
            if (currentPage > 1) {
                currentPage--;
                reloadCurrentPage();
            }
        });

        btnNext.setOnAction(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                reloadCurrentPage();
            }
        });

        pageLbl.setStyle("-fx-font-weight:bold;");
        err.setStyle("-fx-text-fill:red;");

        GridPane filters = new GridPane();
        filters.setHgap(10); filters.setVgap(10);

        filters.add(new Label("From:"), 0, 0);
        filters.add(cbFrom, 1, 0);

        filters.add(new Label("To:"), 2, 0);
        filters.add(cbTo, 3, 0);

        filters.add(new Label("Day:"), 4, 0);
        filters.add(dpDay, 5, 0);

        filters.add(btnFilter, 6, 0);

        HBox actions = new HBox(10, btnBuy);
        actions.setAlignment(Pos.CENTER_LEFT);

        HBox paging = new HBox(10, btnPrev, pageLbl, btnNext);
        paging.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(10,
                new Label("Logged user: " + client.getName()),
                filters,
                tv,
                actions,
                paging,
                err
        );
        root.setPadding(new Insets(10));

        stage.setScene(new Scene(root, 1100, 600));
    }

    private void setupTable() {
        TableColumn<FlightRow, Number> cId = new TableColumn<>("flightId");
        cId.setCellValueFactory(x -> new SimpleLongProperty(x.getValue().getFlightId()));

        TableColumn<FlightRow, String> cFrom = new TableColumn<>("from");
        cFrom.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getFrom()));

        TableColumn<FlightRow, String> cTo = new TableColumn<>("to");
        cTo.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getTo()));

        TableColumn<FlightRow, String> cDep = new TableColumn<>("departureTime");
        cDep.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getDepartureTime().toString()));

        TableColumn<FlightRow, String> cLand = new TableColumn<>("landingTime");
        cLand.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getLandingTime().toString()));

        TableColumn<FlightRow, Number> cSeats = new TableColumn<>("seats");
        cSeats.setCellValueFactory(x -> new SimpleIntegerProperty(x.getValue().getSeats()));

        TableColumn<FlightRow, Number> cAvail = new TableColumn<>("availableSeats");
        cAvail.setCellValueFactory(x -> new SimpleIntegerProperty(x.getValue().getAvailableSeats()));

        tv.getColumns().addAll(cId, cFrom, cTo, cDep, cLand, cSeats, cAvail);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void applyFilterAndReload() {
        String from = cbFrom.getValue();
        String to = cbTo.getValue();
        LocalDate day = dpDay.getValue();

        if (from == null || to == null || day == null) {
            err.setText("Selecteaza from, to si ziua.");
            return;
        }

        totalCount = service.countFlights(from, to, day);
        totalPages = Math.max(1, (totalCount + limit - 1) / limit);

        if (currentPage > totalPages) currentPage = totalPages;

        reloadCurrentPage();
    }

    private void reloadCurrentPage() {
        String from = cbFrom.getValue();
        String to = cbTo.getValue();
        LocalDate day = dpDay.getValue();

        if (from == null || to == null || day == null) return;

        int offset = (currentPage - 1) * limit;
        var page = service.getFlightsPage(from, to, day, limit, offset);

        tv.setItems(observableArrayList(page));
        pageLbl.setText("Page " + currentPage + " / " + totalPages + " (total: " + totalCount + ")");
    }

    private void onBuy() {
        err.setText("");
        FlightRow sel = tv.getSelectionModel().getSelectedItem();
        if (sel == null) {
            err.setText("Selecteaza un zbor.");
            return;
        }

        try {
            service.buyTicket(client.getUsername(), sel.getFlightId());
            reloadCurrentPage();
        } catch (Exception ex) {
            err.setText(ex.getMessage());
        }
    }

    @Override
    public void update(FlightEvent e) {
        if (e.type() == FlightEventType.TICKET_BOUGHT) {
            Platform.runLater(this::reloadCurrentPage);
        }
    }

    public void show() {
        stage.show();
    }
}
