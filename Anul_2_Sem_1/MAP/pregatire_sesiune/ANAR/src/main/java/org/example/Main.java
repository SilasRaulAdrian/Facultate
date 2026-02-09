package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.controller.AlertsController;
import org.example.controller.RiversController;
import org.example.repository.Db;
import org.example.repository.LocalityRepository;
import org.example.repository.RiverRepository;
import org.example.service.AnarService;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Db db = new Db("jdbc:postgresql://localhost:5432/anar",
                "postgres",
                "root");

        RiverRepository riverRepo = new RiverRepository(db);
        LocalityRepository locRepo = new LocalityRepository(db);

        AnarService service = new AnarService(riverRepo, locRepo);

        RiversController riversCtrl = new RiversController(service);
        riversCtrl.show();
        AlertsController alertsCtrl = new AlertsController(service);
        alertsCtrl.show();
    }
}