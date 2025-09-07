#include "procesor_repo.h"
#include <QFile>
#include <QTextStream>

ProcesorRepo::ProcesorRepo(const QString &filename) : filename(filename) {
    loadFromFile();
}

void ProcesorRepo::loadFromFile() {
    QFile file(filename);
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu s-a putut deschide fisierul.");
    }
    QTextStream in(&file);

    while(!in.atEnd()) {
        QString line = in.readLine();
        QStringList tokens = line.split(',');

        if (tokens.size() != 4) continue;

        QString nume = tokens[0];
        int numarThreaduri = tokens[1].toInt();
        QString socluProcesor = tokens[2];
        int pret = tokens[3].toInt();

        procesoare.emplace_back(nume, numarThreaduri, socluProcesor, pret);
    }

    file.close();
}

const std::vector<Procesor> &ProcesorRepo::getAll() const {
    return procesoare;
}