package org.example.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.Hotel;
import org.example.domain.SpecialOffer;
import org.example.service.VacantaService;

import java.time.LocalDate;

import static javafx.collections.FXCollections.observableArrayList;

public class HotelOffersController {
    private final VacantaService service;
    private final Hotel hotel;
    private final Stage stage = new Stage();

    private final DatePicker dpStart = new DatePicker(LocalDate.now().minusDays(10));
    private final DatePicker dpEnd = new DatePicker(LocalDate.now().plusDays(10));
    private final TableView<SpecialOffer> tbl = new TableView<>();
    private final Label err = new Label();

    public HotelOffersController(VacantaService service, Hotel hotel) {
        this.service = service;
        this.hotel = hotel;

        stage.setTitle("Offers - " + hotel.getHotelName());
        err.setStyle("-fx-text-fill: red;");

        setupTable();

        Button btnSearch = new Button("Search");
        btnSearch.setOnAction(e -> load());

        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10);
        form.addRow(0, new Label("Start date:"), dpStart);
        form.addRow(1, new Label("End date:"), dpEnd);
        form.add(btnSearch, 1, 2);

        VBox root = new VBox(10, form, tbl, err);
        root.setPadding(new Insets(10));
        stage.setScene(new Scene(root, 700, 450));

        load();
    }

    private void setupTable() {
        TableColumn<SpecialOffer, LocalDate> cS = new TableColumn<>("Start");
        cS.setCellValueFactory(x -> new SimpleObjectProperty<>(x.getValue().getStartDate()));

        TableColumn<SpecialOffer, LocalDate> cE = new TableColumn<>("End");
        cE.setCellValueFactory(x -> new SimpleObjectProperty<>(x.getValue().getEndDate()));

        TableColumn<SpecialOffer, Number> cP = new TableColumn<>("Percent");
        cP.setCellValueFactory(x -> new SimpleIntegerProperty(x.getValue().getPercents()));

        tbl.getColumns().addAll(cS, cE, cP);
    }

    private void load() {
        err.setText("");
        LocalDate s = dpStart.getValue();
        LocalDate e = dpEnd.getValue();
        if (s == null || e == null || e.isBefore(s)) {
            err.setText("Interval invalid.");
            return;
        }
        tbl.setItems(observableArrayList(service.getOffersForHotelInterval(hotel.getHotelId(), s, e)));
    }

    public void show() {
        stage.show();
    }
}
