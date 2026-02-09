package org.example.domain;

public class River {
    private final String name;
    private double avgLevel;

    public River(String name, double avgLevel) {
        this.name = name;
        this.avgLevel = avgLevel;
    }

    public String getName() {
        return name;
    }

    public double getAvgLevel() {
        return avgLevel;
    }

    public void setAvgLevel(double avgLevel) {
        this.avgLevel = avgLevel;
    }
}
