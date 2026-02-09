package org.example.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.Consultatie;
import org.example.domain.Medic;
import org.example.observer.ClinicEvent;
import org.example.observer.ClinicEventType;
import org.example.observer.Observer;
import org.example.service.ClinicService;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;

public class MedicController implements Observer<ClinicEvent> {
    private final ClinicService service;
    private final Medic medic;
    private final Stage stage = new Stage();

    private final TableView<Consultatie> table = new TableView<>();

    public MedicController(ClinicService service, Medic medic) {
        this.service = service;
        this.medic = medic;

        stage.setTitle("Medic: " + medic.getNume());
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        setupTable();
        root.getChildren().addAll(new Label("Consultatii viitoare:"), table);

        stage.setScene(new Scene(root, 650, 450));

        service.addObserver(this);
        reload();
    }

    private void setupTable() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");

        TableColumn<Consultatie, String> cDate = new TableColumn<>("Data");
        cDate.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getData().format(df)));

        TableColumn<Consultatie, String> cTime = new TableColumn<>("Ora");
        cTime.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getOra().format(tf)));

        TableColumn<Consultatie, String> cCnp = new TableColumn<>("CNP");
        cCnp.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getCnpPacient()));

        TableColumn<Consultatie, String> cName = new TableColumn<>("Pacient");
        cName.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getNumePacient()));

        table.getColumns().addAll(cDate, cTime, cCnp, cName);
    }

    private void reload() {
        List<Consultatie> list = service.getFutureConsultatiiForMedic(medic.getId());
        table.setItems(observableArrayList(list));
    }

    @Override
    public void update(ClinicEvent event) {
        if (event.type() == ClinicEventType.CONSULTATII_CHANGED) {
            Platform.runLater(this::reload);
        }
    }

    public void show() {
        stage.show();
    }
}
