package org.example.model;

/**
 * Clasa wrapper pentru filtrarea ratelor dupa tip
 * Permite afisarea unui label custom pentru optiunea "toate ratele"
 */
public class DuckTypeFilter {
    private final TipRata tipRata;
    private final String displayName;

    /**
     * Constructor pentru clasa DuckTypeFilter
     * @param tipRata TipRata, tipul ratei pentru filtru (null pentru toate ratele)
     * @param displayName String, numele de afisat pentru acest filtru
     */
    public DuckTypeFilter(TipRata tipRata, String displayName) {
        this.tipRata = tipRata;
        this.displayName = displayName;
    }

    /**
     * Getter pentru tipul ratei
     * @return TipRata, tipul ratei pentru filtru
     */
    public TipRata getTipRata() {
        return tipRata;
    }

    /**
     * Suprascrierea metodei toString pentru afisarea numelui de afisat
     * @return String, numele de afisat pentru acest filtru
     */
    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Creeaza optiunea "ALL DUCKS"
     */
    public static DuckTypeFilter all() {
        return new DuckTypeFilter(null, "ALL DUCKS");
    }

    /**
     * Creeaza un filtru pentru un tip specific de rata
     */
    public static DuckTypeFilter of(TipRata tip) {
        return new DuckTypeFilter(tip, tip.toString());
    }
}
