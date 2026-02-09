package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.example.controllers.StaffController;
import org.example.controllers.TableController;
import org.example.domain.Table;
import org.example.repository.Db;
import org.example.repository.MenuRepository;
import org.example.repository.OrderRepository;
import org.example.repository.TableRepository;
import org.example.service.RestaurantService;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Db db = new Db("jdbc:postgresql://localhost:5432/comenzirestaurant",
                "postgres",
                "root");

        var tableRepo = new TableRepository(db);
        var menuRepo = new MenuRepository(db);
        var orderRepo = new OrderRepository(db);

        var service = new RestaurantService(tableRepo, menuRepo, orderRepo);

        StaffController staff = new StaffController(service);
        staff.show();

        for (Table t : service.getTables()) {
            TableController tc = new TableController(service, t);
            tc.show();
        }
    }
}