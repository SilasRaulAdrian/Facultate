package org.example.controller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.Sectie;
import org.example.service.ClinicService;


import static javafx.collections.FXCollections.observableArrayList;

public class SectieController {
    private final ClinicService service;
    private final Stage stage = new Stage();

    private final ListView<Sectie> list = new ListView<>();

    public SectieController(ClinicService service) {
        this.service = service;
        stage.setTitle("Sectii");

        Button open = new Button("Programeaza consultatie");
        open.setOnAction(e -> onOpenSchedule());

        VBox root = new VBox(10,
                new Label("Sectii (selecteaza una):"),
                list,
                open
        );
        root.setPadding(new Insets(10));
        stage.setScene(new Scene(root, 450, 450));

        list.setItems(observableArrayList(service.getSectii()));
    }

    private void onOpenSchedule() {
        Sectie s = list.getSelectionModel().getSelectedItem();
        if (s == null) {
            new Alert(Alert.AlertType.WARNING, "Selecteaza o sectie!").showAndWait();
            return;
        }
        new ProgramareController(service, s).show();
    }

    public void show() {
        stage.show();
    }
}
