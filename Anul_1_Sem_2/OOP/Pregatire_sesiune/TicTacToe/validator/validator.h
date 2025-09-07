#ifndef TICTACTOE_VALIDATOR_H
#define TICTACTOE_VALIDATOR_H

#include "../domain/joc.h"
#include <stdexcept>

class Validator {
public:
    void valideaza(const Joc& j);
};

#endif //TICTACTOE_VALIDATOR_H
