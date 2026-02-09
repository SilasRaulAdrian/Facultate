package org.example.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.Bed;
import org.example.domain.BedType;
import org.example.domain.Patient;
import org.example.observer.IcuEvent;
import org.example.observer.IcuEventType;
import org.example.observer.Observer;
import org.example.service.IcuService;

import java.util.ArrayList;

import static javafx.collections.FXCollections.observableArrayList;

public class WaitingController implements Observer<IcuEvent> {
    private final IcuService service;
    private final Stage stage = new Stage();

    private final TableView<Patient> tvPatients = new TableView<>();
    private final TableView<Bed> tvFreeBeds = new TableView<>();
    private final Label err = new Label();

    private final ComboBox<BedType> cbType = new ComboBox<>();
    private final CheckBox chkVentOnly = new CheckBox("Doar paturi cu ventilatie");

    private final RadioButton rbAny = new RadioButton("Orice pat liber");
    private final RadioButton rbFilter = new RadioButton("Filtreaza dupa tip");

    public WaitingController(IcuService service) {
        this.service = service;
        service.addObserver(this);

        stage.setTitle("Waiting Room");

        setupPatientsTable();
        setupBedsTable();

        cbType.setItems(observableArrayList(BedType.values()));
        cbType.getSelectionModel().select(BedType.TIC);

        ToggleGroup tg = new ToggleGroup();
        rbAny.setToggleGroup(tg);
        rbFilter.setToggleGroup(tg);
        rbFilter.setSelected(true);

        Button btnLoadBeds = new Button("Incarca paturi libere");
        btnLoadBeds.setOnAction(e -> reloadFreeBeds());

        Button btnAssign = new Button("Atribuie pat pacientului selectat");
        btnAssign.setOnAction(e -> onAssign());

        err.setStyle("-fx-text-fill:red;");

        HBox controls = new HBox(10,
                new Label("Tip pat:"), cbType,
                chkVentOnly,
                rbFilter, rbAny,
                btnLoadBeds
        );
        controls.setAlignment(Pos.CENTER_LEFT);

        VBox left = new VBox(10, new Label("Lista asteptare (severity desc)"), tvPatients);
        VBox right = new VBox(10, new Label("Paturi libere"), tvFreeBeds);

        HBox center = new HBox(10, left, right);
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);

        VBox root = new VBox(10, controls, center, btnAssign, err);
        root.setPadding(new Insets(10));

        stage.setScene(new Scene(root, 1100, 600));

        reloadAll();
    }

    private void setupPatientsTable() {
        TableColumn<Patient, String> cCnp = new TableColumn<>("CNP");
        cCnp.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getCnp()));

        TableColumn<Patient, Number> cSev = new TableColumn<>("gravitate");
        cSev.setCellValueFactory(x -> new SimpleIntegerProperty(x.getValue().getSeverity()));

        TableColumn<Patient, String> cDiag = new TableColumn<>("diagnostic");
        cDiag.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getDiagnosis()));

        TableColumn<Patient, Number> cAge = new TableColumn<>("varsta(luni)");
        cAge.setCellValueFactory(x -> new SimpleIntegerProperty(x.getValue().getAgeMonths()));

        TableColumn<Patient, Boolean> cPrem = new TableColumn<>("prematur");
        cPrem.setCellValueFactory(x -> new SimpleBooleanProperty(x.getValue().isPremature()));

        tvPatients.getColumns().addAll(cCnp, cSev, cDiag, cAge, cPrem);
        tvPatients.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void setupBedsTable() {
        TableColumn<Bed, Number> cId = new TableColumn<>("id");
        cId.setCellValueFactory(x -> new SimpleIntegerProperty(x.getValue().getId()));

        TableColumn<Bed, String> cType = new TableColumn<>("type");
        cType.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getType().name()));

        TableColumn<Bed, Boolean> cVent = new TableColumn<>("ventilatie");
        cVent.setCellValueFactory(x -> new SimpleBooleanProperty(x.getValue().isVentilation()));

        tvFreeBeds.getColumns().addAll(cId, cType, cVent);
        tvFreeBeds.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void reloadAll() {
        tvPatients.setItems(observableArrayList(service.getWaitingPatients()));
        reloadFreeBeds();
    }

    private void reloadFreeBeds() {
        BedType type = cbType.getValue();
        Boolean ventOnly = chkVentOnly.isSelected() ? Boolean.TRUE : null;

        if (rbAny.isSelected()) {
            var all = new ArrayList<Bed>();
            for (BedType t : BedType.values()) {
                all.addAll(service.getFreeBeds(t, ventOnly));
            }
            tvFreeBeds.setItems(observableArrayList(all));
        } else {
            tvFreeBeds.setItems(observableArrayList(service.getFreeBeds(type, ventOnly)));
        }
    }

    private void onAssign() {
        err.setText("");

        Patient p = tvPatients.getSelectionModel().getSelectedItem();
        Bed b = tvFreeBeds.getSelectionModel().getSelectedItem();

        if (p == null) {
            err.setText("Selectati un pacient din lista de asteptare!");
            return;
        }

        if (b == null) {
            err.setText("Selectati un pat liber!");
            return;
        }

        try {
            service.assign(p.getCnp(), b.getId());
            tvPatients.getItems().remove(p);
            tvFreeBeds.getItems().remove(b);
        } catch (Exception ex) {
            err.setText(ex.getMessage());
        }
    }

    @Override
    public void update(IcuEvent e) {
        if (e.type() == IcuEventType.DATA_CHANGED) {
            Platform.runLater(this::reloadAll);
        }
    }

    public void show() {
        stage.show();
    }
}
