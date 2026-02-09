package org.example.controller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.Medic;
import org.example.domain.Sectie;
import org.example.service.ClinicService;

import java.time.LocalDate;
import java.time.LocalTime;

import static javafx.collections.FXCollections.observableArrayList;

public class ProgramareController {
    private final ClinicService service;
    private final Sectie sectie;
    private final Stage stage = new Stage();

    public ProgramareController(ClinicService service, Sectie sectie) {
        this.service = service;
        this.sectie = sectie;

        stage.setTitle("Programare - " + sectie.getNume());

        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(10));

        ComboBox<Medic> cbMedic = new ComboBox<>();
        cbMedic.setItems(observableArrayList(service.getMediciBySectie(sectie.getId())));
        cbMedic.setPrefWidth(250);

        TextField tfCnp = new TextField();
        TextField tfNume = new TextField();

        DatePicker dp = new DatePicker(LocalDate.now().plusDays(1));

        Spinner<Integer> spHour = new Spinner<>(0, 23, 10);
        Spinner<Integer> spMin = new Spinner<>(0, 59, 0);
        spHour.setEditable(true);
        spMin.setEditable(true);

        Label err = new Label();
        err.setStyle("-fx-text-fill: red;");

        Button save = new Button("Programeaza");
        save.setOnAction(e -> {
            err.setText("");
            try {
                Medic m = cbMedic.getValue();
                if (m == null) throw new IllegalArgumentException("Selecteaza un medic.");

                LocalDate date = dp.getValue();
                LocalTime time = LocalTime.of(spHour.getValue(), spMin.getValue());

                service.programeazaConsultatie(
                        sectie.getId(),
                        m.getId(),
                        tfCnp.getText(),
                        tfNume.getText(),
                        date,
                        time
                );

                tfCnp.clear(); tfNume.clear();
                new Alert(Alert.AlertType.INFORMATION, "Programare efectuata!").showAndWait();
            } catch (Exception ex) {
                err.setText(ex.getMessage());
            }
        });

        int r = 0;
        gp.add(new Label("Sectie:"), 0, r);
        gp.add(new Label(sectie.getNume()), 1, r++);

        gp.add(new Label("Medic:"), 0, r);
        gp.add(cbMedic, 1, r++);

        gp.add(new Label("CNP pacient:"), 0, r);
        gp.add(tfCnp, 1, r++);

        gp.add(new Label("Nume pacient:"), 0, r);
        gp.add(tfNume, 1, r++);

        gp.add(new Label("Data:"), 0, r);
        gp.add(dp, 1, r++);

        HBox hbTime = new HBox(8, new Label("Ora:"), spHour, new Label("Min:"), spMin);
        gp.add(hbTime, 1, r++);

        VBox root = new VBox(10, gp, save, err);
        root.setPadding(new Insets(10));
        stage.setScene(new Scene(root, 520, 420));
    }

    public void show() {
        stage.show();
    }
}
