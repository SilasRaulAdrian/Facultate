package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.controller.LoginController;
import org.example.repository.CarRepository;
import org.example.repository.Db;
import org.example.repository.UserRepository;
import org.example.service.CarsService;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Db db = new Db("jdbc:postgresql://localhost:5432/masini",
                "postgres",
                "root");

        CarRepository carRepo = new CarRepository(db);
        UserRepository userRepo = new UserRepository(db);

        CarsService carsService = new CarsService(userRepo, carRepo);

        LoginController loginController = new LoginController(carsService);
        loginController.show();
    }
}