//
// Created by Raul on 23.06.2025.
//
#include "melodie.h"

Melodie::Melodie(int id, const QString &titlu, const QString &artist, int rank) :
                id(id), titlu(titlu), artist(artist), rank(rank) {}

int Melodie::getId() const {
    return id;
}

QString Melodie::getTitlu() const {
    return titlu;
}

QString Melodie::getArtist() const {
    return artist;
}

int Melodie::getRank() const {
    return rank;
}

void Melodie::setTitlu(const QString &newTitlu) {
    titlu = newTitlu;
}

void Melodie::setRank(int newRank) {
    rank = newRank;
}