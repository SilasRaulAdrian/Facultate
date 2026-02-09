package org.example.model;

/**
 * Clasa pentru cardurile cu rate zburatoare
 */
public class FlyingCard extends Card<Duck> implements Zburator {
    /**
     * Constructor pentru clasa FlyingCard
     * @param id Long, id-ul cardului
     * @param numeCard String, numele cardului
     * @param tip String, tipul cardului
     */
    public FlyingCard(Long id, String numeCard, String tip) {
        super(id, numeCard, tip);
    }

    /**
     * Metoda pentru zburat
     */
    @Override
    public void zboara() {
        System.out.println("Zburatorul din cardul " + getNumeCard() + " zboara.");
    }
}
