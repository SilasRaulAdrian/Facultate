package org.example.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.Car;
import org.example.domain.User;
import org.example.observer.AppEvent;
import org.example.observer.AppEventType;
import org.example.observer.Observer;
import org.example.service.CarsService;

import static javafx.collections.FXCollections.observableArrayList;

public class DealerController implements Observer<AppEvent> {
    private final CarsService service;
    private final User user;
    private final Stage stage = new Stage();

    private final TableView<Car> table = new TableView<>();
    private final Label lblMsg = new Label();

    public DealerController(CarsService service, User user) {
        this.service = service;
        this.user = user;

        stage.setTitle("DEALER: " + user.getUsername());

        setupTable();
        refresh();

        Button btnSend = new Button("Send for approval");
        btnSend.setOnAction(e -> onSendForApproval());

        HBox actions = new HBox(10, btnSend);

        VBox root = new VBox(10,
                table,
                actions,
                lblMsg
        );

        stage.setScene(new Scene(root, 900, 600));

        service.addObserver(this);
        stage.setOnCloseRequest(e -> service.removeObserver(this));
    }

    private void setupTable() {
        TableColumn<Car, String> cName = new TableColumn<>("Denumire");
        cName.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getName()));

        TableColumn<Car, String> cDesc = new TableColumn<>("Descriere");
        cDesc.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getDescriere()));

        TableColumn<Car, Number> cPrice = new TableColumn<>("Pret");
        cPrice.setCellValueFactory(x -> new SimpleIntegerProperty(x.getValue().getBase_price()));

        TableColumn<Car, String> cStatus = new TableColumn<>("Status");
        cStatus.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getStatus().name()));

        table.getColumns().addAll(cName, cDesc, cPrice, cStatus);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(420);
    }

    private void refresh() {
        table.setItems(observableArrayList(service.getAllCars()));
    }

    @Override
    public void update(AppEvent event) {
        if (event.type() == AppEventType.CARS_CHANGED) {
            Platform.runLater(this::refresh);
        }
        if (event.type() == AppEventType.USER_MESSAGE && event.targetUserId() != null
                && event.targetUserId() == user.getId()) {
            Platform.runLater(() -> lblMsg.setText(event.message()));
        }
    }

    private void onSendForApproval() {
        lblMsg.setText("");

        Car selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMsg.setText("Select a car first.");
            return;
        }

        try {
            service.submitForApproval(selected.getId(), user.getId());
        } catch (Exception e) {
            lblMsg.setText("Error: " + e.getMessage());
        }
    }

    public void show() {
        stage.show();
    }
}
