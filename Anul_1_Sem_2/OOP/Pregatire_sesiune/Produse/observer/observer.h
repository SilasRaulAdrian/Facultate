#ifndef PRODUSE_OBSERVER_H
#define PRODUSE_OBSERVER_H

#include <functional>
#include <vector>

class Observer {
public:
    /**
     * Destructorul clasei
     */
    virtual ~Observer() = default;

    /**
     * Metoda care va fi apelata pt a actualiza observatorul
     */
    virtual void update() = 0;
};

#endif //PRODUSE_OBSERVER_H
