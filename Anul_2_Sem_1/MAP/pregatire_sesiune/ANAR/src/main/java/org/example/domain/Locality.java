package org.example.domain;

public class Locality {
    private final String name;
    private final String riverName;
    private final double cmdr;
    private final double cma;

    public Locality(String name, String riverName, double cmdr, double cma) {
        this.name = name;
        this.riverName = riverName;
        this.cmdr = cmdr;
        this.cma = cma;
    }

    public String getName() {
        return name;
    }

    public String getRiverName() {
        return riverName;
    }

    public double getCmdr() {
        return cmdr;
    }

    public double getCma() {
        return cma;
    }
}
