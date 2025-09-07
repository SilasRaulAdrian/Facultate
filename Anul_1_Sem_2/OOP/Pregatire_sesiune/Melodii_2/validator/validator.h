#ifndef MELODII_2_VALIDATOR_H
#define MELODII_2_VALIDATOR_H

#include "../domain/melodie.h"
#include <stdexcept>

class ValidatorMelodie {
public:
    /**
     * Valideaza o melodie
     * @param m Melodie, melodie de validat
     */
    void valideaza(const Melodie& m);
};

#endif //MELODII_2_VALIDATOR_H
