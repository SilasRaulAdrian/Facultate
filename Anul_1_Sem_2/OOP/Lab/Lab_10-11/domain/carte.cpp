#include "carte.h"

const int Carte::getId() const {
    return id;
}

const std::string &Carte::getGen() const {
    return gen;
}

const std::string &Carte::getTitlu() const {
    return titlu;
}

const std::string &Carte::getAutor() const {
    return autor;
}

const int Carte::getAnAparitie() const {
    return an_aparitie;
}

void Carte::setTitlu(const std::string &newTitlu) const {
    Carte::titlu = newTitlu;
}

void Carte::setAutor(const std::string &newAutor) const {
    Carte::autor = newAutor;
}

void Carte::setGen(const std::string &newGen) const {
    Carte::gen = newGen;
}

void Carte::setAnAparitie(int newAnAparitie) const {
    Carte::an_aparitie = newAnAparitie;
}