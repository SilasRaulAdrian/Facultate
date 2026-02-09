package org.example.domain;

public class Driver {
    private final int id;
    private final String name;

    public Driver(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Driver " + name + " (ID: " + id + ")";
    }
}
