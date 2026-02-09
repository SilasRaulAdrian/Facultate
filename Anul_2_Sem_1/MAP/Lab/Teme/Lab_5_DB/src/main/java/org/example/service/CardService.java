package org.example.service;

import org.example.exceptions.CardNotFoundException;
import org.example.exceptions.DuplicateCardException;
import org.example.model.*;
import org.example.repository.CardRepository;
import org.example.repository.UserRepository;
import org.example.validation.CardValidationStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Clasa CardService care gestioneaza cardurile de rate
 */
public class CardService {
    private final CardRepository repo;
    private final UserRepository userRepo;
    private final CardValidationStrategy validator = new CardValidationStrategy();

    /**
     * Constructor pentru clasa CardService
     * @param repo CardRepository, repository-ul pentru carduri
     * @param userRepo UserRepository, repository-ul pentru utilizatori
     */
    public CardService(CardRepository repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    /**
     * Metoda pentru adaugarea unui card nou
     * @param numeCard String, numele cardului
     * @param tip TipRata, tipul cardului (SWIMMING sau FLYING)
     * @throws DuplicateCardException daca exista deja un card cu acelasi nume
     */
    public void addCard(String numeCard, TipRata tip) {
        for (Card<? extends Duck> c : repo.getAll()) {
            if (c.getNumeCard().equalsIgnoreCase(numeCard)) {
                throw new DuplicateCardException(numeCard);
            }
        }

        Card<Duck> card;
        if (tip == TipRata.SWIMMING) {
            card = new SwimmingCard(null, numeCard, "SWIMMING");
        } else if (tip == TipRata.FLYING) {
            card = new FlyingCard(null, numeCard, "FLYING");
        } else {
            throw new IllegalArgumentException("Tip de card necunoscut: " + tip);
        }

        repo.save(card);
        validator.validate(card);
    }

    /**
     * Metoda pentru eliminarea unui card
     * @param cardId Long, id-ul cardului care va fi eliminat
     * @throws CardNotFoundException daca cardul cu id-ul specificat nu exista
     */
    public void removeCard(Long cardId) {
        Card<? extends Duck> card = repo.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));

        validator.validate(card);
        repo.delete(cardId);
    }

    /**
     * Metoda pentru adaugarea unui membru intr-un card
     * @param cardId Long, id-ul cardului
     * @param duck Duck, membrul care va fi adaugat
     * @throws CardNotFoundException daca cardul cu id-ul specificat nu exista
     */
    public void addMemberToCard(Long cardId, Duck duck) {
        Card<? extends Duck> card = repo.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));

        ((Card<Duck>) card).addMember(duck, repo, userRepo);
        duck.setCardId(cardId);
        userRepo.update(duck);
        validator.validate(card);
        repo.update(card);
    }

    /**
     * Metoda pentru eliminarea unui membru dintr-un card
     * @param cardId Long, id-ul cardului
     * @param duck Duck, membrul care va fi eliminat
     * @throws CardNotFoundException daca cardul cu id-ul specificat nu exista
     */
    public void removeMemberFromCard(Long cardId, Duck duck) {
        Card<? extends Duck> card = repo.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));

        ((Card<Duck>) card).removeMember(duck, repo, userRepo);
        duck.setCardId(null);
        userRepo.update(duck);
        validator.validate(card);
        repo.update(card);
    }

    /**
     * Metoda pentru obtinerea tuturor cardurilor
     * @return List<Card<? extends Duck>>, lista de carduri
     */
    public List<Card<? extends Duck>> getAllCards() {
        List<Card<? extends Duck>> list = new ArrayList<>();
        for (Card<? extends Duck> card : repo.getAll()) {
            list.add(card);
        }
        return list;
    }

    /**
     * Metoda pentru obtinerea performantei medii a unui card
     * @param cardId Long, id-ul cardului
     * @return double, performanta medie a cardului
     * @throws CardNotFoundException daca cardul cu id-ul specificat nu exista
     */
    public double getPerformantaMedie(Long cardId) {
        Card<? extends Duck> card = repo.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));

        validator.validate(card);
        return card.getPerformantaMedie();
    }

    /**
     * Metoda pentru gasirea unui card dupa id
     * @param cardId Long, id-ul cardului
     * @return Card<? extends Duck>, cardul gasit
     * @throws CardNotFoundException daca cardul cu id-ul specificat nu exista
     */
    public Card<? extends Duck> findCardById(Long cardId) {
        return repo.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));
    }

    /**
     * Metoda pentru actualizarea unui card
     * @param card Card<? extends Duck>, cardul care va fi actualizat
     * @throws CardNotFoundException daca cardul cu id-ul specificat nu exista
     */
    public void updateCard(Card<? extends Duck> card) {
        if (card.getId() == null || repo.findById(card.getId()).isEmpty()) {
            throw new CardNotFoundException(card.getId());
        }
        validator.validate(card);
        repo.update(card);
    }
}
