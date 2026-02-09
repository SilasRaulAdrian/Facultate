package org.example.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.BedType;
import org.example.domain.OccupiedBedRow;
import org.example.observer.IcuEvent;
import org.example.observer.IcuEventType;
import org.example.observer.Observer;
import org.example.service.IcuService;

import static javafx.collections.FXCollections.observableArrayList;


public class BedsController implements Observer<IcuEvent> {
    private final IcuService service;
    private final Stage stage = new Stage();

    private final TableView<OccupiedBedRow> tv = new TableView<>();

    private final Label lblOccupied = new Label();
    private final Label lblTic = new Label();
    private final Label lblTim = new Label();
    private final Label lblTiip = new Label();

    public BedsController(IcuService service) {
        this.service = service;
        service.addObserver(this);

        stage.setTitle("Beds");

        setupTable();

        VBox stats = new VBox(5,
                lblOccupied,
                lblTic,
                lblTim,
                lblTiip
        );
        stats.setPadding(new Insets(10));
        stats.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 6; -fx-padding: 10;");

        VBox root = new VBox(10, tv, stats);
        root.setPadding(new Insets(10));

        stage.setScene(new Scene(root, 900, 520));

        reload();
    }

    private void setupTable() {
        TableColumn<OccupiedBedRow, Number> cId = new TableColumn<>("bedId");
        cId.setCellValueFactory(x -> new SimpleIntegerProperty(x.getValue().getBedId()));

        TableColumn<OccupiedBedRow, String> cType = new TableColumn<>("type");
        cType.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getType().name()));

        TableColumn<OccupiedBedRow, Boolean> cVent = new TableColumn<>("ventilation");
        cVent.setCellValueFactory(x -> new SimpleBooleanProperty(x.getValue().isVentilation()));

        TableColumn<OccupiedBedRow, String> cCnp = new TableColumn<>("patientCNP");
        cCnp.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getPatientCnp()));

        TableColumn<OccupiedBedRow, Number> cSev = new TableColumn<>("severity");
        cSev.setCellValueFactory(x -> new SimpleIntegerProperty(x.getValue().getSeverity()));

        TableColumn<OccupiedBedRow, String> cDiag = new TableColumn<>("diagnosis");
        cDiag.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getDiagnosis()));

        tv.getColumns().addAll(cId, cType, cVent, cCnp, cSev, cDiag);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void reload() {
        tv.setItems(observableArrayList(service.getOccupiedBeds()));

        int occ = service.getOccupiedCount();
        var free = service.getFreeByType();

        lblOccupied.setText("Paturi ocupate: " + occ);
        lblTic.setText("TIC " + free.get(BedType.TIC) + " paturi libere");
        lblTim.setText("TIM " + free.get(BedType.TIM) + " paturi libere");
        lblTiip.setText("TIIP " + free.get(BedType.TIIP) + " paturi libere");
    }

    @Override
    public void update(IcuEvent e) {
        if (e.type() == IcuEventType.DATA_CHANGED) {
            Platform.runLater(this::reload);
        }
    }

    public void show() {
        stage.show();
    }
}
