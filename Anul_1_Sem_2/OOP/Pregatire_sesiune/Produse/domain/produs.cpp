#include "produs.h"

Produs::Produs(int id, const QString &nume, const QString &tip, double pret) :
    id(id), nume(nume), tip(tip), pret(pret) {}

int Produs::getId() const {
    return id;
}

const QString Produs::getNume() const {
    return nume;
}

const QString Produs::getTip() const {
    return tip;
}

double Produs::getPret() const {
    return pret;
}

void Produs::setNume(const QString &newNume) {
    nume = newNume;
}

void Produs::setTip(const QString &newTip) {
    tip = newTip;
}

void Produs::setPret(double newPret) {
    pret = newPret;
}