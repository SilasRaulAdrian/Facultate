//
// Created by Raul on 24.06.2025.
//

#ifndef TASKURI_OBSERVER_H
#define TASKURI_OBSERVER_H

#include <functional>
#include <vector>

class Observer {
public:
    virtual ~Observer() = default;
    virtual void update() = 0;
};

#endif //TASKURI_OBSERVER_H
