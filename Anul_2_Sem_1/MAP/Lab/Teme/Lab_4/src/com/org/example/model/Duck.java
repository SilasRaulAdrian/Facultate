package com.org.example.model;

public class Duck {
    private int id;
    private double speed;
    private double endurance;

    public Duck(int id, double speed, double endurance) {
        this.id = id;
        this.speed = speed;
        this.endurance = endurance;
    }

    public int getId() {
        return id;
    }

    public double getSpeed() {
        return speed;
    }

    public double getEndurance() {
        return endurance;
    }

    @Override
    public String toString() {
        return "Duck{" +
                "id=" + id +
                ", speed=" + speed +
                ", endurance=" + endurance +
                '}';
    }
}
