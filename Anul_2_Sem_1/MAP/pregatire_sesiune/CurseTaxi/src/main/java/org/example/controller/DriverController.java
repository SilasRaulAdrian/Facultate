package org.example.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.Driver;
import org.example.domain.Order;
import org.example.observer.Observer;
import org.example.observer.TaxiEvent;
import org.example.observer.TaxiEventType;
import org.example.service.TaxiService;

import java.util.List;
import java.util.Optional;

import static javafx.collections.FXCollections.observableArrayList;

public class DriverController implements Observer<TaxiEvent> {
    private final TaxiService service;
    private final Driver driver;
    private final Stage stage = new Stage();

    private final TableView<Order> activeTable = new TableView<>();

    public DriverController(TaxiService service, Driver driver) {
        this.service = service;
        this.driver = driver;
        service.addObserver(this);

        stage.setTitle("Driver: " + driver.getName());

        Label name = new Label(driver.getName());
        name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        setupActiveTable();
        reload();

        VBox root = new VBox(10, name, activeTable);
        root.setPadding(new Insets(10));
        stage.setScene(new Scene(root, 900, 400));
    }

    private void setupActiveTable() {
        TableColumn<Order, String> cClient = new TableColumn<>("Client");
        cClient.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getClientName()));

        TableColumn<Order, String> cPickup = new TableColumn<>("Pickup");
        cPickup.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getPickupAddress()));

        TableColumn<Order, String> cDest = new TableColumn<>("Destination");
        cDest.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getDestinationAddress()));

        TableColumn<Order, String> cStart = new TableColumn<>("Start");
        cStart.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getStartDate().toString()));

        TableColumn<Order, Void> cFinish = new TableColumn<>("Action");
        cFinish.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Finished");
            {
                btn.setOnAction(e -> {
                    Order o = getTableView().getItems().get(getIndex());
                    service.finishOrder(o.getId());
                    reload();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        activeTable.getColumns().addAll(cClient, cPickup, cDest, cStart, cFinish);
    }

    private void reload() {
        List<Order> active = service.getActiveOrdersForDriver(driver.getId());
        activeTable.setItems(observableArrayList(active));
    }

    @Override
    public void update(TaxiEvent event) {
        if (event.type() == TaxiEventType.ORDERS_CHANGED) {
            Platform.runLater(this::reload);
            return;
        }

        if (event.type() == TaxiEventType.ORDER_OFFERED
                && event.targetDriverId() != null
                && event.targetDriverId() == driver.getId()) {

            Order o = event.order();
            Platform.runLater(() -> showOfferDialog(o));
        }
    }

    private void showOfferDialog(Order o) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("New order");
        alert.setHeaderText("New order: " + o.getPickupAddress() + " -> " + o.getDestinationAddress());
        alert.setContentText("Client: " + o.getClientName());

        ButtonType accept = new ButtonType("Accept", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(accept);

        Optional<ButtonType> res = alert.showAndWait();
        if (res.isPresent() && res.get() == accept) {
            service.acceptOrder(o.getId(), driver.getId());
            reload();
        }
    }

    public void show() {
        stage.show();
    }
}
