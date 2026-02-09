package org.example.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.PlacedOrderRow;
import org.example.observer.Observer;
import org.example.observer.RestaurantEvent;
import org.example.observer.RestaurantEventType;
import org.example.service.RestaurantService;

public class StaffController implements Observer<RestaurantEvent> {
    private final RestaurantService service;
    private final Stage stage = new Stage();

    private final TableView<PlacedOrderRow> placedTable = new TableView<>();

    public StaffController(RestaurantService service) {
        this.service = service;
        service.addObserver(this);

        stage.setTitle("Staff");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        setupPlacedTable();

        root.getChildren().addAll(placedTable);
        stage.setScene(new Scene(root, 900, 450));

        reloadPlaced();
    }

    private void setupPlacedTable() {
        TableColumn<PlacedOrderRow, Number> cTable = new TableColumn<>("TableId");
        cTable.setCellValueFactory(x -> new javafx.beans.property.SimpleIntegerProperty(x.getValue().tableId()));

        TableColumn<PlacedOrderRow, String> cDate = new TableColumn<>("Date");
        cDate.setCellValueFactory(x -> new javafx.beans.property.SimpleStringProperty(x.getValue().date().toString()));

        TableColumn<PlacedOrderRow, String> cProducts = new TableColumn<>("Products");
        cProducts.setCellValueFactory(x -> new javafx.beans.property.SimpleStringProperty(x.getValue().products()));

        placedTable.getColumns().addAll(cTable, cDate, cProducts);
    }

    private void reloadPlaced() {
        var rows = service.getPlacedOrdersRows();
        placedTable.setItems(FXCollections.observableArrayList(rows));
    }

    @Override
    public void update(RestaurantEvent event) {
        if (event.type() == RestaurantEventType.ORDER_PLACED) {
            reloadPlaced();
        }
    }

    public void show() {
        stage.show();
    }
}
