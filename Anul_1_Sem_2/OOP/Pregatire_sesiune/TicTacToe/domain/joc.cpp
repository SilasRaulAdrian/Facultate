#include "joc.h"

Joc::Joc(int id, int dim, const QString &tabla, const QChar &jucator, const QString &stare) :
    id(id), dim(dim), tabla(tabla), jucator(jucator), stare(stare) {}

int Joc::getId() const {
    return id;
}

int Joc::getDim() const {
    return dim;
}

QString Joc::getTabla() const {
    return tabla;
}

QChar Joc::getJucator() const {
    return jucator;
}

QString Joc::getStare() const {
    return stare;
}

void Joc::setId(int newId) {
    id = newId;
}

void Joc::setDim(int newDim) {
    dim = newDim;
}

void Joc::setTabla(const QString &newTabla) {
    tabla = newTabla;
}

void Joc::setJucator(const QChar &newJucator) {
    jucator = newJucator;
}

void Joc::setStare(const QString &newStare) {
    stare = newStare;
}