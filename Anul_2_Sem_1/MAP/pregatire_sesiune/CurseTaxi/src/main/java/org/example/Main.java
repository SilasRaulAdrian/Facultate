package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.controller.DispatcherController;
import org.example.controller.DriverController;
import org.example.repository.Db;
import org.example.repository.DriverRepository;
import org.example.repository.OrderRepository;
import org.example.service.TaxiService;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Db db = new Db("jdbc:postgresql://localhost:5432/cursetaxi",
                "postgres",
                "root");

        DriverRepository driverRepo = new DriverRepository(db);
        OrderRepository orderRepo = new OrderRepository(db);
        TaxiService service = new TaxiService(driverRepo, orderRepo);

        DispatcherController dispatcher = new DispatcherController(service);
        dispatcher.show();

        service.getDrivers().forEach(d -> new DriverController(service, d).show());
    }
}