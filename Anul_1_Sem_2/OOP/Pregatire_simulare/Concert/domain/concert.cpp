#include "concert.h"

QString Concert::getNumeFormatie() const {
    return numeFormatie;
}

QDate Concert::getData() const {
    return data;
}

int Concert::getNrBilete() const {
    return nrBilete;
}

void Concert::setNrBilete(int bilete) {
    this->nrBilete = bilete;
}

void Concert::inmultesteBilete(int factor) {
    nrBilete *= factor;
}

QString Concert::getDataString() const {
    return data.toString("MM/dd");
}