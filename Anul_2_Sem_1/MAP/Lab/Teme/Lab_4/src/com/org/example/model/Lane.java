package com.org.example.model;

public class Lane {
    private int id;
    private double distance;

    public Lane(int id, double distance) {
        this.id = id;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Lane{" +
                "id=" + id +
                ", distance=" + distance +
                '}';
    }
}
