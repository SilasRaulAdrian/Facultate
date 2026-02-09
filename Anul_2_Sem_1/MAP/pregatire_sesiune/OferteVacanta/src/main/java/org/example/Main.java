package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.controller.ClientWindowController;
import org.example.controller.MainController;
import org.example.repository.*;
import org.example.service.VacantaService;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Db db = new Db("jdbc:postgresql://localhost:5432/ofertevacanta", "postgres", "root");

        var locRepo = new LocationRepository(db);
        var hotelRepo = new HotelRepository(db);
        var offerRepo = new SpecialOfferRepository(db);
        var clientRepo = new ClientRepository(db);
        var resRepo = new ReservationRepository(db);

        var service = new VacantaService(locRepo, hotelRepo, offerRepo, clientRepo, resRepo);

        new MainController(service).show();

        // exemplu: rulezi cu args: 1 2 3
        for (String arg : getParameters().getRaw()) {
            long cid = Long.parseLong(arg);
            new ClientWindowController(service, cid).show();
        }
    }
}