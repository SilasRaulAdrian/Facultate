#include "validator.h"

void Validator::valideaza(const Joc &j) {
    int dim = j.getDim();
    QString tabla = j.getTabla();
    QChar jucator = j.getJucator();
    QString stare = j.getStare();

    if (dim != 3 && dim != 4 && dim != 5)
        throw std::runtime_error("Dimensiune invalida");
    if (tabla.size() != dim * dim)
        throw std::runtime_error("Dimensiune tabla invalida");
    for (int i = 0; i < tabla.size(); ++i)
        if (tabla[i] != "X" && tabla[i] != "O" && tabla[i] != "-")
            throw std::runtime_error("Tabla invalida");
    if (jucator != "X" && jucator != "O")
        throw std::runtime_error("Jucator invalid");
    if (stare != "Neinceput" && stare != "InDerulare" && stare != "Terminat")
        throw std::runtime_error("Stare invalida");
}