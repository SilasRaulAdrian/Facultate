#include "melodie.h"

Melodie::Melodie(int id, const QString &titlu, const QString &artist, const QString &gen) :
    id(id), titlu(titlu), artist(artist), gen(gen) {}

int Melodie::getId() const {
    return id;
}

const QString &Melodie::getTitlu() const {
    return titlu;
}

const QString &Melodie::getArtist() const {
    return artist;
}

const QString &Melodie::getGen() const {
    return gen;
}

void Melodie::setTitlu(const QString &newTitlu) {
    titlu = newTitlu;
}

void Melodie::setGen(const QString &newGen) {
    gen = newGen;
}