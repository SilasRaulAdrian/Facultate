#include "jucator_service.h"
#include <algorithm>

std::vector<Jucator> JucatorService::getAll() {
    return repo.getAll();
}

void JucatorService::adaugaJucator(const QString &nume, const QString &tara, int rank) {
    int puncte = repo.getPunctajPtRank(rank);
    repo.adaugaJucator(Jucator(nume, tara, puncte, rank));
    repo.saveToFile();
}

void JucatorService::sortByRank() {
    auto jucatori = repo.getAll();
    std::sort(jucatori.begin(), jucatori.end(), [](const Jucator &a, const Jucator &b) {
        return a.getRanking() < b.getRanking();
    });
}

bool JucatorService::valideazaRank(int rank) const {
    return !repo.existaRank(rank);
}