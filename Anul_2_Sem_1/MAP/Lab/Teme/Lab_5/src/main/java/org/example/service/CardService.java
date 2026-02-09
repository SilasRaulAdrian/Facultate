package org.example.service;

import org.example.exceptions.CardNotFoundException;
import org.example.exceptions.DuplicateCardException;
import org.example.model.*;
import org.example.repository.CardRepository;
import org.example.repository.InMemoryRepository;
import org.example.validation.CardValidationStrategy;

import java.util.List;

/**
 * Clasa CardService care gestioneaza cardurile de rate
 */
public class CardService {
    private final CardRepository repo;
    private final InMemoryRepository userRepo;
    private final CardValidationStrategy validator = new CardValidationStrategy();

    /**
     * Constructor pentru clasa CardService
     * @param repo CardRepository, repository-ul pentru carduri
     */
    public CardService(CardRepository repo, InMemoryRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
        userRepo.getAllUsers();

        loadCardsFromCsv();
    }

    /**
     * Metoda pentru incarcarea cardurilor din fisier
     */
    private void loadCardsFromCsv() {
        List<Card<? extends Duck>> cards = repo.loadCards(userRepo.getAllUsers());
        for (Card<? extends Duck> card : cards) {
            repo.addCard(card);
        }
    }

    /**
     * Metoda pentru adaugarea unui card nou
     * @param numeCard String, numele cardului
     * @param tip TipRata, tipul cardului (SWIMMING sau FLYING)
     * @throws DuplicateCardException daca exista deja un card cu acelasi nume
     */
    public void addCard(String numeCard, TipRata tip) {
        for (Card<? extends Duck> c : repo.getAllCards()) {
            if (c.getNumeCard().equalsIgnoreCase(numeCard)) {
                throw new DuplicateCardException(numeCard);
            }
        }

        Card<Duck> card;
        if (tip == TipRata.SWIMMING) {
            card = new SwimmingCard(repo.nextId(), numeCard, "SWIMMING");
        } else if (tip == TipRata.FLYING) {
            card = new FlyingCard(repo.nextId(), numeCard, "FLYING");
        } else {
            throw new IllegalArgumentException("Tip de card necunoscut: " + tip);
        }

        validator.validate(card);
        repo.addCard(card);
    }

    /**
     * Metoda pentru eliminarea unui card
     * @param cardId Long, id-ul cardului care va fi eliminat
     * @throws CardNotFoundException daca cardul cu id-ul specificat nu exista
     */
    public void removeCard(Long cardId) {
        Card<? extends Duck> card = repo.findById(cardId);
        if (card == null) {
            throw new CardNotFoundException(cardId);
        }
        validator.validate(card);
        repo.removeCard(cardId);
    }

    /**
     * Metoda pentru adaugarea unui membru intr-un card
     * @param cardId Long, id-ul cardului
     * @param duck Duck, membrul care va fi adaugat
     * @throws CardNotFoundException daca cardul cu id-ul specificat nu exista
     */
    public void addMemberToCard(Long cardId, Duck duck) {
        Card<Duck> card = repo.findById(cardId);
        if (card == null) {
            throw new CardNotFoundException(cardId);
        }
        card.addMember(duck);
        validator.validate(card);
    }

    /**
     * Metoda pentru eliminarea unui membru dintr-un card
     * @param cardId Long, id-ul cardului
     * @param duck Duck, membrul care va fi eliminat
     * @throws CardNotFoundException daca cardul cu id-ul specificat nu exista
     */
    public void removeMemberFromCard(Long cardId, Duck duck) {
        Card<Duck> card = repo.findById(cardId);
        if (card == null) {
            throw new CardNotFoundException(cardId);
        }
        card.removeMember(duck);
        validator.validate(card);
    }

    /**
     * Metoda pentru obtinerea tuturor cardurilor
     * @return List<Card<Duck>>, lista de carduri
     */
    public List<Card<Duck>> getAllCards() {
        return repo.getAllCards();
    }

    /**
     * Metoda pentru obtinerea performantei medii a unui card
     * @param cardId Long, id-ul cardului
     * @return double, performanta medie a cardului
     * @throws CardNotFoundException daca cardul cu id-ul specificat nu exista
     */
    public double getPerformantaMedie(Long cardId) {
        Card<? extends Duck> card = repo.findById(cardId);
        if (card == null) {
            throw new CardNotFoundException(cardId);
        }
        validator.validate(card);
        return card.getPerformantaMedie();
    }
}
