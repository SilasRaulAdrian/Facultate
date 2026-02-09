package org.example.model;

/**
 * Clasa pentru asignarea unei rate la o pista si timpul aferent
 */
public class Assignment {
    private final Duck duck;
    private final Lane lane;
    private final double time;

    /**
     * Constructor pentru clasa Assignment
     * @param duck Duck, rata asignata
     * @param lane Lane, pista asignata
     * @param time double, timpul aferent
     */
    public Assignment(Duck duck, Lane lane, double time) {
        this.duck = duck;
        this.lane = lane;
        this.time = time;
    }

    /**
     * Getter pentru rata asignata
     * @return Duck, rata asignata
     */
    public Duck getDuck() {
        return duck;
    }

    /**
     * Getter pentru pista asignata
     * @return Lane, pista asignata
     */
    public Lane getLane() {
        return lane;
    }

    /**
     * Getter pentru timpul aferent
     * @return double, timpul aferent
     */
    public double getTime() {
        return time;
    }

    /**
     * Suprascrierea metodei toString pentru afisarea asignarii
     * @return String, reprezentarea asignarii ca String
     */
    @Override
    public String toString() {
        return String.format("Duck %s on lane %d: t=%.3f s", duck.getUsername(), lane.getId(), time);
    }
}
