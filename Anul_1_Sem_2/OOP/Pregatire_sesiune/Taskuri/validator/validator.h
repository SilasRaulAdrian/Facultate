//
// Created by Raul on 24.06.2025.
//

#ifndef TASKURI_VALIDATOR_H
#define TASKURI_VALIDATOR_H

#include "../domain/task.h"
#include <stdexcept>

class Validator {
public:
    void valideaza(const Task& task);
};

#endif //TASKURI_VALIDATOR_H
