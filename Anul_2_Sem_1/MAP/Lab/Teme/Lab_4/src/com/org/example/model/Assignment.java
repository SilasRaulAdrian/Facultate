package com.org.example.model;

public class Assignment {
    private Duck duck;
    private Lane lane;
    private double time;

    public Assignment(Duck duck, Lane lane) {
        this.duck = duck;
        this.lane = lane;
        this.time = 2.0 * lane.getDistance() / duck.getSpeed();
    }

    public Duck getDuck() {
        return duck;
    }

    public Lane getLane() {
        return lane;
    }

    public double getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Duck " + duck.getId() +
                " on lane " + lane.getId() +
                ": t=" + String.format("%.3f", time) + " secunde";
    }
}
