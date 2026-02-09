package org.example.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.Hotel;
import org.example.domain.Location;
import org.example.service.VacantaService;

import static javafx.collections.FXCollections.observableArrayList;

public class MainController {
    private final VacantaService service;
    private final Stage stage = new Stage();

    private final ComboBox<Location> cbLocation = new ComboBox<>();
    private final TableView<Hotel> tblHotels = new TableView<>();

    public MainController(VacantaService service) {
        this.service = service;

        stage.setTitle("Hotels");

        setupHotelsTable();

        Button btnShowOffers = new Button("Show offers for selected hotel");
        btnShowOffers.setOnAction(e -> onShowOffers());

        HBox top = new HBox(10, new Label("Location:"), cbLocation);
        top.setPadding(new Insets(10));

        VBox root = new VBox(10, top, tblHotels, btnShowOffers);
        root.setPadding(new Insets(10));
        stage.setScene(new Scene(root, 900, 500));

        loadLocations();
        cbLocation.setOnAction(e -> loadHotels());
    }

    private void setupHotelsTable() {
        TableColumn<Hotel, String> cName = new TableColumn<>("Hotel");
        cName.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getHotelName()));

        TableColumn<Hotel, Number> cRooms = new TableColumn<>("Rooms");
        cRooms.setCellValueFactory(x -> new SimpleIntegerProperty(x.getValue().getNoRooms()));

        TableColumn<Hotel, Number> cPrice = new TableColumn<>("Price/night");
        cPrice.setCellValueFactory(x -> new SimpleDoubleProperty(x.getValue().getPricePerNight()));

        TableColumn<Hotel, String> cType = new TableColumn<>("Type");
        cType.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getType()));

        tblHotels.getColumns().addAll(cName, cRooms, cPrice, cType);
    }

    private void loadLocations() {
        cbLocation.setItems(observableArrayList(service.getLocations()));
        if (!cbLocation.getItems().isEmpty()) {
            cbLocation.getSelectionModel().select(0);
            loadHotels();
        }
    }

    private void loadHotels() {
        Location loc = cbLocation.getSelectionModel().getSelectedItem();
        if (loc == null) return;
        tblHotels.setItems(observableArrayList(service.getHotelsForLocation(loc.getLocationId())));
    }

    private void onShowOffers() {
        Hotel h = tblHotels.getSelectionModel().getSelectedItem();
        if (h == null) return;

        new HotelOffersController(service, h).show();
    }

    public void show() {
        stage.show();
    }
}
