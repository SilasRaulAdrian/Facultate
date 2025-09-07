#include "concert_service.h"
#include <algorithm>

void ConcertService::modificaBilete(int indexConcert, int factor) {
    auto concerte = repo.getAll();
    if (indexConcert >= 0 && indexConcert < concerte.size()) {
        Concert concert = concerte[indexConcert];
        concert.inmultesteBilete(factor);
        repo.actualizeazaConcert(indexConcert, concert);
    }
}

void ConcertService::cumparaBilete(int indexConcert) {
    auto concerte = repo.getAll();
    if (indexConcert >= 0 && indexConcert < concerte.size()) {
        Concert concert = concerte[indexConcert];
        concert.setNrBilete(concert.getNrBilete() / 2);
        repo.actualizeazaConcert(indexConcert, concert);
    }
}

void ConcertService::sorteazaCronologic() {
    auto concerte = repo.getAll();
    std::sort(concerte.begin(), concerte.end(), [](const Concert &a, const Concert &b) {
        return a.getData() < b.getData();
    });

    for (int i = 0; i < concerte.size(); ++i)
        repo.actualizeazaConcert(i, concerte[i]);
}

std::vector<Concert> ConcertService::getAll() const {
    return repo.getAll();
}