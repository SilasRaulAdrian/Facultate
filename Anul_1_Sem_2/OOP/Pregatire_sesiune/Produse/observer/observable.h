#ifndef PRODUSE_OBSERVABLE_H
#define PRODUSE_OBSERVABLE_H

#include "observer.h"
#include <vector>

class Observable {
private:
    std::vector<Observer*> observers;
public:
    /**
     * Adauga un observer la lista de observatori
     * @param obs Observer*
     */
    void addObserver(Observer* obs) {
        observers.push_back(obs);
    }
protected:
    /**
     * Notifica toti observatorii ca s-a produs o schimbare
     */
    void notify() {
        for (auto obs : observers) {
            obs->update();
        }
    }
};

#endif //PRODUSE_OBSERVABLE_H
