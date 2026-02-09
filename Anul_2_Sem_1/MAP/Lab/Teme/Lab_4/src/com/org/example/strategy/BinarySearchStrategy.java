package com.org.example.strategy;

import com.org.example.model.Assignment;
import com.org.example.model.Duck;
import com.org.example.model.Lane;
import com.org.example.strategy.OptimisationStrategy;

import java.util.Arrays;

public class BinarySearchStrategy implements OptimisationStrategy {
    @Override
    public Assignment[] solve(Duck[] ducks, Lane[] lanes) {
        Arrays.sort(ducks, (a, b) -> Double.compare(a.getEndurance(), b.getEndurance()));
        Arrays.sort(lanes, (a, b) -> Double.compare(a.getDistance(), b.getDistance()));

        double low = 0.0;
        double high = 2.0 * lanes[lanes.length - 1].getDistance() / getMinSpeed(ducks);
        double ans = high;

        for (int it = 0; it < 60; ++it) {
            double mid = (low + high) / 2.0;
            if (feasible(ducks, lanes, mid)) {
                ans = mid;
                high = mid;
            } else {
                low = mid;
            }
        }

        return buildAssignments(ducks, lanes, ans);
    }

    private Assignment[] buildAssignments(Duck[] ducks, Lane[] lanes, double T) {
        Assignment[] result = new Assignment[lanes.length];
        int i = 0;
        for (Duck duck : ducks) {
            if (i == lanes.length) break;
            double needV = 2.0 * lanes[i].getDistance() / T;
            if (duck.getSpeed() >= needV) {
                result[i] = new Assignment(duck, lanes[i]);
                i++;
            }
        }
        return result;
    }

    private boolean feasible(Duck[] ducks, Lane[] lanes, double T) {
        int i = 0;
        for (Duck duck : ducks) {
            if (i == lanes.length) break;
            double needV = 2.0 * lanes[i].getDistance() / T;
            if (duck.getSpeed() >= needV) i++;
        }
        return i == lanes.length;
    }

    private double getMinSpeed(Duck[] ducks) {
        double min = Double.MAX_VALUE;
        for (Duck d : ducks) {
            min = Math.min(min, d.getSpeed());
        }
        return min;
    }
}
