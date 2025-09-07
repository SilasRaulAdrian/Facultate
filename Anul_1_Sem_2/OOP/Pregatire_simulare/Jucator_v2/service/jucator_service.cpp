#include "jucator_service.h"
#include <algorithm>
#include <QRandomGenerator>

void JucatorService::adaugaJucator(const QString &nume, const QString &tara, int puncte) {
    Jucator jucator(nume, tara, puncte, 0);
    repo.adaugaJucator(jucator);
}

std::vector<Jucator> JucatorService::getJucatoriFiltrati(const QString& tara) const {
    if (tara.isEmpty()) {
        return repo.getAll();
    }
    return repo.getJucatoriDupaTara(tara);
}

QSet<QString> JucatorService::getTarileDisponibile() const {
    return repo.getToateTarile();
}

QPair<Jucator, Jucator> JucatorService::genereazaMeci() const {
    std::vector<Jucator> jucatori = repo.getAll();
    if (jucatori.size() < 2) {
        throw std::runtime_error("Nu sunt suficienti jucatori pentru un meci");
    }

    int index1 = QRandomGenerator::global()->bounded(jucatori.size());
    int index2;
    do {
        index2 = QRandomGenerator::global()->bounded(jucatori.size());
    } while (index1 == index2);

    return qMakePair(jucatori[index1], jucatori[index2]);
}

std::vector<Jucator> JucatorService::getAll() const {
    return repo.getAll();
}