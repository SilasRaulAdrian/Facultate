package org.example.model;

/**
 * Clasa pentru rata care inoata
 */
public class SwimmingDuck extends Duck implements Inotator {
    /**
     * Constructor pentru clasa SwimmingDuck
     * @param id Long, id-ul ratei
     * @param username String, username-ul ratei
     * @param email String, email-ul ratei
     * @param password String, parola ratei
     * @param viteza double, viteza ratei
     * @param rezistenta double, rezistenta ratei
     */
    public SwimmingDuck(Long id, String username, String email, String password,
                       double viteza, double rezistenta) {
        super(id, username, email, password, TipRata.SWIMMING, viteza, rezistenta);
    }

    /**
     * Suprascrierea metodei inoata din interfata Inotator
     */
    @Override
    public void inoata() {
        System.out.println(getUsername() + " inoata cu viteza " + viteza);
    }

    /**
     * Suprascrierea metodei toString pentru clasa SwimmingDuck
     * @return String, reprezentarea ratei care inoata ca String
     */
    @Override
    public String toString() {
        return String.format("SwimmingDuck: %s [v=%.1f, r=%.1f, card=%s]",
                username, viteza, rezistenta,
                cardId != null ? cardId : "null");
    }
}
