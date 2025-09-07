//
// Created by Raul on 23.06.2025.
//
#include "melodie_service.h"

ServiceMelodii::ServiceMelodii(RepoMelodii &repo) : repo(repo) {}

std::vector<Melodie> ServiceMelodii::getAllSortate() const {
    auto all = repo.getAll();
    std::sort(all.begin(), all.end(), [](const Melodie &a, const Melodie &b){
        return a.getRank() < b.getRank();
    });
    return all;
}

void ServiceMelodii::updateMelodie(int id, const QString &titlu, int rank) {
    Melodie& m = repo.find(id);
    m.setTitlu(titlu);
    m.setRank(rank);
    repo.update(m);
}

void ServiceMelodii::stergeMelodie(int id) {
    if (esteUltimaMelodie(id)) {
        throw std::runtime_error("Nu se poate sterge ultima melodie a artistului!");
    }
    repo.sterge(id);
}

bool ServiceMelodii::esteUltimaMelodie(int id) {
    Melodie& m = repo.find(id);
    int count = 0;
    for (auto& mel : repo.getAll())
        if (mel.getArtist() == m.getArtist())
            count++;
    return count == 1;
}

int ServiceMelodii::countRank(int rank) const {
    int count = 0;
    for (auto& m : repo.getAll())
        if (m.getRank() == rank)
            count++;
    return count;
}