package org.example.model;

/**
 * Clasa pentru cardurile de inotatori
 */
public class SwimmingCard extends Card<Duck> implements Inotator {
    /**
     * Constructor pentru clasa SwimmingCard
     * @param id Long, id-ul cardului
     * @param numeCard String, numele cardului
     * @param tip String, tipul cardului
     */
    public SwimmingCard(Long id, String numeCard, String tip) {
        super(id, numeCard, tip);
    }

    /**
     * Metoda pentru inotat
     */
    @Override
    public void inoata() {
        System.out.println("Inotatorul din cardul " + getNumeCard() + " inoata.");
    }
}
