package org.example.model;

/**
 * Clasa pentru rata care poate zbura
 */
public class FlyingDuck extends Duck implements Zburator {
    /**
     * Constructor pentru clasa FlyingDuck
     * @param id Long, id-ul ratei
     * @param username String, username-ul ratei
     * @param email String, email-ul ratei
     * @param password String, parola ratei
     * @param viteza double, viteza ratei
     * @param rezistenta double, rezistenta ratei
     */
    public FlyingDuck(Long id, String username, String email, String password,
                        double viteza, double rezistenta) {
        super(id, username, email, password, TipRata.FLYING, viteza, rezistenta);
    }

    /**
     * Suprascrierea metodei zboara din interfata Zburator
     */
    @Override
    public void zboara() {
        System.out.println(getUsername() + " zboara la viteza " + viteza);
    }

    /**
     * Suprascrierea metodei toString pentru clasa FlyingDuck
     * @return String, reprezentarea ratei care poate zbura ca String
     */
    @Override
    public String toString() {
        return String.format("FlyingDuck: %s [v=%.1f, r=%.1f, card=%s]",
                username, viteza, rezistenta,
                card != null ? card.getNumeCard() : "null");
    }
}
