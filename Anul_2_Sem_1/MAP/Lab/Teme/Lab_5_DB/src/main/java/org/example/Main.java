package org.example;

import org.example.config.ConnectionManager;
import org.example.repository.CardRepository;
import org.example.repository.EventRepository;
import org.example.repository.FriendshipRepository;
import org.example.repository.UserRepository;
import org.example.service.CardService;
import org.example.service.EventService;
import org.example.service.FriendshipService;
import org.example.service.UserService;
import org.example.ui.ConsoleUI;

/**
 * Clasa principala pentru aplicatie
 */
public class Main {
    /**
     * Metoda principala pentru rularea aplicatiei
     * @param args argumentele din linia de comanda
     */
    public static void main(String[] args) {
        var url = "jdbc:postgresql://localhost:5432/DuckSocialNetwork";
        var username = "postgres";
        var password = "root";

        ConnectionManager.init(url, username, password);

        UserRepository userRepo = new UserRepository();
        FriendshipRepository friendshipRepo = new FriendshipRepository(userRepo);
        CardRepository cardRepo = new CardRepository(userRepo);
        EventRepository eventRepo = new EventRepository(userRepo);

        UserService userService = new UserService(userRepo, friendshipRepo);
        CardService cardService = new CardService(cardRepo, userRepo);
        EventService eventService = new EventService(userRepo, eventRepo);
        FriendshipService friendshipService = new FriendshipService(userRepo, friendshipRepo);

        ConsoleUI console = new ConsoleUI(userService, friendshipService, cardService, eventService);
        console.start();
    }
}