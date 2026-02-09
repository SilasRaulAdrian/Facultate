package org.example.repository;

import org.example.model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clasa pentru gestionarea depozitului de carduri
 */
public class CardRepository {
    private final Map<Long, Card<Duck>> cards = new HashMap<>();
    private Long nextId = 1L;
    private final String cardsFile;

    /**
     * Constructor pentru clasa CardRepository
     * @param cardsFile String, numele fisierului dedicat cardurilor
     */
    public CardRepository(String cardsFile) {
        this.cardsFile = cardsFile;
    }

    /**
     * Metoda pentru adaugarea unui card in depozit
     * @param card Card<Duck>, cardul care va fi adaugat
     */
    public void addCard(Card<? extends Duck> card) {
        if (card.getId() == null) {
            Long newId = nextId();
            card.setId(newId);
        } else if (card.getId() >= nextId) {
            nextId = card.getId() + 1;
        }

        if (card.getId() >= nextId) {
            nextId = card.getId() + 1;
        }

        cards.put(card.getId(), (Card<Duck>) card);
        saveCards(getAllCards());
    }

    /**
     * Metoda pentru eliminarea unui card din depozit dupa id
     * @param id Long, id-ul cardului care va fi eliminat
     */
    public void removeCard(Long id) {
        cards.remove(id);
        saveCards(getAllCards());
    }

    /**
     * Metoda pentru gasirea unui card dupa id
     * @param id Long, id-ul cardului care va fi gasit
     * @return Card<Duck>, cardul gasit sau null daca nu exista
     */
    public Card<Duck> findById(Long id) {
        return cards.get(id);
    }

    /**
     * Metoda pentru obtinerea tuturor cardurilor din depozit
     *
     * @return List<Card<Duck>>, lista de carduri
     */
    public List<Card<Duck>> getAllCards() {
        return new ArrayList<>(cards.values());
    }

    /**
     * Metoda pentru generarea urmatorului id unic
     * @return Long, urmatorul id unic
     */
    public Long nextId() {
        Long candidateId = 1L;

        while (cards.containsKey(candidateId)) {
            candidateId++;
        }

        if (candidateId > nextId) {
            nextId = candidateId;
        }

        return candidateId;
    }

    /**
     * Incarca cardurile din fisier
     * @param users List<User>, lista de utilizatori pentru a asocia membrii cardurilor
     * @return List<Card<Duck>>, lista de carduri incarcate
     */
    public List<Card<? extends Duck>> loadCards(List<User> users) {
        List<Card<? extends Duck>> cards = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(cardsFile))) {
            br.readLine(); // header
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                Long id = Long.parseLong(parts[0]);
                String numeCard = parts[1].trim();
                String tipCard = parts[2].trim();
                String[] memberUsernames = parts[3].split("\\|");

                Card<? extends Duck> card;

                if (tipCard.equalsIgnoreCase("SWIMMING")) {
                    card = new SwimmingCard(id, numeCard, tipCard);
                } else if (tipCard.equalsIgnoreCase("FLYING")) {
                    card = new FlyingCard(id, numeCard, tipCard);
                } else {
                    System.out.println("Tip necunoscut: " + tipCard);
                    continue;
                }

                for (String username : memberUsernames) {
                    users.stream()
                            .filter(u -> u instanceof Duck && u.getUsername().equals(username))
                            .findFirst()
                            .ifPresent(u -> {
                                ((Card<Duck>) card).getMembri().add((Duck) u);
                                ((Duck) u).setCard(card);
                            });
                }

                cards.add(card);
            }
        } catch (Exception e) {
            System.out.println("Error loading cards: " + e.getMessage());
        }

        return cards;
    }

    /**
     * Salveaza cardurile in fisier
     * @param cards List<Card<Duck>>, lista de carduri de salvat
     */
    public void saveCards(List<Card<Duck>> cards) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(cardsFile))) {
            pw.println("ID,NAME,TIP,MEMBERS");
            for (Card<Duck> c : cards) {
                String membri = String.join("|",
                        c.getMembri().stream()
                                .map(Duck::getUsername)
                                .toList());
                pw.printf("%d,%s,%s,%s%n", c.getId(), c.getNumeCard(), c.getTip(), membri);
            }
        } catch (Exception e) {
            System.out.println("Error saving cards: " + e.getMessage());
        }
    }
}
