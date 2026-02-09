package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.controller.MedicController;
import org.example.controller.SectieController;
import org.example.domain.Medic;
import org.example.repository.ConsultatieRepository;
import org.example.repository.Db;
import org.example.repository.MedicRepository;
import org.example.repository.SectieRepository;
import org.example.service.ClinicService;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Db db = new Db("jdbc:postgresql://localhost:5432/clinicaprivata",
                "postgres",
                "root");

        SectieRepository sectieRepo = new SectieRepository(db);
        MedicRepository medicRepo = new MedicRepository(db);
        ConsultatieRepository consultRepo = new ConsultatieRepository(db);

        ClinicService service = new ClinicService(sectieRepo, medicRepo, consultRepo);

        new SectieController(service).show();
        for (Medic m : service.getMedici()) new MedicController(service, m).show();
    }
}