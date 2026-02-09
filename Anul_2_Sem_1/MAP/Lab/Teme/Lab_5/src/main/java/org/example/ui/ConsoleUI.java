package org.example.ui;

import org.example.model.*;
import org.example.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Interfata unica de consola pentru gestionarea aplicatiei
 * Organizata pe submeniuri pentru claritate
 */
public class ConsoleUI {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final CardService cardService;
    private final EventService eventService;
    private final Scanner sc = new Scanner(System.in);

    /**
     * Constructor pentru ConsoleUI
     * @param userService serviciul pentru utilizatori
     * @param friendshipService serviciul pentru prietenii
     * @param cardService serviciul pentru carduri
     * @param eventService serviciul pentru evenimente
     */
    public ConsoleUI(UserService userService, FriendshipService friendshipService,
                     CardService cardService, EventService eventService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.cardService = cardService;
        this.eventService = eventService;
    }

    /**
     * Pornirea aplicatiei
     * Meniu principal cu submeniuri pentru fiecare categorie
     */
    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("\n=== DuckSocialNetwork Main Menu ===");
            System.out.println("1. Manage Users");
            System.out.println("2. Manage Friendships");
            System.out.println("3. Manage Cards");
            System.out.println("4. Manage Events");
            System.out.println("0. Exit");
            System.out.print("Select option: ");

            int option = sc.nextInt();

            switch (option) {
                case 1 -> usersMenu();
                case 2 -> friendshipsMenu();
                case 3 -> cardsMenu();
                case 4 -> eventsMenu();
                case 0 -> running = false;
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    // ==================== USERS MENU ====================

    /**
     * Submeniu pentru gestionarea utilizatorilor
     */
    private void usersMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Users Menu ===");
            System.out.println("1. Add User");
            System.out.println("2. Remove User");
            System.out.println("3. Show All Users");
            System.out.println("0. Back");
            System.out.print("Select option: ");

            int option = sc.nextInt();
            try {
                switch (option) {
                    case 1 -> addUser();
                    case 2 -> removeUser();
                    case 3 -> showAllUsers();
                    case 0 -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ==================== FRIENDSHIPS MENU ====================

    /**
     * Submeniu pentru gestionarea prieteniilor
     */
    private void friendshipsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Friendships Menu ===");
            System.out.println("1. Add Friendship");
            System.out.println("2. Remove Friendship");
            System.out.println("3. Show All Friendships");
            System.out.println("4. Show Number of Communities");
            System.out.println("5. Show Most Sociable Community");
            System.out.println("0. Back");
            System.out.print("Select option: ");

            int option = sc.nextInt();
            try {
                switch (option) {
                    case 1 -> addFriendship();
                    case 2 -> removeFriendship();
                    case 3 -> showAllFriendships();
                    case 4 -> showNumberOfCommunities();
                    case 5 -> showMostSociableCommunity();
                    case 0 -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ==================== CARDS MENU ====================

    /**
     * Submeniu pentru gestionarea cardurilor
     */
    private void cardsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Cards Menu ===");
            System.out.println("1. Add Card");
            System.out.println("2. Remove Card");
            System.out.println("3. Add Member to Card");
            System.out.println("4. Remove Member from Card");
            System.out.println("5. Show All Cards");
            System.out.println("6. Show Card Average Performance");
            System.out.println("0. Back");
            System.out.print("Select option: ");

            int option = sc.nextInt();
            try {
                switch (option) {
                    case 1 -> addCard();
                    case 2 -> removeCard();
                    case 3 -> addMemberToCard();
                    case 4 -> removeMemberFromCard();
                    case 5 -> showAllCards();
                    case 6 -> showCardPerformance();
                    case 0 -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ==================== EVENTS MENU ====================

    /**
     * Submeniu pentru gestionarea evenimentelor
     */
    private void eventsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Events Menu ===");
            System.out.println("1. Create and Start Race Event");
            System.out.println("2. Remove Event");
            System.out.println("3. Show All Events");
            System.out.println("0. Back");
            System.out.print("Select option: ");

            int option = sc.nextInt();
            try {
                switch (option) {
                    case 1 -> createAndStartRaceEvent();
                    case 2 -> removeEvent();
                    case 3 -> showAllEvents();
                    case 0 -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ==================== USER METHODS ====================

    /**
     * Metoda pentru adaugarea unui utilizator nou
     */
    private void addUser() {
        sc.nextLine();
        System.out.print("Type of User (Person / Duck): ");
        String type = sc.nextLine().trim().toLowerCase();

        if (type.equals("person")) {
            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();
            System.out.print("Nume: ");
            String nume = sc.nextLine();
            System.out.print("Prenume: ");
            String prenume = sc.nextLine();
            System.out.print("Data Nasterii (YYYY-MM-DD): ");
            LocalDate data = LocalDate.parse(sc.nextLine());
            System.out.print("Ocupatie: ");
            String ocupatie = sc.nextLine();
            System.out.print("Nivel Empatie: ");
            double emp = sc.nextDouble();

            userService.addPerson(username, email, password, nume, prenume, data, ocupatie, emp);
            System.out.println("Person added successfully.");
        } else if (type.equals("duck")) {
            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();
            System.out.print("Tip Rata (FLYING / SWIMMING): ");
            TipRata tip = TipRata.valueOf(sc.nextLine().trim().toUpperCase());
            System.out.print("Viteza: ");
            double viteza = sc.nextDouble();
            System.out.print("Rezistenta: ");
            double rezistenta = sc.nextDouble();

            userService.addDuck(username, email, password, tip, viteza, rezistenta);
            System.out.println("Duck added successfully.");
        } else {
            System.out.println("Invalid user type.");
        }
    }

    /**
     * Metoda pentru eliminarea unui utilizator
     */
    private void removeUser() {
        System.out.print("Enter User ID to remove: ");
        Long id = sc.nextLong();
        userService.removeUser(id);
        System.out.println("User removed successfully.");
    }

    /**
     * Metoda pentru afisarea tuturor utilizatorilor
     */
    private void showAllUsers() {
        System.out.println("=== All Users ===");
        userService.getAllUsers().forEach(System.out::println);
    }

    // ==================== FRIENDSHIP METHODS ====================

    /**
     * Metoda pentru adaugarea unei prietenii
     */
    private void addFriendship() {
        System.out.print("User ID 1: ");
        Long id1 = sc.nextLong();
        System.out.print("User ID 2: ");
        Long id2 = sc.nextLong();
        friendshipService.addFriendship(id1, id2);
        System.out.println("Friendship added.");
    }

    /**
     * Metoda pentru eliminarea unei prietenii
     */
    private void removeFriendship() {
        System.out.print("User ID 1: ");
        Long id1 = sc.nextLong();
        System.out.print("User ID 2: ");
        Long id2 = sc.nextLong();
        friendshipService.removeFriendship(id1, id2);
        System.out.println("Friendship removed.");
    }

    /**
     * Metoda pentru afisarea tuturor prieteniilor
     */
    private void showAllFriendships() {
        System.out.println("=== All Friendships ===");
        friendshipService.getAllFriendships().forEach(System.out::println);
    }

    /**
     * Metoda pentru afisarea numarului de comunitati
     */
    private void showNumberOfCommunities() {
        System.out.println("Number of communities: " + friendshipService.numberOfCommunities());
    }

    /**
     * Metoda pentru afisarea celei mai sociabile comunitati
     */
    private void showMostSociableCommunity() {
        System.out.println("=== Most Sociable Community ===");
        friendshipService.mostSociableCommunity().forEach(System.out::println);
    }

    // ==================== CARDS METHODS ====================

    /**
     * Metoda pentru adaugarea unui card nou
     */
    private void addCard() {
        sc.nextLine();
        System.out.print("Enter card name: ");
        String name = sc.nextLine();
        System.out.print("Select card type (SWIMMING / FLYING): ");
        String typeStr = sc.nextLine().trim().toUpperCase();
        TipRata type = TipRata.valueOf(typeStr);

        cardService.addCard(name, type);
        System.out.println("Card added successfully.");
    }

    /**
     * Metoda pentru eliminarea unui card
     */
    private void removeCard() {
        System.out.print("Enter card ID: ");
        Long id = sc.nextLong();
        cardService.removeCard(id);
        System.out.println("Card removed successfully.");
    }

    /**
     * Metoda pentru adaugarea unui membru intr-un card
     */
    private void addMemberToCard() {
        System.out.print("Card ID: ");
        Long cardId = sc.nextLong();
        System.out.print("Duck ID: ");
        Long duckId = sc.nextLong();

        Duck duck = (Duck) userService.findById(duckId);
        cardService.addMemberToCard(cardId, duck);
        System.out.println("Member added to card.");
    }

    /**
     * Metoda pentru eliminarea unui membru dintr-un card
     */
    private void removeMemberFromCard() {
        System.out.print("Card ID: ");
        Long cardId = sc.nextLong();
        System.out.print("Duck ID: ");
        Long duckId = sc.nextLong();

        Duck duck = (Duck) userService.findById(duckId);
        cardService.removeMemberFromCard(cardId, duck);
        System.out.println("Member removed from card.");
    }

    /**
     * Metoda pentru afisarea tuturor cardurilor
     */
    private void showAllCards() {
        System.out.println("=== All Cards ===");
        cardService.getAllCards().forEach(System.out::println);
    }

    /**
     * Metoda pentru afisarea performantei medii a unui card
     */
    private void showCardPerformance() {
        System.out.print("Card ID: ");
        Long id = sc.nextLong();
        System.out.println("Average Performance: " + cardService.getPerformantaMedie(id));
    }

    // ==================== EVENTS METHODS ====================

    /**
     * Metoda pentru crearea si pornirea unui eveniment de tip cursa
     */
    private void createAndStartRaceEvent() {
        System.out.print("Number of lanes: ");
        int n = sc.nextInt();

        List<Lane> lanes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.print("Lane " + (i + 1) + " distance: ");
            double dist = sc.nextDouble();
            lanes.add(new Lane(i + 1, dist));
        }

        eventService.createAndStartRace(lanes);
        System.out.println("Race event created successfully.");
    }

    /**
     * Metoda pentru eliminarea unui eveniment
     */
    private void removeEvent() {
        System.out.print("Enter Event ID to remove: ");
        Long id = sc.nextLong();
        eventService.removeRace(id);
        System.out.println("Event removed successfully.");
    }

    /**
     * Metoda pentru afisarea tuturor evenimentelor
     */
    private void showAllEvents() {
        System.out.println("=== All Events ===");
        eventService.getAllEvents().forEach(System.out::println);
    }
}
