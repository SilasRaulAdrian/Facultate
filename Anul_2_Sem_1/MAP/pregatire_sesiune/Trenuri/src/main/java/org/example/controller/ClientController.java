package org.example.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.City;
import org.example.domain.RouteRow;
import org.example.observer.Observer;
import org.example.observer.TrainEvent;
import org.example.observer.TrainEventType;
import org.example.service.TrainService;

import java.util.List;
import java.util.UUID;

import static javafx.collections.FXCollections.observableArrayList;

public class ClientController implements Observer<TrainEvent> {
    private final TrainService service;
    private final UUID windowId = UUID.randomUUID();

    private final Stage stage = new Stage();

    private final ComboBox<City> cbDep = new ComboBox<>();
    private final ComboBox<City> cbDest = new ComboBox<>();
    private final CheckBox chkDirect = new CheckBox("Direct Routes Only");
    private final Button btnSearch = new Button("Search");

    private final Label lblRealtime = new Label();
    private final Label lblErr = new Label();

    private final TableView<RouteRow> table = new TableView<>();

    public ClientController(TrainService service) {
        this.service = service;
        service.addObserver(this);

        stage.setTitle("Client");

        lblErr.setStyle("-fx-text-fill: red;");
        lblRealtime.setStyle("-fx-font-style: italic;");

        loadCities();

        setupTable();

        btnSearch.setOnAction(e -> onSearch());

        cbDep.setOnAction(e -> onFiltersChangedNoSearch());
        cbDest.setOnAction(e -> onFiltersChangedNoSearch());

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        form.add(new Label("Departure City:"), 0, 0);
        form.add(cbDep, 1, 0);

        form.add(new Label("Destination City:"), 0, 1);
        form.add(cbDest, 1, 1);

        form.add(chkDirect, 1, 2);

        HBox actions = new HBox(10, btnSearch, lblRealtime);

        VBox root = new VBox(10, form, actions, table, lblErr);
        root.setPadding(new Insets(10));

        stage.setScene(new Scene(root, 900, 500));

        stage.setOnCloseRequest(e -> {
            service.closeWindow(windowId);
            service.removeObserver(this);
        });
    }

    private void loadCities() {
        List<City> cities = service.getCities();
        cbDep.setItems(observableArrayList(cities));
        cbDest.setItems(observableArrayList(cities));
    }

    private void setupTable() {
        TableColumn<RouteRow, String> cRoute = new TableColumn<>("Route");
        cRoute.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().routeText()));
        cRoute.setPrefWidth(700);

        TableColumn<RouteRow, Number> cPrice = new TableColumn<>("Price");
        cPrice.setCellValueFactory(x -> new SimpleDoubleProperty(x.getValue().price()));

        table.getColumns().addAll(cRoute, cPrice);
    }

    private void onFiltersChangedNoSearch() {
        City d = cbDep.getValue();
        City t = cbDest.getValue();
        service.updateWindowFilters(windowId, d == null ? null : d.getId(), t == null ? null : t.getId());
        refreshRealtimeLabel();
    }

    private void onSearch() {
        lblErr.setText("");
        City dep = cbDep.getValue();
        City dest = cbDest.getValue();

        if (dep == null || dest == null) {
            lblErr.setText("Selecteaza ambele orase!");
            return;
        }
        if (dep.getId().equals(dest.getId())) {
            lblErr.setText("Orasele trebuie sa fie diferite!");
            return;
        }

        service.updateWindowFilters(windowId, dep.getId(), dest.getId());
        refreshRealtimeLabel();

        boolean directOnly = chkDirect.isSelected();
        List<RouteRow> routes = service.searchRoutes(dep.getId(), dest.getId(), directOnly);

        table.setItems(observableArrayList(routes));
        if (routes.isEmpty()) {
            lblErr.setText("Nu s-a gasit nicio ruta.");
        }
    }

    private void refreshRealtimeLabel() {
        City dep = cbDep.getValue();
        City dest = cbDest.getValue();
        int others = service.countOtherWindows(windowId,
                dep == null ? null : dep.getId(),
                dest == null ? null : dest.getId());

        if (dep == null || dest == null || others == 0) {
            lblRealtime.setText("");
        } else {
            lblRealtime.setText(others + " other user(s) are looking at the same route");
        }
    }

    @Override
    public void update(TrainEvent event) {
        if (event.type() == TrainEventType.FILTERS_CHANGED) {
            Platform.runLater(this::refreshRealtimeLabel);
        }
    }

    public void show() {
        stage.show();
    }
}
