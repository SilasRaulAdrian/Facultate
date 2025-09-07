#ifndef LAB_6_7_OBSERVER_H
#define LAB_6_7_OBSERVER_H

class Observer {
public:
    /**
     * Functie virtuala pura care va fi implementata de clasele derivate
     */
    virtual void update() = 0;

    /**
     * Destructor virtual pentru a permite distrugerea corecta a obiectelor derivate
     */
    virtual ~Observer() = default;
};

#endif //LAB_6_7_OBSERVER_H
