package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.controller.LoginController;
import org.example.repository.ClientRepository;
import org.example.repository.Db;
import org.example.repository.FlightRepository;
import org.example.repository.TicketRepository;
import org.example.service.FlightService;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Db db = new Db("jdbc:postgresql://localhost:5432/zboruri", "postgres", "root");

        ClientRepository clientRepo = new ClientRepository(db);
        FlightRepository flightRepo = new FlightRepository(db);
        TicketRepository ticketRepo = new TicketRepository(db);

        FlightService service = new FlightService(clientRepo, flightRepo, ticketRepo);

        new LoginController(service).show();
    }
}