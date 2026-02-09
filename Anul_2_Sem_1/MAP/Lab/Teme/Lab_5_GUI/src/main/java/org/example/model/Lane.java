package org.example.model;

/**
 * Clasa pentru pista de alergare
 */
public class Lane {
    private final int id;
    private final double distance;

    /**
     * Constructor pentru clasa Lane
     * @param id int, id-ul pistei
     * @param distance double, distanta pistei
     */
    public Lane(int id, double distance) {
        this.id = id;
        this.distance = distance;
    }

    /**
     * Getter pentru id-ul pistei
     * @return int, id-ul pistei
     */
    public int getId() {
        return id;
    }

    /**
     * Getter pentru distanta pistei
     * @return double, distanta pistei
     */
    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Lane " + id + " [distance=" + distance + "]";
    }
}
