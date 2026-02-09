package org.example.domain;

public class Table {
    private final int id;

    public Table(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Table " + id;
    }
}
