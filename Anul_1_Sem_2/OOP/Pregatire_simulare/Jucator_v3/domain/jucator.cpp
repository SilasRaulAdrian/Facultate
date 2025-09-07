#include "jucator.h"

QString Jucator::getNume() const {
    return nume;
}

QString Jucator::getTara() const {
    return tara;
}

int Jucator::getPuncte() const {
    return puncte;
}

int Jucator::getRanking() const {
    return ranking;
}

void Jucator::setNume(const QString &nume) {
    this->nume = nume;
}

void Jucator::setTara(const QString &tara) {
    this->tara = tara;
}

void Jucator::setPuncte(int puncte) {
    this->puncte = puncte;
}

void Jucator::setRanking(int ranking) {
    this->ranking = ranking;
}