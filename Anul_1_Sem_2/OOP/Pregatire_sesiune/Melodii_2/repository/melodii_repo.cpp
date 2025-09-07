#include <QFile>
#include <QTextStream>
#include "melodii_repo.h"

RepoMelodii::RepoMelodii(const QString &filename) : filename(filename) {
    loadFromFile();
}

void RepoMelodii::loadFromFile() {
    QFile file(filename);
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu s-a putut deschide fisierul.\n");
    }
    QTextStream in(&file);

    while (!in.atEnd()) {
        QString line = in.readLine();
        QStringList tokens = line.split(',');

        if (tokens.size() != 4) continue;

        int id = tokens[0].toInt();
        QString titlu = tokens[1];
        QString artist = tokens[2];
        QString gen = tokens[3];

        melodii.emplace_back(id, titlu, artist, gen);
    }

    file.close();
}

void RepoMelodii::saveToFile() {
    QFile file(filename);
    if (!file.open(QIODevice::WriteOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu s-a putut deschide fisierul pentru scriere.\n");
    }
    QTextStream out(&file);

    for (const auto &melodie : melodii) {
        out << melodie.getId() << ","
            << melodie.getTitlu() << ","
            << melodie.getArtist() << ","
            << melodie.getGen() << "\n";
    }

    file.close();
}

const std::vector<Melodie> &RepoMelodii::getAll() const {
    return melodii;
}

void RepoMelodii::adauga(const Melodie &m) {
    melodii.push_back(m);
    saveToFile();
}

void RepoMelodii::sterge(int id) {
    for (auto it = melodii.begin(); it != melodii.end(); ++it) {
        if (it->getId() == id) {
            melodii.erase(it);
            saveToFile();
            return;
        }
    }
}

int RepoMelodii::genereazaId() {
    int maxId = 0;
    for (const auto& m : melodii) {
        if (m.getId() > maxId) maxId = m.getId();
    }
    return maxId + 1;
}