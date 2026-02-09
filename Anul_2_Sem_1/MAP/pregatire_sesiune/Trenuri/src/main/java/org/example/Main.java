package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.controller.MainController;
import org.example.repository.CityRepository;
import org.example.repository.Db;
import org.example.repository.TrainStationRepository;
import org.example.service.TrainService;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Db db = new Db(
                "jdbc:postgresql://localhost:5432/trenuri",
                "postgres",
                "root"
        );

        CityRepository cityRepo = new CityRepository(db);
        TrainStationRepository stationRepo = new TrainStationRepository(db);

        TrainService service = new TrainService(cityRepo, stationRepo);

        new MainController(service).show();
    }
}