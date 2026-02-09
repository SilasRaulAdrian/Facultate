package com.org.example.factory;

import com.org.example.model.Duck;
import com.org.example.model.Lane;
import com.org.example.model.Race;
import com.org.example.strategy.OptimisationStrategy;

public class RaceFactory {
    private RaceFactory() {
        // c-tor privat pentru a preveni instantierea
    }

    public static Race createRace(Duck[] ducks, Lane[] lanes, OptimisationStrategy strategy) {
        return new Race(ducks, lanes, strategy);
    }
}
