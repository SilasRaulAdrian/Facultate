package org.example.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
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

public class AdminController implements Observer<AppEvent> {
    private final CarsService service;
    private final User user;
    private final Stage stage = new Stage();

    private final TableView<Car> table = new TableView<>();
    private final Label lblNotif = new Label();
    private final Label lblMsg = new Label();

    public AdminController(CarsService service, User user) {
        this.service = service;
        this.user = user;
        stage.setTitle("Admin - " + user.getUsername());

        setupTable();
        refresh();

        Button btnApprove = new Button("Approve");
        btnApprove.setOnAction(e -> onApprove());
        Button btnReject = new Button("Reject");
        btnReject.setOnAction(e -> onReject());

        HBox actions = new HBox(10, btnApprove, btnReject);

        VBox root = new VBox(10, table, actions, lblNotif, lblMsg);
        root.setPadding(new Insets(10));
        stage.setScene(new Scene(root, 900, 600));

        service.addObserver(this);
        stage.setOnCloseRequest(e -> {service.removeObserver(this);});
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

    public void refresh() {
        table.setItems(observableArrayList(service.getCarsNeedsApproval()));
        lblNotif.setText("Cars needs approval: " + service.countNeedsApproval());
    }

    @Override
    public void update(AppEvent event) {
        if (event.type() == AppEventType.CARS_CHANGED || event.type() == AppEventType.APPROVALS_CHANGED) {
            Platform.runLater(this::refresh);
        }
        if (event.type() == AppEventType.USER_MESSAGE && event.targetUserId() != null
                && event.targetUserId() == user.getId()) {
            Platform.runLater(() -> lblMsg.setText(event.message()));
        }
    }

    private void onApprove() {
        Car selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMsg.setText("Select a car to approve.");
            return;
        }
        service.approveCar(selected.getId());
    }

    private void onReject() {
        Car selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMsg.setText("Select a car to reject.");
            return;
        }
        service.rejectCar(selected.getId());
    }

    public void show() {
        stage.show();
    }
}
