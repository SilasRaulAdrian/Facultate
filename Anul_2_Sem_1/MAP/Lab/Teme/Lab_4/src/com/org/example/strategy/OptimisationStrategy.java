package com.org.example.strategy;

import com.org.example.model.Assignment;
import com.org.example.model.Duck;
import com.org.example.model.Lane;

public interface OptimisationStrategy {
    Assignment[] solve(Duck[] ducks, Lane[] lanes);
}
