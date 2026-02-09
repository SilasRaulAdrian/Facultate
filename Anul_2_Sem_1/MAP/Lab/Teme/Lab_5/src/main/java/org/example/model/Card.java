package org.example.model;

import org.example.repository.CardRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Clasa generica pentru Card
 * @param <T> tipul de rata care extinde Duck (SwimmingDuck, FlyingDuck)
 */
public abstract class Card<T extends Duck> {
    protected Long id;
    protected String numeCard;
    protected String tip;
    protected final List<T> membri = new ArrayList<>();
    protected final CardRepository cardRepo = new CardRepository("cards.csv");

    /**
     * Constructor pentru clasa Card
     * @param id Long, id-ul cardului
     * @param numeCard String, numele cardului
     */
    public Card(Long id, String numeCard, String tip) {
        this.id = id;
        this.numeCard = numeCard;
        this.tip = tip;
    }

    /**
     * Getter pentru id-ul cardului
     * @return Long, id-ul cardului
     */
    public Long getId() {
        return id;
    }

    /**
     * Getter pentru numele cardului
     * @return String, numele cardului
     */
    public String getNumeCard() {
        return numeCard;
    }

    /**
     * Getter pentru lista de membri ai cardului
     * @return List<T>, lista de membri ai cardului
     */
    public List<T> getMembri() {
        return membri;
    }

    /**
     * Getter pentru tipul cardului
     * @return String, tipul cardului
     */
    public String getTip() {
        return tip;
    }

    /**
     * Setter pentru id-ul cardului
     * @param id Long, id-ul cardului
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Metoda pentru adaugarea unui membru in card
     * @param duck T, membrul care va fi adaugat
     */
    public void addMember(T duck) {
        if (!membri.contains(duck)) {
            membri.add(duck);
            duck.setCard(this);
        }
        cardRepo.saveCards(cardRepo.getAllCards());
    }

    /**
     * Metoda pentru eliminarea unui membru din card
     * @param duck T, membrul care va fi eliminat
     */
    public void removeMember(T duck) {
        membri.remove(duck);
        duck.setCard(null);
        cardRepo.saveCards(cardRepo.getAllCards());
    }

    /**
     * Metoda pentru calcularea performantei medii a membrilor din card
     * @return double, performanta medie a membrilor din card
     */
    public double getPerformantaMedie() {
        if (membri.isEmpty()) {
            return 0.0;
        }

        double sumV = 0, sumR = 0;
        for (T duck : membri) {
            sumV += duck.getViteza();
            sumR += duck.getRezistenta();
        }

        double avgV = sumV / membri.size();
        double avgR = sumR / membri.size();

        return (avgV + avgR) / 2;
    }

    /**
     * Suprascrierea metodei toString pentru clasa Card
     * @return String, reprezentarea cardului ca String
     */
    @Override
    public String toString() {
        return "Card{" + id + ", " + numeCard + ", " + tip + ", size=" + membri.size() + "}";
    }
}
