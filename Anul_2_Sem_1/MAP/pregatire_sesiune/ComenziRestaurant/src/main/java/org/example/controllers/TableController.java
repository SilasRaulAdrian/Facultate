package org.example.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.MenuItem;
import org.example.domain.Table;
import org.example.service.RestaurantService;

import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;

public class TableController {
    private final RestaurantService service;
    private final Table table;
    private final Stage stage = new Stage();

    private final TableView<MenuItem> menuTable = new TableView<>();
    private final Label err = new Label();

    public TableController(RestaurantService service, Table table) {
        this.service = service;
        this.table = table;

        stage.setTitle("Table" + table.getId());
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        setupMenuTable();

        Button place = new Button("Place Order");
        place.setOnAction(e -> onPlaceOrder());

        err.setStyle("-fx-text-fill: red");

        root.getChildren().addAll(menuTable, place, err);
        stage.setScene(new Scene(root, 800, 450));

        loadMenu();
    }

    private void setupMenuTable() {
        menuTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TableColumn<MenuItem, String> cItem = new TableColumn<>("Item");
        cItem.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getItem()));

        TableColumn<MenuItem, String> cCat = new TableColumn<>("Category");
        cCat.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getCategory()));

        TableColumn<MenuItem, Number> cPrice = new TableColumn<>("Price");
        cPrice.setCellValueFactory(x -> new SimpleDoubleProperty(x.getValue().getPrice()));

        TableColumn<MenuItem, String> cCur = new TableColumn<>("Currency");
        cCur.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getCurrency()));

        menuTable.getColumns().addAll(cItem, cCat, cPrice, cCur);
    }

    private void loadMenu() {
        var menu = service.getMenu();
        menuTable.setItems(observableArrayList(menu));
    }

    private void onPlaceOrder() {
        err.setText("");

        List<MenuItem> selected = menuTable.getSelectionModel().getSelectedItems();
        List<Integer> ids = selected.stream().map(MenuItem::getId).toList();

        try {
            service.placeOrder(table.getId(), ids);
            menuTable.getSelectionModel().clearSelection();
        } catch (Exception ex) {
            err.setText(ex.getMessage());
        }
    }

    public void show() {
        stage.show();
    }
}
