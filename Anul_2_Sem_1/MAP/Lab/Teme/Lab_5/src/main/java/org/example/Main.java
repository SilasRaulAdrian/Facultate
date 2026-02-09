package org.example;

import org.example.repository.CardRepository;
import org.example.repository.EventRepository;
import org.example.repository.InMemoryRepository;
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
        InMemoryRepository repo = new InMemoryRepository("users.csv", "friendships.csv");
        CardRepository cardRepo = new CardRepository("cards.csv");
        EventRepository eventRepo = new EventRepository("events.csv");

        UserService userService = new UserService(repo);
        CardService cardService = new CardService(cardRepo, repo);
        EventService eventService = new EventService(repo, eventRepo);
        FriendshipService friendshipService = new FriendshipService(repo);

        ConsoleUI console = new ConsoleUI(userService, friendshipService, cardService, eventService);
        console.start();
    }
}