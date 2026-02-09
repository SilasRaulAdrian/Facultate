package org.example.ui;

import org.example.event.RaceEvent;
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
            System.out.println("2. Update User");
            System.out.println("3. Remove User");
            System.out.println("4. Show User by ID");
            System.out.println("5. Show All Users");
            System.out.println("0. Back");
            System.out.print("Select option: ");

            int option = sc.nextInt();
            try {
                switch (option) {
                    case 1 -> addUser();
                    case 2 -> updateUser();
                    case 3 -> removeUser();
                    case 4 -> showUserById();
                    case 5 -> showAllUsers();
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
            System.out.println("2. Update Friendship");
            System.out.println("3. Remove Friendship");
            System.out.println("4. Show Friendship by ID");
            System.out.println("5. Show All Friendships");
            System.out.println("6. Show Number of Communities");
            System.out.println("7. Show Most Sociable Community");
            System.out.println("0. Back");
            System.out.print("Select option: ");

            int option = sc.nextInt();
            try {
                switch (option) {
                    case 1 -> addFriendship();
                    case 2 -> updateFriendship();
                    case 3 -> removeFriendship();
                    case 4 -> showFriendshipById();
                    case 5 -> showAllFriendships();
                    case 6 -> showNumberOfCommunities();
                    case 7 -> showMostSociableCommunity();
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
            System.out.println("2. Update Card");
            System.out.println("3. Remove Card");
            System.out.println("4. Add Member to Card");
            System.out.println("5. Remove Member from Card");
            System.out.println("6. Show Card by ID");
            System.out.println("7. Show All Cards");
            System.out.println("8. Show Card Average Performance");
            System.out.println("0. Back");
            System.out.print("Select option: ");

            int option = sc.nextInt();
            try {
                switch (option) {
                    case 1 -> addCard();
                    case 2 -> updateCard();
                    case 3 -> removeCard();
                    case 4 -> addMemberToCard();
                    case 5 -> removeMemberFromCard();
                    case 6 -> showCardById();
                    case 7 -> showAllCards();
                    case 8 -> showCardPerformance();
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
            System.out.println("2. Update Event");
            System.out.println("3. Remove Event");
            System.out.println("4. Show Event by ID");
            System.out.println("5. Show All Events");
            System.out.println("6. Subscribe User to Event");
            System.out.println("7. Unsubscribe User from Event");
            System.out.println("0. Back");
            System.out.print("Select option: ");

            int option = sc.nextInt();
            try {
                switch (option) {
                    case 1 -> createAndStartRaceEvent();
                    case 2 -> updateEvent();
                    case 3 -> removeEvent();
                    case 4 -> showEventById();
                    case 5 -> showAllEvents();
                    case 6 -> subscribeUserToEvent();
                    case 7 -> unsubscribeUserFromEvent();
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
            System.out.print("Tip Rata (FLYING / SWIMMING / FLYING AND SWIMMING): ");
            TipRata tip = TipRata.valueOf(sc.nextLine().trim().toUpperCase().replace(" ", "_"));
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
     * Metoda pentru actualizarea unui utilizator
     */
    private void updateUser() {
        System.out.print("Enter User ID to update: ");
        Long id = sc.nextLong();
        sc.nextLine();

        User existingUser;
        try {
            existingUser = userService.findById(id);
        } catch (Exception e) {
            System.out.println("User not found: " + e.getMessage());
            return;
        }

        System.out.println("Updating user: " + existingUser.getUsername());

        System.out.print("New Username (current: " + existingUser.getUsername() + "): ");
        String username = sc.nextLine().trim();
        if (!username.isEmpty()) existingUser.setUsername(username);

        System.out.print("New Email (current: " + existingUser.getEmail() + "): ");
        String email = sc.nextLine().trim();
        if (!email.isEmpty()) existingUser.setEmail(email);

        System.out.print("New Password (current: " + existingUser.getPassword() + "): ");
        String password = sc.nextLine().trim();
        if (!password.isEmpty()) existingUser.setPassword(password);

        if (existingUser instanceof Person p) {
            System.out.print("New Last Name (current: " + p.getNume() + "): ");
            String nume = sc.nextLine().trim();
            if (!nume.isEmpty()) p.setNume(nume);

            System.out.print("New First Name (current: " + p.getPrenume() + "): ");
            String prenume = sc.nextLine().trim();
            if (!prenume.isEmpty()) p.setPrenume(prenume);

            System.out.print("New Birthdate (yyyy-mm-dd, current: " + p.getDataNasterii() + "): ");
            String dataStr = sc.nextLine().trim();
            if (!dataStr.isEmpty()) {
                try {
                    p.setDataNasterii(LocalDate.parse(dataStr));
                } catch (Exception e) {
                    System.out.println("Invalid date, keeping old one.");
                }
            }

            System.out.print("New Occupation (current: " + p.getOcupatie() + "): ");
            String ocupatie = sc.nextLine().trim();
            if (!ocupatie.isEmpty()) p.setOcupatie(ocupatie);

            System.out.print("New Empathy level (current: " + p.getNivelEmpatie() + "): ");
            String empStr = sc.nextLine().trim();
            if (!empStr.isEmpty()) {
                try {
                    p.setNivelEmpatie(Double.parseDouble(empStr));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number, keeping old one.");
                }
            }

        } else if (existingUser instanceof Duck d) {
            System.out.print("New Duck Type (current: " + d.getTip() + ") [FLYING/SWIMMING]: ");
            String tipStr = sc.nextLine().trim();
            if (!tipStr.isEmpty()) {
                try {
                    d.setTip(TipRata.valueOf(tipStr.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid type, keeping old one.");
                }
            }

            System.out.print("New Speed (current: " + d.getViteza() + "): ");
            String vitezaStr = sc.nextLine().trim();
            if (!vitezaStr.isEmpty()) {
                try {
                    d.setViteza(Double.parseDouble(vitezaStr));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number, keeping old one.");
                }
            }

            System.out.print("New Stamina (current: " + d.getRezistenta() + "): ");
            String rezStr = sc.nextLine().trim();
            if (!rezStr.isEmpty()) {
                try {
                    d.setRezistenta(Double.parseDouble(rezStr));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number, keeping old one.");
                }
            }
        }

        userService.modifyUser(id, existingUser);
        System.out.println("User updated successfully.");
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
     * Metoda pentru afisarea unui utilizator dupa ID
     */
    private void showUserById() {
        System.out.print("Enter User ID: ");
        Long id = sc.nextLong();
        User user = userService.findById(id);
        System.out.println("User details: " + user);
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
     * Metoda pentru actualizarea unei prietenii
     */
    private void updateFriendship() {
        System.out.print("Enter Friendship ID to update: ");
        Long friendshipId = sc.nextLong();

        Friendship existingFriendship;
        try {
            existingFriendship = friendshipService.findFriendshipById(friendshipId);
        } catch (Exception e) {
            System.out.println("Friendship not found: " + e.getMessage());
            return;
        }

        System.out.println("Current friendship: " + existingFriendship.getU1().getUsername() +
                " <-> " + existingFriendship.getU2().getUsername());

        System.out.print("New User ID 1: ");
        Long newId1 = sc.nextLong();
        System.out.print("New User ID 2: ");
        Long newId2 = sc.nextLong();

        friendshipService.updateFriendship(friendshipId, newId1, newId2);
        System.out.println("Friendship updated successfully.");
    }

    /**
     * Metoda pentru eliminarea unei prietenii
     */
    private void removeFriendship() {
        System.out.print("Friendship ID: ");
        Long friendshipId = sc.nextLong();

        friendshipService.removeFriendship(friendshipId);
        System.out.println("Friendship removed.");
    }

    /**
     * Metoda pentru afisarea unei prietenii dupa ID
     */
    private void showFriendshipById() {
        System.out.print("Enter Friendship ID: ");
        Long id = sc.nextLong();
        Friendship friendship = friendshipService.findFriendshipById(id);
        System.out.println("Friendship details: " + friendship);
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
     * Metoda pentru actualizarea unui card
     */
    private void updateCard() {
        System.out.print("Enter Card ID to update: ");
        Long id = sc.nextLong();
        sc.nextLine();

        Card<? extends Duck> existingCard;
        try {
            existingCard = cardService.findCardById(id);
        } catch (Exception e) {
            System.out.println("Card not found: " + e.getMessage());
            return;
        }

        System.out.println("Updating card: " + existingCard.getNumeCard());

        System.out.print("New Card Name (current: " + existingCard.getNumeCard() + "): ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) {
            existingCard.setNumeCard(name);
        }

        System.out.print("New Card Type (current: " + existingCard.getTip() + ") [SWIMMING/FLYING]: ");
        String typeStr = sc.nextLine().trim();
        if (!typeStr.isEmpty()) {
            existingCard.setTip(typeStr.toUpperCase());
        }

        cardService.updateCard(existingCard);
        System.out.println("Card updated successfully.");
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
     * Metoda pentru afisarea unui card dupa ID
     */
    private void showCardById() {
        System.out.print("Enter Card ID: ");
        Long id = sc.nextLong();
        Card<? extends Duck> card = cardService.findCardById(id);
        System.out.println("Card details: " + card);
        System.out.println("Members: ");
        card.getMembri().forEach(System.out::println);
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
     * Metoda pentru actualizarea unui eveniment
     */
    private void updateEvent() {
        System.out.print("Enter Event ID to update: ");
        Long id = sc.nextLong();

        RaceEvent existingEvent;
        try {
            existingEvent = eventService.findEventById(id);
        } catch (Exception e) {
            System.out.println("Event not found: " + e.getMessage());
            return;
        }

        System.out.println("Updating event with ID: " + existingEvent.getId());
        System.out.print("Number of new lanes: ");
        int n = sc.nextInt();

        List<Lane> newLanes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.print("Lane " + (i + 1) + " distance: ");
            double dist = sc.nextDouble();
            newLanes.add(new Lane(i + 1, dist));
        }

        System.out.print("Update participants? (yes/no): ");
        sc.nextLine();
        String updateParticipants = sc.nextLine().trim().toLowerCase();

        List<SwimmingDuck> ducks = existingEvent.getDucks();
        if (updateParticipants.equals("yes")) {
            System.out.print("Number of duck IDs to add: ");
            int numDucks = sc.nextInt();
            ducks = new ArrayList<>();
            for (int i = 0; i < numDucks; i++) {
                System.out.print("Duck ID " + (i + 1) + ": ");
                Long duckId = sc.nextLong();
                User user = userService.findById(duckId);
                if (user instanceof SwimmingDuck duck) {
                    ducks.add(duck);
                }
            }
        }

        RaceEvent updatedEvent = new RaceEvent(id, ducks, newLanes);
        eventService.updateEvent(updatedEvent);
        System.out.println("Event updated successfully.");
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
     * Metoda pentru afisarea unui eveniment dupa ID
     */
    private void showEventById() {
        System.out.print("Enter Event ID: ");
        Long id = sc.nextLong();
        RaceEvent event = eventService.findEventById(id);
        System.out.println("Event details: " + event);
        System.out.println("Participants: ");
        event.getDucks().forEach(System.out::println);
        System.out.println("Lanes: ");
        event.getLanes().forEach(System.out::println);
    }

    /**
     * Metoda pentru afisarea tuturor evenimentelor
     */
    private void showAllEvents() {
        System.out.println("=== All Events ===");
        eventService.getAllEvents().forEach(System.out::println);
    }

    /**
     * Metoda pentru abonarea unui utilizator la un eveniment
     */
    private void subscribeUserToEvent() {
        System.out.print("Event ID: ");
        Long eventId = sc.nextLong();

        System.out.print("User ID: ");
        Long userId = sc.nextLong();

        eventService.subscribeUser(eventId, userId);
        System.out.println("User subscribed to event.");
    }

    /**
     * Metoda pentru dezabonarea unui utilizator de la un eveniment
     */
    private void unsubscribeUserFromEvent() {
        System.out.print("Event ID: ");
        Long eventId = sc.nextLong();

        System.out.print("User ID: ");
        Long userId = sc.nextLong();

        eventService.unsubscribeUser(eventId, userId);
        System.out.println("User unsubscribed from event.");
    }
}
