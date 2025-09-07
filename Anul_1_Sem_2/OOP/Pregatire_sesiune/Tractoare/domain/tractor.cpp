#include "tractor.h"

Tractor::Tractor(int id, const QString& denumire, const QString& tip, int numarRoti)
    : id(id), denumire(denumire), tip(tip), numarRoti(numarRoti) {}

int Tractor::getId() const {
    return id;
}

QString Tractor::getDenumire() const {
    return denumire;
}

QString Tractor::getTip() const {
    return tip;
}

int Tractor::getNumarRoti() const {
    return numarRoti;
}

void Tractor::setNumarRoti(int nr) {
    numarRoti = nr;
}