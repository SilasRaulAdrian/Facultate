package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.controller.LoginRegisterController;
import org.example.repository.NevoieFileRepository;
import org.example.repository.PersoanaFileRepository;
import org.example.service.AppService;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        var persoanaRepo = new PersoanaFileRepository("persoane.csv");
        var nevoieRepo = new NevoieFileRepository("nevoi.csv");

        var service = new AppService(persoanaRepo, nevoieRepo);

        LoginRegisterController ctrl = new LoginRegisterController(service);
        ctrl.show();
    }
}