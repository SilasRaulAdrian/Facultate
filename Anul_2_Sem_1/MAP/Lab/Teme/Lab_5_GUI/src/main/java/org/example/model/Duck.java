package org.example.model;

/**
 * Clasa Duck care extinde clasa User
 */
public abstract class Duck extends User {
    protected TipRata tip;
    protected double viteza;
    protected double rezistenta;
    protected Long cardId;

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
     * Getter pentru id-ul cardului ratei
     * @return Long, id-ul cardului ratei
     */
    public Long getCardId() {
        return cardId;
    }

    /**
     * Setter pentru cardul ratei
     * @param cardId Long, id-ul cardului
     */
    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    /**
     * Setter pentru tipul ratei
     * @param tip TipRata, tipul ratei
     */
    public void setTip(TipRata tip) {
        this.tip = tip;
    }

    /**
     * Setter pentru viteza ratei
     * @param viteza double, viteza ratei
     */
    public void setViteza(double viteza) {
        this.viteza = viteza;
    }

    /**
     * Setter pentru rezistenta ratei
     * @param rezistenta double, rezistenta ratei
     */
    public void setRezistenta(double rezistenta) {
        this.rezistenta = rezistenta;
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
                cardId != null ? cardId : "null");
    }
}
