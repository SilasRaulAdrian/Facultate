package org.example.model;

/**
 * Clasa Duck care extinde clasa User
 */
public abstract class Duck extends User {
    protected TipRata tip;
    protected double viteza;
    protected double rezistenta;
    protected Card<? extends Duck> card;

    /**
     * Constructor pentru clasa Duck
     * @param id Long, id-ul ratei
     * @param username String, username-ul ratei
     * @param email String, email-ul ratei
     * @param password String, parola ratei
     * @param tip TipRata, tipul ratei
     * @param viteza double, viteza ratei
     * @param rezistenta double, rezistenta ratei
     */
    public Duck(Long id, String username, String email, String password,
                TipRata tip, double viteza, double rezistenta) {
        super(id, username, email, password);
        this.tip = tip;
        this.viteza = viteza;
        this.rezistenta = rezistenta;
    }

    /**
     * Getter pentru tipul ratei
     * @return TipRata, tipul ratei
     */
    public TipRata getTip() {
        return tip;
    }

    /**
     * Getter pentru viteza ratei
     * @return double, viteza ratei
     */
    public double getViteza() {
        return viteza;
    }

    /**
     * Getter pentru rezistenta ratei
     * @return double, rezistenta ratei
     */
    public double getRezistenta() {
        return rezistenta;
    }

    /**
     * Getter pentru cardul ratei
     * @return Card<? extends Duck>, cardul ratei
     */
    public Card<? extends Duck> getCard() {
        return card;
    }

    /**
     * Setter pentru cardul ratei
     * @param card Card<? extends Duck>, cardul ratei
     */
    public void setCard(Card<? extends Duck> card) {
        this.card = card;
    }

    /**
     * Suprascrierea metodei toString pentru clasa Duck
     * @return String, reprezentarea ratei ca String
     */
    @Override
    public String toString() {
        return String.format("%sDuck: %s [v=%.1f, r=%.1f, card=%s]",
                tip.name().charAt(0) + tip.name().substring(1).toLowerCase(),
                username, viteza, rezistenta,
                card != null ? card.getNumeCard() : "null");
    }
}
