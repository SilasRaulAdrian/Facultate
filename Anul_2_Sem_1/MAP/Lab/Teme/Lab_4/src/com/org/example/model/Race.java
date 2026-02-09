package com.org.example.model;

import com.org.example.strategy.OptimisationStrategy;

public class Race {
    private Duck[] ducks;
    private Lane[] lanes;
    private Assignment[] assignments;
    private OptimisationStrategy strategy;

    public Race(Duck[] ducks, Lane[] lanes, OptimisationStrategy strategy) {
        this.ducks = ducks;
        this.lanes = lanes;
        this.strategy = strategy;
    }

    public void computeAssignments() {
        this.assignments = strategy.solve(ducks, lanes);
    }

    public double getTotalDuration() {
        double max = 0.0;
        for (Assignment a : assignments) {
            if (a.getTime() > max) {
                max = a.getTime();
            }
        }
        return max;
    }

    public Assignment[] getAssignments() {
        return assignments;
    }

    public OptimisationStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(OptimisationStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Durata minima: %.3f secunde%n", getTotalDuration()));
        for (Assignment a : assignments) {
            sb.append(a).append("\n");
        }
        return sb.toString();
    }
}
