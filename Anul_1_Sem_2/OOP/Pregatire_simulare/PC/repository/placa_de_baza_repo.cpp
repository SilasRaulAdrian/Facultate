#include "placa_de_baza_repo.h"
#include <QFile>
#include <QTextStream>

PlacaDeBazaRepo::PlacaDeBazaRepo(const QString &filename) : filename(filename) {
    loadFromFile();
}

void PlacaDeBazaRepo::loadFromFile() {
    QFile file(filename);
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu s-a putut deschide fisierul.");
    }
    QTextStream in(&file);

    while (!in.atEnd()) {
        QString line = in.readLine();
        QStringList tokens = line.split(',');

        if (tokens.size() != 3) continue;

        QString nume = tokens[0];
        QString socluProcesor = tokens[1];
        int pret = tokens[2].toInt();

        placiDeBaza.emplace_back(nume, socluProcesor, pret);
    }

    file.close();
}

void PlacaDeBazaRepo::adaugaPlacaDeBaza(const QString &nume, const QString &socluProcesor, int pret) {
    placiDeBaza.emplace_back(nume, socluProcesor, pret);
}

const std::vector<PlacaDeBaza> &PlacaDeBazaRepo::getAll() const {
    return placiDeBaza;
}