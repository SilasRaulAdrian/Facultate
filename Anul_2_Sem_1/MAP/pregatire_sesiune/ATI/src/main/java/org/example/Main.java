package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.controller.BedsController;
import org.example.controller.WaitingController;
import org.example.repository.BedRepository;
import org.example.repository.Db;
import org.example.repository.PatientRepository;
import org.example.service.IcuService;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Db db = new Db("jdbc:postgresql://localhost:5432/ati", "postgres", "root");

        BedRepository bedRepo = new BedRepository(db);
        PatientRepository patientRepo = new PatientRepository(db);

        IcuService service = new IcuService(bedRepo, patientRepo);

        new BedsController(service).show();
        new WaitingController(service).show();
    }
}