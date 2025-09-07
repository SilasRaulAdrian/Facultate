//
// Created by Raul on 24.06.2025.
//

#ifndef TASKURI_OBSERVABLE_H
#define TASKURI_OBSERVABLE_H

#include "observer.h"
#include <vector>

class Observable {
private:
    std::vector<Observer*> observers;
public:
    void addObserver(Observer* obs) {
        observers.push_back(obs);
    }
protected:
    void notify() {
        for (auto obs : observers)
            obs->update();
    }
};

#endif //TASKURI_OBSERVABLE_H
