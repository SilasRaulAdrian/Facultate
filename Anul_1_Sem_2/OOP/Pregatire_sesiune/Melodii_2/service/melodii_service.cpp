#include "melodii_service.h"
#include <algorithm>

std::vector<Melodie> ServiceMelodii::getAllSortate() {
    auto all = repo.getAll();
    std::sort(all.begin(), all.end(), [](const Melodie &a, const Melodie &b) {
        return a.getArtist() < b.getArtist();
    });
    return all;
}

void ServiceMelodii::adaugaMelodie(const QString &titlu, const QString &artist, const QString &gen) {
    int id = repo.genereazaId();
    Melodie m{id, titlu, artist, gen};
    val.valideaza(m);
    repo.adauga(m);
}

void ServiceMelodii::stergeMelodie(int id) {
    repo.sterge(id);
}

int ServiceMelodii::countArtist(const QString &artist) {
    int count = 0;
    for (const auto &melodie : repo.getAll()) {
        if (melodie.getArtist() == artist) {
            count++;
        }
    }
    return count;
}

int ServiceMelodii::countGen(const QString &gen) {
    int count = 0;
    for (const auto &melodie : repo.getAll()) {
        if (melodie.getGen() == gen) {
            count++;
        }
    }
    return count;
}