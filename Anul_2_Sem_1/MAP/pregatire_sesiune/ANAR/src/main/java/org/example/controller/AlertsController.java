package org.example.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.AlertRow;
import org.example.domain.RiskLevel;
import org.example.observer.AnarEvent;
import org.example.observer.AnarEventType;
import org.example.observer.Observer;
import org.example.service.AnarService;

import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;

public class AlertsController implements Observer<AnarEvent> {
    private final AnarService service;
    private final Stage stage = new Stage();

    private final TableView<AlertRow> tvReduced = new TableView<>();
    private final TableView<AlertRow> tvMedium = new TableView<>();
    private final TableView<AlertRow> tvMajor  = new TableView<>();

    public AlertsController(AnarService service) {
        this.service = service;
        this.service.addObserver(this);

        stage.setTitle("Avertizari");

        setupTable(tvReduced);
        setupTable(tvMedium);
        setupTable(tvMajor);

        reload();

        VBox col1 = new VBox(8, new Label("Risc redus"), tvReduced);
        VBox col2 = new VBox(8, new Label("Risc mediu"), tvMedium);
        VBox col3 = new VBox(8, new Label("Risc major"), tvMajor);

        HBox root = new HBox(10, col1, col2, col3);
        root.setPadding(new Insets(10));
        HBox.setHgrow(col1, Priority.ALWAYS);
        HBox.setHgrow(col2, Priority.ALWAYS);
        HBox.setHgrow(col3, Priority.ALWAYS);

        stage.setScene(new Scene(root, 1100, 500));
    }

    private void setupTable(TableView<AlertRow> tv) {
        tv.getColumns().clear();

        TableColumn<AlertRow, String> cLoc = new TableColumn<>("Localitate");
        cLoc.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getLocality()));

        TableColumn<AlertRow, String> cRiver = new TableColumn<>("Rau");
        cRiver.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getRiver()));

        TableColumn<AlertRow, Number> cLevel = new TableColumn<>("Cota rau");
        cLevel.setCellValueFactory(x -> new SimpleDoubleProperty(x.getValue().getRiverLevel()));

        TableColumn<AlertRow, Number> cCmdr = new TableColumn<>("cmdr");
        cCmdr.setCellValueFactory(x -> new SimpleDoubleProperty(x.getValue().getCmdr()));

        TableColumn<AlertRow, Number> cCma = new TableColumn<>("cma");
        cCma.setCellValueFactory(x -> new SimpleDoubleProperty(x.getValue().getCma()));

        tv.getColumns().addAll(cLoc, cRiver, cLevel, cCmdr, cCma);
    }

    private void reload() {
        List<AlertRow> all = service.computeAlerts();

        tvReduced.setItems(observableArrayList(all.stream().filter(a -> a.getRisk() == RiskLevel.REDUCED).toList()));
        tvMedium.setItems(observableArrayList(all.stream().filter(a -> a.getRisk() == RiskLevel.MEDIUM).toList()));
        tvMajor.setItems(observableArrayList(all.stream().filter(a -> a.getRisk() == RiskLevel.MAJOR).toList()));
    }

    @Override
    public void update(AnarEvent e) {
        if (e.type() == AnarEventType.RIVER_LEVEL_CHANGED) {
            Platform.runLater(this::reload);
        }
    }

    public void show() {
        stage.show();
    }
}
