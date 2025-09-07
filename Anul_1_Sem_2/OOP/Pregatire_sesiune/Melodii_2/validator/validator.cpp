#include <QList>
#include "validator.h"

void ValidatorMelodie::valideaza(const Melodie &m) {
    if (m.getId() < 0) throw std::runtime_error("Id-ul nu poate fi negativ!");
    if (m.getTitlu().isEmpty()) throw std::runtime_error("Titlul nu poate fi vid!");
    if (m.getArtist().isEmpty()) throw std::runtime_error("Artistul nu poate fi vid!");
    QStringList genuri = {"pop", "rock", "folk", "disco"};
    if (!genuri.contains(m.getGen())) throw std::runtime_error("Gen invalid!");
}