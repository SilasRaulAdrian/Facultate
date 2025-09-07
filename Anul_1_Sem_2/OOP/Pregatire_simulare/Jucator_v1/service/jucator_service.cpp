#include "jucator_service.h"
#include <algorithm>

std::vector<Jucator> JucatorService::getAll() {
    return repo.getAll();
}

void JucatorService::adaugaPuncte(QString nume, int puncte, QString tip) {
    float factor = 0.5;
    if (tip == "Finala") factor = 1.0;
    else if (tip == "Semifinala") factor = 0.75;

    if (puncte < 0)
        throw std::runtime_error("Punctele nu pot fi negative!");

    auto& jucatori = repo.getAll();
    bool gasit = false;
    for (auto& j : jucatori) {
        if (j.getNume() == nume) {
            int puncteActuale = j.getPuncte();
            j.setPuncte(puncteActuale + static_cast<int>(puncte * factor));
            gasit = true;
            break;
        }
    }

    if(!gasit)
        throw std::runtime_error("Jucatorul nu a fost gasit!");
}

void JucatorService::recalculeazaRanking() {
    auto& jucatori = repo.getAll();
    std::sort(jucatori.begin(), jucatori.end(), [](const Jucator &a, const Jucator &b) {
        return a.getPuncte() > b.getPuncte();
    });

    for (int i = 0; i < jucatori.size(); ++i)
        jucatori[i].setRanking(i + 1);
}

void JucatorService::eliminaUltimii(int m) {
    auto& jucatori = repo.getAll();
    if (m > jucatori.size() / 2)
        throw std::runtime_error("Nu poti elimina mai mult de jumatate dintre jucatori!");

    std::sort(jucatori.begin(), jucatori.end(), [](const Jucator &a, const Jucator &b) {
        return a.getPuncte() < b.getPuncte();
    });

    for (int i = 0; i < m; ++i)
        jucatori.pop_back();
}