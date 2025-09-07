//
// Created by Raul on 24.06.2025.
//
#include "student.h"

Student::Student(int nrMatricol, const QString &nume, int varsta, const QString &facultate) :
        nrMatricol(nrMatricol), nume(nume), varsta(varsta), facultate(facultate) {}

int Student::getNrMatricol() const {
    return nrMatricol;
}

QString Student::getNume() const {
    return nume;
}

int Student::getVarsta() const {
    return varsta;
}

QString Student::getFacultate() const {
    return facultate;
}

void Student::setVarsta(int newVarsta) {
    varsta = newVarsta;
}